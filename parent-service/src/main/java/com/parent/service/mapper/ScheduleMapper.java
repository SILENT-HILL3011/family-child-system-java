package com.parent.service.mapper;

import com.child.common.entity.po.ScheduleInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface ScheduleMapper {
    @Select("select * from schedule_info where user_id = #{userId}")
    ScheduleInfo selectScheduleInfoByUserId(String userId);

    @Insert("insert into schedule_info(user_id,form,importance,date) values(#{userId},#{form},#{importance},#{date})")
    void insertScheduleInfo(ScheduleInfo scheduleInfo);
}
