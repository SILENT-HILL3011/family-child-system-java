package com.parent.service.mapper;

import com.child.common.entity.po.AppointExamination;
import com.child.common.entity.po.Child;
import com.child.common.entity.po.Examination;
import com.child.common.entity.po.VaccineRecord;
import com.child.common.entity.vo.ChildInfoVO;
import com.child.common.entity.vo.ExaminationVO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.Date;
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

    @Update("update personal_examination_record " +
            "set booked_child_ids = #{bookedChildIds} " +
            "where examination_id = #{examinationId}")
    void updateExamination(Examination examination);

    List<ChildInfoVO> selectChildInfo(String familyId);


    List<Examination> selectAvailableExamination(LocalDateTime now);

    @Delete("delete from child_info where child_id = #{childId}")
    void deleteById(String childId);

    @Delete("delete from examination_appoint where appoint_id = #{appointId}")
    void deleteExamination(String appointId);

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

    @Select("select * from personal_examination_record where examination_id = #{examinationId}")
    Examination selectExamById(String examinationId);

    @Select("select * from examination_appoint where examination_id = #{examinationId}")
    AppointExamination selectAppointByExamId(String examinationId);


    @Insert("INSERT INTO examination_appoint " +
            "(appoint_id, child_id, examination_id, appoint_time) " +
            "VALUES (#{appointId}, #{childId}, #{examinationId}, #{appointTime})")
    void insertAppoint(AppointExamination appointmentExamination);

    List<ExaminationVO> findMyExamination(@Param("familyId") String familyId);

    @Select("select * from examination_appoint where examination_id = #{examinationId}")
    List<AppointExamination> selectAppointExamination(String examinationId);
}
