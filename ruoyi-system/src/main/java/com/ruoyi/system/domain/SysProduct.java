package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品对象 sys_product
 * 
 * @author ruoyi
 * @date 2023-04-12
 */
public class SysProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商品id */
    private Long id;

    /** 商户Id */
    @Excel(name = "商户Id")
    private Long merId;

    /** 商品图片 */
    @Excel(name = "商品图片")
    private String image;

    /** 展示图 */
    @Excel(name = "展示图")
    private String flatPattern;

    /** 轮播图 */
    @Excel(name = "轮播图")
    private String sliderImage;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String name;

    /** 商品简介 */
    @Excel(name = "商品简介")
    private String intro;

    /** 关键字,英文逗号拼接 */
    @Excel(name = "关键字,英文逗号拼接")
    private String keyword;

    /** 商户分类id(逗号拼接) */
    @Excel(name = "商户分类id(逗号拼接)")
    private String cateId;

    /** 品牌id */
    @Excel(name = "品牌id")
    private Long brandId;

    /** 平台分类id */
    @Excel(name = "平台分类id")
    private Long categoryId;

    /** 保障服务ids(英文逗号拼接) */
    @Excel(name = "保障服务ids(英文逗号拼接)")
    private String guaranteeIds;

    /** 商品价格 */
    @Excel(name = "商品价格")
    private BigDecimal price;

    /** 会员价格 */
    @Excel(name = "会员价格")
    private BigDecimal vipPrice;

    /** 市场价 */
    @Excel(name = "市场价")
    private BigDecimal otPrice;

    /** 单位名 */
    @Excel(name = "单位名")
    private String unitName;

    /** 销量 */
    @Excel(name = "销量")
    private Long sales;

    /** 库存 */
    @Excel(name = "库存")
    private Long stock;

    /** 成本价 */
    @Excel(name = "成本价")
    private BigDecimal cost;

    /** 虚拟销量 */
    @Excel(name = "虚拟销量")
    private Long ficti;

    /** 浏览量 */
    @Excel(name = "浏览量")
    private Long browse;

    /** 商品二维码地址(用户小程序海报) */
    @Excel(name = "商品二维码地址(用户小程序海报)")
    private String codePath;

    /** 淘宝京东1688类型 */
    @Excel(name = "淘宝京东1688类型")
    private String soureLink;

    /** 主图视频链接 */
    @Excel(name = "主图视频链接")
    private String videoLink;

    /** 运费模板ID */
    @Excel(name = "运费模板ID")
    private Long tempId;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    /** 总后台排序 */
    @Excel(name = "总后台排序")
    private Long rank;

    /** 规格 0单 1多 */
    @Excel(name = "规格 0单 1多")
    private Integer specType;

    /** 是否回收站 */
    @Excel(name = "是否回收站")
    private Integer isRecycle;

    /** 是否单独分佣 */
    @Excel(name = "是否单独分佣")
    private Integer isSub;

    /** 状态（0：未上架，1：上架） */
    @Excel(name = "状态", readConverterExp = "0=：未上架，1：上架")
    private Integer isShow;

    /** 审核状态：0-无需审核 1-待审核，2-审核成功，3-审核拒绝 */
    @Excel(name = "审核状态：0-无需审核 1-待审核，2-审核成功，3-审核拒绝")
    private Integer auditStatus;

    /** 是否加入审核，0-正常，1-审核流程中 */
    @Excel(name = "是否加入审核，0-正常，1-审核流程中")
    private Integer isAudit;

    /** 拒绝原因 */
    @Excel(name = "拒绝原因")
    private String reason;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Integer isDel;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMerId(Long merId) 
    {
        this.merId = merId;
    }

    public Long getMerId() 
    {
        return merId;
    }
    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
    }
    public void setFlatPattern(String flatPattern) 
    {
        this.flatPattern = flatPattern;
    }

    public String getFlatPattern() 
    {
        return flatPattern;
    }
    public void setSliderImage(String sliderImage) 
    {
        this.sliderImage = sliderImage;
    }

    public String getSliderImage() 
    {
        return sliderImage;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setIntro(String intro) 
    {
        this.intro = intro;
    }

    public String getIntro() 
    {
        return intro;
    }
    public void setKeyword(String keyword) 
    {
        this.keyword = keyword;
    }

    public String getKeyword() 
    {
        return keyword;
    }
    public void setCateId(String cateId) 
    {
        this.cateId = cateId;
    }

    public String getCateId() 
    {
        return cateId;
    }
    public void setBrandId(Long brandId) 
    {
        this.brandId = brandId;
    }

    public Long getBrandId() 
    {
        return brandId;
    }
    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }
    public void setGuaranteeIds(String guaranteeIds) 
    {
        this.guaranteeIds = guaranteeIds;
    }

    public String getGuaranteeIds() 
    {
        return guaranteeIds;
    }
    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }
    public void setVipPrice(BigDecimal vipPrice) 
    {
        this.vipPrice = vipPrice;
    }

    public BigDecimal getVipPrice() 
    {
        return vipPrice;
    }
    public void setOtPrice(BigDecimal otPrice) 
    {
        this.otPrice = otPrice;
    }

    public BigDecimal getOtPrice() 
    {
        return otPrice;
    }
    public void setUnitName(String unitName) 
    {
        this.unitName = unitName;
    }

    public String getUnitName() 
    {
        return unitName;
    }
    public void setSales(Long sales) 
    {
        this.sales = sales;
    }

    public Long getSales() 
    {
        return sales;
    }
    public void setStock(Long stock) 
    {
        this.stock = stock;
    }

    public Long getStock() 
    {
        return stock;
    }
    public void setCost(BigDecimal cost) 
    {
        this.cost = cost;
    }

    public BigDecimal getCost() 
    {
        return cost;
    }
    public void setFicti(Long ficti) 
    {
        this.ficti = ficti;
    }

    public Long getFicti() 
    {
        return ficti;
    }
    public void setBrowse(Long browse) 
    {
        this.browse = browse;
    }

    public Long getBrowse() 
    {
        return browse;
    }
    public void setCodePath(String codePath) 
    {
        this.codePath = codePath;
    }

    public String getCodePath() 
    {
        return codePath;
    }
    public void setSoureLink(String soureLink) 
    {
        this.soureLink = soureLink;
    }

    public String getSoureLink() 
    {
        return soureLink;
    }
    public void setVideoLink(String videoLink) 
    {
        this.videoLink = videoLink;
    }

    public String getVideoLink() 
    {
        return videoLink;
    }
    public void setTempId(Long tempId) 
    {
        this.tempId = tempId;
    }

    public Long getTempId() 
    {
        return tempId;
    }
    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }
    public void setRank(Long rank) 
    {
        this.rank = rank;
    }

    public Long getRank() 
    {
        return rank;
    }
    public void setSpecType(Integer specType) 
    {
        this.specType = specType;
    }

    public Integer getSpecType() 
    {
        return specType;
    }
    public void setIsRecycle(Integer isRecycle) 
    {
        this.isRecycle = isRecycle;
    }

    public Integer getIsRecycle() 
    {
        return isRecycle;
    }
    public void setIsSub(Integer isSub) 
    {
        this.isSub = isSub;
    }

    public Integer getIsSub() 
    {
        return isSub;
    }
    public void setIsShow(Integer isShow) 
    {
        this.isShow = isShow;
    }

    public Integer getIsShow() 
    {
        return isShow;
    }
    public void setAuditStatus(Integer auditStatus) 
    {
        this.auditStatus = auditStatus;
    }

    public Integer getAuditStatus() 
    {
        return auditStatus;
    }
    public void setIsAudit(Integer isAudit) 
    {
        this.isAudit = isAudit;
    }

    public Integer getIsAudit() 
    {
        return isAudit;
    }
    public void setReason(String reason) 
    {
        this.reason = reason;
    }

    public String getReason() 
    {
        return reason;
    }
    public void setIsDel(Integer isDel) 
    {
        this.isDel = isDel;
    }

    public Integer getIsDel() 
    {
        return isDel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("merId", getMerId())
            .append("image", getImage())
            .append("flatPattern", getFlatPattern())
            .append("sliderImage", getSliderImage())
            .append("name", getName())
            .append("intro", getIntro())
            .append("keyword", getKeyword())
            .append("cateId", getCateId())
            .append("brandId", getBrandId())
            .append("categoryId", getCategoryId())
            .append("guaranteeIds", getGuaranteeIds())
            .append("price", getPrice())
            .append("vipPrice", getVipPrice())
            .append("otPrice", getOtPrice())
            .append("unitName", getUnitName())
            .append("sales", getSales())
            .append("stock", getStock())
            .append("cost", getCost())
            .append("ficti", getFicti())
            .append("browse", getBrowse())
            .append("codePath", getCodePath())
            .append("soureLink", getSoureLink())
            .append("videoLink", getVideoLink())
            .append("tempId", getTempId())
            .append("sort", getSort())
            .append("rank", getRank())
            .append("specType", getSpecType())
            .append("isRecycle", getIsRecycle())
            .append("isSub", getIsSub())
            .append("isShow", getIsShow())
            .append("auditStatus", getAuditStatus())
            .append("isAudit", getIsAudit())
            .append("reason", getReason())
            .append("isDel", getIsDel())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
