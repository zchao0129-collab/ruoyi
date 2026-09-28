package com.ruoyi.web.controller.outside;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.common.utils.security.RSAUtil;
import com.ruoyi.common.utils.security.SignStrUtil;
import com.ruoyi.common.utils.uuid.IdWorkerUtil;
import com.ruoyi.framework.web.domain.server.Sys;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.*;
import com.ruoyi.web.controller.outside.domain.HuiYanDaCallBackModel;
import com.ruoyi.web.controller.outside.domain.OutsideOrderVO;
import com.ruoyi.web.controller.outside.domain.UMiyunCallBackModel;
import com.ruoyi.web.controller.outside.domain.WebSiteCallBackModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Controller
@RequestMapping("/outside/order")
public class OutsideAlipayController extends BaseController {
    private String prefix = "system/order";
    @Autowired
    private AlipayServer alipayServer;
    @Autowired
    private IOrgOrderInfoService orderService;

    @Autowired
    private IAlipayUserInfoService alipayUserInfoService;

    @Autowired
    private IOrgAccountService accountService;
    @Autowired
    private IOrgOrderInfoService orgOrderInfoService;

    @Autowired
    private ISysAlipayConfigService sysAlipayConfigService;


    @Autowired
    private PayZftServer payZftServer;

    @Autowired
    private IMyPayServer myPayServer;


    @Autowired
    private  IOrgChannelMerchantService ocmService;

    @Value(value = "${ruoyi.profile}")
    private String uploadUrl;

    @Value(value = "${alipay.authorizeUrl}")
    private String authorizeUrl;

    @Value(value = "${alipay.orderPay}")
    private String actionUrl;

    @Value(value = "${alipay.getUidUrl}")
    private String getUidUrl;

    @Value(value = "${alipay.appid}")
    private String appid;

    @Value(value = "${alipay.yjType}")
    private String yjType;

    @Value(value = "${alipay.outChinaIp}")
    private String outChinaIp;

    @Value(value = "${alipay.payCount}")
    private int payCount;

    @Value(value = "${alipay.payUserCount}")
    private int payInitUserCount;

    @Value(value = "${alipay.payMy4FanSys}")
    private int payMy4FanSys;

    @Value(value = "${4FangSys.url}")
    private String fourSysUrl;

    @Value(value = "${blacklist.blackType}")
    private boolean blackType;



    private static final Logger logger = LoggerFactory.getLogger(OutsideAlipayController.class);


