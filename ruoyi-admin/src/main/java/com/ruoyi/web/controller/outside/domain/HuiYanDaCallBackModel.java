package com.ruoyi.web.controller.outside.domain;

public class HuiYanDaCallBackModel {
    private String payOrderId;
    private String mchNo;
    private String appId;
    private String mchOrderNo;
    private String ifCode;
    private String wayCode;
    private int amount;
    private String currency;
    private int state;
    private String clientIp;
    private String subject;
    private String body;
    private String channelOrderNo;
    private String errCode;
    private String errMsg;
    private String extParam;
    private long createdAt;
    private long successTime;
    private String reqTime;
    private String sign;

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getMchNo() {
        return mchNo;
    }

    public void setMchNo(String mchNo) {
        this.mchNo = mchNo;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public String getIfCode() {
        return ifCode;
    }

    public void setIfCode(String ifCode) {
        this.ifCode = ifCode;
    }

    public String getWayCode() {
        return wayCode;
    }

    public void setWayCode(String wayCode) {
        this.wayCode = wayCode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getExtParam() {
        return extParam;
    }

    public void setExtParam(String extParam) {
        this.extParam = extParam;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(long successTime) {
        this.successTime = successTime;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "HuiYanDaCallBackModel{" +
                "payOrderId='" + payOrderId + '\'' +
                ", mchNo='" + mchNo + '\'' +
                ", appId='" + appId + '\'' +
                ", mchOrderNo='" + mchOrderNo + '\'' +
                ", ifCode='" + ifCode + '\'' +
                ", wayCode='" + wayCode + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", state=" + state +
                ", clientIp='" + clientIp + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", channelOrderNo='" + channelOrderNo + '\'' +
                ", errCode='" + errCode + '\'' +
                ", errMsg='" + errMsg + '\'' +
                ", extParam='" + extParam + '\'' +
                ", createdAt=" + createdAt +
                ", successTime=" + successTime +
                ", reqTime='" + reqTime + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
