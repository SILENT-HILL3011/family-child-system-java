package com.parent.service.service.impl;

import com.child.common.entity.enums.ImportanceEnum;
import com.child.common.entity.enums.ScheduleFormEnum;
import com.child.common.entity.po.ScheduleInfo;
import com.child.common.entity.vo.ResponseCodeEnum;
import com.child.common.exception.BusinessException;
import com.parent.service.mapper.ScheduleMapper;
import com.parent.service.service.ScheduleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Resource
    private ScheduleMapper scheduleMapper;
    @Override
    public void crateSchedule(String userId, String scheduleForm, String importance, Integer date) {
        ScheduleInfo scheduleInfo = scheduleMapper.selectScheduleInfoByUserId(userId);
        if (scheduleInfo != null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        scheduleInfo = new ScheduleInfo();
        scheduleInfo.setUserId(userId);
        scheduleInfo.setForm(ScheduleFormEnum.getCodeByDesc(scheduleForm));
        scheduleInfo.setImportance(ImportanceEnum.getCodeByDesc(importance));
        scheduleInfo.setDate(date);
        scheduleMapper.insertScheduleInfo(scheduleInfo);
    }
}
