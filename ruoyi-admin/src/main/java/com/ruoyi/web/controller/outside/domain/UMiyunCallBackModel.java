package com.ruoyi.web.controller.outside.domain;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2023年02月13日 9:38
 */
public class UMiyunCallBackModel {

    private String customerid;
    /**
     *  商户订单号
     */
    private String sdorderno;
    /**
     *  平台订单号
     */
    private String sdpayno;
    /**
     *  交易金额
     */
    private String totalfee;	//通知回调业务处理过程中务必验证交易金额，支付失败返回交易金额为0

    /**
     *  支付类型
     */
    private String paytype;	//
    /**
     *  参照md5签名方法
     */
    private String sign;	//
    /**
     *  分区判断
     */
    private String server;	//	原样返回
    /**
     *  备注说明
     */
    private String remark;	//	原样返回


    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getSdorderno() {
        return sdorderno;
    }

    public void setSdorderno(String sdorderno) {
        this.sdorderno = sdorderno;
    }

    public String getSdpayno() {
        return sdpayno;
    }

    public void setSdpayno(String sdpayno) {
        this.sdpayno = sdpayno;
    }

    public String getTotalfee() {
        return totalfee;
    }

    public void setTotalfee(String totalfee) {
        this.totalfee = totalfee;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
