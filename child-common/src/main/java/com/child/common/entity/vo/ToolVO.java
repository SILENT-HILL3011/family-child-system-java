package com.child.common.entity.vo;

public class ToolVO {

    private String name;
    private String platform;
    private String coreFunc;
    private String brightPoint;
    private String isPay;

    public ToolVO() {
    }
    public ToolVO(String name, String platform, String coreFunc, String brightPoint, String isPay) {
        this.name = name;
        this.platform = platform;
        this.coreFunc = coreFunc;
        this.brightPoint = brightPoint;
        this.isPay = isPay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public String getBrightPoint() {
        return brightPoint;
    }

    public void setBrightPoint(String brightPoint) {
        this.brightPoint = brightPoint;
    }

    public String getCoreFunc() {
        return coreFunc;
    }

    public void setCoreFunc(String coreFunc) {
        this.coreFunc = coreFunc;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
