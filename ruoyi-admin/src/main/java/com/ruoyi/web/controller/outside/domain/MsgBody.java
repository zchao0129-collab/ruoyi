package com.ruoyi.web.controller.outside.domain;


import cn.hutool.core.date.DateTime;

public class MsgBody {
    /**
     * 转账账户ID
     */
    private String accountId;
    /**
     * 支付宝备注
     */
    private String alipayRemark;
    /**
     * 商户单号
     */
    private String channelMchId;
    /**
     * 订单创建时间
     */
    private String createTime;
    /**
     * 转账失败说明
     */
    private String description;
    /**
     * 转账详情订单ID
     */
    private String id;
    /**
     * 收款账号
     */
    private String identity;
    /**
     * 收款账号类型：ALIPAY_LOGON_ID=支付宝账号、ALIPAY_USER_ID=支付宝uid、BANKCARD_ACCOUNT=银行卡
     */
    private String identityType;
    /**
     * 所属银行
     */
    private String instName;
    /**
     * 支付宝订单ID
     */
    private String outOrderNo;
    /**
     * 收款人姓名
     */
    private String recipientName;
    /**
     * 转账金额
     */
    private String transferAmount;
    /**
     * 转账状态（0=待转账、1=转账成功、2=已终止/已拒绝、3=转账失败、4=转账中、5=失效）
     */
    private String transferStatus;
    /**
     * 转账任务ID
     */
    private String transferTaskId;
    /**
     * 转账时间
     */
    private DateTime transferTime;

    public String getAccountId() { return accountId; }
    public void setAccountId(String value) { this.accountId = value; }

    public String getAlipayRemark() { return alipayRemark; }
    public void setAlipayRemark(String value) { this.alipayRemark = value; }

    public String getChannelMchId() { return channelMchId; }
    public void setChannelMchId(String value) { this.channelMchId = value; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String value) { this.createTime = value; }

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }

    public String getid() { return id; }
    public void setid(String value) { this.id = value; }

    public String getIdentity() { return identity; }
    public void setIdentity(String value) { this.identity = value; }

    public String getIdentityType() { return identityType; }
    public void setIdentityType(String value) { this.identityType = value; }

    public String getInstName() { return instName; }
    public void setInstName(String value) { this.instName = value; }

    public String getOutOrderNo() { return outOrderNo; }
    public void setOutOrderNo(String value) { this.outOrderNo = value; }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String value) { this.recipientName = value; }

    public String getTransferAmount() { return transferAmount; }
    public void setTransferAmount(String value) { this.transferAmount = value; }

    public String getTransferStatus() { return transferStatus; }
    public void setTransferStatus(String value) { this.transferStatus = value; }

    public String getTransferTaskId() { return transferTaskId; }
    public void setTransferTaskId(String value) { this.transferTaskId = value; }

    public DateTime getTransferTime() { return transferTime; }
    public void setTransferTime(DateTime value) { this.transferTime = value; }
}
