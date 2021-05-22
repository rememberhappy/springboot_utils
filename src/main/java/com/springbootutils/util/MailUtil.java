package com.springbootutils.util;

public class MailUtil {

    public static boolean check(String mail) {
        if (mail.contains("@") && mail.contains(".")) {
            if (mail.lastIndexOf(".") > mail.indexOf("@")) {
                return true;
            }
        }
        return false;
    }

}
