package com.ruoyi.system.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.common.utils.security.XRSAUtil;
import com.ruoyi.system.domain.OrgChannelMerchant;
import com.ruoyi.system.domain.OrgMerchant;
import com.ruoyi.system.domain.OrgOrderInfo;
import com.ruoyi.system.domain.OrgPayChannel;
import com.ruoyi.system.service.*;
import com.ruoyi.system.uwqkejicn.Hzxj_demo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PayZftServerImpl implements PayZftServer {

    @Autowired
    private IOrgChannelMerchantService orgChannelMerchantService;

    @Autowired
    private IOrgPayChannelService orgPayChannelService;

    @Autowired
    private IOrgMerchantService orgMerchantService;

    @Autowired
    private IOrgOrderInfoService orgOrderInfoService;

    private static final Logger logger = LoggerFactory.getLogger(PayZftServerImpl.class);

    // 安阳瑞和悦
    public AjaxResult tradeOrder(OrgOrderInfo orderInfo) {
        //1、查询商户通道是否可用
        List<OrgChannelMerchant> ocmlist = orgChannelMerchantService.selectOrgChannelMerchantList(null);
        if(ocmlist == null || ocmlist.isEmpty()){
            logger.info("2000：商户通道不可用！请联系管理员！");
            return AjaxResult.error();
        }
        OrgChannelMerchant ocm = ocmlist.get(0);
        OrgPayChannel orgPayChannel = orgPayChannelService.selectOrgPayChannelById(ocm.getChannelId());

        String url = orgPayChannel.getCreaterOrderUrl();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("merchantBaseId",ocm.getMerchantNo());  //商户ID
        map.put("payMoney", orderInfo.getYjamount().toString());  //支付金额
        map.put("payType", "QUICK_WAP_WAY");//唯一随机字符串   支付方式 FAST_INSTANT_TRADE_PAY电脑支付 ,QUICK_WAP_WAY手机支付,可用值:JSAPI_PAY,QUICK_WAP_WAY,FAST_INSTANT_TRADE_PAY,WX_MWEB,WX_NATIVE,WX_APP
        map.put("appId","");//
        map.put("merchantTradeNo",orderInfo.getOrderNo());//平台订单号
        map.put("notifyUrl",orgPayChannel.getCallbackUrl());//回调地址
        map.put("returnUrl","");//支付完成跳转地址
        map.put("subject","游戏充值");//订单描述

        String  afterSign = ocm.getMerchantNo()+orderInfo.getOrderNo()+ocm.getMerchanKey();
        String sign = Md5Utils.hash(afterSign);
        map.put("sign",sign);//签名(不参与签名)
        JSONObject respJson = new JSONObject(map);
        String strjson = respJson.toString();
        logger.info("输入参数 ——"+strjson);
        String callbackJson = HttpUtil.createPost(url).body(strjson).execute().body();
        logger.info("输出参数 ——"+callbackJson);
        JSONObject json = JSONObject.parseObject(callbackJson);
        if("200".equals(json.getString("code")) || "0".equals(json.getString("code"))){
            orderInfo.setPayUrl(json.getString("data"));
            orgOrderInfoService.insertOrgOrderInfo(orderInfo);
            Map<String,String > resMap = new HashMap();
            resMap.put("orderPayLink",json.getString("data"));
            resMap.put("orderNo",orderInfo.getOrderNo());
            resMap.put("merchantOrderNo",orderInfo.getAccountOrderNo());
            return new AjaxResult(AjaxResult.Type.SUCCESS,null, JSONObject.toJSON(resMap));
        }else{
            logger.info("3000：调用失败！请联系管理员！");
            return AjaxResult.error();
        }
    }


    // 安阳瑞和悦
    public String tradeGameOrder(OrgOrderInfo orderInfo) {
        //1、查询商户通道是否可用
        List<OrgChannelMerchant> ocmlist = orgChannelMerchantService.selectOrgChannelMerchantList(null);
        if(ocmlist == null || ocmlist.isEmpty()){
            logger.info("2000：商户通道不可用！请联系管理员！");
            return AjaxResult.error().toString();
        }
        OrgChannelMerchant ocm = ocmlist.get(0);
        OrgPayChannel orgPayChannel = orgPayChannelService.selectOrgPayChannelById(ocm.getChannelId());

        String url = orgPayChannel.getCreaterOrderUrl();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("merchantBaseId",ocm.getMerchantNo());  //商户ID
        map.put("payMoney", orderInfo.getYjamount().toString());  //支付金额
        map.put("payType", "QUICK_WAP_WAY");//唯一随机字符串   支付方式 FAST_INSTANT_TRADE_PAY电脑支付 ,QUICK_WAP_WAY手机支付,可用值:JSAPI_PAY,QUICK_WAP_WAY,FAST_INSTANT_TRADE_PAY,WX_MWEB,WX_NATIVE,WX_APP
        map.put("appId","");//
        map.put("merchantTradeNo",orderInfo.getOrderNo());//平台订单号
        map.put("notifyUrl",orgPayChannel.getCallbackUrl());//回调地址
        map.put("returnUrl","");//支付完成跳转地址
        map.put("subject","游戏充值");//订单描述

        String  afterSign = ocm.getMerchantNo()+orderInfo.getOrderNo()+ocm.getMerchanKey();
        String sign = Md5Utils.hash(afterSign);
        map.put("sign",sign);//签名(不参与签名)
        JSONObject respJson = new JSONObject(map);
        String strjson = respJson.toString();
        logger.info("输入参数 ——"+strjson);
        String callbackJson = HttpUtil.createPost(url).body(strjson).execute().body();
        logger.info("输出参数 ——"+callbackJson);
        JSONObject json = JSONObject.parseObject(callbackJson);
        if("200".equals(json.getString("code")) || "0".equals(json.getString("code"))){
            orderInfo.setPayUrl(json.getString("data"));
            orgOrderInfoService.insertOrgOrderInfo(orderInfo);
            Map<String,String > resMap = new HashMap();
            resMap.put("orderPayLink",json.getString("data"));
            resMap.put("orderNo",orderInfo.getOrderNo());
            resMap.put("merchantOrderNo",orderInfo.getAccountOrderNo());
            return json.getString("data");
        }else{
            logger.info("3000：调用失败！请联系管理员！");
            return AjaxResult.error().toString();
        }
    }



    @Override
    public String queryOrder(String merchantId,String merchantTradeNo,String sign) {
        String url = "http://api.game0571.com/back-jar/open/trade/info";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("merchantBaseId",merchantId);  //商户ID
        map.put("merchantTradeNo",merchantTradeNo);//平台订单号
        map.put("sign",sign);//签名(不参与签名)
        JSONObject respJson = new JSONObject(map);
        String strjson = respJson.toString();
        logger.info("queryOrder 输入参数 ——"+strjson);
        String callbackJson = HttpUtil.createPost(url).body(strjson).execute().body();
        logger.info("queryOrder 输出参数 ——"+callbackJson);
        JSONObject json = JSONObject.parseObject(callbackJson);
        if("200".equals(json.getString("code")) || "0".equals(json.getString("code"))){
            JSONObject jsonData = JSONObject.parseObject(json.getString("data"));
            String tradeState = jsonData.getString("tradeState");
            if("TRADE_SUCCESS".equals(tradeState)){
                return "success";
            }
        }else{
            return "fail";
        }

        return "fail";
    }

    @Override
    public AjaxResult refundOrder(OrgOrderInfo orderInfo) {
        return null;
    }

    @Override
    public AjaxResult notifyOrder() {
        return null;
    }

    @Override
    public AjaxResult yujianTradeOrder(OrgOrderInfo orderInfo) throws IOException {
        //1、查询商户通道是否可用
        List<OrgChannelMerchant> ocmlist = orgChannelMerchantService.selectOrgChannelMerchantList(null);
        if(ocmlist == null || ocmlist.isEmpty()){
            logger.info("2000：商户通道不可用！请联系管理员！");
            return AjaxResult.error();
        }
        OrgChannelMerchant ocm = ocmlist.get(0);
        OrgPayChannel orgPayChannel = orgPayChannelService.selectOrgPayChannelById(ocm.getChannelId());

        String url = orgPayChannel.getCreaterOrderUrl();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("pay_memberid","10415");  //商户号
        map.put("pay_orderid", orderInfo.getOrderNo());  //订单号 上送订单号唯一,
        String resultstring =orderInfo.getAmount().doubleValue()+"";
        String amountStr = resultstring.replaceAll("0*$", "").replaceAll("\\.$", "");
        map.put("pay_amount",amountStr);//订单金额
        map.put("pay_applydate", sdf.format(new Date()));//提交时间
        map.put("pay_bankcode","903");//银行编码
        map.put("pay_notifyurl",orgPayChannel.getCallbackUrl());//服务端通知
        map.put("pay_callbackurl",orderInfo.getReturnUrl());//页面跳转通知
        //map.put("pay_attach","游戏充值");//订单描述
        // map.put("pay_productnum","游戏充值");//订单描述
        //map.put("pay_productdesc","游戏充值");//订单描述
        //map.put("pay_userid","游戏充值");//订单描述
        //map.put("pay_producturl","游戏充值");//订单描述
        //map.put("json","游戏充值");//订单描述
        String signTemp = Hzxj_demo.mapToStringAndTrim(map)+"&"+ocm.getMerchanKey();
        System.out.println("订单参数: signTemp:"+signTemp);
        String sign = Md5Utils.hash(signTemp).toUpperCase();
        System.out.println("订单参数: sign:"+sign);
        map.put("pay_productname","游戏充值");//商品名称
        map.put("pay_md5sign",sign);//MD5签名
        String callbackJson =Hzxj_demo.sendPost(url, Hzxj_demo.mapToStringAndTrim(map),"utf-8");
        logger.info("输出参数 ——"+callbackJson);
        JSONObject json = JSONObject.parseObject(callbackJson);
        if("200".equals(json.getString("code")) || "0".equals(json.getString("code"))){
            orderInfo.setPayUrl(json.getString("data"));
            orgOrderInfoService.insertOrgOrderInfo(orderInfo);
            Map<String,String > resMap = new HashMap();
            resMap.put("orderPayLink",json.getString("data"));
            resMap.put("orderNo",orderInfo.getOrderNo());
            resMap.put("merchantOrderNo",orderInfo.getAccountOrderNo());
            return new AjaxResult(AjaxResult.Type.SUCCESS,null, JSONObject.toJSON(resMap));
        }else{
            logger.info("3000：调用失败！请联系管理员！");
            return AjaxResult.error();
        }
    }

    @Override
    public String yujianTradeGameOrder(OrgOrderInfo orderInfo) {
        return "";
    }

    @Override
    public String yujianQueryOrder(String merchantId, String merchantTradeNo, String sign) {
        return "";
    }

//    public static void main(String[] args) {
//        BigDecimal bd = new BigDecimal(1.00);
//        String resultstring =bd.doubleValue()+"";
//        String str1 = resultstring.replaceAll("0*$", "").replaceAll("\\.$", "");
//        System.out.println(resultstring);
//        System.out.println(str1);
//    }
}
