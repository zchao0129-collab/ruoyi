package com.ruoyi.web.controller.outside;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.pay.AlipaySignUtil;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.common.utils.uuid.IdWorkerUtil;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import com.ruoyi.web.controller.outside.domain.OutsideOrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Controller
@RequestMapping("/outside/order")
public class Outside4FangController extends BaseController {
    private String prefix = "system/order";
    @Autowired
    private AlipayServer alipayServer;
    @Autowired
    private IOrgOrderInfoService orderService;
    @Autowired
    private IOrgAccountService accountService;
    @Autowired
    private IOrgOrderInfoService orgOrderInfoService;

    @Autowired
    private IAlipayUserInfoService alipayUserInfoService;

    @Value(value = "${alipay.yjType}")
    private String yjType;

    @Value(value = "${alipay.outChinaIp}")
    private String outChinaIp;



    @Value(value = "${alipay.orderPay}")
    private String alipay;

    @Value(value = "${alipay.alipayPublicKey}")
    private String alipayPublicKey;

    private static final Logger logger = LoggerFactory.getLogger(Outside4FangController.class);


    @PostMapping("/createTradeOrder")
    @ResponseBody
    public AjaxResult createTradeOrder(@RequestBody OutsideOrderVO orderVo,HttpServletRequest request) throws Exception{
        logger.info("接收参数:"+orderVo.toString());
        if(StringUtils.isEmpty(orderVo.getAppid())){
            return new AjaxResult(AjaxResult.Type.ERROR,"appid为空","appid为空");
        }
        //验证appid
        OrgAccount account = new OrgAccount();
        account.setAccountAppId(orderVo.getAppid());
        account.setAccountStatus(1L);
        account = accountService.selectOne(account);
        if(account == null){
            return new AjaxResult(AjaxResult.Type.ERROR,"客户通道停用！","");
        }

        String afterSign = orderVo.getAppid()+orderVo.getMerchantOrderNo()+orderVo.getCallbackUrl()+
                orderVo.getAmount()+orderVo.getTimestamps()+account.getAccountToken();

        logger.info("afterSign:"+afterSign);
        String sign = Md5Utils.hash(afterSign).toUpperCase();
        logger.info("sign:"+sign);

        if(!sign.equals(orderVo.getSign())){
            return new AjaxResult(AjaxResult.Type.ERROR,"验签失败！","");
        }

        OrgOrderInfo orderInfo = new OrgOrderInfo();
        orderInfo.setAccountName(account.getAccountName());
        orderInfo.setAccountId(account.getId());
        orderInfo.setUid(orderVo.getUid());
        orderInfo.setAccountOrderNo(orderVo.getMerchantOrderNo());
        orderInfo.setCallbackUrl(orderVo.getCallbackUrl());
        orderInfo.setReturnUrl(orderVo.getReturnUrl());
        orderInfo.setAmount(orderVo.getAmount());
        if("0".equals(yjType)){ //
            orderInfo.setYjamount(orderVo.getAmount());
        }else if("1".equals(yjType)){   //递减
            BigDecimal bd = orderVo.getAmount().subtract(getRandomRedPacketBetweenMinAndMax(orderVo.getAmount()));
            orderInfo.setAmount(orderVo.getAmount());
            orderInfo.setYjamount(bd);
        }else{                           //递增
            BigDecimal bd = orderVo.getAmount().add(getRandomRedPacketBetweenMinAndMaxAdd(orderVo.getAmount()));
            orderInfo.setAmount(orderVo.getAmount());
            orderInfo.setYjamount(bd);
        }
        long id = IdWorkerUtil.getId();
        String orderNo = "D"+id+System.currentTimeMillis();
        orderInfo.setOrderNo(orderNo);
        orderInfo.setSubject("用户充值");//sdf1
        orderInfo.setAcountAppId(orderVo.getAppid());
        orderInfo.setCashier(account.getCashier());
        orderInfo.setCallbackStatus(0L);
        orderInfo.setResponse(orderVo.getMethod());
        String orderMerMd5 = Md5Utils.hash(orderInfo.getOrderNo()+orderInfo.getMerchantNo()).toUpperCase();
        String payurl =  alipay+"payTradeOrderInfo/"+ orderInfo.getOrderNo()+"/"+orderMerMd5;
        orderInfo.setPayUrl(payurl);
        //1.保存订单信息
        orgOrderInfoService.insertOrgOrderInfo(orderInfo);
        Map<String,String > resMap = new HashMap();
        resMap.put("orderPayLink",payurl);
        resMap.put("orderNo",orderInfo.getOrderNo());
        resMap.put("merchantOrderNo",orderInfo.getAccountOrderNo());
        return new AjaxResult(AjaxResult.Type.SUCCESS,null, JSONObject.toJSON(resMap));
    }

