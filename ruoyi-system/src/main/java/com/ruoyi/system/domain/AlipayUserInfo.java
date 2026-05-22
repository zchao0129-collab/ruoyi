package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 支付宝用户信息对象 alipay_user_info
 *
 * @author ruoyi
 * @date 2023-08-19
 */
public class AlipayUserInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** uid */
    @Excel(name = "uid")
    private String uid;

    @Excel(name = "appid")
    private String appid;

    private Long iszt;

    private Long initCount;

    private Long payCount;
    /** ip地址 */
    @Excel(name = "ip地址")
    private String ipadd;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date gmtCreate;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getUid()
    {
        return uid;
    }
    public void setIpadd(String ipadd)
    {
        this.ipadd = ipadd;
    }

    public String getIpadd()
    {
        return ipadd;
    }
    public void setGmtCreate(Date gmtCreate)
    {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtCreate()
    {
        return gmtCreate;
    }

    public Long getIszt() {
        return iszt;
    }

    public void setIszt(Long iszt) {
        this.iszt = iszt;
    }

    public Long getInitCount() {
        return initCount;
    }

    public void setInitCount(Long initCount) {
        this.initCount = initCount;
    }

    public Long getPayCount() {
        return payCount;
    }

    public void setPayCount(Long payCount) {
        this.payCount = payCount;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    @Override
    public String toString() {
        return "AlipayUserInfo{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", appid='" + appid + '\'' +
                ", iszt=" + iszt +
                ", initCount=" + initCount +
                ", payCount=" + payCount +
                ", ipadd='" + ipadd + '\'' +
                ", gmtCreate=" + gmtCreate +
                '}';
    }
}
