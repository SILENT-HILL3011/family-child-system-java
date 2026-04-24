package com.parent.service.mapper;

import com.child.common.entity.po.Child;
import com.child.common.entity.po.Examination;
import com.child.common.entity.po.VaccineRecord;
import com.child.common.entity.vo.ChildInfoVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

public interface ChildMapper {
    @Select("select * from child_info where child_name = #{childName} and family_id = #{familyId}")
    Child selectByNameAndFamilyId(String childName, String familyId);

    @Insert("insert into child_info(child_id, child_name, family_id, sex,idnumber,birthdate,age) values(#{childId},#{childName},#{familyId},#{sex},#{idNumber},#{birthDate},#{age})")
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

    @Insert("insert into personal_examination_record(child_id,ischecked,doctor_id,examination_id,start_time) values(#{childId},#{isChecked},#{doctorId},#{examinationId},#{startTime})")
    void updateExamination(Examination examination);

    @Select("select family_id,child_id,child_name,sex,age,idnumber from child_info where family_id = #{familyId}")
    List<ChildInfoVO> selectChildInfo(String familyId);


    List<Examination> selectAvailableExamination(LocalDateTime now);

    @Delete("delete from child_info where child_id = #{childId}")
    void deleteById(String childId);

    @Delete("delete from personal_examination_record where examination_id = #{examinationId}")
    void deleteExamination(String examinationId);

    @Select("select child_id,age from child_info")
    List<Child> selectChildIds();

    void updateFood(
            @Param("childId") String childId,
            @Param("recordTime") String recordTime,
            @Param("time") Integer time,
            @Param("food") String food
    );

    void updateSleep(
            @Param("childId") String childId,
            @Param("recordTime") String recordTime,
            @Param("time") Integer time,
            @Param("sleepTime") Integer sleepTime
    );
}
