package com.ruoyi.web.controller.outside.domain;

public class XiaoJuCallBackModel {
    private String memberid;
    private String orderid;
    private String successdate;
    private String status;
    private String attach;
    private String buyer_id;
    private String out_trade_id;

    private String  notifyData;
    private String num;
    private String  sign;

    public String getNotifyData() {
        return notifyData;
    }

    public void setNotifyData(String notifyData) {
        this.notifyData = notifyData;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getSuccessdate() {
        return successdate;
    }

    public void setSuccessdate(String successdate) {
        this.successdate = successdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getOut_trade_id() {
        return out_trade_id;
    }

    public void setOut_trade_id(String out_trade_id) {
        this.out_trade_id = out_trade_id;
    }


    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
