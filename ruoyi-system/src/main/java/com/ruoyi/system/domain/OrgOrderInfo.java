package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单对象 org_order_info
 *
 * @author ruoyi
 * @date 2023-03-08
 */
public class OrgOrderInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 客户ID */
    @Excel(name = "客户ID")
    private Long accountId;

    private String clientIp; //客户IP地址

    private String uid; //客户用户ID

    private String subject; //商品主题

    private String accountAppId;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String accountName;

    /** 通道ID */
    @Excel(name = "通道ID")
    private Long channelId;

    /** 通道名称 */
    @Excel(name = "通道名称")
    private String channelName;

    /** 商户ID */
    @Excel(name = "商户ID")
    private Long merchantId;

    /** 商户名称 */
    @Excel(name = "商户名称")
    private String merchantName;

    /** 支付宝appid */
    private String merchantNo;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderNo;

    /** 客户订单号 */
    @Excel(name = "客户订单号")
    private String accountOrderNo;

    /** 商户订单号 */
    @Excel(name = "商户订单号")
    private String merchanOrderNo;

    /** 订单金额 */
    @Excel(name = "订单金额")
    private BigDecimal amount;
    private BigDecimal yjamount;
    /** 订单金额 */
    @Excel(name = "订单议价金额")
    private BigDecimal myAmount;

    /** 支付方式 */
    @Excel(name = "支付方式")
    private Long method;

    /** 支付地址 */
    @Excel(name = "支付地址")
    private String payUrl;

    /** 回调地址 */
    @Excel(name = "回调地址")
    private String callbackUrl;

    /** 回调状态 */
    @Excel(name = "回调状态")
    private Long callbackStatus;

    /** 回调时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "回调时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date callbackTime;

    /** 跳转地址 */
    @Excel(name = "跳转地址")
    private String returnUrl;

    /** 订单内容 */
    @Excel(name = "订单内容")
    private String bodys;

    /** response */
    @Excel(name = "response")
    private String response;

    /** orderKey */
    @Excel(name = "orderKey")
    private String orderKey;

    /** 订单状态 */
    @Excel(name = "订单状态")
    private Long orderStatus;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completionTime;

    /** 退款时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "退款时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date returnTime;


    private Long cashier;//是否要收银台   1显示    或  0 不显示


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }

    public Long getAccountId()
    {
        return accountId;
    }
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    public String getAccountName()
    {
        return accountName;
    }
    public void setChannelId(Long channelId)
    {
        this.channelId = channelId;
    }

    public Long getChannelId()
    {
        return channelId;
    }
    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }

    public String getChannelName()
    {
        return channelName;
    }
    public void setMerchantId(Long merchantId)
    {
        this.merchantId = merchantId;
    }

    public Long getMerchantId()
    {
        return merchantId;
    }
    public void setMerchantName(String merchantName)
    {
        this.merchantName = merchantName;
    }

    public String getMerchantName()
    {
        return merchantName;
    }
    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo()
    {
        return orderNo;
    }
    public void setAccountOrderNo(String accountOrderNo)
    {
        this.accountOrderNo = accountOrderNo;
    }

    public String getAccountOrderNo()
    {
        return accountOrderNo;
    }
    public void setMerchanOrderNo(String merchanOrderNo)
    {
        this.merchanOrderNo = merchanOrderNo;
    }

    public String getMerchanOrderNo()
    {
        return merchanOrderNo;
    }
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }
    public void setMethod(Long method)
    {
        this.method = method;
    }

    public Long getMethod()
    {
        return method;
    }
    public void setPayUrl(String payUrl)
    {
        this.payUrl = payUrl;
    }

    public String getPayUrl()
    {
        return payUrl;
    }
    public void setCallbackUrl(String callbackUrl)
    {
        this.callbackUrl = callbackUrl;
    }

    public String getCallbackUrl()
    {
        return callbackUrl;
    }
    public void setCallbackStatus(Long callbackStatus)
    {
        this.callbackStatus = callbackStatus;
    }

    public Long getCallbackStatus()
    {
        return callbackStatus;
    }
    public void setCallbackTime(Date callbackTime)
    {
        this.callbackTime = callbackTime;
    }

    public Date getCallbackTime()
    {
        return callbackTime;
    }
    public void setReturnUrl(String returnUrl)
    {
        this.returnUrl = returnUrl;
    }

    public String getReturnUrl()
    {
        return returnUrl;
    }
    public void setBodys(String bodys)
    {
        this.bodys = bodys;
    }

    public String getBodys()
    {
        return bodys;
    }
    public void setResponse(String response)
    {
        this.response = response;
    }

    public String getResponse()
    {
        return response;
    }
    public void setOrderKey(String orderKey)
    {
        this.orderKey = orderKey;
    }

    public String getOrderKey()
    {
        return orderKey;
    }
    public void setOrderStatus(Long orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public Long getOrderStatus()
    {
        return orderStatus;
    }
    public void setCompletionTime(Date completionTime)
    {
        this.completionTime = completionTime;
    }

    public Date getCompletionTime()
    {
        return completionTime;
    }
    public void setReturnTime(Date returnTime)
    {
        this.returnTime = returnTime;
    }

    public Date getReturnTime()
    {
        return returnTime;
    }

    public String getAcountAppId() {
        return accountAppId;
    }

    public void setAcountAppId(String appid) {
        this.accountAppId = appid;
    }

    public String getAccountAppId() {
        return accountAppId;
    }

    public void setAccountAppId(String accountAppId) {
        this.accountAppId = accountAppId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public BigDecimal getMyAmount() {
        return myAmount;
    }

    public void setMyAmount(BigDecimal myAmount) {
        this.myAmount = myAmount;
    }
    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Long getCashier() {
        return cashier;
    }

    public void setCashier(Long cashier) {
        this.cashier = cashier;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("accountId", getAccountId())
            .append("accountName", getAccountName())
            .append("channelId", getChannelId())
            .append("channelName", getChannelName())
            .append("merchantId", getMerchantId())
            .append("merchantName", getMerchantName())
            .append("orderNo", getOrderNo())
            .append("accountOrderNo", getAccountOrderNo())
            .append("merchanOrderNo", getMerchanOrderNo())
            .append("amount", getAmount())
            .append("myAmount", getMyAmount())
            .append("method", getMethod())
            .append("payUrl", getPayUrl())
            .append("callbackUrl", getCallbackUrl())
            .append("callbackStatus", getCallbackStatus())
            .append("callbackTime", getCallbackTime())
            .append("returnUrl", getReturnUrl())
            .append("bodys", getBodys())
            .append("response", getResponse())
            .append("orderKey", getOrderKey())
            .append("orderStatus", getOrderStatus())
            .append("completionTime", getCompletionTime())
            .append("returnTime", getReturnTime())
            .append("createTime", getCreateTime())
            .append("cashier", getCashier())
            .toString();
    }
}
