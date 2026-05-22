package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * tradecomplain对象 org_trade_complain
 * 
 * @author ruoyi
 * @date 2023-08-18
 */
public class OrgTradeComplain extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 应用id，例如小程序id、生活号id、商家pid */
    @Excel(name = "应用id，例如小程序id、生活号id、商家pid")
    private String targetId;

    /** 应用类型小程序为：APPID生活号为：PUBLICID商家为：PID */
    @Excel(name = "应用类型小程序为：APPID生活号为：PUBLICID商家为：PID")
    private String targetType;

    /** 支付宝侧投诉单号 */
    @Excel(name = "支付宝侧投诉单号")
    private String complainEventId;

    /** 状态商家处理中：MERCHANT_PROCESSING商家已反馈：MERCHANT_FEEDBACKED投诉已完结：FINISHED投诉已撤销：CANCELLED平台处理中：PLATFORM_PROCESSING平台处理完结：PLATFORM_FINISH系统关闭：CLOSED */
    @Excel(name = "状态商家处理中：MERCHANT_PROCESSING商家已反馈：MERCHANT_FEEDBACKED投诉已完结：FINISHED投诉已撤销：CANCELLED平台处理中：PLATFORM_PROCESSING平台处理完结：PLATFORM_FINISH系统关闭：CLOSED")
    private String status;

    /** 支付宝交易号 */
    @Excel(name = "支付宝交易号")
    private String tradeNo;

    /** 商家订单号 */
    @Excel(name = "商家订单号")
    private String merchantOrderNo;

    /** 投诉单创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投诉单创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gmtCreate;

    /** 投诉单修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投诉单修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gmtModified;

    /** 投诉单结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投诉单结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gmtFinished;

    /** 投诉诉求 */
    @Excel(name = "投诉诉求")
    private String leafCategoryName;

    /** 投诉原因 */
    @Excel(name = "投诉原因")
    private String complainReason;

    /** 投诉内容 */
    @Excel(name = "投诉内容")
    private String content;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTargetId(String targetId) 
    {
        this.targetId = targetId;
    }

    public String getTargetId() 
    {
        return targetId;
    }
    public void setTargetType(String targetType) 
    {
        this.targetType = targetType;
    }

    public String getTargetType() 
    {
        return targetType;
    }
    public void setComplainEventId(String complainEventId) 
    {
        this.complainEventId = complainEventId;
    }

    public String getComplainEventId() 
    {
        return complainEventId;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setTradeNo(String tradeNo) 
    {
        this.tradeNo = tradeNo;
    }

    public String getTradeNo() 
    {
        return tradeNo;
    }
    public void setMerchantOrderNo(String merchantOrderNo) 
    {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getMerchantOrderNo() 
    {
        return merchantOrderNo;
    }
    public void setGmtCreate(Date gmtCreate) 
    {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtCreate() 
    {
        return gmtCreate;
    }
    public void setGmtModified(Date gmtModified) 
    {
        this.gmtModified = gmtModified;
    }

    public Date getGmtModified() 
    {
        return gmtModified;
    }
    public void setGmtFinished(Date gmtFinished) 
    {
        this.gmtFinished = gmtFinished;
    }

    public Date getGmtFinished() 
    {
        return gmtFinished;
    }
    public void setLeafCategoryName(String leafCategoryName) 
    {
        this.leafCategoryName = leafCategoryName;
    }

    public String getLeafCategoryName() 
    {
        return leafCategoryName;
    }
    public void setComplainReason(String complainReason) 
    {
        this.complainReason = complainReason;
    }

    public String getComplainReason() 
    {
        return complainReason;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("targetId", getTargetId())
            .append("targetType", getTargetType())
            .append("complainEventId", getComplainEventId())
            .append("status", getStatus())
            .append("tradeNo", getTradeNo())
            .append("merchantOrderNo", getMerchantOrderNo())
            .append("gmtCreate", getGmtCreate())
            .append("gmtModified", getGmtModified())
            .append("gmtFinished", getGmtFinished())
            .append("leafCategoryName", getLeafCategoryName())
            .append("complainReason", getComplainReason())
            .append("content", getContent())
            .toString();
    }
}
