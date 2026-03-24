package com.child.common.entity.enums;

public enum ScheduleFormEnum {

    HEALTH(1, "健康"),
    BREED(2,"养育"),
    GROWS(3,"发展"),
    SELF(4,"自定义")
    ;
    private Integer code;
    private String desc;

    ScheduleFormEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Integer getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }

    public static Integer getCodeByDesc(String desc) {
        for (ScheduleFormEnum value : ScheduleFormEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value.getCode();
            }
        }
        return null;
    }
}
