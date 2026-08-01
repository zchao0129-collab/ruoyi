package com.ruoyi.common.utils.pay;

import cn.hutool.core.util.StrUtil;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * 支付宝RSA2验签工具类
 * 用于支付异步通知、RiskGO风险普惠推送验签
 */
public class AlipaySignUtil {

    /**
     * 统一验签入口
     * @param paramMap request.getParameterMap()
     * @param alipayPublicKey 支付宝公钥（开放平台复制完整字符串）
     * @return true 验签通过
     */
    public static boolean verify(Map<String, String[]> paramMap, String alipayPublicKey) {
        if (paramMap == null || paramMap.isEmpty() || StrUtil.isBlank(alipayPublicKey)) {
            return false;
        }

        // 1. 提取sign、sign_type
        String sign = getSingleValue(paramMap, "sign");
        String signType = getSingleValue(paramMap, "sign_type");
        if (StrUtil.isBlank(sign) || !"RSA2".equals(signType)) {
            return false;
        }

        // 2. 组装待签原文：除sign、sign_type外，按key ASCII升序拼接 key=value&
        Map<String, String> sortedParams = new TreeMap<>();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            if ("sign".equals(key) || "sign_type".equals(key)) {
                continue;
            }
            String val = getSingleValue(paramMap, key);
            if (StrUtil.isNotBlank(val)) {
                sortedParams.put(key, val);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String content;
        if (sb.length() > 0) {
            content = sb.substring(0, sb.length() - 1);
        } else {
            content = "";
        }

        // 3. base64解码签名、公钥验签
        try {
            PublicKey publicKey = getPublicKey(alipayPublicKey);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));
            byte[] signBytes = Base64.getDecoder().decode(sign);
            return signature.verify(signBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从request.getParameterMap取单值（表单参数均单个）
     */
    private static String getSingleValue(Map<String, String[]> paramMap, String key) {
        String[] arr = paramMap.get(key);
        if (arr == null || arr.length == 0) {
            return "";
        }
        return arr[0];
    }

    /**
     * 支付宝公钥字符串转PublicKey对象
     */
    private static PublicKey getPublicKey(String pubKeyStr) throws Exception {
        // 去除换行、空格
        pubKeyStr = pubKeyStr.replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(pubKeyStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    // ===================== 重载：直接传入HttpServletRequest =====================
    public static boolean verify(HttpServletRequest request, String alipayPublicKey) {
        Map<String, String[]> paramMap = request.getParameterMap();
        return verify(paramMap, alipayPublicKey);
    }
}

