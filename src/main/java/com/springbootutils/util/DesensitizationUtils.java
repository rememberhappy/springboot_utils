package com.springbootutils.util;

import org.apache.commons.lang3.StringUtils;

public class DesensitizationUtils {

    /**
     * 身份证号脱敏
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
}
