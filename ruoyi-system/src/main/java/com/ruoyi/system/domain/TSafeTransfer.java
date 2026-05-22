package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 安全发对象 t_safe_transfer
 *
 * @author ruoyi
 * @date 2025-05-01
 */
public class TSafeTransfer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 商户号ID */
    @Excel(name = "商户号ID")
    private String appId;

    /** 请求时的时间戳 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "请求时的时间戳", width = 30, dateFormat = "yyyy-MM-dd")
    private Date timestamp;

    /** 收款人账号 */
    @Excel(name = "收款人账号")
    private String payeeInfoIdentity;

    /** 通道号ID */
    @Excel(name = "通道号ID")
    private Long channelId;

    /** 外部订单号 */
    @Excel(name = "外部订单号")
    private String outBizNo;

    /** 转账的金额，单位为“分”  */
    @Excel(name = "转账的金额，单位为“分” ")
    private Long transAmount;

    /** 收款人姓名 */
    @Excel(name = "收款人姓名")
    private String payeeInfoName;

    /** 转账类型（ALIPAY|BANKCARD） */
    @Excel(name = "转账类型", readConverterExp = "A=LIPAY|BANKCARD")
    private String transferType;

    /** 智库预检（LOW|MEDIUM|HIGH）三选一 */
    @Excel(name = "智库预检", readConverterExp = "L=OW|MEDIUM|HIGH")
    private String allowRiskLevel;

    /** 转账标题 （支付宝转账记录会显示） */
    @Excel(name = "转账标题 ", readConverterExp = "支=付宝转账记录会显示")
    private String orderTitle;

    /** 异步通知回调地址 */
    @Excel(name = "异步通知回调地址")
    private String notifyUrl;

    /** CREATE|SUCCESS|FAIL|DEALING|REFUND 新建|成功|失败|处理中|退回 */
    @Excel(name = "CREATE|SUCCESS|FAIL|DEALING|REFUND 新建|成功|失败|处理中|退回")
    private String status;

    /** 订单号1 */
    @Excel(name = "订单号1")
    private String orderId;

    /** 订单号2 */
    @Excel(name = "订单号2")
    private String payFundOrderId;

    /** 订单号3 */
    @Excel(name = "订单号3")
    private String settleSerialNo;

    /** 交易时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交易时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date transDate;

    /** 失败原因 */
    @Excel(name = "失败原因")
    private String failReason;

    /** 付款账户ID（发薪中心-账户管理-账户ID）
     */
    @Excel(name = "付款账户ID", readConverterExp = "发=薪中心-账户管理-账户ID")
    private String accountId;

    /** 银行卡机构名称
     */
    @Excel(name = "银行卡机构名称")
    private String instName;

    /** 转账任务ID */
    @Excel(name = "转账任务ID")
    private String taskId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getAppId()
    {
        return appId;
    }
    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }
    public void setPayeeInfoIdentity(String payeeInfoIdentity)
    {
        this.payeeInfoIdentity = payeeInfoIdentity;
    }

    public String getPayeeInfoIdentity()
    {
        return payeeInfoIdentity;
    }
    public void setChannelId(Long channelId)
    {
        this.channelId = channelId;
    }

    public Long getChannelId()
    {
        return channelId;
    }
    public void setOutBizNo(String outBizNo)
    {
        this.outBizNo = outBizNo;
    }

    public String getOutBizNo()
    {
        return outBizNo;
    }
    public void setTransAmount(Long transAmount)
    {
        this.transAmount = transAmount;
    }

    public Long getTransAmount()
    {
        return transAmount;
    }
    public void setPayeeInfoName(String payeeInfoName)
    {
        this.payeeInfoName = payeeInfoName;
    }

    public String getPayeeInfoName()
    {
        return payeeInfoName;
    }
    public void setTransferType(String transferType)
    {
        this.transferType = transferType;
    }

    public String getTransferType()
    {
        return transferType;
    }
    public void setAllowRiskLevel(String allowRiskLevel)
    {
        this.allowRiskLevel = allowRiskLevel;
    }

    public String getAllowRiskLevel()
    {
        return allowRiskLevel;
    }
    public void setOrderTitle(String orderTitle)
    {
        this.orderTitle = orderTitle;
    }

    public String getOrderTitle()
    {
        return orderTitle;
    }
    public void setNotifyUrl(String notifyUrl)
    {
        this.notifyUrl = notifyUrl;
    }

    public String getNotifyUrl()
    {
        return notifyUrl;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderId()
    {
        return orderId;
    }
    public void setPayFundOrderId(String payFundOrderId)
    {
        this.payFundOrderId = payFundOrderId;
    }

    public String getPayFundOrderId()
    {
        return payFundOrderId;
    }
    public void setSettleSerialNo(String settleSerialNo)
    {
        this.settleSerialNo = settleSerialNo;
    }

    public String getSettleSerialNo()
    {
        return settleSerialNo;
    }
    public void setTransDate(Date transDate)
    {
        this.transDate = transDate;
    }

    public Date getTransDate()
    {
        return transDate;
    }
    public void setFailReason(String failReason)
    {
        this.failReason = failReason;
    }

    public String getFailReason()
    {
        return failReason;
    }
    public void setAccountId(String accountId)
    {
        this.accountId = accountId;
    }

    public String getAccountId()
    {
        return accountId;
    }
    public void setInstName(String instName)
    {
        this.instName = instName;
    }

    public String getInstName()
    {
        return instName;
    }
    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public String getTaskId()
    {
        return taskId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("appId", getAppId())
                .append("timestamp", getTimestamp())
                .append("payeeInfoIdentity", getPayeeInfoIdentity())
                .append("channelId", getChannelId())
                .append("outBizNo", getOutBizNo())
                .append("transAmount", getTransAmount())
                .append("payeeInfoName", getPayeeInfoName())
                .append("transferType", getTransferType())
                .append("allowRiskLevel", getAllowRiskLevel())
                .append("orderTitle", getOrderTitle())
                .append("remark", getRemark())
                .append("notifyUrl", getNotifyUrl())
                .append("createTime", getCreateTime())
                .append("status", getStatus())
                .append("orderId", getOrderId())
                .append("payFundOrderId", getPayFundOrderId())
                .append("settleSerialNo", getSettleSerialNo())
                .append("transDate", getTransDate())
                .append("failReason", getFailReason())
                .append("accountId", getAccountId())
                .append("instName", getInstName())
                .append("taskId", getTaskId())
                .toString();
    }
}