    @PostMapping("/createOrderInfo")
    @ResponseBody
    public AjaxResult createAlipayOrder(@RequestBody OutsideOrderVO orderVo) throws Exception {
        logger.info("接收参数:{}", orderVo);

        // 缓存常用变量，避免重复调用getter
        String appId = orderVo.getAppid();
        if (StringUtils.isEmpty(appId)) {
            return new AjaxResult(AjaxResult.Type.ERROR, "appid为空", "appid为空");
        }

        // 验证appid
        OrgAccount account = new OrgAccount();
        account.setAccountAppId(appId);
        account.setAccountStatus(1L);
        account = accountService.selectOne(account);
        if (account == null) {
            return new AjaxResult(AjaxResult.Type.ERROR, "客户通道停用！", "");
        }

        // 验签
        String afterSign = appId + orderVo.getMerchantOrderNo() + orderVo.getCallbackUrl()
                + orderVo.getAmount() + orderVo.getTimestamps() + account.getAccountToken();
        logger.info("afterSign:{}", afterSign);
        String sign = Md5Utils.hash(afterSign).toUpperCase();
        logger.info("sign:{}", sign);
        if (!sign.equals(orderVo.getSign())) {
            return new AjaxResult(AjaxResult.Type.ERROR, "验签失败！", "");
        }

        // 构建订单信息
        BigDecimal amount = orderVo.getAmount();
        OrgOrderInfo orderInfo = new OrgOrderInfo();
        orderInfo.setAccountName(account.getAccountName());
        orderInfo.setAccountId(account.getId());
        orderInfo.setUid(orderVo.getUid());
        orderInfo.setClientIp("");
        orderInfo.setAccountOrderNo(orderVo.getMerchantOrderNo());
        orderInfo.setCallbackUrl(orderVo.getCallbackUrl());
        orderInfo.setReturnUrl(orderVo.getReturnUrl());
        orderInfo.setAmount(amount);

        // 根据yjType计算佣金金额
        switch (yjType) {
            case "0":
                orderInfo.setYjamount(amount);
                break;
            case "1":
                // 递减
                orderInfo.setYjamount(amount.subtract(getRandomRedPacketBetweenMinAndMax()));
                break;
            default:
                // 递增
                orderInfo.setYjamount(amount.add(getRandomRedPacketBetweenMinAndMaxAdd()));
                break;
        }

        String orderNo = "D" + IdWorkerUtil.getId() + System.currentTimeMillis();
        orderInfo.setOrderNo(orderNo);
        orderInfo.setSubject("用户充值");
        orderInfo.setAcountAppId(appId);
        orderInfo.setCashier(account.getCashier());
        orderInfo.setCallbackStatus(0L);

        // 根据支付方式路由到不同的支付通道
        String method = orderVo.getMethod();
        if ("30".equals(method)) {
            // 直付通
            return payZftServer.tradeOrder(orderInfo);
        } else if ("60".equals(method)) {
            // QR当面付
            return alipayServer.face2FaceQRPayment(orderInfo);
        }

        Long payChannel = account.getPayChannel();
        if (payChannel != null) {
            switch (payChannel.intValue()) {
                case 1:
                    return payZftServer.tradeOrder(orderInfo);
                case 2:
                    return payZftServer.yujianTradeOrder(orderInfo);
                case 90:
                    return alipayServer.aliJSapiPayment(orderInfo);
                default:
                    return alipayServer.aliPayment(orderInfo);
            }
        }
        return alipayServer.aliPayment(orderInfo);
    }

    @GetMapping("/payOrderInfo/{orderNo}/{sign}")
    @ResponseBody
    public String alipayOrder(@PathVariable("orderNo") String orderNo, @PathVariable("sign") String sign, HttpServletRequest request) {
        if (StringUtils.isEmpty(orderNo) && StringUtils.isEmpty(sign)) {
            return "调用失败";
        }
        logger.info("   orderNo:{}", orderNo);
        logger.info("      sign:{}", sign);

        OrgOrderInfo orderInfo = orderService.selectorderByOrderId(orderNo);

        // IP校验：仅在开启境外IP限制时才获取IP并校验，避免不必要的header解析和外部HTTP调用
        if ("1".equals(outChinaIp) && !clientIpInChina(getIpAddr(request))) {
            return "非境内IP！";
        }

        if (BeanUtil.isEmpty(orderInfo)) {
            return "调用失败";
        }

        String aftSign = Md5Utils.hash(orderInfo.getOrderNo() + orderInfo.getMerchantNo()).toUpperCase();
        logger.info("   aftSign:{}", aftSign);
        if (sign.equals(aftSign)) {
            logger.info("-----------------------");
            return orderInfo.getBodys();
        }
        logger.error("解密失败：");
        return "调用失败";
    }





    @PostMapping("/yucreateOrder")
    @ResponseBody
    public AjaxResult yucreateOrder(@RequestBody OutsideOrderVO orderVo) throws Exception{

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

        OrgChannelMerchant ocm = ocmService.selectCollectionRatioDesc();
        OrgOrderInfo orderInfo = new OrgOrderInfo();
        orderInfo.setAccountName(account.getAccountName());
        orderInfo.setAccountId(account.getId());
        orderInfo.setAccountOrderNo(orderVo.getMerchantOrderNo());
        orderInfo.setCallbackUrl(orderVo.getCallbackUrl());
        orderInfo.setAmount(orderVo.getAmount());
        orderInfo.setAcountAppId(orderVo.getAppid());
        orderInfo.setReturnUrl(orderVo.getReturnUrl());
        long id = IdWorkerUtil.getId();
        String orderNo = "D"+ id;
        orderInfo.setOrderNo(orderNo);  //订单号
        orderInfo.setCreateTime(new Date());
        orderInfo.setMerchantId(ocm.getMerchantId());
        orderInfo.setMerchantNo(ocm.getMerchantNo());
        orderInfo.setMerchantName(ocm.getMerchantName());
        orderInfo.setChannelId(ocm.getChannelId());
        orderInfo.setChannelName(ocm.getChannelName());
        orderInfo.setUpdateTime(new Date());
        orderInfo.setCallbackStatus(0L);
        orgOrderInfoService.insertOrgOrderInfo(orderInfo);
        Map<String,String > resMap = new HashMap();
        String orderNoSgin = Md5Utils.hash(orderNo+orderInfo.getAccountOrderNo()+account.getAccountToken()).toUpperCase();
        resMap.put("orderPayLink",prefix+"/rechargeOrder/"+orderNo+"/"+orderNoSgin);
        resMap.put("orderNo",orderNo);
        resMap.put("merchantOrderNo",orderInfo.getAccountOrderNo());
        return new AjaxResult(AjaxResult.Type.SUCCESS,null, JSONObject.toJSON(resMap));
    }

