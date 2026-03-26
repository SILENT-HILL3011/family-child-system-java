package com.child.common.entity.enums;

public enum ExpertTypeEnum {

    DOCTOR(1, "医生"),
    TEACHER(2, "老师"),
    COOKER(3, "厨师"),
    ;

    private Integer code;
    private String desc;
    ExpertTypeEnum(Integer code, String desc) {
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
        for (ExpertTypeEnum value : ExpertTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return null;
    }
}
