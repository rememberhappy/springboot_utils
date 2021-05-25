package com.springbootutils.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @Author zhangdj
 * @Date 2021/5/15:16:56
 * @Description 手机号格式验证工具类，港澳台以及大陆手机号都可
 */
public class PhoneFormatCheckUtils {

    /**
     * 验证手机格式是否合法。港澳台以及大陆均可
     *
     * @param phone
     * @return boolean
     * @Throws
     * @Author zhangdj
     * @date 2021/5/15 17:15
     */
    public static boolean isPhoneLegal(String phone) throws PatternSyntaxException {
        return isChinaPhoneLegal(phone) || isHKPhoneLegal(phone) || isTWPhoneLegal(phone) || isAMPhoneLegal(phone);
    }

    /**
     * 大陆手机号格式检测
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 145,147,149
     * 15+除4的任意数(不要写^4，这样的话字母也会被认为是正确的)
     * 166
     * 17+3,5,6,7,8
     * 18+任意数
     * 198,199
     *
     * @param phone 手机号
     * @return boolean
     * @Throws
     * @Author zhangdj
     * @date 2021/5/15 16:57
     */
    public static boolean isChinaPhoneLegal(String phone) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                "|(18[0-9])|(19[8,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 香港手机号
     * 香港手机号码：8位数，51-56、59、6、90-98开头的号码为手机号码。区号为852
     *
     * @param phone
     * @return boolean
     * @Throws
     * @Author zhangdj
     * @date 2021/5/15 17:00
     */
    public static boolean isHKPhoneLegal(String phone) throws PatternSyntaxException {
        String regExp = "^(852)?(5[1234569]\\d{6}|6\\d{7}|9[0-8]\\d{6})$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 台湾手机号
     * 台湾手机号码：09开头后面跟8位数字。区号为886
     *
     * @param phone
     * @return boolean
     * @Throws
     * @Author zhangdj
     * @date 2021/5/15 17:18
     */
    public static boolean isTWPhoneLegal(String phone) throws PatternSyntaxException {
        String regExp = "^(886)?09\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 澳门手机号
     * 澳门电话号码和手机号码都是8位数字，区号是853。
     * 电话号码是28开头，后面跟6位数字
     * 手机号码是6开头，后面跟7位数字。
     *
     * @param phone
     * @return boolean
     * @Throws
     * @Author zhangdj
     * @date 2021/5/15 17:18
     */
    public static boolean isAMPhoneLegal(String phone) throws PatternSyntaxException {
        String regExp = "^(853)?(?:28\\d{6}|6\\d{7})$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 手机号验证，自定义验证规则
     *
     * @param phone       手机号
     * @param regexMobile 正则表达式
     * @return boolean
     * @Throws
     * @Author zhangdj
     * @date 2021/5/15 17:05
     */
    public static boolean checkUserPhone(String phone, String regexMobile) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        if (StringUtils.isNotBlank(phone)) {
            p = Pattern.compile(regexMobile);
            m = p.matcher(phone);
            b = m.matches();
        }
        return b;
    }
}
