package com.child.common.entity.enums;

public enum ImportanceEnum {

    PRIMARY(1, "重要"),
    SECONDARY(2, "次要"),
    ;

    private Integer code;
    private String desc;

    ImportanceEnum(Integer code, String desc) {
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
        for (ImportanceEnum importanceEnum : ImportanceEnum.values()) {
            if (importanceEnum.getDesc().equals(desc)) {
                return importanceEnum.getCode();
            }
        }
        return null;
    }
}
