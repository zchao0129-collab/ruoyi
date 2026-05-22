package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户对象 org_account
 *
 * @author ruoyi
 * @date 2023-03-08
 */
public class OrgAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String accountName;

    /** 登录密码 */
    @Excel(name = "登录密码")
    private String accountPwd;

    /** APPID */
    @Excel(name = "APPID")
    private String accountAppId;

    private Long cashier;//是否要收银台   1显示    或  0 不显示

    private Long payChannel;//收款渠道   0 原生通道  或  1直付通 或  90 小程序

    /** 客户状态 */
    @Excel(name = "客户状态")
    private Long accountStatus;

    /** token */
    @Excel(name = "token")
    private String accountToken;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    private BigDecimal totleTodayAmount;
    private BigDecimal totleTodayYjAmount;
    private BigDecimal totleAmount;
    private BigDecimal totleYjAmount;

    public Long getCashier() {
        return cashier;
    }

    public void setCashier(Long cashier) {
        this.cashier = cashier;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setAccountName(String accounName)
    {
        this.accountName = accounName;
    }

    public String getAccountName()
    {
        return accountName;
    }
    public void setAccountPwd(String accountPwd)
    {
        this.accountPwd = accountPwd;
    }

    public String getAccountPwd()
    {
        return accountPwd;
    }
    public void setAccountAppId(String accountAppId)
    {
        this.accountAppId = accountAppId;
    }

    public String getAccountAppId()
    {
        return accountAppId;
    }
    public void setAccountStatus(Long accountStatus)
    {
        this.accountStatus = accountStatus;
    }

    public Long getAccountStatus()
    {
        return accountStatus;
    }
    public void setAccountToken(String accountToken)
    {
        this.accountToken = accountToken;
    }

    public String getAccountToken()
    {
        return accountToken;
    }
    public void setCreatedTime(Date createdTime)
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }

    public BigDecimal getTotleTodayAmount() {
        return totleTodayAmount;
    }

    public void setTotleTodayAmount(BigDecimal totleTodayAmount) {
        this.totleTodayAmount = totleTodayAmount;
    }

    public BigDecimal getTotleTodayYjAmount() {
        return totleTodayYjAmount;
    }

    public void setTotleTodayYjAmount(BigDecimal totleTodayYjAmount) {
        this.totleTodayYjAmount = totleTodayYjAmount;
    }

    public BigDecimal getTotleAmount() {
        return totleAmount;
    }

    public void setTotleAmount(BigDecimal totleAmount) {
        this.totleAmount = totleAmount;
    }

    public BigDecimal getTotleYjAmount() {
        return totleYjAmount;
    }

    public void setTotleYjAmount(BigDecimal totleYjAmount) {
        this.totleYjAmount = totleYjAmount;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("accounName", getAccountName())
            .append("accountPwd", getAccountPwd())
            .append("accountAppId", getAccountAppId())
            .append("accountStatus", getAccountStatus())
            .append("accountToken", getAccountToken())
            .append("createdTime", getCreatedTime())
            .toString();
    }

    public Long getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Long payChannel) {
        this.payChannel = payChannel;
    }
}
