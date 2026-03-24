package com.parent.service.mapper;

import com.child.common.entity.po.DailyTime;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

public interface DailyTimeMapper {
    @Insert("insert into daily_time(child_id,time,record_time,food) values(#{childId},#{time},#{recordTime},#{food})")
    void insert4Food(DailyTime dailyTime);

    @Insert("insert into daily_time(child_id,time,record_time,sleep_time) values(#{childId},#{time},#{recordTime},#{sleepTime})")
    void insert4Sleep(DailyTime dailyTime);


    List<DailyTime> selectWeeklyRecordsByChildId(String childId);
}
