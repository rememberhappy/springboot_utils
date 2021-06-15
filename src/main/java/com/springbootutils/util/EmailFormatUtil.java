package com.springbootutils.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 邮箱格式验证工具类
 *
 * @Author zhangdj
 * @Date 2021/5/15:16:56
 */
public class EmailFormatUtil {
    /**
     * 验证邮箱格式是否合法
     * 1. @之前必须有内容且只能是字母（大小写）、数字、下划线(_)、减号（-）、点（.）
     * 2. @和最后一个点（.）之间必须有内容且只能是字母（大小写）、数字、点（.）、减号（-），且两个点不能挨着
     * 3. 最后一个点（.）之后必须有内容且内容只能是字母（大小写）、数字且长度为大于等于2个字节，小于等于6个字节
     *
     * @param email 邮箱账号
     * @return boolean
     * @Throws
     * @Author zhangdj
     * @date 2021/5/17 19:22
     */
    public static boolean isEmailLegal(String email) throws PatternSyntaxException {
        String regExp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
