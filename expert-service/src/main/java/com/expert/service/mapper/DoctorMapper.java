package com.expert.service.mapper;

import com.child.common.entity.po.Examination;
import com.expert.service.entity.po.doctor.DoctorInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface DoctorMapper {

    void updateDoctorInfo(DoctorInfo doctorInfo);

    @Select("select * from doctor_info where doctor_id = #{doctorId}")
    DoctorInfo selectDoctorInfoById(String doctorId);


    @Insert("insert into personal_examination_record(examination_id,doctor_id,appointment_time) values(#{examinationId},#{doctorId},#{examinationTime})")
    void insertExamination(Examination examination);
}
