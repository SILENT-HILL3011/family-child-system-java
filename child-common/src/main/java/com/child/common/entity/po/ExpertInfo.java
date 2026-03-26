package com.child.common.entity.po;

public class ExpertInfo {

    private String expertId;
    private String expertName;
    private String expertPhone;
    private String expertPassword;
    private String expertEmail;
    private String workPlace;
    private String expertType;

    public String getExpertPassword() {
        return expertPassword;
    }

    public void setExpertPassword(String expertPassword) {
        this.expertPassword = expertPassword;
    }

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public String getExpertName() {
        return expertName;
    }

    public void setExpertName(String expertName) {
        this.expertName = expertName;
    }

    public String getExpertPhone() {
        return expertPhone;
    }

    public void setExpertPhone(String expertPhone) {
        this.expertPhone = expertPhone;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getExpertEmail() {
        return expertEmail;
    }

    public void setExpertEmail(String expertEmail) {
        this.expertEmail = expertEmail;
    }

    public String getExpertType() {
        return expertType;
    }

    public void setExpertType(String expertType) {
        this.expertType = expertType;
    }
}
