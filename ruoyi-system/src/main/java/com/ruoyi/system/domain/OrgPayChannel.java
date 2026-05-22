package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 通道对象 org_pay_channel
 * 
 * @author ruoyi
 * @date 2023-03-08
 */
public class OrgPayChannel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String channelName;

    /** 状态 */
    @Excel(name = "状态")
    private Long channelStatus;

    /** 创建订单地址 */
    @Excel(name = "创建订单地址")
    private String createrOrderUrl;

    /** 回调地址 */
    @Excel(name = "回调地址")
    private String callbackUrl;

    /** 跳转地址 */
    @Excel(name = "跳转地址")
    private String refundUrl;

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
    public void setChannelName(String channelName) 
    {
        this.channelName = channelName;
    }

    public String getChannelName() 
    {
        return channelName;
    }
    public void setChannelStatus(Long channelStatus) 
    {
        this.channelStatus = channelStatus;
    }

    public Long getChannelStatus() 
    {
        return channelStatus;
    }
    public void setCreaterOrderUrl(String createrOrderUrl) 
    {
        this.createrOrderUrl = createrOrderUrl;
    }

    public String getCreaterOrderUrl() 
    {
        return createrOrderUrl;
    }
    public void setCallbackUrl(String callbackUrl) 
    {
        this.callbackUrl = callbackUrl;
    }

    public String getCallbackUrl() 
    {
        return callbackUrl;
    }
    public void setRefundUrl(String refundUrl) 
    {
        this.refundUrl = refundUrl;
    }

    public String getRefundUrl() 
    {
        return refundUrl;
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
            .append("channelName", getChannelName())
            .append("channelStatus", getChannelStatus())
            .append("createrOrderUrl", getCreaterOrderUrl())
            .append("callbackUrl", getCallbackUrl())
            .append("refundUrl", getRefundUrl())
            .append("createdTime", getCreatedTime())
            .toString();
    }
}
