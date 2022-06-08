package com.springbootutils.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DateUtils {


    public static String getDate2String(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }

    public static Date getDateToDayAboutBaidu(String birthday) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");//注意月份是MM
            return simpleDateFormat.parse(birthday);
        } catch (Exception e) {
            return null;
        }
    }
}
