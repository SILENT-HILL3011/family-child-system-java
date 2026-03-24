package com.child.common.entity.po;

import java.util.Date;

public class Examination {

    private String examinationId;
    private String doctorId;
    private String childId;
    private Date examinationTime;
    private Integer isChecked;

    public Integer getChecked() {
        return isChecked;
    }

    public void setChecked(Integer checked) {
        isChecked = checked;
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

    public Date getExaminationTime() {
        return examinationTime;
    }

    public void setExaminationTime(Date examinationTime) {
        this.examinationTime = examinationTime;
    }


}
