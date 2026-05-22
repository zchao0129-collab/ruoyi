package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 商户通道对象 org_channel_merchant
 *
 * @author ruoyi
 * @date 2023-03-08
 */
public class OrgChannelMerchant extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 通道ID */
    @Excel(name = "通道ID")
    private Long channelId;
    private String channelName;
    private String createrOrderUrl;
    private String callbackUrl;

    /** 商户ID */
    @Excel(name = "商户ID")
    private Long merchantId;
    private String merchantName;
    private String merchantNo;//商户编码 针对直付通给出的商户编码
    /** 商户秘钥 */
    @Excel(name = "商户秘钥")
    private String merchanKey;

    /** 公钥 */
    @Excel(name = "公钥")
    private String publicKey;

    /** 私钥 */
    @Excel(name = "私钥")
    private String privateKey;

    /** 平台公钥 */
    @Excel(name = "平台公钥")
    private String passPublicKey;

    /** 总金额 */
    @Excel(name = "总金额")
    private Long totleAmount;

    /**
     * 收款比率
     */
    private BigDecimal collectionRatio;

    /** 当日金额 */
    @Excel(name = "当日金额")
    private Long totleTodayAmount;

    /** 当日日期 */
    @Excel(name = "当日日期")
    private String totleToday;

    /** 商户状态 */
    private Long merchantChannelStatus;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setChannelId(Long channelId)
    {
        this.channelId = channelId;
    }

    public Long getChannelId()
    {
        return channelId;
    }
    public void setMerchantId(Long merchantId)
    {
        this.merchantId = merchantId;
    }

    public Long getMerchantId()
    {
        return merchantId;
    }
    public void setMerchanKey(String merchanKey)
    {
        this.merchanKey = merchanKey;
    }

    public String getMerchanKey()
    {
        return merchanKey;
    }
    public void setPublicKey(String publicKey)
    {
        this.publicKey = publicKey;
    }

    public String getPublicKey()
    {
        return publicKey;
    }
    public void setPrivateKey(String privateKey)
    {
        this.privateKey = privateKey;
    }

    public String getPrivateKey()
    {
        return privateKey;
    }
    public void setPassPublicKey(String passPublicKey)
    {
        this.passPublicKey = passPublicKey;
    }

    public String getPassPublicKey()
    {
        return passPublicKey;
    }
    public void setTotleAmount(Long totleAmount)
    {
        this.totleAmount = totleAmount;
    }

    public Long getTotleAmount()
    {
        return totleAmount;
    }
    public void setTotleTodayAmount(Long totleTodayAmount)
    {
        this.totleTodayAmount = totleTodayAmount;
    }

    public Long getTotleTodayAmount()
    {
        return totleTodayAmount;
    }
    public void setTotleToday(String totleToday)
    {
        this.totleToday = totleToday;
    }

    public String getTotleToday()
    {
        return totleToday;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getMerchantChannelStatus() {
        return merchantChannelStatus;
    }

    public void setMerchantChannelStatus(Long merchantChannelStatus) {
        this.merchantChannelStatus = merchantChannelStatus;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public BigDecimal getCollectionRatio() {
        return collectionRatio;
    }

    public void setCollectionRatio(BigDecimal collectionRatio) {
        this.collectionRatio = collectionRatio;
    }

    public String getCreaterOrderUrl() {
        return createrOrderUrl;
    }

    public void setCreaterOrderUrl(String createrOrderUrl) {
        this.createrOrderUrl = createrOrderUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("channelId", getChannelId())
            .append("merchantId", getMerchantId())
            .append("merchantNo", getMerchantNo())
            .append("merchanKey", getMerchanKey())
            .append("publicKey", getPublicKey())
            .append("privateKey", getPrivateKey())
            .append("passPublicKey", getPassPublicKey())
            .append("totleAmount", getTotleAmount())
            .append("totleTodayAmount", getTotleTodayAmount())
            .append("totleToday", getTotleToday())
            .toString();
    }
}
