package com.parent.service.service.impl;


import com.child.common.entity.enums.ImportanceEnum;
import com.child.common.entity.enums.ScheduleFormEnum;
import com.child.common.entity.po.ScheduleInfo;
import com.child.common.exception.BusinessException;
import com.child.common.utils.DateUtils;
import com.parent.service.mapper.ScheduleMapper;
import com.parent.service.rabbit.RabbitDelayConfig;
import com.parent.service.service.ScheduleService;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Resource
    private ScheduleMapper scheduleMapper;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void createSchedule(String userId, String scheduleForm, String importance, String date, String work) {
        Date curDate = DateUtils.ChangeStr2Date(date);
        ScheduleInfo scheduleInfo = scheduleMapper.selectScheduleInfoByDate(curDate, userId);
        if (scheduleInfo != null) {
            throw new BusinessException("日程已存在");
        }
        scheduleInfo = new ScheduleInfo();
        scheduleInfo.setUserId(userId);
        scheduleInfo.setForm(ScheduleFormEnum.getCodeByDesc(scheduleForm));
        scheduleInfo.setImportance(ImportanceEnum.getCodeByDesc(importance));
        scheduleInfo.setDate(curDate);
        scheduleInfo.setWork(work);
        scheduleMapper.insertScheduleInfo(scheduleInfo);
        if (scheduleInfo.getImportance() == 1) {
            sendDelayRemind(scheduleInfo);
        }
    }

    @Override
    public List<ScheduleInfo> getScheduleByMonth(String userId, String month) {
        return scheduleMapper.selectScheduleByMonth(userId, month);
    }

    private void sendDelayRemind(ScheduleInfo info) {
        long now = System.currentTimeMillis();
        long scheduleTime = info.getDate().getTime();
        long delay = scheduleTime - now - 1 * 60 * 60 * 1000;
        if (delay > 0) {
            rabbitTemplate.convertAndSend(
                    RabbitDelayConfig.DLX_EXCHANGE,
                    "dlx.key",
                    info,
                    message -> {
                        message.getMessageProperties().setExpiration(String.valueOf(delay));
                        return message;
                    }
            );
        }
    }
}
