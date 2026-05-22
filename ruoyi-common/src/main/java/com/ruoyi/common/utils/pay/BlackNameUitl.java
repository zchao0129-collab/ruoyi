package com.ruoyi.common.utils.pay;

import cn.hutool.crypto.symmetric.AES;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class BlackNameUitl {

    public static String Encode(String text, String key) throws NoSuchAlgorithmException {
        AES aes = new AES(generateKey(key));
        return aes.encryptBase64(text);
    }

    public static byte[] generateKey(String key) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha256.digest(key.getBytes());
        byte[] keyBytes = new byte[32];
        System.arraycopy(hash, 0, keyBytes, 0, 32);
        return keyBytes;
    }

}
