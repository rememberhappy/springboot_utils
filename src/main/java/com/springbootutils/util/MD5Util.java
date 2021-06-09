package com.springbootutils.util;

import org.apache.commons.lang3.StringUtils;
import com.google.common.base.Preconditions;

import java.security.MessageDigest;
import java.util.Random;

/**
 * @Author zhangdj
 * @Date 2021/5/15 11:52
 * @Description MD5工具类
 */
public class MD5Util {
    /**
     * 对多个数据加盐生成MD5
     *
     * @param salt    盐
     * @param message 要加密的多个信息
     * @return java.lang.String
     * @Throws
     * @Author zhangdj
     * @date 2021/5/15 17:30
     */
    public static String getMD5(String salt, String... message) {
        if (null == message) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        for (String item : message) {
            buffer.append(item);
        }
        return getMD5(buffer.toString(), salt);
    }

    /**
     * 加盐生成MD5
     *
     * @param message 要加密的信息
     * @param salt    盐
     * @return java.lang.String
     * @Throws
     * @Author zhangdj
     * @date 2021/5/15 14:55
     */
    public static String getMD5(String message, String salt) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(message), "MD5加密时，要加密的信息不能为空！");
//        Preconditions.checkArgument(StringUtils.isNotEmpty(salt), "MD5加密时，盐不能为空！");// 后期会加，现在为了兼容以前的 逻辑
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");  // 创建一个md5算法对象
            byte[] messageByte = (message + salt).getBytes("UTF-8");
            byte[] md5Byte = md.digest(messageByte);              // 获得MD5字节数组,16*8=128位
            md5 = bytesToHex(md5Byte);                            // 转换为16进制字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    // 二进制转十六进制
    public static String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if (num < 0) {
                num += 256;
            }
            if (num < 16) {
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }

    /**
     * 生成16位盐
     *
     * @param
     * @return java.lang.String
     * @Throws
     * @Author zhangdj
     * @date 2021/5/15 14:53
     */
    public static String getSalt() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        return sb.toString();
    }
}
