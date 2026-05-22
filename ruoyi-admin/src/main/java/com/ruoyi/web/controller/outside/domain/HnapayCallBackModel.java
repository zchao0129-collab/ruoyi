package com.ruoyi.web.controller.outside.domain;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2023年02月09日 15:03
 */
public class HnapayCallBackModel {

        private String tranCode;

        private String version;

        private String merId;

        private String merOrderNum;

        private String tranAmt;

        private String submitTime;

        private String hnapayOrderId;

        private String tranFinishTime;

        private String respCode;

        private String charset;

        private String signType;

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getMerOrderNum() {
        return merOrderNum;
    }

    public void setMerOrderNum(String merOrderNum) {
        this.merOrderNum = merOrderNum;
    }

    public String getTranAmt() {
        return tranAmt;
    }

    public void setTranAmt(String tranAmt) {
        this.tranAmt = tranAmt;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getHnapayOrderId() {
        return hnapayOrderId;
    }

    public void setHnapayOrderId(String hnapayOrderId) {
        this.hnapayOrderId = hnapayOrderId;
    }

    public String getTranFinishTime() {
        return tranFinishTime;
    }

    public void setTranFinishTime(String tranFinishTime) {
        this.tranFinishTime = tranFinishTime;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    @Override
    public String toString() {
        return "HnapayCallBackModel{" +
                "tranCode='" + tranCode + '\'' +
                ", version='" + version + '\'' +
                ", merId='" + merId + '\'' +
                ", merOrderNum='" + merOrderNum + '\'' +
                ", tranAmt='" + tranAmt + '\'' +
                ", submitTime='" + submitTime + '\'' +
                ", hnapayOrderId='" + hnapayOrderId + '\'' +
                ", tranFinishTime='" + tranFinishTime + '\'' +
                ", respCode='" + respCode + '\'' +
                ", charset='" + charset + '\'' +
                ", signType='" + signType + '\'' +
                '}';
    }
}
