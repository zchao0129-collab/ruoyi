package com.ruoyi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.*;
import com.alipay.api.domain.*;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.system.domain.AlipayUserInfo;
import com.ruoyi.system.domain.OrgOrderInfo;
import com.ruoyi.system.domain.OrgTradeComplain;
import com.ruoyi.system.domain.SysAlipayConfig;
import com.ruoyi.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.util.*;
import java.util.ArrayList;

@Service
public class AlipayServerImpl implements AlipayServer {

    @Value(value = "${alipay.orderPay}")
    private String alipay;

    @Value(value = "${alipay.returnUrl}")
    private String returnUrl;

    @Value(value = "${ruoyi.profile}")
    private String uploadUrl;

    private static final Logger logger = LoggerFactory.getLogger(AlipayServerImpl.class);
    @Autowired
    private ISysAlipayConfigService sysAlipayConfigService;

    @Autowired
    private IOrgOrderInfoService orderInfoService;
    @Autowired
    private IOrgOrderInfoService orgOrderInfoService;

    @Autowired
    private IMyPayServer myPayServer;

    @Autowired
    private IOrgTradeComplainService tradeComplainService;

    @Autowired
    private IAlipayUserInfoService alipayUserInfoService;

    @Override
    public AjaxResult aliPayment(OrgOrderInfo orderInfo) {
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = new String(orderInfo.getOrderNo());
        // 订单名称，必填
        String subject = new String(orderInfo.getSubject());
        System.out.println(subject);
        // 付款金额，必填
        String total_amount=new String(orderInfo.getYjamount()+"");
        // 商品描述，可空
        String body = new String(orderInfo.getSubject());
        // 超时时间 可空
        String timeout_express="2m";
        // 销售产品码 必填
        String product_code="QUICK_WAP_WAY";//QUICK_MSECURITY_PAY
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        int weight = (int)(Math.random()*10000)%100;
        SysAlipayConfig alipayConfig  = sysAlipayConfigService.selectSysAlipayConfigStatusWeight(weight);;
        if(alipayConfig == null || BeanUtil.isEmpty(alipayConfig)){
            alipayConfig = sysAlipayConfigService.selectSysAlipayConfigStatusTopOne();
        }
        String paygetway = "https://openapi.alipay.com/gateway.do";
        if(paygetway.equals(alipayConfig.getURL())){
            String aliPayAppid = alipayConfig.getAPPID();
            AlipayClient alipayClient = null;
            if(alipayConfig.getKeyOrCert() == 1){
                logger.info("证书客户端！");
                alipayClient =  certClient(alipayConfig);
            }else{
                logger.info("秘钥客户端！");
                alipayClient =  alipayClient(alipayConfig);
            }

            AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();
            // 封装请求支付信息
            AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();

            //AlipayTradePayRequest alipay_request = new AlipayTradePayRequest();
            //AlipayTradePayModel model = new AlipayTradePayModel();

            model.setOutTradeNo(out_trade_no);
            model.setSubject(subject);
            model.setTotalAmount(total_amount);
            model.setBody(body);
            model.setTimeoutExpress(timeout_express);
            model.setProductCode(product_code);
            alipay_request.setBizModel(model);
            // 设置异步通知地址
            alipay_request.setNotifyUrl(alipayConfig.getNotifyUrl());
            // 设置同步地址
            if(StringUtils.isNotEmpty(orderInfo.getReturnUrl())){
                alipay_request.setReturnUrl(returnUrl);
            }else{
                alipay_request.setReturnUrl(alipayConfig.getReturnUrl());
            }
            // form表单生产
            String form = "";
            try {
                // 调用SDK生成表单
               // form = alipayClient.sdkExecute(alipay_request).getBody();
                form = alipayClient.pageExecute(alipay_request).getBody();
            } catch (AlipayApiException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            if(form==""||"".equals(form)|| StringUtils.isEmpty(form)){
                return new AjaxResult(AjaxResult.Type.ERROR,"调用异常10010！");
            }
            orderInfo.setBodys(form);
            orderInfo.setMerchantNo(aliPayAppid);
            orderInfo.setUpdateTime(new Date());
            orderInfo.setCallbackStatus(0L);
            String orderMerMd5 = Md5Utils.hash(orderInfo.getOrderNo()+orderInfo.getMerchantNo()).toUpperCase();
            String payurl ="";
            if(ObjectUtil.isNotEmpty(orderInfo.getCashier())&&orderInfo.getCashier()==1){
                payurl = alipay+"rechargeOrder/"+ orderInfo.getOrderNo()+"/"+orderMerMd5;  //收银台
            }else{
                payurl = alipay+"payOrderInfo/"+ orderInfo.getOrderNo()+"/"+orderMerMd5;
            }
            orderInfo.setPayUrl(payurl);
            orgOrderInfoService.insertOrgOrderInfo(orderInfo);
            Map<String,String > resMap = new HashMap();
            resMap.put("orderPayLink",payurl);
            resMap.put("orderNo",orderInfo.getOrderNo());
            resMap.put("merchantOrderNo",orderInfo.getAccountOrderNo());
            return new AjaxResult(AjaxResult.Type.SUCCESS,null, JSONObject.toJSON(resMap));
        }else{
            return myPayServer.tradeOrder(orderInfo,alipayConfig);
        }
    }

    @Override
    public AjaxResult face2FaceQRPayment(OrgOrderInfo orderInfo) throws AlipayApiException {

        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        int weight = (int)(Math.random()*10000)%100;
        SysAlipayConfig alipayConfig  = sysAlipayConfigService.selectSysAlipayConfigStatusWeight(weight);;
        if(alipayConfig == null || BeanUtil.isEmpty(alipayConfig)){
            alipayConfig = sysAlipayConfigService.selectSysAlipayConfigStatusTopOne();
        }
        // 初始化SDK
        AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig(alipayConfig));

        // 构造请求参数以调用接口
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();

        // 设置商户订单号
        model.setOutTradeNo(orderInfo.getOrderNo());

        // 设置订单总金额
        model.setTotalAmount(new String(orderInfo.getYjamount()+""));

        // 设置订单标题
        model.setSubject(orderInfo.getSubject());

        // 设置产品码
        model.setProductCode("FACE_TO_FACE_PAYMENT");

        // 设置卖家支付宝用户ID
        model.setSellerId(alipayConfig.getSIGNTYPE());

        // 设置订单附加信息
        model.setBody(orderInfo.getSubject());
        // 设置异步通知地址
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        // 设置同步地址
        if(StringUtils.isNotEmpty(orderInfo.getReturnUrl())){
            request.setReturnUrl(returnUrl);
        }else{
            request.setReturnUrl(alipayConfig.getReturnUrl());
        }

//        // 设置订单包含的商品列表信息
//        List<GoodsDetail> goodsDetail = new ArrayList<GoodsDetail>();
//        GoodsDetail goodsDetail0 = new GoodsDetail();
//        goodsDetail0.setGoodsName("ipad");
//        goodsDetail0.setQuantity(1L);
//        goodsDetail0.setPrice("2000");
//        goodsDetail0.setGoodsId("apple-01");
//        goodsDetail0.setGoodsCategory("34543238");
//        goodsDetail0.setCategoriesTree("124868003|126232002|126252004");
//        goodsDetail0.setShowUrl("http://www.alipay.com/xxx.jpg");
//        goodsDetail.add(goodsDetail0);
//        model.setGoodsDetail(goodsDetail);
//
//        // 设置业务扩展参数
//        ExtendParams extendParams = new ExtendParams();
//        extendParams.setSysServiceProviderId("2088511833207846");
//        extendParams.setSpecifiedSellerName("XXX的跨境小铺");
//        extendParams.setCardType("S0JP0000");
//        model.setExtendParams(extendParams);
//
//        // 设置商户传入业务信息
//        BusinessParams businessParams = new BusinessParams();
//        businessParams.setMcCreateTradeIp("127.0.0.1");
//        model.setBusinessParams(businessParams);
//
//        // 设置可打折金额
//        model.setDiscountableAmount("80.00");
//
//        // 设置商户门店编号
//        model.setStoreId("NJ_001");
//
//        // 设置商户操作员编号
//        model.setOperatorId("yx_001");
//
//        // 设置商户机具终端编号
//        model.setTerminalId("NJ_T_001");
//
//        // 设置商户的原始订单号
//        model.setMerchantOrderNo("20161008001");

        request.setBizModel(model);
        // 第三方代调用模式下请设置app_auth_token
        // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");

        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());
        orderInfo.setBodys(response.getQrCode());
        orderInfo.setMerchantNo(alipayConfig.getAPPID());
        orderInfo.setUpdateTime(new Date());
        orderInfo.setCallbackStatus(0L);
        String orderMerMd5 = Md5Utils.hash(orderInfo.getOrderNo()+orderInfo.getMerchantNo()).toUpperCase();
        String payurl =  alipay+"payOrderInfo/"+ orderInfo.getOrderNo()+"/"+orderMerMd5;
        orderInfo.setPayUrl(payurl);
        orgOrderInfoService.insertOrgOrderInfo(orderInfo);
        Map<String,String > resMap = new HashMap();
        resMap.put("orderPayLink",response.getQrCode());
        resMap.put("orderNo",orderInfo.getOrderNo());
        resMap.put("merchantOrderNo",orderInfo.getAccountOrderNo());
        return new AjaxResult(AjaxResult.Type.SUCCESS,null, JSONObject.toJSON(resMap));

    }
    @Override
    public String face2FaceQRPaymentURL(OrgOrderInfo orderInfo) throws AlipayApiException {

        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        int weight = (int)(Math.random()*10000)%100;
        SysAlipayConfig alipayConfig  = sysAlipayConfigService.selectSysAlipayConfigStatusWeight(weight);;
        if(alipayConfig == null || BeanUtil.isEmpty(alipayConfig)){
            alipayConfig = sysAlipayConfigService.selectSysAlipayConfigStatusTopOne();
        }
        // 初始化SDK
        AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig(alipayConfig));

        // 构造请求参数以调用接口
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();

        // 设置商户订单号
        model.setOutTradeNo(orderInfo.getOrderNo());

        // 设置订单总金额
        model.setTotalAmount(new String(orderInfo.getYjamount()+""));

        // 设置订单标题
        model.setSubject(orderInfo.getSubject());

        // 设置产品码
        model.setProductCode("FACE_TO_FACE_PAYMENT");

        // 设置卖家支付宝用户ID
        model.setSellerId(alipayConfig.getSIGNTYPE());

        // 设置订单附加信息
        model.setBody(orderInfo.getSubject());
        // 设置异步通知地址
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        // 设置同步地址
        if(StringUtils.isNotEmpty(orderInfo.getReturnUrl())){
            request.setReturnUrl(returnUrl);
        }else{
            request.setReturnUrl(alipayConfig.getReturnUrl());
        }

