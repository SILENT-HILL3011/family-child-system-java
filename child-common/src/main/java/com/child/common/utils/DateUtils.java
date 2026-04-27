package com.child.common.utils;

import com.child.common.constants.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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


    public static Integer getAgeFromBirthDate(String birthDate) {
        Date birthdate = ChangeStr2Date(birthDate);
        if (birthdate == null) {
            return null;
        }
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthdate);
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int month = (now.get(Calendar.YEAR) - birth.get(Calendar.YEAR)) * 12;
        month += now.get(Calendar.MONTH) - birth.get(Calendar.MONTH);
        return month;
    }

    public static Integer getAgeFromIDNumber(String idNumber){
        String birthDate = idNumber.substring(6, 14);
        Date birthdate = ChangeStr2Date(birthDate);
        if (birthdate == null) {
            return null;
        }
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthdate);
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int month = (now.get(Calendar.YEAR) - birth.get(Calendar.YEAR)) * 12;
        month += now.get(Calendar.MONTH) - birth.get(Calendar.MONTH);
        return month;
    }

    public static int getHourDiff(Date start,Date end){
        if (start == null || end == null){
            return 0;
        }
        long startTime = start.getTime();
        long endTime = end.getTime();
        return (int) ((endTime - startTime) / (1000 * 60 * 60));
    }

    public static int getHour(Date date){
        if (date == null){
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

}
