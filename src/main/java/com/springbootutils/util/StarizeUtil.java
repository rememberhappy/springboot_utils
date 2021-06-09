package com.springbootutils.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 手机号：只展示前三位和后四位，中间4位隐藏
 * 身份证号：
 * 18位身份证号，只展示前三位和后四位，中间11位隐藏；
 * 15位身份证号，只展示前三位和后四位，中间8位隐藏；
 * 身份证修改为只显示首位各一位，中间全部隐藏
 * @author daocers
 * @date 2020/12/16 17:31
 */
public class StarizeUtil {
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

    public static String desensitizedPhoneNumber(String phoneNumber) {
        if (StringUtils.isNotEmpty(phoneNumber)) {
            phoneNumber = phoneNumber.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2");
        }
        return phoneNumber;
    }

    public static void main(String[] args) {
        String s = desensitizedIdCardNo("4115111232435346475");
        System.out.println(s);
    }
}
