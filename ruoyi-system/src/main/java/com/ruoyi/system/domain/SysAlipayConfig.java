package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * alipayConfig对象 sys_alipay_config
 *
 * @author ruoyi
 * @date 2023-06-10
 */
public class SysAlipayConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 商户appid */
    @Excel(name = "商户appid")
    private String APPID;

    /** 应用私钥  */
    @Excel(name = "应用私钥")
    private String rsaPrivateKey;

    /** 异步通知地址 */
    @Excel(name = "异步通知地址")
    private String notifyUrl;

    /** 同步跳转地址 */
    @Excel(name = "同步跳转地址")
    private String returnUrl;

    /** 请求网关地址 */
    @Excel(name = "请求网关地址")
    private String URL;

    /** 编码 */
    @Excel(name = "编码")
    private String CHARSET;

    /** 返回格式 */
    @Excel(name = "返回格式")
    private String FORMAT;

    /** 支付宝私钥 */
    @Excel(name = "支付宝私钥")
    private String alipayPrivateKey;

    /** 支付宝公钥 */
    @Excel(name = "支付宝公钥")
    private String alipayPublicKey;

    /** 日志记录目录 */
    @Excel(name = "日志记录目录")
    private String logPath;

    /** 用户 UID */
    @Excel(name = "用户 UID")
    private String SIGNTYPE;

    /** 状态 0=禁用 1=启用 */
    @Excel(name = "状态", readConverterExp = "0=禁用,1=启用")
    private Integer alipayStatus;

    /** 验证方式 0=秘钥 1=证书 */
    @Excel(name = "验证方式", readConverterExp = "0=秘钥,1=证书")
    private Integer keyOrCert;

    private Integer weightStart;

    private Integer weightEnd;
    /** 应用公钥证书路径 */
    private String appCertPath;
    /** 支付宝公钥证书文件路径 */
    private String alipayCertPath;
    /** 支付宝CA根证书文件路径 */
    private String  alipayRootCertPath;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setAPPID(String APPID)
    {
        this.APPID = APPID;
    }

    public String getAPPID()
    {
        return APPID;
    }
    public void setRsaPrivateKey(String rsaPrivateKey)
    {
        this.rsaPrivateKey = rsaPrivateKey;
    }

    public String getRsaPrivateKey()
    {
        return rsaPrivateKey;
    }
    public void setNotifyUrl(String notifyUrl)
    {
        this.notifyUrl = notifyUrl;
    }

    public String getNotifyUrl()
    {
        return notifyUrl;
    }
    public void setReturnUrl(String returnUrl)
    {
        this.returnUrl = returnUrl;
    }

    public String getReturnUrl()
    {
        return returnUrl;
    }
    public void setURL(String URL)
    {
        this.URL = URL;
    }

    public String getURL()
    {
        return URL;
    }
    public void setCHARSET(String CHARSET)
    {
        this.CHARSET = CHARSET;
    }

    public String getCHARSET()
    {
        return CHARSET;
    }
    public void setFORMAT(String FORMAT)
    {
        this.FORMAT = FORMAT;
    }

    public String getFORMAT()
    {
        return FORMAT;
    }
    public void setAlipayPrivateKey(String alipayPrivateKey)
    {
        this.alipayPrivateKey = alipayPrivateKey;
    }

    public String getAlipayPrivateKey()
    {
        return alipayPrivateKey;
    }
    public void setAlipayPublicKey(String alipayPublicKey)
    {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getAlipayPublicKey()
    {
        return alipayPublicKey;
    }
    public void setLogPath(String logPath)
    {
        this.logPath = logPath;
    }

    public String getLogPath()
    {
        return logPath;
    }
    public void setSIGNTYPE(String SIGNTYPE)
    {
        this.SIGNTYPE = SIGNTYPE;
    }

    public String getSIGNTYPE()
    {
        return SIGNTYPE;
    }
    public void setAlipayStatus(Integer alipayStatus)
    {
        this.alipayStatus = alipayStatus;
    }

    public Integer getAlipayStatus()
    {
        return alipayStatus;
    }

    @Override
    public String toString() {
        return "SysAlipayConfig{" +
                "id=" + id +
                ", APPID='" + APPID + '\'' +
                ", rsaPrivateKey='" + rsaPrivateKey + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                ", URL='" + URL + '\'' +
                ", CHARSET='" + CHARSET + '\'' +
                ", FORMAT='" + FORMAT + '\'' +
                ", alipayPrivateKey='" + alipayPrivateKey + '\'' +
                ", alipayPublicKey='" + alipayPublicKey + '\'' +
                ", logPath='" + logPath + '\'' +
                ", SIGNTYPE='" + SIGNTYPE + '\'' +
                ", alipayStatus=" + alipayStatus +
                ", keyOrCert=" + keyOrCert +
                ", weightStart=" + weightStart +
                ", weightEnd=" + weightEnd +
                ", appCertPath='" + appCertPath + '\'' +
                ", alipayCertPath='" + alipayCertPath + '\'' +
                ", alipayRootCertPath='" + alipayRootCertPath + '\'' +
                '}';
    }

    public String getAppCertPath() {
        return appCertPath;
    }

    public void setAppCertPath(String appCertPath) {
        this.appCertPath = appCertPath;
    }

    public String getAlipayCertPath() {
        return alipayCertPath;
    }

    public void setAlipayCertPath(String alipayCertPath) {
        this.alipayCertPath = alipayCertPath;
    }

    public String getAlipayRootCertPath() {
        return alipayRootCertPath;
    }

    public void setAlipayRootCertPath(String alipayRootCertPath) {
        this.alipayRootCertPath = alipayRootCertPath;
    }

    public Integer getKeyOrCert() {
        return keyOrCert;
    }

    public void setKeyOrCert(Integer keyOrCert) {
        this.keyOrCert = keyOrCert;
    }

    public Integer getWeightStart() {
        return weightStart;
    }

    public void setWeightStart(Integer weightStart) {
        this.weightStart = weightStart;
    }

    public Integer getWeightEnd() {
        return weightEnd;
    }

    public void setWeightEnd(Integer weightEnd) {
        this.weightEnd = weightEnd;
    }
}
