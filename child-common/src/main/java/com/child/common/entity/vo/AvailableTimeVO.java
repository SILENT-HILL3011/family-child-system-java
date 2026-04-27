package com.child.common.entity.vo;

import java.util.List;

public class AvailableTimeVO {

    private String doctorId;
    private String doctorName;
    private String date;
    private List<Integer> availableHours;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public List<Integer> getAvailableHours() {
        return availableHours;
    }

    public void setAvailableHours(List<Integer> availableHours) {
        this.availableHours = availableHours;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