    @GetMapping("/rechargeOrder/{orderNo}/{toSign}")
    public String rechargeOrder(@PathVariable("orderNo") String orderNo,@PathVariable("toSign") String toSign, ModelMap mmap)
    {
        logger.info("rechargeOrder-orderNo："+orderNo);
        OrgOrderInfo ooi = orgOrderInfoService.selectorderByOrderId(orderNo);
        OrgAccount account = new OrgAccount();
        account.setAccountAppId(ooi.getAccountAppId());
        account.setAccountStatus(1L);
        account = accountService.selectOne(account);
        if(account == null){
            return null;
        }
        if(ObjUtil.isEmpty(ooi)|| ooi ==null){
            logger.info(orderNo+" not found!");
            return null;
        }
        if(StringUtils.isNotEmpty(ooi.getMerchanOrderNo())){
            logger.info(orderNo+" 订单已生成！调用失败");
            return null;
        }

        String signstr = orderNo+ooi.getMerchantNo();
        String sign = Md5Utils.hash(signstr).toUpperCase();
        logger.info("sign:"+sign);
        if(!sign.equals(toSign)){
            logger.info("sign验证失败");
            return null;
        }
        mmap.put("orderNo", orderNo);
        mmap.put("amount", ooi.getAmount());
        mmap.put("yjAmount", ooi.getMyAmount());
        mmap.put("toSign", toSign);
        mmap.put("actionUrl", actionUrl);
        return prefix + "/payOrder";
    }


    @GetMapping("/alipay/publicAppAuthorize/{orderNo}/{toSign}")
    public void publicAppAuthorize(@PathVariable("orderNo") String orderNo,@PathVariable("toSign") String toSign,HttpServletRequest request,HttpServletResponse response) throws IOException {
        String url = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id="+appid+"&scope=auth_base&redirect_uri="+authorizeUrl;
        response.sendRedirect(url+"&state="+orderNo+"_"+toSign);
    }

    @GetMapping("/alipay/publicAppAuthorize")
    public String loginCallBack(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        String code = new String(request.getParameter("auth_code").getBytes("ISO-8859-1"),"UTF-8");
        return alipayServer.loginCallBack(code,null);
    }


    @GetMapping("/alipay/getOpenId")
    public String getOpenId(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        String code = new String(request.getParameter("auth_code").getBytes("ISO-8859-1"),"UTF-8");
        return alipayServer.getOpenId(code,appid);
    }

