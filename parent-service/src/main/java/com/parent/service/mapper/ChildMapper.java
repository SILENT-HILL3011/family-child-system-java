package com.parent.service.mapper;

import com.child.common.entity.po.Child;
import com.child.common.entity.po.Examination;
import com.child.common.entity.po.VaccineRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

public interface ChildMapper {
    @Select("select * from child_info where child_name = #{childName} and family_id = #{familyId}")
    Child selectByNameAndFamilyId(String childName, String familyId);

    @Insert("insert into child_info(child_id, child_name, family_id, sex,idnumber) values(#{childId},#{childName},#{familyId},#{sex},#{idNumber})")
    void insert(Child child);

    @Select("select * from child_info where child_id = #{childId}")
    Child selectById(String childId);


    void update(Child child);

    @Select("select * from vaccine_record where child_id = #{childId}")
    VaccineRecord selectVaccineRecord(String childId);

    @Select("select * from vaccine_record where child_id = #{childId}")
    Child selectChildFromVaccineRecord(String childId);

    @Insert("insert into vaccine_record(child_id) values(#{childId})")
    void insertVaccineRecord(String childId);


    @Select("select * from personal_examination_record where doctor_id = #{doctorId}")
    Examination selectExaminationByDoctorId(String doctorId);

    @Insert("insert into personal_examination_record(child_id,ischecked,doctor_id,examination_id,appointment_time) values(#{childId},#{isChecked},#{doctorId},#{examinationId},#{examinationTime})")
    void updateExamination(Examination examination);
}
