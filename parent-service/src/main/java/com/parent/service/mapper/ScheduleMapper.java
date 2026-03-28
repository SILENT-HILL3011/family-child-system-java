package com.parent.service.mapper;

import com.child.common.entity.po.ScheduleInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface ScheduleMapper {


    @Insert("insert into schedule_info(user_id,form,importance,date,work) values(#{userId},#{form},#{importance},#{date},#{work})")
    void insertScheduleInfo(ScheduleInfo scheduleInfo);

    @Select("select * from schedule_info where date=#{curDate} and user_id=#{userId}")
    ScheduleInfo selectScheduleInfoByDate(Date curDate, String userId);

    @Select("""
                    select * from schedule_info
                    where user_id = #{userId}
                      and date_format(date, '%Y-%m') = #{month}
                    order by date asc
            """)
    List<ScheduleInfo> selectScheduleByMonth(String userId, String month);

    @Select("select * from schedule_info where importance=1 and date=date(#{tomorrow})")
    List<ScheduleInfo> selectTomorrowImportantSchedule(Date tomorrow);
}