    @GetMapping("/payTradeOrderInfo/{orderNo}/{sign}")
    @ResponseBody
    public String alipayOrder(@PathVariable("orderNo") String orderNo,@PathVariable("sign") String sign,HttpServletResponse response,HttpServletRequest request) throws AlipayApiException, IOException {
        if(StringUtils.isEmpty(orderNo)&&StringUtils.isEmpty(sign)){
            return "调用失败";
        }
        logger.info("   orderNo:"+orderNo);
        logger.info("      sign:"+sign);

        OrgOrderInfo orderInfo = orderService.selectorderByOrderId(orderNo);

        String ipadd = getIpAddr(request);
        if("1".equals(outChinaIp)){
            if(!clientIpInChina(ipadd)){
                return "非境内IP！";
            }
        }
        orderInfo.setClientIp(ipadd);
        if(BeanUtil.isNotEmpty(orderInfo)) {
            String  aftSign = Md5Utils.hash(orderInfo.getOrderNo()+orderInfo.getMerchantNo()).toUpperCase();
            logger.info("   aftSign:"+aftSign);
            if(sign.equals(aftSign)){
                logger.info("-----------------------:"+orderInfo.getResponse());
                if("60".equals(orderInfo.getResponse())){  // QR当面付
                    logger.info("--------QR当面付---------------");
                    String qrurl = alipayServer.face2FaceQRPaymentURL(orderInfo);
                    logger.info("--------qrurl--------------- : "+qrurl);
                    response.sendRedirect(qrurl);
                    return null;
                }else if("10".equals(orderInfo.getResponse())){
                    logger.info("--------手机网站---------------");
                    String from = alipayServer.aliPaymentUrl(orderInfo,request);
                    logger.info("--------手机网站---------------:" + from);
                    return from;
                }
                return "调用失败1";
            }else{
                logger.error("解密失败：");
                return "调用失败2";
            }
        }else{
            return "调用失败3";
        }
    }





    public static BigDecimal getRandomRedPacketBetweenMinAndMax(BigDecimal amount){
        float minF = 0.01f;
        float maxF = 0.05f;
        //生成随机数
        BigDecimal db = new BigDecimal(Math.random() * (maxF - minF) + minF);
        //返回保留两位小数的随机数。不进行四舍五入
        return db.setScale(2,BigDecimal.ROUND_DOWN);
    }


    public static BigDecimal getRandomRedPacketBetweenMinAndMaxAdd(BigDecimal amount){
        float minF = 0.20f;
        float maxF = 0.59f;
        //生成随机数
        BigDecimal db = new BigDecimal(Math.random() * (maxF - minF) + minF);
        //返回保留两位小数的随机数。不进行四舍五入
        return db.setScale(2,BigDecimal.ROUND_DOWN);
    }



    public boolean clientIpInChina(String ip){
        String res  = HttpUtil.createGet("http://ip-api.com/json/" + ip).execute().body();
        JSONObject json = JSONObject.parseObject(res);
        String country = json.getString("country");
        String countryCode = json.getString("countryCode");
        logger.info("支付订单ip地址是：o "+ip+",IP 归属地为："+country);
        if("CN".equals(countryCode)){
            return true;
        }else {
            return false;
        }
    }


    public static String getIpAddr(HttpServletRequest request) {

        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 8) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        logger.info("支付订单ip地址："+ipAddress+",");
        return ipAddress;
    }




    @PostMapping("/alipayRiskGateway")
    @ResponseBody
    public String riskGateway(HttpServletRequest request) {

        Map<String, String[]> paramMap = request.getParameterMap();
        // 1. RSA2验签，失败直接返回空
        boolean verify = AlipaySignUtil.verify(paramMap, alipayPublicKey);
        if (!verify) {
            return "";
        }
//        ### risk_type 风险标签枚举（非正常用户判定依据）
//            - `gamble`：涉赌付款用户（高风险）
//            - `fraud`：欺诈 / 盗号用户
//            - `cash_out`：套现风险
//            - `brush_order`：刷单虚假交易
//            - `complaint`：投诉纠纷风险
//            ### 判断规则
//        只要存在 `risk_type` + `risk_level=high/mid` → 判定为带标签非正常用户，入库黑名单；
//        无推送 = 用户纯白正常用户。
//
        // 2. 解析核心风险字段
        String pid = request.getParameter("pid");
        String appId = request.getParameter("app_id");
        String buyerId = request.getParameter("buyer_id");
        String riskType = request.getParameter("risk_type");
        String riskLevel = request.getParameter("risk_level");
        String outTradeNo = request.getParameter("out_trade_no");
        String tradeNo = request.getParameter("trade_no");  //支付宝交易号

        if (StringUtils.isNotBlank(riskType) && "high".equals(riskLevel)) {
            AlipayUserInfo alipayUserInfo = alipayUserInfoService.selectAlipayUserInfoByUid(buyerId);
            if(alipayUserInfo == null || BeanUtil.isEmpty(alipayUserInfo) || StringUtils.isEmpty(alipayUserInfo.getUid())){
                alipayUserInfo = new AlipayUserInfo();
                alipayUserInfo.setUid(buyerId);
                alipayUserInfo.setAppid(appId);
                alipayUserInfo.setPayCount(0L);
                alipayUserInfo.setInitCount(1L);
                alipayUserInfo.setRiskType(riskType);
                alipayUserInfo.setRiskLevel(riskLevel);
                alipayUserInfo.setGmtCreate(new Date());
                alipayUserInfo.setUpdateTime(new Date());
                // 存入风险用户表，buyer_id作为唯一标识
                inseterAlipayUserInfo(alipayUserInfo);
            }else{
                alipayUserInfo.setUid(buyerId);
                alipayUserInfo.setAppid(appId);
                alipayUserInfo.setRiskType(riskType);
                alipayUserInfo.setRiskLevel(riskLevel);
                alipayUserInfo.setUpdateTime(new Date());
                // 存入风险用户表，buyer_id作为唯一标识
                updateAlipayUserInfo(alipayUserInfo);
            }
        }
        // 必须返回纯success
        return "success";
    }

    @Async
    public void inseterAlipayUserInfo(AlipayUserInfo alipayUserInfo){
        int count  = alipayUserInfoService.insertAlipayUserInfo(alipayUserInfo);
        logger.info("更新支付订单用户UID和IP地址："+count+" 条数据");
    }
    @Async
    public void updateAlipayUserInfo(AlipayUserInfo alipayUserInfo){
        int count  = alipayUserInfoService.updateAlipayUserInfo(alipayUserInfo);
        logger.info("更新支付订单用户UID和IP地址："+count+" 条数据");
    }


}
