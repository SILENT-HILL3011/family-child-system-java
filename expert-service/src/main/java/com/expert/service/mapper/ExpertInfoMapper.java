package com.expert.service.mapper;

import com.child.common.entity.po.Examination;
import com.child.common.entity.po.ExpertInfo;
import com.child.common.entity.po.MainBox;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface ExpertInfoMapper {


    @Insert("insert into expert_info(expert_id,expert_phone,expert_password) values(#{expertId},#{expertPhone},#{expertPassword})")
    void register(ExpertInfo expertInfo);

    @Select("select * from expert_info where expert_phone = #{expertPhone}")
    ExpertInfo selectByPhoneNumber(String expertPhone);

    void updateExpertInfo(ExpertInfo expertInfo);

    @Select("select * from expert_info where expert_id = #{expertId}")
    ExpertInfo selectById(String expertId);

    @Insert("insert into personal_examination_record(examination_id,doctor_id,appointment_time) values(#{examinationId},#{doctorId},#{examinationTime})")
    void insertExamination(Examination examination);

    @Insert("insert into sys_message (id,send_user_id,title,content,create_time,is_read) values (#{id},#{sendUserId},#{title},#{content},#{createTime},#{isRead})")
    void insertMailOfExamination(MainBox mainBox);
}
