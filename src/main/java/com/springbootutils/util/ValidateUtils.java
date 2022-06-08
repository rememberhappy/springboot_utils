package com.springbootutils.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 验证的类
 */
public class ValidateUtils {
    static Pattern postcode = Pattern.compile("^[1-9]\\d{5}$");
    static Pattern phone = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
    static Pattern telephone = Pattern.compile("^[1-9]\\d{5}$");
    static Pattern idCard = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    /**
     * 是否手机号
     * 大陆，港澳台
     *
     * @param phoneStr 手机号
     * @return boolean
     * @Throws
     * @Author zhangdj
     * @date 2021/6/15 16:16
     */
    public static boolean isPhone(String phoneStr) {
        return PhoneFormatCheckUtils.isPhoneLegal(phoneStr);
    }

    public static boolean isPostcode(String info) {
        if (StringUtils.isBlank(info)) {
            return false;
        }
        return postcode.matcher(info).matches();
    }

    /**
     * @param
     * @return
     * @author daocers
     * @date 2020/11/23 18:19
     */
    public static boolean isTelephone(String info) {
        if (StringUtils.isBlank(info)) {
            return false;
        }
        return telephone.matcher(info).matches();
    }


    /**
     * 是否是身份证号码，有问题
     *
     * @param
     * @return
     * @author daocers
     * @date 2020/11/23 18:19
     */
    public static boolean isIdCard(String info) {
        if (StringUtils.isBlank(info)) {
            return false;
        }
        return idCard.matcher(info).matches();
    }

    public static void postCode(String code) throws Exception {
        String regex = "^[1-9]\\d{5}$";
        if (!Pattern.compile(regex).matcher(code).matches()) {
            throw new Exception("请输入正确的邮政编码");
        }
    }

    public static void notBlank(String value, String message) throws Exception {
        if (StringUtils.isBlank(value)) {
            throw new Exception("请输入正确的".concat(message));
        }
    }

    public static void notNull(Object value, String message) throws Exception {
        if (value == null) {
            throw new Exception(message.concat("不得为空"));
        }
    }

    public static void phone(String phone) throws Exception {
        if (StringUtils.isBlank(phone) || (PhoneFormatCheckUtils.isChinaPhoneLegal(phone) && isFixedPhone(phone))) {
            throw new Exception("手机号有误");
        }
    }

    public static boolean phoneNoException(String phone) {
        if (StringUtils.isBlank(phone) || (PhoneFormatCheckUtils.isChinaPhoneLegal(phone) && isFixedPhone(phone))) {
            return false;
        }
        return true;
    }

    public static void idCard(String idCardNo) throws Exception {
        if (StringUtils.isBlank(idCardNo) || !IdCardUtil.IDCardValidate(idCardNo)) {
            throw new Exception("身份证有误");
        }
    }

    public static void mail(String mail) throws Exception {
        if (StringUtils.isNotBlank(mail)) {
            String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            if (!Pattern.compile(regEx1).matcher(mail).matches()) {
                throw new Exception("邮箱有误");
            }
        }
    }

    /**
     * 区号+座机号码+分机号码
     *
     * @param fixedPhone
     * @return
     */
    public static boolean isFixedPhone(String fixedPhone) {
        String reg = "(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|" +
                "(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)";
        return Pattern.matches(reg, fixedPhone);
    }
}
