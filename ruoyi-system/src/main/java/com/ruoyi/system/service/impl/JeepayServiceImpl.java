package com.ruoyi.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jeequan.jeepay.JeepayClient;
import com.jeequan.jeepay.exception.JeepayException;
import com.jeequan.jeepay.model.PayOrderCreateReqModel;
import com.jeequan.jeepay.request.PayOrderCreateRequest;
import com.jeequan.jeepay.response.PayOrderCreateResponse;
import com.ruoyi.system.service.JeepayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2023年06月07日 15:21
 */
@Service
public class JeepayServiceImpl implements JeepayService {

//    @Value(value = "${taqi.notifyurl}")
//    private String notifyurl;
//    @Value(value = "${taqi.returnurl}")
//    private String returnurl;


    @Override
    public void doPay() {
//        Byte divisionMode = 0;
//        PayOrderCreateRequest request = new PayOrderCreateRequest();
//        PayOrderCreateReqModel model = new PayOrderCreateReqModel();
//        request.setBizModel(model);
//        model.setMchNo(getCurrentMchNo()); // 商户号
//        model.setAppId(appId);
//        model.setMchOrderNo(mchOrderNo);
//        model.setWayCode(wayCode);
//        model.setAmount(amount);
//        // paypal通道使用USD类型货币
//        if(wayCode.equalsIgnoreCase("pp_pc")) {
//            model.setCurrency("USD");
//        }else {
//            model.setCurrency("CNY");
//        }
//        model.setClientIp(getClientIp());
//        model.setSubject(orderTitle + "[" + getCurrentMchNo() + "商户联调]");
//        model.setBody(orderTitle + "[" + getCurrentMchNo() + "商户联调]");
//
//        DBApplicationConfig dbApplicationConfig = sysConfigService.getDBApplicationConfig();
//
//        model.setNotifyUrl(notifyurl); //回调地址
//        model.setDivisionMode(divisionMode); //分账模式
//
//        //设置扩展参数
//        JSONObject extParams = new JSONObject();
//        if(StringUtils.isNotEmpty(payDataType)) {
//            extParams.put("payDataType", payDataType.trim());
//        }
//        if(StringUtils.isNotEmpty(authCode)) {
//            extParams.put("authCode", authCode.trim());
//        }
//        model.setChannelExtra(extParams.toString());
//
//        JeepayClient jeepayClient = new JeepayClient(dbApplicationConfig.getPaySiteUrl(), mchApp.getAppSecret());
//
//        try {
//            PayOrderCreateResponse response = jeepayClient.execute(request);
//            if(response.getCode() != 0){
//                throw new BizException(response.getMsg());
//            }
//            return ApiRes.ok(response.get());
//        } catch (JeepayException e) {
//            throw new BizException(e.getMessage());
//        }
    }
}
