package com.ruoyi.web.controller.outside;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpRequest;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.common.utils.uuid.IdWorkerUtil;
import com.ruoyi.system.domain.OrgAccount;
import com.ruoyi.system.domain.OrgOrderInfo;
import com.ruoyi.system.service.*;
import com.ruoyi.web.controller.outside.domain.OutsideOrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;


@Controller
@RequestMapping("/outside/pay")
public class OutsidePayController extends BaseController {

    @Autowired
    private AlipayServer alipayServer;
    @Autowired
    private IOrgAccountService accountService;

    @Value(value = "${alipay.appUrl}")
    private String appUrl;

    @Autowired
    private PayZftServer payZftServer;


    @Value(value = "${alipay.subject}")
    private String orderSubject;

    private static final Logger logger = LoggerFactory.getLogger(OutsidePayController.class);
//
//    @GetMapping("/create")
//    public String createPay(ModelMap mmap)
//    {
//        long id = IdWorkerUtil.getId();
//        String orderNo = "D"+ id;
//        mmap.put("orderNo",orderNo);//订单号
//        mmap.put("regionName","充值大区");//充值大区
//        mmap.put("remark","元宝");//内容
//        mmap.put("appid","adc79b373ae54f49b5921e27468ad203");//
//        mmap.put("merchantOrderNo","P01202301111128472031111");//角色名称
//        mmap.put("callbackUrl","测试");//
//        mmap.put("returnUrl","www.baidu.com");//
//        mmap.put("amount",new BigDecimal(10));//金额
//        mmap.put("yjAmount",new BigDecimal(0.00));//随机金额
//        mmap.put("method","10");//支付方式
//        mmap.put("timestamps","1673407727219");
//        mmap.put("sign","90128A11F632F45AC155B600CF1F7257");//验签
//        return "system/pay/index";
//    }

    @PostMapping("/alipay")
    public String aliPayment(HttpServletRequest request, OutsideOrderVO orderVo, ModelMap mmap)
    {
        logger.info("接收参数 request ===> " + request.getParameterMap());
        logger.info("接收参数:"+orderVo.toString());
        if(StringUtils.isEmpty(orderVo.getAppid())){
           // return "";
        }
        //验证appid
        OrgAccount account = new OrgAccount();
        account.setAccountAppId(orderVo.getAppid());
        account.setAccountStatus(1L);
        account = accountService.selectOne(account);
        if(account == null){
            logger.info("客户通道停用！");
           // return "";
        }
        String afterSign = orderVo.getAppid()+orderVo.getMerchantOrderNo()+orderVo.getCallbackUrl()+
                orderVo.getAmount()+orderVo.getTimestamps()+account.getAccountToken();
        logger.info("afterSign:"+afterSign);
        String sign = Md5Utils.hash(afterSign).toUpperCase();
        logger.info("sign:"+sign);
        if(!sign.equals(orderVo.getSign())){
            logger.info("验签失败！");
           // return "";
        }
        BigDecimal bd = orderVo.getAmount().subtract(getRandomRedPacketBetweenMinAndMax());
//        String afterSign1 = orderVo.getAppid()+orderVo.getMerchantOrderNo()+orderVo.getCallbackUrl()+
//                bd+orderVo.getTimestamps()+account.getAccountToken();
//        String sign1 = Md5Utils.hash(afterSign1).toUpperCase();
        String id = IdWorkerUtil.getId()+"";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String orderNo = sdf.format(new Date())+id.substring(id.length()-10,id.length());
        mmap.put("orderNo",orderNo);//订单号
        mmap.put("subject",orderSubject);//订单号
        mmap.put("regionName",account.getAccountName());//充值大区
        mmap.put("remark","");//内容
        mmap.put("appid",orderVo.getAppid());//
        mmap.put("merchantOrderNo",orderVo.getMerchantOrderNo());//角色名称
        mmap.put("callbackUrl",orderVo.getCallbackUrl());//
        mmap.put("returnUrl",orderVo.getReturnUrl());//
        mmap.put("amount",orderVo.getAmount());//金额
        mmap.put("yjamount",bd);//随机金额
        mmap.put("method",orderVo.getMethod());//支付方式
        mmap.put("timestamps",orderVo.getTimestamps());
        mmap.put("action_url",appUrl+"/outside/pay/createOrderInfo");
        mmap.put("sign",sign);//验签
        return "system/pay/payment";
    }


    @PostMapping("/createOrderInfo")
    @ResponseBody
    public String createAlipayOrder(HttpServletResponse response,OutsideOrderVO orderVo) throws Exception{
        logger.info("接收参数:"+orderVo.toString());
        if(StringUtils.isEmpty(orderVo.getAppid())){
            return "system/pay/payment";
        }
        //验证appid
        OrgAccount account = new OrgAccount();
        account.setAccountAppId(orderVo.getAppid());
        account.setAccountStatus(1L);
        account = accountService.selectOne(account);
        if(account == null){
            return "客户通道停用！";
        }

        String afterSign = orderVo.getAppid()+orderVo.getMerchantOrderNo()+orderVo.getCallbackUrl()+
                orderVo.getAmount()+orderVo.getTimestamps()+account.getAccountToken();
        logger.info("afterSign:"+afterSign);
        String sign = Md5Utils.hash(afterSign).toUpperCase();
        logger.info("sign:"+sign);
        if(!sign.equals(orderVo.getSign())){
            return "验签失败！";
        }
        OrgOrderInfo orderInfo = new OrgOrderInfo();
        orderInfo.setAccountName(account.getAccountName());
        orderInfo.setAccountId(account.getId());
        orderInfo.setAccountOrderNo(orderVo.getMerchantOrderNo());
        orderInfo.setCallbackUrl(orderVo.getCallbackUrl());
        orderInfo.setReturnUrl(orderVo.getReturnUrl());
        orderInfo.setAmount(orderVo.getAmount());
        orderInfo.setYjamount(orderVo.getYjamount());
        String id = IdWorkerUtil.getId()+"";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String orderNo = sdf.format(new Date())+id.substring(id.length()-10,id.length());
        orderInfo.setOrderNo(orderNo);
        orderInfo.setSubject(orderSubject);//
        orderInfo.setAcountAppId(orderVo.getAppid());
        orderInfo.setReturnUrl(orderVo.getReturnUrl());
        orderInfo.setCashier(orderVo.getCashier());
        //return alipayServer.aliPaymentReString(orderInfo);
        if("30".equals(orderVo.getMethod())){
            String url = payZftServer.tradeGameOrder(orderInfo);
            //跳转页面至url
            response.sendRedirect(url);
        }else if("60".equals(orderVo.getMethod())){  // QR当面付
            response.sendRedirect(alipayServer.face2FaceQRPaymentURL(orderInfo));
        }else{
            return alipayServer.aliPaymentReString(orderInfo);
        }
        return "系统错误";
    }

    public static BigDecimal getRandomRedPacketBetweenMinAndMax(){
        float minF = 0.01f;
        float maxF = 0.05f;
        //生成随机数
        BigDecimal db = new BigDecimal(Math.random() * (maxF - minF) + minF);
        //返回保留两位小数的随机数。不进行四舍五入
        return db.setScale(2,BigDecimal.ROUND_DOWN);
    }
}
