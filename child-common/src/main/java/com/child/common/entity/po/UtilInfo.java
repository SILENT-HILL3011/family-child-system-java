package com.child.common.entity.po;

import com.child.common.utils.StringTools;

public class UtilInfo {
    private String utilId;
    private String utilName;
    private String type;
    private String coreFunction;
    private String platform;
    private Integer pay;

    public String getUtilId() {
        return utilId;
    }

    public void setUtilId(String utilId) {
        this.utilId = utilId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUtilName() {
        return utilName;
    }

    public void setUtilName(String utilName) {
        this.utilName = utilName;
    }

    public String getCoreFunction() {
        return coreFunction;
    }

    public void setCoreFunction(String coreFunction) {
        this.coreFunction = coreFunction;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Integer getPay() {
        return pay;
    }

    public void setPay(Integer pay) {
        this.pay = pay;
    }

    public static void main(String[] args) {
        System.out.println(StringTools.getRandomNumber(12));
    }
}
