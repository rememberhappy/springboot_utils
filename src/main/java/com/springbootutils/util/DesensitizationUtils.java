package com.springbootutils.util;

import org.apache.commons.lang3.StringUtils;

public class DesensitizationUtils {

    public static String getId(String id) {
        if (StringUtils.isNotBlank(id)) {
            return id.substring(0, 1).concat("****************").concat(id.substring(id.length()-1, id.length()));
        } else {
            return id;
        }
    }
}
