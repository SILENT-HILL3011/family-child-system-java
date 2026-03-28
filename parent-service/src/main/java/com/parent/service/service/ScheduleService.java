package com.parent.service.service;

import com.child.common.entity.po.ScheduleInfo;

import java.util.List;

public interface ScheduleService {
    void createSchedule(String userId, String scheduleForm, String importance, String date,String work);

    List<ScheduleInfo> getScheduleByMonth(String userId, String month);
}
