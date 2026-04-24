package com.child.common.entity.enums;

public enum DailyTimeEnum {

    Breakfast(1, "早餐"),
    Lunch(2, "午餐"),
    MidSleep(3, "午睡"),
    Dinner(4, "晚餐"),
    BedTime(5, "晚睡");

    private Integer code;
    private String time;

    DailyTimeEnum(Integer code, String time) {
        this.code = code;
        this.time = time;
    }

    public Integer getCode() {
        return code;
    }

    public String getTime() {
        return time;
    }

    public static Integer getCodeByTime(String time) {
        for (DailyTimeEnum value : values()) {
            if (value.getTime().equals(time)) {
                return value.getCode();
            }
        }
        return null;
    }

}