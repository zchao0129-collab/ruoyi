package com.ruoyi.common.utils.http;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UA解析工具：获取设备类型、系统、浏览器版本
 */
public class UserAgentUtil {

    // 正则匹配
    private static final Pattern PATTERN_IPHONE = Pattern.compile("iPhone OS (\\d+)_(\\d+)");
    private static final Pattern PATTERN_ANDROID = Pattern.compile("Android (\\d+)\\.(\\d+)");
    private static final Pattern PATTERN_WECHAT = Pattern.compile("MicroMessenger/(\\d+)\\.(\\d+)");
    private static final Pattern PATTERN_ALIPAY = Pattern.compile("Alipay/(\\d+)\\.(\\d+)");
    private static final Pattern PATTERN_SAFARI = Pattern.compile("Version/(\\d+)\\.(\\d+)");
    private static final Pattern PATTERN_CHROME = Pattern.compile("Chrome/(\\d+)\\.(\\d+)");

    /**
     * 封装设备信息实体
     */
    public static class DeviceInfo {
        // 设备系统 ios / android / pc
        private String os;
        // 系统版本 17.0 / 14
        private String osVersion;
        // 浏览器类型 safari / chrome / wechat / alipay / other
        private String browser;
        // 浏览器版本
        private String browserVersion;

        // getter setter
        public String getOs() { return os; }
        public void setOs(String os) { this.os = os; }
        public String getOsVersion() { return osVersion; }
        public void setOsVersion(String osVersion) { this.osVersion = osVersion; }
        public String getBrowser() { return browser; }
        public void setBrowser(String browser) { this.browser = browser; }
        public String getBrowserVersion() { return browserVersion; }
        public void setBrowserVersion(String browserVersion) { this.browserVersion = browserVersion; }
    }

    /**
     * 入口方法，传入request解析全部设备信息
     */
    public static DeviceInfo parse(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        if (ua == null || ua.isEmpty()) {
            DeviceInfo empty = new DeviceInfo();
            empty.setOs("pc");
            empty.setBrowser("unknown");
            return empty;
        }
        return parseUA(ua);
    }

    /**
     * 纯UA字符串解析
     */
    public static DeviceInfo parseUA(String ua) {
        DeviceInfo info = new DeviceInfo();
        // 1. 判断系统
        Matcher iphoneMatcher = PATTERN_IPHONE.matcher(ua);
        Matcher androidMatcher = PATTERN_ANDROID.matcher(ua);
        if (iphoneMatcher.find()) {
            info.setOs("ios");
            info.setOsVersion(iphoneMatcher.group(1) + "." + iphoneMatcher.group(2));
        } else if (androidMatcher.find()) {
            info.setOs("android");
            info.setOsVersion(androidMatcher.group(1) + "." + androidMatcher.group(2));
        } else {
            info.setOs("pc_web");
            info.setOsVersion("");
        }

        // 2. 判断浏览器优先级：支付宝 > 微信 > Safari > Chrome
        Matcher alipayMatcher = PATTERN_ALIPAY.matcher(ua);
        Matcher wechatMatcher = PATTERN_WECHAT.matcher(ua);
        Matcher safariMatcher = PATTERN_SAFARI.matcher(ua);
        Matcher chromeMatcher = PATTERN_CHROME.matcher(ua);

        if (alipayMatcher.find()) {
            info.setBrowser("alipay");
            info.setBrowserVersion(alipayMatcher.group(1) + "." + alipayMatcher.group(2));
        } else if (wechatMatcher.find()) {
            info.setBrowser("wechat");
            info.setBrowserVersion(wechatMatcher.group(1) + "." + wechatMatcher.group(2));
        } else if (safariMatcher.find()) {
            info.setBrowser("safari");
            info.setBrowserVersion(safariMatcher.group(1) + "." + safariMatcher.group(2));
        } else if (chromeMatcher.find()) {
            info.setBrowser("chrome");
            info.setBrowserVersion(chromeMatcher.group(1) + "." + chromeMatcher.group(2));
        } else {
            info.setBrowser("other");
            info.setBrowserVersion("");
        }
        return info;
    }
}
