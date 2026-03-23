package com.child.common.entity.enums;

public enum MemberRole {
    MAIN(1, "主力"),
    WITH(0, "协同")
    ;

    private Integer code;
    private String desc;
    MemberRole(Integer code, String desc) {
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
