package com.parent.service.mapper;

import com.child.common.entity.po.DailyTime;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

public interface DailyTimeMapper {
    @Insert("insert into daily_time(daily_id,child_id,time,record_time,food) values(#{dailyId},#{childId},#{time},#{recordTime},#{food})")
    void insert4Food(DailyTime dailyTime);

    @Insert("insert into daily_time(daily_id,child_id,time,record_time,sleep_time) values(#{dailyId},#{childId},#{time},#{recordTime},#{sleepTime})")
    void insert4Sleep(DailyTime dailyTime);


    List<DailyTime> selectWeeklyRecordsByChildId(String childId);


    @Delete("delete from daily_time where daily_id = #{dailyId}")
    void deleteLiveRecord(String dailyId);
}
