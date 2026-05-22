package com.ruoyi.web.controller.outside.domain;
// ApifoxModel.java

public class NewShengTondCallbackModel {
    /**
     * accessKey
     */
    private String accessKey;
    private MsgBody msgBody;
    /**
     * 通知类型（ALIPAY_FUND_BATCH_UNI_TRANSFER=转账订单状态变更消息）
     */
    private String msgType;
    /**
     * 通知ID
     */
    private long notifyId;
    /**
     * 数据签名（见签名算法文档）
     */
    private String sign;
    /**
     * 系统时间戳/毫秒
     */
    private long timestamp;

    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String value) { this.accessKey = value; }

    public MsgBody getMsgBody() { return msgBody; }
    public void setMsgBody(MsgBody value) { this.msgBody = value; }

    public String getMsgType() { return msgType; }
    public void setMsgType(String value) { this.msgType = value; }

    public long getNotifyId() { return notifyId; }
    public void setNotifyId(long value) { this.notifyId = value; }

    public String getSign() { return sign; }
    public void setSign(String value) { this.sign = value; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long value) { this.timestamp = value; }
}
