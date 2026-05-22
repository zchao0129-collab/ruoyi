package com.ruoyi.common.utils.security;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.http.HttpUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述
 *
 * @author: scott
 * @date: 2023年04月10日 14:03
 */
public class RSAUtil {

    /**     * 加密算法RSA     */
    private static final String KEY_ALGORITHM = "RSA";
    /**     * 签名算法     */
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static void main(String[] args) throws Exception {
        // 1. 签名生成
      Map<String,Object> params = new HashMap<>();
      params.put("mchNo","M1680992044");
      params.put("appId","6431e72ce4b03cff896aefd3");
      params.put("mchOrderNo","202209281537516666");
      params.put("wayCode","ALI_WAP");
      params.put("amount",10);
      params.put("clientIp","127.0.0.1");
      //下单用户IP
      params.put("subject","测试商品");
      params.put("body","测试商品描述");
      params.put("notifyUrl","http://localhost:9216/notify");
      params.put("returnUrl","http://localhost:9216");
      params.put("version","1.0");        // 待签名字符串
      String signStr = getSignStr(params);
     // System.out.println("待签名字符串"+signStr);
      // 商户私钥
      String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCsXP/uyT27zOfXSQe9ia6X/UxTFtc72ICDqjy8QKrwuz5UhBy/VKPGiK9YAycAtG3g1hl3Y7I2T2afgwsto9jvK5ypWKYP0SA/N89FI/vFCwGBFOOyPhnonflU0F9SkwjTZmgfo3kFZdvbzo+7UTmz+d8pc8Iu3nZr2wNyNjCV4D1U7V6APSpfnV28moUw/PpJk6cH1I2zyzipbrnXdjTWlcH2Me0JJm9lIB3uPTO7Xg0BECuZCvA9N8Fv0ZsTRPATUKvtbV3hr3GWxfLX6B9Hb+mI/dB5T6khkBZ5KSXFfmXdeBRxk+cG7H5iRzE8grDgXvcZKw+YLc9a2uKN7V6/AgMBAAECggEAI/03IzC5xyC9t+Hzz8uTvQCSkzbNsh0+mD9Pdy85ghJWrWb4aarOl4GquwHNfwQtfSi3IIqch2KE1wGRr/HqNeYxS9rKsbrEtO5+RL2CsF3f7ofiEzGMT1nM2by4apFw4lMbop+LgWkHQxoaQM5w8fdWdV1KSmcA6vxL9+kAtApKP9YMA60zp8sALenntQHNSgJlTJrbGkmBsTaZYUH1mGwI1bp8l/ABUA4sajPZsGi+vHGs77CZNEd+53xDSTfITHl0mxCoZV3PT8QIIe0PQMOz9JmaFED20aucFa7GRTibM2GgInWbTpwV47PIvwoQ/6y3BxwOju9gHzUDM8cyQQKBgQDkA51m4KEJMa67o5XFsdGAC4aYWtwL6hgr/QdOBR7rz/m6uSRqWha9pqRifJ6boQEwSPccgcxfYtMu/PLVQk1NYV1rftOCbsn7JJr6ig9Yt4cWRmWMoupQDoFn9hElG7I5dKzMjJw8WlKfOIijoPTLKDzJ/IBrNweKmQzWt0h3twKBgQDBhMqLdYgWT2g8TyhpnC0n2yEM5f1gZDdHJCEuKa6itGneDEZqBM63gUfKQOYir9TVcC083MWe57MpLeQIsDVX7ufIPgLuaxKS2kV2W/e75uI5/6p5Tr5XJ4QPZS6tc0QfA8Fpfgp9xWG9Cy5+SgJ8GU7gCdN4GQzX5zMZXkUBOQKBgQC4fWBHVGuYB9YMxuCxdB5CD5FZBvNSxJfIR9Q4QZZyUPYMKi+XycrqqsIJWjHvVIekrPUc4L0fVNPxCgPln97yRFyVVKoIuGG/TjADld+y9dRcppsV/t+95O650nSu5MkBCjGi1Aa6Io8Fe3W7h8lp8+WzvRWfKoh0kT+9H9GH4wKBgDn7Q4SNbnzgxeZsATvDY3Qxn7Fxp30eCcHy/U4LhxuQ2reBDgft8NHillPvJgT2kdpxhdxaeYum+HM6ILzJrvHtztycV9x/kE0XwQA5hfxyrQ2boMQ/55/NYzAJrPajNqHkSisFbLdKUIKHuyJzolL0ckGGMCont0VpLFJJgHthAoGBAI76BIN3xf+diUaqJjOl76QULIc2eXnlNC+AMuWL3kUR0dCkWwGhV0zFZBEtG4A5NQEGhCt6u5Bu38hWQX9gxAoON4gzTXwYfU5DN8c3+NIUdLg/ap7d+cdhsfxWSRcj01aOSE3CD0G+cA6DN74qNASnF5dXUbWvAsyw3OcYMGUA";
      String sign = sign(signStr,privateKey);
      //System.out.println("签名结果："+sign);        // 2.验签
      // 商户公钥 - 平台返回的签名，需要使用平台公钥进行验签
      String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArFz/7sk9u8zn10kHvYmul/1MUxbXO9iAg6o8vECq8Ls+VIQcv1SjxoivWAMnALRt4NYZd2OyNk9mn4MLLaPY7yucqVimD9EgPzfPRSP7xQsBgRTjsj4Z6J35VNBfUpMI02ZoH6N5BWXb286Pu1E5s/nfKXPCLt52a9sDcjYwleA9VO1egD0qX51dvJqFMPz6SZOnB9SNs8s4qW6513Y01pXB9jHtCSZvZSAd7j0zu14NARArmQrwPTfBb9GbE0TwE1Cr7W1d4a9xlsXy1+gfR2/piP3QeU+pIZAWeSklxX5l3XgUcZPnBux+YkcxPIKw4F73GSsPmC3PWtrije1evwIDAQAB";
     System.out.println("验签结果："+ verify(signStr,publicKey,sign));
        String res =  HttpUtils.sendPost("https://pay.xmhyd.top/api/pay/unifiedOrder", JSONObject.toJSONString(params),"utf-8");
        System.out.println("结果："+ res);
    }


    public static String getSignStr(Map<String,Object> map){
        String[] signKeys = {"mchNo","appId","transferId", "refundOrderId", "channelOrderNo", "mchRefundNo","mchOrderNo","wayCode","amount","notifyUrl","returnUrl","payOrderId","ifCode","state", "version", "payData", "refundAmount"};
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(null != entry.getValue() && !"".equals(entry.getValue()) && Arrays.asList(signKeys).contains(entry.getKey())){
                list.add(entry.getKey() + "=" + entry.getValue());
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
            if (i != size - 1){
                sb.append("&");
            }
        }
        String result = sb.toString();
        return result;
    }

    /**     * 对已加密数据进行签名     *     * @param data
     * 已加密的数据
     * * @param privateKey 私钥
     * * @return 对已加密数据生成的签名
     * * @throws Exception     */
    public static String sign(String dataStr, String privateKey) throws Exception {
        byte[] data = dataStr.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encode(signature.sign());
    }
    /**     * 验签     *     *
     *  @param dataStr 签名之前的数据
     *  @param publicKey 公钥
     *  @param sign      签名之后的数据
     *  @return 验签是否成功
     * *@throws Exception     */
    public static boolean verify(String dataStr, String publicKey, String sign) throws Exception {
        byte[] data = dataStr.getBytes();
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decode(sign));
    }
}
