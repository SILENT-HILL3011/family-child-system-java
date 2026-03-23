package com.child.common.entity.enums;

public enum ChildStatusEnum {

    A(1, "优秀"),
    B(2, "良好"),
    C(3, "一般"),
    D(4, "及格"),
    E(5, "不及格"),
    ;

    private Integer code;
    private String desc;
    ChildStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public Integer getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}
