package com.child.common.entity.po;

import java.util.Date;

public class Examination {

    private String examinationId;
    private String doctorId;
    private String doctorName;
    private String childId;
    private Date startTime;
    private Date endTime;
    private String bookedChildIds;

    public String getBookedChildIds() {
        return bookedChildIds;
    }

    public void setBookedChildIds(String bookedChildIds) {
        this.bookedChildIds = bookedChildIds;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }


    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    public String getExaminationId() {
        return examinationId;
    }

    public void setExaminationId(String examinationId) {
        this.examinationId = examinationId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }


}
