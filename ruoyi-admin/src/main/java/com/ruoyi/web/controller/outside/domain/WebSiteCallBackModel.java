package com.ruoyi.web.controller.outside.domain;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2023年02月09日 15:03
 */
public class WebSiteCallBackModel {

        private String payOrderId;

        private String mchId;

        private String appId;

        private String product_id;

        private String mchOrderNo;

        private String amount;

        private String income;

        private String status;

        private String channelOrderNo;

        private String param;

        private String paySuccTime;
    private String backType;
    private String requestTime;
    private String sign;

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getPaySuccTime() {
        return paySuccTime;
    }

    public void setPaySuccTime(String paySuccTime) {
        this.paySuccTime = paySuccTime;
    }

    public String getBackType() {
        return backType;
    }

    public void setBackType(String backType) {
        this.backType = backType;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "WebSiteCallBackModel{" +
                "payOrderId='" + payOrderId + '\'' +
                ", mchId='" + mchId + '\'' +
                ", appId='" + appId + '\'' +
                ", product_id='" + product_id + '\'' +
                ", mchOrderNo='" + mchOrderNo + '\'' +
                ", amount='" + amount + '\'' +
                ", income='" + income + '\'' +
                ", status='" + status + '\'' +
                ", channelOrderNo='" + channelOrderNo + '\'' +
                ", param='" + param + '\'' +
                ", paySuccTime='" + paySuccTime + '\'' +
                ", backType='" + backType + '\'' +
                ", requestTime='" + requestTime + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
