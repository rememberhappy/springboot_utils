package com.springbootutils.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 手机号身份证号脱敏
 * 手机号：只展示前三位和后四位，中间4位隐藏
 * 身份证号：
 * 18位身份证号，只展示前三位和后四位，中间11位隐藏；
 * 15位身份证号，只展示前三位和后四位，中间8位隐藏；
 * 身份证修改为只显示首位各一位，中间全部隐藏
 * @author daocers
 * @date 2020/12/16 17:31
 */
public class StarizeUtil {
    /**
     * 身份证号脱敏【正则】
     *
     * @param idCardNo
     * @return java.lang.String
     * @Throws
     * @Author zhangdj
     * @date 2021/6/15 15:51
     */
    public static String desensitizedIdCardNo(String idCardNo) {
        if (StringUtils.isBlank(idCardNo)) {
            return "";
        }
        int length = idCardNo.length();
        if (length == 15) {
            idCardNo = idCardNo.replaceAll("(\\w{1})\\w*(\\w{1})", "$1*************$2");
        } else {
            idCardNo = idCardNo.replaceAll("(\\w{1})\\w*(\\w{1})", "$1****************$2");
        }
        return idCardNo;
    }
    /**
     * 身份证号脱敏【字符串】
     *
     * @param idCard 身份证号
     * @return java.lang.String
     * @Throws
     * @Author zhangdj
     * @date 2021/6/9 17:09
     */
    public static String getIdCard(String idCard) {
        if (StringUtils.isNotBlank(idCard)) {
            return idCard.substring(0, 1).concat("****************").concat(idCard.substring(idCard.length() - 1, idCard.length()));
        } else {
            return idCard;
        }
    }
    /**
     * 手机号脱敏
     *
     * @param phoneNumber 手机号
     * @return java.lang.String
     * @Throws
     * @Author zhangdj
     * @date 2021/6/15 15:51
     */
    public static String desensitizedPhoneNumber(String phoneNumber) {
        if (StringUtils.isNotEmpty(phoneNumber)) {
            phoneNumber = phoneNumber.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2");
        }
        return phoneNumber;
    }
}
