package com.child.common.utils;

import com.child.common.constants.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final Logger log = LoggerFactory.getLogger(DateUtils.class);

    public static Date ChangeStr2DateTime(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.PATTERN_DATETIME);
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            log.error("日期转换异常", e);
            return null;
        }
    }

    public static Date ChangeStr2Date(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.PATTERN_DATE);
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            log.error("日期转换异常", e);
            return null;
        }
    }

    public static Date ChangeStr2Date4YYYYMMDD(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.PATTERN_DATE);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }



    public static Boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.PATTERN_DATETIME);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static String changeDate2Str(Date date) {
        if (date == null) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.PATTERN_DATETIME);
            return sdf.format(date);
        } catch (Exception e) {
            return "";
        }
    }


}
