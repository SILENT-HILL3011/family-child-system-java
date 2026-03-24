package com.child.common.entity.enums;

public enum TimePerEnum {

    BREAKFAST(1, "早餐"),
    LUNCH(2, "午餐"),
    NOON_BREAK(3, "午休"),
    DINNER(4, "晚餐"),
    SLEEP(5, "晚睡"),
    ;

    private Integer code;
    private String desc;

    TimePerEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Integer getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(Integer code) {
        for (TimePerEnum value : TimePerEnum.values()) {
            if (value.code.equals(code)) {
                return value.desc;
            }
        }
        return null;
    }
}
