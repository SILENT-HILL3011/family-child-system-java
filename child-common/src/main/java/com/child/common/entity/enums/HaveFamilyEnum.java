package com.child.common.entity.enums;

public enum HaveFamilyEnum {
    NO(0, "没有"),
    YES(1, "有"),
    ;
    private Integer value;
    private String desc;
    HaveFamilyEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
