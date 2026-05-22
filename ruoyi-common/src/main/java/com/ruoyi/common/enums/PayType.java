/**
 * 说明：
 * 项目名称：WebsiteScanPayDemo
 * 包名称：com.hnapay.com.ruoyi.xinsheng.pay.constant
 * 创建者：zhangwei
 * 创建时间：2017年9月5日 下午1:50:18
 * 版本：1.0
 */
package com.ruoyi.common.enums;

/**
 * 说明: 目标资金机构代码枚举
 *
 * @author zhangwei
 * @date 2017年9月5日 下午1:50:18
 * @操作列表 编号 | 操作时间 | 操作人员 | 操作说明（Create、Modify、Deprecated）
 * @操作列表 (1) | 2017年9月5日 | zhangwei | Create
 */
public enum PayType {
    ALIPAY("alipay"),
    ALIPAYWAP("alipaywap"),
    ALIPAYJSAPI("alipayjsapi"),
    ALIPAYQR("alipayqr"),
    ALIPAYMICRO("alipaymicro"),
    WEIXIN("weixin"),
    WEIXINH5("weixinh5"),
    WEIXINJSAPI("weixinjsapi"),
    WEIXINMICRO("weixinmicro"),
    QQ("qq"),
    QQJSAPI("qqjsapi"),
    QQMICRO("qqmicro"),
    UNIONPAY("unionpay");

    /**
     * 扫码类型枚举值
     */
    private final String value;

    /**
     * 构造函数
     *
     * @param value 枚举值
     */
    private PayType(String value) {
        this.value = value;
    }

    /**
     * 根据枚举值获取枚举
     *
     * @param value 枚举值
     * @return OrgCode
     */
    public static PayType getByValue(String value) {
        for (PayType payType : PayType.values()) {
            if (payType.value.equals(value)) {
                return payType;
            }
        }
        return null;
    }

    /**
     * 获取枚举值
     *
     * @return String
     */
    public String getValue() {
        return this.value;
    }
}
