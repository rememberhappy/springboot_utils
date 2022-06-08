
package com.springbootutils.util;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class AESUtil {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";//默认的加密算法
    private static final String S_KEY = "7b9e90e937b6406f"; // #加密秘钥(16位)
    private static final String IV_PARAMETER_KEY = "4b64fa346be34d33"; // #算法参数规范

    public static String getKey() {
        String str = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(36);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String encrypt(byte[] sSrc) throws Exception {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, S_KEY, IV_PARAMETER_KEY);
        byte[] encrypted = cipher.doFinal(sSrc);
        return Base64Util.encodeBase64Str(encrypted);
    }

    public static String encrypt(byte[] sSrc, String sKey, String ivParameterKey) throws Exception {
        if (StringUtils.isBlank(sKey)) {
            sKey = S_KEY;
        }
        if (StringUtils.isBlank(ivParameterKey)) {
            ivParameterKey = IV_PARAMETER_KEY;
        }
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, sKey, ivParameterKey);
        byte[] encrypted = cipher.doFinal(sSrc);
        return Base64Util.encodeBase64Str(encrypted);
    }

    public static byte[] decrypt(String sSrc) throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, S_KEY, IV_PARAMETER_KEY);
        byte[] encrypted = Base64Util.decodeBase64byte(sSrc);
        return cipher.doFinal(encrypted);
    }

    public static byte[] decrypt(String sSrc, String sKey, String ivParameterKey) throws Exception {
        if (StringUtils.isBlank(sKey)) {
            sKey = S_KEY;
        }
        if (StringUtils.isBlank(ivParameterKey)) {
            ivParameterKey = IV_PARAMETER_KEY;
        }
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, sKey, ivParameterKey);
        byte[] encrypted = Base64Util.decodeBase64byte(sSrc);
        return cipher.doFinal(encrypted);
    }

    private static Cipher getCipher(int type, String sKey, String ivParameterKey) throws Exception {
        byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(ivParameterKey.getBytes(StandardCharsets.UTF_8));
        cipher.init(type, skeySpec, iv);
        return cipher;
    }

}