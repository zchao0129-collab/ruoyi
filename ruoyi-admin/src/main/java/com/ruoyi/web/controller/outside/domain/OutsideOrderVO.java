package com.ruoyi.web.controller.outside.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2023年03月16日 12:13
 */
public class OutsideOrderVO extends BaseEntity {
    private String appid;
    private String timestamps;
    private String merchantOrderNo;
    private String merchantNo;
    private String callbackUrl;
    private String returnUrl;
    private BigDecimal amount;
    private BigDecimal yjamount;
    private String sign;
    private String method;
    private String orderNo;

    private String uid;
    private String subject;
    private String regionName; //大区名称/店铺名称
    private Long cashier;//是否要收银台   1显示    或  0 不显示


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(String timestamps) {
        this.timestamps = timestamps;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public Long getCashier() {
        return cashier;
    }

    public void setCashier(Long cashier) {
        this.cashier = cashier;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public BigDecimal getYjamount() {
        return yjamount;
    }

    public void setYjamount(BigDecimal yjamount) {
        this.yjamount = yjamount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    @Override
    public String toString() {
        return "OutsideOrderVO{" +
                "appid='" + appid + '\'' +
                ", subject='" + subject + '\'' +
                ", timestamps='" + timestamps + '\'' +
                ", merchantOrderNo='" + merchantOrderNo + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                ", amount=" + amount +
                ", yjamount=" + yjamount +
                ", orderNo=" + orderNo +
                ", regionName=" + regionName +
                ", sign='" + sign + '\'' +
                ", cashier='" + cashier + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
