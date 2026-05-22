package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商户对象 org_merchant
 * 
 * @author ruoyi
 * @date 2023-03-08
 */
public class OrgMerchant extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 商户名称 */
    @Excel(name = "商户名称")
    private String merchantName;

    /** 状态 */
    @Excel(name = "状态")
    private Long merchantStatus;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String merchantPhone;

    /** 商户类型 */
    @Excel(name = "商户类型")
    private String merchantType;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMerchantName(String merchantName) 
    {
        this.merchantName = merchantName;
    }

    public String getMerchantName() 
    {
        return merchantName;
    }
    public void setMerchantStatus(Long merchantStatus) 
    {
        this.merchantStatus = merchantStatus;
    }

    public Long getMerchantStatus() 
    {
        return merchantStatus;
    }
    public void setMerchantPhone(String merchantPhone) 
    {
        this.merchantPhone = merchantPhone;
    }

    public String getMerchantPhone() 
    {
        return merchantPhone;
    }
    public void setMerchantType(String merchantType) 
    {
        this.merchantType = merchantType;
    }

    public String getMerchantType() 
    {
        return merchantType;
    }
    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("merchantName", getMerchantName())
            .append("merchantStatus", getMerchantStatus())
            .append("merchantPhone", getMerchantPhone())
            .append("merchantType", getMerchantType())
            .append("createdTime", getCreatedTime())
            .toString();
    }
}
