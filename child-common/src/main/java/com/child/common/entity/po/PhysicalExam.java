package com.child.common.entity.po;

import java.util.Date;

public class PhysicalExam {

    private String reportId;
    private String appointId;
    private String childId;
    private Integer height;
    private Integer weight;
    private Integer headCirc;
    private Integer vision;
    private Integer hearing;
    private Integer tooth;
    private Integer heart;
    private Integer abdomen;
    private Integer limb;
    private Integer skin;
    private Integer nerve;
    private String suggestion;
    private Date examDate;
    private String doctor;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getAppointId() {
        return appointId;
    }

    public void setAppointId(String appointId) {
        this.appointId = appointId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeadCirc() {
        return headCirc;
    }

    public void setHeadCirc(Integer headCirc) {
        this.headCirc = headCirc;
    }

    public Integer getVision() {
        return vision;
    }

    public void setVision(Integer vision) {
        this.vision = vision;
    }

    public Integer getHearing() {
        return hearing;
    }

    public void setHearing(Integer hearing) {
        this.hearing = hearing;
    }

    public Integer getTooth() {
        return tooth;
    }

    public void setTooth(Integer tooth) {
        this.tooth = tooth;
    }

    public Integer getHeart() {
        return heart;
    }

    public void setHeart(Integer heart) {
        this.heart = heart;
    }

    public Integer getAbdomen() {
        return abdomen;
    }

    public void setAbdomen(Integer abdomen) {
        this.abdomen = abdomen;
    }

    public Integer getLimb() {
        return limb;
    }

    public void setLimb(Integer limb) {
        this.limb = limb;
    }

    public Integer getSkin() {
        return skin;
    }

    public void setSkin(Integer skin) {
        this.skin = skin;
    }

    public Integer getNerve() {
        return nerve;
    }

    public void setNerve(Integer nerve) {
        this.nerve = nerve;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