//        // 设置订单包含的商品列表信息
//        List<GoodsDetail> goodsDetail = new ArrayList<GoodsDetail>();
//        GoodsDetail goodsDetail0 = new GoodsDetail();
//        goodsDetail0.setGoodsName("ipad");
//        goodsDetail0.setQuantity(1L);
//        goodsDetail0.setPrice("2000");
//        goodsDetail0.setGoodsId("apple-01");
//        goodsDetail0.setGoodsCategory("34543238");
//        goodsDetail0.setCategoriesTree("124868003|126232002|126252004");
//        goodsDetail0.setShowUrl("http://www.alipay.com/xxx.jpg");
//        goodsDetail.add(goodsDetail0);
//        model.setGoodsDetail(goodsDetail);
//
//        // 设置业务扩展参数
//        ExtendParams extendParams = new ExtendParams();
//        extendParams.setSysServiceProviderId("2088511833207846");
//        extendParams.setSpecifiedSellerName("XXX的跨境小铺");
//        extendParams.setCardType("S0JP0000");
//        model.setExtendParams(extendParams);
//
//        // 设置商户传入业务信息
//        BusinessParams businessParams = new BusinessParams();
//        businessParams.setMcCreateTradeIp("127.0.0.1");
//        model.setBusinessParams(businessParams);
//
//        // 设置可打折金额
//        model.setDiscountableAmount("80.00");
//
//        // 设置商户门店编号
//        model.setStoreId("NJ_001");
//
//        // 设置商户操作员编号
//        model.setOperatorId("yx_001");
//
//        // 设置商户机具终端编号
//        model.setTerminalId("NJ_T_001");
//
//        // 设置商户的原始订单号
//        model.setMerchantOrderNo("20161008001");

        request.setBizModel(model);
        // 第三方代调用模式下请设置app_auth_token
        // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");

        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());
        orderInfo.setBodys(response.getQrCode());
        orderInfo.setMerchantNo(alipayConfig.getAPPID());
        orderInfo.setUpdateTime(new Date());
        orderInfo.setCallbackStatus(0L);
        String orderMerMd5 = Md5Utils.hash(orderInfo.getOrderNo()+orderInfo.getMerchantNo()).toUpperCase();
        String payurl =  alipay+"payOrderInfo/"+ orderInfo.getOrderNo()+"/"+orderMerMd5;
        orderInfo.setPayUrl(payurl);
        orgOrderInfoService.insertOrgOrderInfo(orderInfo);

        return response.getQrCode();

    }
    private static AlipayConfig getAlipayConfig(SysAlipayConfig sysAlipayConfig) {
        //String privateKey  = "<-- 请填写您的应用私钥，例如：MIIEvQIBADANB ... ... -->";
        //String alipayPublicKey = "<-- 请填写您的支付宝公钥，例如：MIIBIjANBg... -->";
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
        alipayConfig.setAppId(sysAlipayConfig.getAPPID());
        alipayConfig.setPrivateKey(sysAlipayConfig.getAlipayPrivateKey());  //请填写您的应用私钥
        alipayConfig.setFormat("json");
        alipayConfig.setAlipayPublicKey(sysAlipayConfig.getAlipayPublicKey()); //请填写您的支付宝公钥
        alipayConfig.setCharset("UTF-8");
        alipayConfig.setSignType("RSA2");
        return alipayConfig;
    }

    @Override
    public AjaxResult aliFace2FacePayment(OrgOrderInfo orderInfo) {
        return null;

    }

    @Override
    public String queryOrder(OrgOrderInfo orderInfo,SysAlipayConfig alipayCnf) throws AlipayApiException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(orderInfo.getOrderNo());
        model.setTradeNo(orderInfo.getMerchanOrderNo());
        request.setBizModel(model);
        AlipayTradeQueryResponse response = null;
        AlipayClient alipayClient = null;
        if(alipayCnf.getKeyOrCert() == 1){
            logger.info("证书客户端！");
            alipayClient =  certClient(alipayCnf);
            response = alipayClient.certificateExecute(request);
            alipayClient = null;
        }else{
            logger.info("秘钥客户端！");
            alipayClient =  alipayClient(alipayCnf);
            response = alipayClient.execute(request);
            alipayClient = null;
        }

        logger.info("支付宝订单查询 ："+response.getBody());
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        JSONObject query_response = JSONObject.parseObject(jsonObject.getString("alipay_trade_query_response"));
        String status = query_response.getString("trade_status");
        logger.info("status:--------"+status);
        if("TRADE_SUCCESS".equals(status)||"TRADE_FINISHED".equals(status)){
            orderInfo.setOrderStatus(1L);
            orderInfo.setCompletionTime(new Date());
            int count =  orgOrderInfoService.updateOrgOrderInfo(orderInfo);
            if (count<=0) {
                logger.error("ali pay error : 订单更新失败==》" + orderInfo.getOrderNo());
                return "fail";
            }
            logger.info("订单更新成功 ：");
            return "SUCCESS";
        }else{
            return "fail";
        }
    }

    @Override
    public String refund(OrgOrderInfo orderInfo) {
        return null;
    }

    @Override
    public String refundQuery(OrgOrderInfo orderInfo) {
        return null;
    }

    /**
     * 将request中的参数转换成Map
     * @param request
     * @return
     */
    private Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();
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
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            retMap.put(name, valueStr);
        }
        return retMap;
    }

    public String aliPayCallback(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
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
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        //支付宝交易号

        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        OrgOrderInfo order =orgOrderInfoService.selectorderByOrderId(out_trade_no);
        SysAlipayConfig alipayConfig = sysAlipayConfigService.selectSysAlipayConfig(order.getMerchantNo());
        boolean verify_result= false;
        if(alipayConfig.getKeyOrCert()==1){
             verify_result = AlipaySignature.certVerifyV1(params,uploadUrl+"/upload/"+alipayConfig.getAlipayCertPath(),"UTF-8", "RSA2");
        }else{
             verify_result = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), "UTF-8", "RSA2");
        }
        if(verify_result){//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码

            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

            if(trade_status.equals("TRADE_FINISHED")||trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                //如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
                order.setMerchanOrderNo(trade_no);
                order.setOrderStatus(1L);
                order.setCompletionTime(new Date());
                asyncUpdateOrderStatus(order);
//                int count =  orgOrderInfoService.updateOrgOrderInfo(order);
//                if (count<=0) {
//                    logger.error("ali pay error : 订单更新失败==》" + out_trade_no);
//                    return "fail";
//                }
                //支付成功设置uid值
                if(StringUtils.isNotEmpty(order.getUid())){
                    inseterAlipayUserInfo(order.getUid(),order.getClientIp());
                }
                if(StringUtils.isNotEmpty(order.getCallbackUrl())){
                    if(order.getAcountAppId().equals("5cf4dc0dc8414bc6b4f0be225dbd64bc")){
                        asyncThreadCQCallbackOrder(order);
                    }else{
                        asyncThread(order);
                    }
                }
            }
            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
            return "success";

            //////////////////////////////////////////////////////////////////////////////////////////
        }else{//验证失败
            return "fail";
        }
    }

    @Override
    public String aliPaymentReString(OrgOrderInfo orderInfo) {
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = new String(orderInfo.getOrderNo());
        // 订单名称，必填
        String subject = new String(orderInfo.getSubject());
        System.out.println(subject);
        // 付款金额，必填
        String total_amount=new String(orderInfo.getYjamount()+"");
        // 商品描述，可空
        String body = new String(orderInfo.getSubject());
        // 超时时间 可空
        String timeout_express="2m";
        // 销售产品码 必填
        String product_code="QUICK_WAP_WAY";//QUICK_MSECURITY_PAY
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        int weight = (int)(Math.random()*10000)%100;
        SysAlipayConfig alipayConfig  = sysAlipayConfigService.selectSysAlipayConfigStatusWeight(weight);;
        if(alipayConfig == null || BeanUtil.isEmpty(alipayConfig)){
            alipayConfig = sysAlipayConfigService.selectSysAlipayConfigStatusTopOne();
        }
        String aliPayAppid = alipayConfig.getAPPID();

        AlipayClient alipayClient = null;
        if(alipayConfig.getKeyOrCert() == 1){
            logger.info("证书客户端！");
            alipayClient =  certClient(alipayConfig);
        }else{
            logger.info("秘钥客户端！");
            alipayClient =  alipayClient(alipayConfig);
        }
        AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();
        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(subject);
        model.setTotalAmount(total_amount);
        model.setBody(body);
        model.setTimeoutExpress(timeout_express);
        model.setProductCode(product_code);
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(alipayConfig.getNotifyUrl());
        // 设置同步地址
        if(StringUtils.isNotEmpty(orderInfo.getReturnUrl())){
            alipay_request.setReturnUrl(returnUrl);
        }else{
            alipay_request.setReturnUrl(alipayConfig.getReturnUrl());
        }
        // form表单生产
        String form = "";
        try {
            // 调用SDK生成表单
            // form = alipayClient.sdkExecute(alipay_request).getBody();
            form = alipayClient.pageExecute(alipay_request).getBody();
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if(form==""||"".equals(form)|| StringUtils.isEmpty(form)){
            return "调用异常10010！";
        }
        orderInfo.setBodys(form);
        orderInfo.setMerchantNo(aliPayAppid);
        orderInfo.setUpdateTime(new Date());
        orderInfo.setCallbackStatus(0L);
        String orderMerMd5 = Md5Utils.hash(orderInfo.getOrderNo()+orderInfo.getMerchantNo()).toUpperCase();
        String payurl ="";
        if(BeanUtil.isNotEmpty(orderInfo.getCashier()) && orderInfo.getCashier()==1){
            payurl = alipay+"rechargeOrder/"+ orderInfo.getOrderNo()+"/"+orderMerMd5;
        }else{
            payurl = alipay+"payOrderInfo/"+ orderInfo.getOrderNo()+"/"+orderMerMd5;
        }
        orderInfo.setPayUrl(payurl);
        orgOrderInfoService.insertOrgOrderInfo(orderInfo);
        return form;
    }

    @Async
    public void asyncThread(OrgOrderInfo orderInfo){
            orderInfoService.callbackOrder(orderInfo);
    }

    @Async
    public void asyncUpdateOrderStatus(OrgOrderInfo orderInfo){
        logger.info("ali pay error : 订单更新開始==》");
        int count =  orgOrderInfoService.updateOrgOrderInfo(orderInfo);
        logger.info("ali pay error : 订单更新結束==》更新數據：" + count+"條");
        if (count<=0) {
            logger.error("ali pay error : 订单更新失败==》" + orderInfo.getOrderNo());
        }
    }

    @Async
    public void inseterAlipayUserInfo(String uid,String ipadd){
        AlipayUserInfo alipayUserInfo = alipayUserInfoService.selectAlipayUserInfoByUid(uid);
        if(alipayUserInfo != null){
            alipayUserInfo.setPayCount(alipayUserInfo.getPayCount()+1);
            int count  = alipayUserInfoService.updateAlipayUserInfo(alipayUserInfo);
            logger.info("更新支付订单用户UID和IP地址："+count+" 条数据");
        }
    }

    //异步调用传奇回调接口
    @Async
    public void asyncThreadCQCallbackOrder(OrgOrderInfo orderInfo){
        orderInfoService.asyncThreadCQCallbackOrder(orderInfo);
    }

    public AlipayClient certClient(SysAlipayConfig alipayConfig) {
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
        certAlipayRequest.setAppId(alipayConfig.getAPPID());  //APPID 即创建应用后生成,详情见创建应用并获取 APPID
        certAlipayRequest.setPrivateKey(alipayConfig.getRsaPrivateKey());  //开发者应用私钥，由开发者自己生成
        certAlipayRequest.setFormat("JSON");  //参数返回格式，只支持 json 格式
        certAlipayRequest.setCharset("utf-8");  //请求和签名使用的字符编码格式，支持 GBK和 UTF-8
        certAlipayRequest.setSignType("RSA2");  //商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐商家使用 RSA2。
        certAlipayRequest.setCertPath(uploadUrl+"/upload/"+alipayConfig.getAppCertPath()); //应用公钥证书路径（app_cert_path 文件绝对路径）
        certAlipayRequest.setAlipayPublicCertPath(uploadUrl+"/upload/"+alipayConfig.getAlipayCertPath()); //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
        certAlipayRequest.setRootCertPath(uploadUrl+"/upload/"+alipayConfig.getAlipayRootCertPath());  //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
        AlipayClient alipayClient = null;
        try {
            alipayClient = new DefaultAlipayClient(certAlipayRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return alipayClient;
    }

    public AlipayClient alipayClient(SysAlipayConfig alipayConfig) {
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getURL(), alipayConfig.getAPPID(),
                alipayConfig.getAlipayPrivateKey(), "json", "UTF-8", alipayConfig.getAlipayPublicKey(), "RSA2");
        return alipayClient;
    }


    /**
     * 交易投诉通知回调
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws AlipayApiException
     */
    public String aliPayTradecomplainChanged(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        logger.info("交易投诉通知回调--------aliPayTradecomplainChanged--");
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
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        String appid = new String(request.getParameter("app_id").getBytes("ISO-8859-1"),"UTF-8");
        logger.info("appid:"+appid);
        String biz_content = new String(request.getParameter("biz_content").getBytes("ISO-8859-1"),"UTF-8");
        logger.info("biz_content:"+biz_content);
        JSONObject jsonObject = JSONObject.parseObject(biz_content);
        String complainEventId = jsonObject.getString("complain_event_id");
        logger.info("complainEventId:"+complainEventId);
        //支付宝侧投诉单号
        //String complainEventId = new String(request.getParameter("complain_event_id").getBytes("ISO-8859-1"),"UTF-8");

        SysAlipayConfig alipayConfig = sysAlipayConfigService.selectSysAlipayConfig(appid);
        boolean verify_result= false;
        if(alipayConfig.getKeyOrCert()==1){
            verify_result = AlipaySignature.certVerifyV1(params,uploadUrl+"/upload/"+alipayConfig.getAlipayCertPath(),"UTF-8", "RSA2");
        }else{
            verify_result = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), "UTF-8", "RSA2");
        }
        if(verify_result){//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码
            OrgTradeComplain orgTradeComplain = new OrgTradeComplain();
            orgTradeComplain.setComplainEventId(complainEventId);
            orgTradeComplain.setStatus("MERCHANT_PROCESSING");//待处理
            orgTradeComplain.setGmtCreate(new Date());
            tradeComplainService.insertOrgTradeComplain(orgTradeComplain);
            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
            logger.debug("交易投诉通知回调-支付宝侧投诉单号:"+complainEventId);
            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
            return "success";

            //////////////////////////////////////////////////////////////////////////////////////////
        }else{//验证失败
            return "fail";
        }
    }

    @Override
    public String synctradecomplain(OrgTradeComplain orgTradeComplain) throws Exception {
        int weight = (int)(Math.random()*10000)%100;
        SysAlipayConfig alipayConfig  = sysAlipayConfigService.selectSysAlipayConfigStatusWeight(weight);;
        if(alipayConfig == null || BeanUtil.isEmpty(alipayConfig)){
            alipayConfig = sysAlipayConfigService.selectSysAlipayConfigStatusTopOne();
        }
        AlipayClient alipayClient = null;
        if(alipayConfig.getKeyOrCert() == 1){
            logger.info("证书客户端！");
            alipayClient =  certClient(alipayConfig);
        }else{
            logger.info("秘钥客户端！");
            alipayClient =  alipayClient(alipayConfig);
        }
            AlipayMerchantTradecomplainQueryRequest request = new AlipayMerchantTradecomplainQueryRequest();
            request.setBizContent("{" +
                    "  \"complain_event_id\":\""+orgTradeComplain.getComplainEventId()+"\"" +
                    "}");
            AlipayMerchantTradecomplainQueryResponse response = alipayClient.certificateExecute(request);
            if(response.isSuccess()){

                //更新查询单条交易投诉详情
                orgTradeComplain.setStatus("MERCHANT_PROCESSING");//待处理
                orgTradeComplain.setGmtCreate(new Date());
                orgTradeComplain.setComplainReason(response.getComplainReason());
                orgTradeComplain.setTargetId(response.getTargetId());    //应用id
                orgTradeComplain.setTradeNo(response.getTradeNo());      //支付宝交易号
                orgTradeComplain.setStatus(response.getStatus());
                orgTradeComplain.setMerchantOrderNo(response.getMerchantOrderNo());   //商家订单号
                orgTradeComplain.setGmtModified(DateUtils.parseDate(response.getGmtModified()));
                orgTradeComplain.setGmtFinished(DateUtils.parseDate(response.getGmtFinished()));
                orgTradeComplain.setLeafCategoryName(response.getLeafCategoryName());
                tradeComplainService.updateOrgTradeComplain(orgTradeComplain);

                if(!"MERCHANT_PROCESSING".equals(response.getStatus())){  //如果查询结果 投诉状态不为待处理  侧返回成功!
                    return "success";
                }
                //根据支付宝订单 号 查询订单信息
                OrgOrderInfo orderInfo = new OrgOrderInfo();
                orderInfo.setMerchanOrderNo(response.getTradeNo());
                orderInfo = orderInfoService.queryOrder(orderInfo);
                String refundStatus = alipayTradeRefund(orderInfo);
                if(!"success".equals(refundStatus)){
                    //退款失败
                    return "fail";
                }
                //退款成功
                String feedbackSubmit = alipayMerchantTradecomplainFeedbackSubmit(orgTradeComplain,alipayClient);
                if(!"success".equals(feedbackSubmit)){
                    //商家处理交易投诉
                    return "fail";
                }
                orgTradeComplain.setStatus("MERCHANT_FEEDBACKED");  //修改投诉处理状态为已处理
                tradeComplainService.updateOrgTradeComplain(orgTradeComplain);
                return "success";
            } else {
                //投诉处理失败
                return "fail";
            }
    }

    @Override
    public String alipayMerchantTradecomplainFeedbackSubmit(OrgTradeComplain orgTradeComplain,AlipayClient alipayClient) throws AlipayApiException {
        AlipayMerchantTradecomplainFeedbackSubmitRequest tradecomplainRequest = new AlipayMerchantTradecomplainFeedbackSubmitRequest();
        tradecomplainRequest.setComplainEventId(orgTradeComplain.getComplainEventId());
        tradecomplainRequest.setFeedbackCode("02");
        tradecomplainRequest.setFeedbackContent("钱已退款给您，请查收");
        tradecomplainRequest.setOperator("王芳");
        AlipayMerchantTradecomplainFeedbackSubmitResponse  tradecomplainResponse = alipayClient.certificateExecute(tradecomplainRequest);
        logger.debug("商家处理交易投诉:"+tradecomplainResponse.getBody());
        if(tradecomplainResponse.isSuccess()){
            return "success";
        } else {
            return "fail";
        }
    }

    @Override
    public String alipayTradeRefund(OrgOrderInfo orderInfo) throws AlipayApiException {
        int weight = (int)(Math.random()*10000)%100;
        SysAlipayConfig alipayConfig  = sysAlipayConfigService.selectSysAlipayConfigStatusWeight(weight);;
        if(alipayConfig == null || BeanUtil.isEmpty(alipayConfig)){
            alipayConfig = sysAlipayConfigService.selectSysAlipayConfigStatusTopOne();
        }
        AlipayClient alipayClient = null;
        if(alipayConfig.getKeyOrCert() == 1){
            logger.info("证书客户端！");
            alipayClient =  certClient(alipayConfig);
        }else{
            logger.info("秘钥客户端！");
            alipayClient =  alipayClient(alipayConfig);
        }
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("trade_no", orderInfo.getMerchanOrderNo());
        bizContent.put("refund_amount", orderInfo.getYjamount());
        bizContent.put("out_request_no", "HZ01RF001");
        request.setBizContent(bizContent.toString());
        AlipayTradeRefundResponse response = alipayClient.certificateExecute(request);
        logger.debug("统一收单交易退款接口:"+response.getBody());
        if(response.isSuccess()){
            //更新订单状态
            orderInfo.setOrderStatus(2L);
            orderInfoService.updateOrgOrderInfo(orderInfo);
            //退款成功
            return "success";
        } else {
            //退款失败
            return "fail";
        }
    }

    @Override
    public String loginCallBack(String code,String appid) throws AlipayApiException { //获取用户扫码授权的参数//
        int weight = (int)(Math.random()*10000)%100;
        SysAlipayConfig alipayConfig  = sysAlipayConfigService.selectSysAlipayConfig(appid);;
        AlipayClient alipayClient = null;
        if(alipayConfig.getKeyOrCert() == 1){
            logger.info("证书客户端！");
            alipayClient =  certClient(alipayConfig);
        }else{
            logger.info("秘钥客户端！");
            alipayClient =  alipayClient(alipayConfig);
        }

        AlipaySystemOauthTokenRequest alipaySystemOauthTokenRequest = new AlipaySystemOauthTokenRequest();
        alipaySystemOauthTokenRequest.setGrantType("authorization_code");
        alipaySystemOauthTokenRequest.setCode(code);
        AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.certificateExecute(alipaySystemOauthTokenRequest);
        String uid =oauthTokenResponse.getUserId();//获取用户信息
        //oauthTokenResponse.getOpenId();
        logger.info("获得用户+++++++++++++++uuid:{}+++++++++++++++",uid);
        return uid;
    }


    @Override
    public String getOpenId(String code,String appid) throws AlipayApiException { //获取用户扫码授权的参数//
        SysAlipayConfig alipayConfig  = sysAlipayConfigService.selectSysAlipayConfig(appid);;
        AlipayClient alipayClient = null;
        AlipaySystemOauthTokenRequest alipaySystemOauthTokenRequest = new AlipaySystemOauthTokenRequest();
        alipaySystemOauthTokenRequest.setGrantType("authorization_code");
        alipaySystemOauthTokenRequest.setCode(code);
        AlipaySystemOauthTokenResponse oauthTokenResponse = null;

        if(alipayConfig.getKeyOrCert() == 1){
            logger.info("证书客户端！");
            alipayClient =  certClient(alipayConfig);
            oauthTokenResponse = alipayClient.certificateExecute(alipaySystemOauthTokenRequest);
        }else{
            logger.info("秘钥客户端！");
            alipayClient =  alipayClient(alipayConfig);
            oauthTokenResponse = alipayClient.execute(alipaySystemOauthTokenRequest);
        }

        String openId =oauthTokenResponse.getOpenId();;//获取用户信息
        logger.info("获得用户+++++++++++++++openId:{}+++++++++++++++",openId);
        return openId;
    }

    public Map getAliPayParam(HttpServletRequest request) throws UnsupportedEncodingException {
        Map map = new HashMap();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i <values.length; i++) { valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ","; }
            // 乱码解决，这段代码在出现乱码时使用 //
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            map.put(name, valueStr);
            logger.info("接受支付宝回调参数：{}",map);
        }
        return map;
    }
