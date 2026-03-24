package com.expert.service.service;

public interface DoctorService {
    void updateDoctorInfo(String doctorId, String doctorName,String doctorPhone, String doctorEmail, String hospitalLocation);

    void createPersonalExamination(String doctorId, String examinationTime);
}