    //支付宝异步支付回调
    @PostMapping("/alipay/callback")
    @ResponseBody
    public String aliCallback(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {
        //支付宝支付回调
        logger.info("支付宝异步支付回调 request ===> " + request.getParameterMap());
        return alipayServer.aliPayCallback(request);
    }

    //支付宝支付同步跳转
    @GetMapping("/alipay/return")
    public void aliReturn(HttpServletRequest request,HttpServletResponse response) throws IOException, AlipayApiException {
        logger.info("支付宝支付同步跳转 request ===> " + request.getParameterMap());
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);

        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        logger.info("商户订单号+out_trade_no" +out_trade_no);
        //支付宝交易号
        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
        logger.info("支付宝交易号+trade_no" +trade_no);
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        OrgOrderInfo order = orgOrderInfoService.selectorderByOrderId(out_trade_no);
        order.setMerchanOrderNo(trade_no);
        SysAlipayConfig alipayConfig = sysAlipayConfigService.selectSysAlipayConfig(order.getMerchantNo());
        //boolean verify_result = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), "UTF-8", "RSA2");
        boolean verify_result= false;
        if(alipayConfig.getKeyOrCert()==1){
            verify_result = AlipaySignature.certVerifyV1(params,uploadUrl+"/upload/"+alipayConfig.getAlipayCertPath(),"UTF-8", "RSA2");
        }else{
            verify_result = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), "UTF-8", "RSA2");
        }
        if(verify_result){//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码
            //该页面可做页面美工编辑
            String res = alipayServer.queryOrder(order,alipayConfig);
            logger.info("res:-----"+res);
            if("SUCCESS".equals(res)){
                OrgAccount account = accountService.selectOrgAccountById(order.getAccountId());
                String param = "?appid="+order.getAcountAppId()+"&orderNo="+order.getOrderNo()+"&merchantOrderNo="+order.getAccountOrderNo()+"&payStatus="+order.getOrderStatus()+"&amount="+order.getAmount()+"&payTime="+order.getCompletionTime().getTime();
                logger.info("param:"+param);
                String parm = account.getAccountAppId()+order.getOrderNo()+order.getAccountOrderNo()+order.getOrderStatus()+ order.getAmount()+order.getCompletionTime().getTime();
                logger.info("parm:"+parm+account.getAccountToken());
                String accountSign = Md5Utils.hash(parm+account.getAccountToken()).toUpperCase();
                logger.info("accountSign:"+accountSign);
                if(StringUtils.isNotEmpty(order.getReturnUrl())){
                    if(order.getReturnUrl().indexOf("baidu")>0){
                        logger.info("ReturnUrl 1===> " + order.getReturnUrl());
                        response.sendRedirect(order.getReturnUrl());
                    }else{
                        logger.info("ReturnUrl 2===> " + order.getReturnUrl());
                        response.sendRedirect(order.getReturnUrl()+param+"&sgin="+accountSign);
                    }
                }
                //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
                //////////////////////////////////////////////////////////////////////////////////////////
            }
        }
        logger.info("支付宝支付同步跳转 request ===> " + request.getParameterMap());
    }
    public static BigDecimal getRandomRedPacketBetweenMinAndMax() {
        float minF = 0.01f;
        float maxF = 0.05f;
        // 使用BigDecimal.valueOf避免double精度丢失
        return BigDecimal.valueOf(Math.random() * (maxF - minF) + minF)
                .setScale(2, BigDecimal.ROUND_DOWN);
    }


    public static BigDecimal getRandomRedPacketBetweenMinAndMaxAdd() {
        float minF = 0.20f;
        float maxF = 0.59f;
        // 使用BigDecimal.valueOf避免double精度丢失
        return BigDecimal.valueOf(Math.random() * (maxF - minF) + minF)
                .setScale(2, BigDecimal.ROUND_DOWN);
    }


    @Async
    public void updateOrderInfoClientIp(OrgOrderInfo orderInfo){
        int count  = orderService.updateOrgOrderInfo(orderInfo);
        logger.info("更新支付订单ip地址："+count+" 条数据");
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


    @Async
    public void inseterAlipayUserInfo(String uid,String ipadd,String appid){
        AlipayUserInfo alipayUserInfo = new AlipayUserInfo();
        alipayUserInfo.setUid(uid);
        alipayUserInfo.setAppid(appid);
        alipayUserInfo.setIpadd(ipadd);
        alipayUserInfo.setPayCount(0L);
        alipayUserInfo.setInitCount(1L);
        alipayUserInfo.setGmtCreate(new Date());
        alipayUserInfo.setUpdateTime(new Date());
        int count  = alipayUserInfoService.insertAlipayUserInfo(alipayUserInfo);
        logger.info("更新支付订单用户UID和IP地址："+count+" 条数据");
    }
    @Async
    public void updateAlipayUserInfo(AlipayUserInfo alipayUserInfo){
        alipayUserInfo.setInitCount(alipayUserInfo.getInitCount()+1);
        alipayUserInfo.setGmtCreate(new Date());
        int count  = alipayUserInfoService.updateAlipayUserInfo(alipayUserInfo);
        logger.info("更新支付订单用户UID和IP地址："+count+" 条数据");
    }

    @Async
    public void getAlipayUserInfo(AlipayUserInfo userInfo){
        AlipayUserInfo alipayUserInfo = new AlipayUserInfo();
        try {
            alipayUserInfo.setUid(alipayServer.getAlipayUid(userInfo.getAppid()));
        } catch (AlipayApiException e) {
            logger.info("无法获取用户Uid ！");
            throw new RuntimeException(e);
        }
        alipayUserInfo.setPayCount(0L);
        alipayUserInfo.setInitCount(1L);
        alipayUserInfo.setUpdateTime(new Date());
        int count  = alipayUserInfoService.insertAlipayUserInfo(alipayUserInfo);
        logger.info("更新支付订单用户UID和IP地址："+count+" 条数据");
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

//    public static void main(String[] args) {
//        double d = Math.random()*10000;
//        System.out.println(d);
//        int a = (int) d%100;
//        System.out.println(a);
//        System.out.println((int)(Math.random()*10000)%100);
//
//    }

    //支付宝投诉通知
    @GetMapping("/alipay/trade/complain/changed")
    @ResponseBody
    public String aliPayTradecomplainChanged(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {
        //支付宝支付回调
        String msgMethod = new String(request.getParameter("msg_method").getBytes("ISO-8859-1"),"UTF-8");
        logger.info("支付宝投诉通知 request ===> " + request.getParameterMap());
        if("alipay.merchant.tradecomplain.changed".equals(msgMethod)){
            return alipayServer.aliPayTradecomplainChanged(request);
        }
        return "success";
    }

    @GetMapping("/payOrderGetUid")
    @ResponseBody
    public String payOrderGetUid(HttpServletRequest request,HttpServletResponse response) throws AlipayApiException, IOException {

        String code = new String(request.getParameter("auth_code").getBytes("ISO-8859-1"),"UTF-8");
        String uid = alipayServer.loginCallBack(code,appid);
        logger.info("获得用户+++++++++++++++uuid:{}+++++++++++++++",uid);
        String ipadd = getIpAddr(request);
        if("1".equals(outChinaIp)){
            if(!clientIpInChina(ipadd)){
                return "非境内IP！";
            }
        }
        String state = new String(request.getParameter("state").getBytes("ISO-8859-1"),"UTF-8");
        logger.info("参数 ---state:"+state);
        if(StringUtils.isEmpty(state)){
            return "调用失败";
        }
        String[] strings =  state.split("_");
        logger.info("   orderNo:"+strings[0]);
        logger.info("      sign:"+strings[1]);
        AlipayUserInfo alipayUserInfo = alipayUserInfoService.selectAlipayUserInfoByUid(uid);
        if(payCount>=1){
            if(alipayUserInfo != null){
                //用户支付1次不可再次支付 如果该用户被标记为砖头或是支付次数超过配阈值
                if(alipayUserInfo.getIszt() == 1 || alipayUserInfo.getPayCount() >= payCount){
                    return "请更换支付通道！";
                }
            }
        }
        OrgOrderInfo orderInfo = orderService.selectorderByOrderId(strings[0]);
        orderInfo.setUid(uid);
        orderInfo.setClientIp(ipadd);
        if(BeanUtil.isEmpty(orderInfo)){
            return "调用失败";
        }
        //如果支付用户数超出限制，哲调用失败  --开始
        int payUserCount = 0;  //支付用户总数
        if(payMy4FanSys == 0 ){
            //调用4方接口，参数 支付宝appid加Uid 返回 appid对应支付宝支付用户总数，4方保存支付宝用户信息以及对应该appid。如果有支付用户信息对应其它appid则 返回1000000000(10亿)
            payUserCount = get4AppidAndUidCount(orderInfo.getMerchantNo(),uid);
        }else if(payMy4FanSys == 1 ){
            AlipayUserInfo userInfo = new AlipayUserInfo();
            userInfo.setUid(uid);
            userInfo.setAppid(orderInfo.getMerchantNo());
            payUserCount =alipayUserInfoService.countByAppidAndUid(userInfo);
        }else{
            payUserCount = 0;
        }

        logger.info(" payUserCount:"+payUserCount);
        if(payUserCount > payInitUserCount){
            return "支付用户数超出限制";
        }
        //如果支付用户数超出限制，哲调用失败  --结束

        //异步调用，更新用户uid和ip地址
        if(alipayUserInfo == null || BeanUtil.isEmpty(alipayUserInfo) || StringUtils.isEmpty(alipayUserInfo.getUid())){
            inseterAlipayUserInfo(uid,ipadd,orderInfo.getMerchantNo());
        }else{
            updateAlipayUserInfo(alipayUserInfo);
        }
        updateOrderInfoClientIp(orderInfo);

        if(BeanUtil.isNotEmpty(orderInfo)) {
            String  aftSign = Md5Utils.hash(orderInfo.getOrderNo()+orderInfo.getMerchantNo()).toUpperCase();
            logger.info("   aftSign:"+aftSign);
            if(strings[1].equals(aftSign)){
                logger.info("-----------Sign-通过-----------");
                if(blackType){
                    logger.info("--------调用黑名单接口--------");
                    //调用黑名单接口 打开form表单
                    boolean temp = alipayUserInfoService.useridIsBlackList(uid);
                    if(!temp){
                        return orderInfo.getBodys();
                    }else{
                        return "调用失败，请重试！";
                    }
                }else {
                    return orderInfo.getBodys();
                }

            }else{
                logger.error("解密失败：");
                return "调用失败";
            }
        }else{
            return "调用失败";
        }
    }

    @GetMapping("/sendAlipay/{orderNo}/{toSign}")
    public String sendAlipay(@PathVariable("orderNo") String orderNo,@PathVariable("toSign") String toSign, ModelMap mmap)
    {
        logger.info("endAlipay-orderNo："+orderNo);
        logger.info("endAlipay-toSign："+toSign);
        mmap.put("orderNo", orderNo);
        mmap.put("toSign", toSign);
        logger.info("prefix:"+prefix);
        return prefix + "/sendAlipay";
    }

    public int get4AppidAndUidCount(String appid,String uid){
        String res  = HttpUtil.createGet(fourSysUrl +"query/"+ appid +"/"+ uid).execute().body();
        logger.info("支付订单 appid:"+appid+",uid是：o "+uid+",支付次数为："+res);
        if(StringUtils.isEmpty(res)){
            return 0;
        }else{
            return  Integer.parseInt(res);
        }
    }

    @GetMapping("/query/{appid}/{uid}")
    @ResponseBody
    public int queryInseterAppidAndUidCount(@PathVariable("appid") String appid,@PathVariable("uid") String uid)
    {
        logger.info("queryInseterAppidAndUidCount-appid："+appid);
        logger.info("queryInseterAppidAndUidCount-uid："+uid);

        AlipayUserInfo alipayUserInfo = new AlipayUserInfo();
        alipayUserInfo.setAppid(appid);
        alipayUserInfo.setUid(uid);
        int count = alipayUserInfoService.countByAppid(appid);
        if(count == 0){
            alipayUserInfo.setPayCount(0L);
            alipayUserInfo.setInitCount(1L);
            alipayUserInfo.setGmtCreate(new Date());
            alipayUserInfo.setUpdateTime(new Date());
            count += alipayUserInfoService.insertAlipayUserInfo(alipayUserInfo);
        }else{
            AlipayUserInfo userInfo = alipayUserInfoService.selectAlipayUserInfoByAppidAndUid(alipayUserInfo);
            if(BeanUtil.isEmpty(userInfo)){
                alipayUserInfo.setPayCount(0L);
                alipayUserInfo.setInitCount(1L);
                alipayUserInfo.setGmtCreate(new Date());
                alipayUserInfo.setUpdateTime(new Date());
                alipayUserInfoService.insertAlipayUserInfo(alipayUserInfo);
            }else{
                userInfo.setInitCount(userInfo.getInitCount()+1);
                alipayUserInfoService.updateAlipayUserInfo(userInfo);
            }

        }
        return count;
    }

}
