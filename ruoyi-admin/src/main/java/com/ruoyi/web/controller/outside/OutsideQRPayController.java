package com.ruoyi.web.controller.outside;


import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.common.utils.uuid.IdWorkerUtil;
import com.ruoyi.system.domain.OrgAccount;
import com.ruoyi.system.domain.OrgOrderInfo;
import com.ruoyi.system.domain.SysAlipayConfig;
import com.ruoyi.system.service.AlipayServer;
import com.ruoyi.system.service.IOrgAccountService;
import com.ruoyi.system.service.ISysAlipayConfigService;
import com.ruoyi.system.service.PayZftServer;
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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


@Controller
@RequestMapping("/outside/pay")
public class OutsideQRPayController extends BaseController {

    @Autowired
    private AlipayServer alipayServer;
    @Autowired
    private IOrgAccountService accountService;
    @Autowired
    private ISysAlipayConfigService sysAlipayConfigService;
    @Value(value = "${alipay.appUrl}")
    private String appUrl;

    @Value(value = "${alipay.apikey}")
    private String apikey;

    @Autowired
    private PayZftServer payZftServer;


    private static final Logger logger = LoggerFactory.getLogger(OutsideQRPayController.class);


    @GetMapping("/qrCode/alipay/{key}")
    public String aliPayment(@PathVariable("key") String key, HttpServletRequest request, OutsideOrderVO orderVo, ModelMap mmap)
    {
            //P2021004170611169SXXXSDSDSDSDSD
            String appid = key.substring(1,17);
            String reSign = key.substring(17,key.length());
            SysAlipayConfig sysAlipayConfig = sysAlipayConfigService.selectSysAlipayConfig(appid);
            String afterSign = appid+"___"+sysAlipayConfig.getId()+apikey;
            String sign = Md5Utils.hash(afterSign);
            if(!sign.equals(reSign)){
                return "请求失败！";
            }

        String id = IdWorkerUtil.getId()+"";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String orderNo = sdf.format(new Date())+id.substring(id.length()-10,id.length());

        OrgAccount account = new OrgAccount();
        account.setAccountAppId("817bc941651c429a80ad77f903b50077");
        account.setAccountStatus(1L);
        account = accountService.selectOne(account);

        String accSign = account.getAccountAppId()+appid+orderNo+account.getAccountToken();
        String QRsign = Md5Utils.hash(accSign).toUpperCase();
        mmap.put("orderNo",orderNo);//订单号
        mmap.put("subject","订单号-"+orderNo);//订单号
        mmap.put("regionName","QR");//充值大区
        mmap.put("remark","");//内容
        mmap.put("appid",account.getAccountAppId());//
        mmap.put("merchantNo",appid);//
        mmap.put("merchantOrderNo",orderNo);//角色名称
        mmap.put("callbackUrl","");//
        mmap.put("returnUrl","");//
        mmap.put("amount",0);//金额
        mmap.put("yjamount",0);//随机金额
        mmap.put("method",10);//支付方式
        mmap.put("action_url",appUrl+"/outside/pay/QRcreateOrderInfo");
        mmap.put("sign",QRsign);//验签
        mmap.put("timestamps",orderVo.getTimestamps());
        return "system/pay/qrPayment";
    }

    @PostMapping("/QRcreateOrderInfo")
    @ResponseBody
    public String createAlipayOrder(HttpServletResponse response,OutsideOrderVO orderVo) throws Exception{
        logger.info("接收参数:"+orderVo.toString());
        if(StringUtils.isEmpty(orderVo.getAppid())){
            return "失败，请重新扫码支付！";
        }
        //验证appid
        OrgAccount account = new OrgAccount();
        account.setAccountAppId(orderVo.getAppid());
        account.setAccountStatus(1L);
        account = accountService.selectOne(account);
        if(account == null){
            return "客户通道停用！";
        }

        String accSign = account.getAccountAppId()+orderVo.getMerchantNo()+orderVo.getOrderNo()+account.getAccountToken();
        String QRsign = Md5Utils.hash(accSign).toUpperCase();
        if(!QRsign.equals(orderVo.getSign())){
            return "验签失败！";
        }
        OrgOrderInfo orderInfo = new OrgOrderInfo();
        orderInfo.setAccountName(account.getAccountName());
        orderInfo.setAccountId(account.getId());
        orderInfo.setAccountOrderNo(orderVo.getMerchantOrderNo());
        orderInfo.setCallbackUrl(orderVo.getCallbackUrl());
        orderInfo.setReturnUrl(orderVo.getReturnUrl());
        orderInfo.setAmount(orderVo.getAmount());
        orderInfo.setYjamount(orderVo.getAmount());
        String id = IdWorkerUtil.getId()+"";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String orderNo = sdf.format(new Date())+id.substring(id.length()-10,id.length());
        orderInfo.setOrderNo(orderNo);
        orderInfo.setSubject("订单号-"+orderNo);//sdf1
        orderInfo.setAcountAppId(orderVo.getAppid());
        orderInfo.setReturnUrl(orderVo.getReturnUrl());
        orderInfo.setCashier(orderVo.getCashier());
        if("30".equals(orderVo.getMethod())){
            String url = payZftServer.tradeGameOrder(orderInfo);
            //跳转页面至url
            response.sendRedirect(url);
        }else{
            return alipayServer.aliPaymentReString(orderInfo);
        }
        return "系统错误";
    }
}