//
//    private String getAliUserToken(String code, AlipayClient alipayClient) throws AlipayApiException {
//        AlipaySystemOauthTokenRequest alipaySystemOauthTokenRequest = new AlipaySystemOauthTokenRequest();
//        alipaySystemOauthTokenRequest.setGrantType("authorization_code");
//        alipaySystemOauthTokenRequest.setCode(code);
//        AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(alipaySystemOauthTokenRequest);
//        logger.info("获得用户+++++++++++++++token:{}+++++++++++++++",oauthTokenResponse.getAccessToken());
//        logger.info("获得用户+++++++++++++++uuid:{}+++++++++++++++",oauthTokenResponse.getUserId());
//        if(oauthTokenResponse.isSuccess()){
//            logger.info("成功");
//        } else {
//            logger.info("***********失败，自旋开始第：{}次");
//           // number += 1;
////            if(number <3){
////                logger.info("获取token失败，尝试：*******{}*******",number);
////                return this.getAliUserToken(apiPayLoginReq, alipayClient, number);
////            }
//        } return oauthTokenResponse.getUserId();
//    }

//    private AlipayUserInfoShareResponse getUserInfo(AlipayClient alipayClient,AlipaySystemOauthTokenResponse aliUserToken,int number) throws AlipayApiException {
//        AlipayUserInfoShareRequest alipayUserInfoShareRequest = new AlipayUserInfoShareRequest();
//        AlipayUserInfoShareResponse infoShareResponse = alipayClient.execute(alipayUserInfoShareRequest,aliUserToken.getAccessToken());
//        logger.info("----------------获得支付宝用户详情：{}",infoShareResponse.getBody());
//        UserInfoReq userInfoReq = new UserInfoReq();
//        if(infoShareResponse.isSuccess()){ //用户授权成功
//            logger.info("----------------获得支付宝用户基本而信息：{}",userInfoReq);
//            logger.info("成功");
//        } else {
//            logger.info("***********失败，自旋开始第：{}次",number);
//            number += 1;
//            if(number <3){
//                logger.info("调用用户详情失败，尝试：*******{}*******",number);
//                return this.getUserInfo(alipayClient,aliUserToken,number);
//            }
//            return infoShareResponse ;
//        }
//    }


    @Override
    public void alipayMerchantTradecomplainBatchquery() throws AlipayApiException {
        int weight = (int)(Math.random()*10000)%100;
        SysAlipayConfig alipayConfig  = sysAlipayConfigService.selectSysAlipayConfigStatusWeight(weight);;
        if(alipayConfig == null || BeanUtil.isEmpty(alipayConfig)){
            alipayConfig = sysAlipayConfigService.selectSysAlipayConfigStatusTopOne();
        }
        AlipayClient alipayClient = null;
        if(alipayConfig.getKeyOrCert() == 1){
            logger.info("证书客户端！");
            alipayClient =  certClient(alipayConfig);
        }else{
            logger.info("秘钥客户端！");
            alipayClient =  alipayClient(alipayConfig);
        }

        String begin_time = DateUtils.dateTimeNow("yyyy-MM-dd")+" 00:00:00";

        String end_time = DateUtils.dateTimeNow("yyyy-MM-dd")+" 23:59:59";
        AlipayMerchantTradecomplainBatchqueryRequest request = new AlipayMerchantTradecomplainBatchqueryRequest();
        request.setBizContent("{" +
//                "  \"target_infos\":[" +
//                "    {" +
//                "      \"target_id\":\""+alipayConfig.getAPPID()+"\"," +
//                "      \"target_type\":\"APPID\"" +
//                "    }" +
//                "  ]," +
                "  \"status\":\"MERCHANT_FEEDBACKED\"," +
                //"  \"begin_time\":\""+begin_time+"\"," +
                //"  \"end_time\":\""+end_time+"\"," +
                "  \"page_size\":20," +
                "  \"page_num\":1" +
                "}");
        AlipayMerchantTradecomplainBatchqueryResponse response = alipayClient.certificateExecute(request);
        logger.info("AlipayMerchantTradecomplainBatchqueryResponse--isSuccess:"+response.isSuccess());
        logger.info("AlipayMerchantTradecomplainBatchqueryResponse--getBody:"+response.getBody());
        if(response.isSuccess()){
            List<TradeComplainQueryResponse>  responseLisrt = response.getTradeComplainInfos();
            if(responseLisrt != null){
                for (TradeComplainQueryResponse res:responseLisrt ) {
                    logger.info("ComplainEventId:"+res.getComplainEventId());
                    logger.info("status:"+res.getStatus());
                    OrgTradeComplain orgTradeComplain = new OrgTradeComplain();
                    orgTradeComplain.setComplainEventId(res.getComplainEventId());
                    orgTradeComplain.setComplainReason(res.getComplainReason());
                    orgTradeComplain.setTargetId(res.getTargetId());    //应用id
                    orgTradeComplain.setTradeNo(res.getTradeNo());      //支付宝交易号
                    orgTradeComplain.setStatus(res.getStatus());
                    orgTradeComplain.setMerchantOrderNo(res.getMerchantOrderNo());   //商家订单号
                    orgTradeComplain.setGmtCreate(DateUtil.date());
                    orgTradeComplain.setGmtModified(DateUtils.parseDate(res.getGmtModified()));
                    orgTradeComplain.setGmtFinished(DateUtils.parseDate(res.getGmtFinished()));
                    orgTradeComplain.setLeafCategoryName(res.getLeafCategoryName());
                    tradeComplainService.insertOrgTradeComplain(orgTradeComplain);
                }
            }
        }else {
            logger.info("查询交易投诉列表__查询失败！");
        }
    }



    @Override
    public String getAlipayUid(String appid) throws AlipayApiException {

        int weight = (int)(Math.random()*10000)%100;
        SysAlipayConfig alipayConfig  = sysAlipayConfigService.selectSysAlipayConfig(appid);;
        if(alipayConfig == null || BeanUtil.isEmpty(alipayConfig)){
            logger.info("SysAlipayConfig is null！");
            return "";
        }
        AlipayClient alipayClient = null;
        if(alipayConfig.getKeyOrCert() == 1){
            logger.info("证书客户端！");
            alipayClient =  certClient(alipayConfig);
        }else{
            logger.info("秘钥客户端！");
            alipayClient =  alipayClient(alipayConfig);
        }

        AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
        AlipayUserInfoShareResponse response = alipayClient.certificateExecute(request);
        if(response.isSuccess()){
            logger.debug("用户信息 :"+response.getBody());
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }

        return "123";
    }

    @Override
    public AjaxResult aliJSapiPayment(OrgOrderInfo orderInfo) throws AlipayApiException {
        // 初始化SDK
        int weight = (int)(Math.random()*10000)%100;
        SysAlipayConfig alipayConfig  = sysAlipayConfigService.selectSysAlipayConfigStatusWeight(weight);;
        if(alipayConfig == null || BeanUtil.isEmpty(alipayConfig)){
            alipayConfig = sysAlipayConfigService.selectSysAlipayConfigStatusTopOne();
        }
        AlipayConfig aliConfig = new AlipayConfig();
        aliConfig.setServerUrl(alipayConfig.getURL());
        aliConfig.setAppId(alipayConfig.getAPPID());
        aliConfig.setPrivateKey(alipayConfig.getAlipayPrivateKey());
        aliConfig.setFormat("json");
        aliConfig.setAlipayPublicKey(alipayConfig.getAlipayPublicKey());
        aliConfig.setCharset("UTF-8");
        aliConfig.setSignType("RSA2");
        AlipayClient alipayClient = new DefaultAlipayClient(aliConfig);

        // 构造请求参数以调用接口
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();

        // 设置商户订单号
        model.setOutTradeNo(orderInfo.getAccountOrderNo());

        // 设置产品码
        model.setProductCode("JSAPI_PAY");

        // 设置小程序支付中
        model.setOpAppId(alipayConfig.getAPPID());

        String total_amount=new String(orderInfo.getAmount()+"");
        // 设置订单总金额
        model.setTotalAmount(total_amount);

        // 设置买家支付宝用户唯一标识
        model.setBuyerOpenId("074a1CcTG1LelxKe4xQC0zgNdId0nxi95b5lsNpazWYoCo5");

        // 设置订单标题
        model.setSubject("千问智能体支付");
        // 设置商户门店编号
        request.setBizModel(model);
        // 第三方代调用模式下请设置app_auth_token
        // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");
        request.setBizModel(model);
        // 设置异步通知地址
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        // 设置同步地址
        if(StringUtils.isNotEmpty(orderInfo.getReturnUrl())){
            request.setReturnUrl(returnUrl);
        }else{
            request.setReturnUrl(alipayConfig.getReturnUrl());
        }

        AlipayTradeCreateResponse response = alipayClient.execute(request);
        orderInfo.setBodys(response.getBody());
        orderInfo.setMerchantNo(orderInfo.getMerchanOrderNo());
        orderInfo.setUpdateTime(new Date());
        orderInfo.setCallbackStatus(0L);
        orgOrderInfoService.insertOrgOrderInfo(orderInfo);
        if (response.isSuccess()) {
            if( "10000".equals(response.getCode()))
            {
                orderInfo.setMerchanOrderNo(response.getTradeNo());
                orgOrderInfoService.updateOrgOrderInfo(orderInfo);
                Map<String,String > resMap = new HashMap();
                resMap.put("amount",orderInfo.getAmount()+"");
                resMap.put("merchantOrderNo",orderInfo.getAccountOrderNo());
                return new AjaxResult(AjaxResult.Type.SUCCESS,null, JSONObject.toJSON(resMap));
            }else{
                return new AjaxResult(AjaxResult.Type.ERROR,null, JSONObject.toJSON("系统繁忙"));
            }
        } else {
            return new AjaxResult(AjaxResult.Type.ERROR,null, JSONObject.toJSON("系统繁忙"));
        }

//        Map<String,String > resMap = new HashMap();
//        resMap.put("amount",orderInfo.getAmount()+"");
//        resMap.put("merchantOrderNo",orderInfo.getAccountOrderNo());
//        return new AjaxResult(AjaxResult.Type.SUCCESS,null, JSONObject.toJSON(resMap));
    }

    @Override
    public AjaxResult alipayTradeQuery(OrgOrderInfo orderInfo) throws AlipayApiException {
        // 初始化SDK
        // 初始化SDK
        int weight = (int)(Math.random()*10000)%100;
        SysAlipayConfig alipayConfig  = sysAlipayConfigService.selectSysAlipayConfigStatusWeight(weight);;
        if(alipayConfig == null || BeanUtil.isEmpty(alipayConfig)){
            alipayConfig = sysAlipayConfigService.selectSysAlipayConfigStatusTopOne();
        }
        AlipayConfig aliConfig = new AlipayConfig();
        aliConfig.setServerUrl(alipayConfig.getURL());
        aliConfig.setAppId(alipayConfig.getAPPID());
        aliConfig.setPrivateKey(alipayConfig.getAlipayPrivateKey());
        aliConfig.setFormat("json");
        aliConfig.setAlipayPublicKey(alipayConfig.getAlipayPublicKey());
        aliConfig.setCharset("UTF-8");
        aliConfig.setSignType("RSA2");
        // 构造请求参数以调用接口
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        // 设置订单支付时传入的商户订单号
        model.setOutTradeNo("20150320010101001");
        request.setBizModel(model);
        AlipayClient alipayClient = new DefaultAlipayClient(aliConfig);
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());

        if (response.isSuccess()) {
            if("TRADE_SUCCESS".equals(response.getTradeStatus())){
                orgOrderInfoService.updateOrgOrderInfo(orderInfo);
                orderInfoService.callbackOrder(orderInfo);
            }
        } else {
            System.out.println("调用失败");

        }
        return null;
    }
}
