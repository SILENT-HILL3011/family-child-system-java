package com.parent.service.service.impl;


import com.child.common.constants.Constant;
import com.child.common.entity.enums.ImportanceEnum;
import com.child.common.entity.enums.ScheduleFormEnum;
import com.child.common.entity.po.ScheduleInfo;
import com.child.common.exception.BusinessException;
import com.child.common.utils.DateUtils;
import com.child.common.utils.MailUtil;
import com.child.common.utils.StringTools;
import com.parent.service.mapper.ScheduleMapper;
import com.parent.service.mapper.UserMapper;
import com.parent.service.rabbit.RabbitDelayConfig;
import com.parent.service.service.ScheduleService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private static final Logger log = LoggerFactory.getLogger(ScheduleServiceImpl.class);
    @Resource
    private ScheduleMapper scheduleMapper;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private MailUtil mailUtil;
    @Resource
    private UserMapper userMapper;

    @Override
    public void createSchedule(String userId, String scheduleForm, String importance, String date, String work) {
        Date curDate = DateUtils.ChangeStr2DateTime(date);
        ScheduleInfo scheduleInfo = scheduleMapper.selectScheduleInfoByDate(curDate, userId);
        if (scheduleInfo != null) {
            throw new BusinessException("日程已存在");
        }
        scheduleInfo = new ScheduleInfo();
        scheduleInfo.setScheduleId(StringTools.getRandomNumber(Constant.LENGTH_12));
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
    public void deleteSchedule(String scheduleId) {
        scheduleMapper.deleteSchedule(scheduleId);
    }

    @Override
    public void editSchedule(String scheduleId, String work) {
        ScheduleInfo scheduleInfo = scheduleMapper.selectScheduleInfoByScheduleId(scheduleId);
        if (scheduleInfo == null){
            throw new BusinessException("日程不存在");
        }
        scheduleInfo.setWork(work);
        scheduleMapper.updateScheduleInfo(scheduleInfo);
    }

    @Override
    public List<ScheduleInfo> getScheduleByMonth(String userId, String month) {
        return scheduleMapper.selectScheduleByMonth(userId, month);
    }

    private void sendDelayRemind(ScheduleInfo info) {
        long now = System.currentTimeMillis();
        long scheduleTime = info.getDate().getTime();
        long delay = scheduleTime - now - 1 * 60 * 1000;
        log.info("【延迟队列】准备发送重要日程提醒");
        log.info("【延迟队列】当前时间：{}", now);
        log.info("【延迟队列】日程时间：{}", scheduleTime);
        log.info("【延迟队列】延迟毫秒：{}", delay);

        if (delay > 0) {
            log.info("【延迟队列】消息进入死信队列，key：schedule.key");
            rabbitTemplate.convertAndSend(
                    RabbitDelayConfig.DLX_EXCHANGE,
                    "schedule.key",
                    info,
                    message -> {
                        message.getMessageProperties().setExpiration(String.valueOf(delay));
                        return message;
                    }
            );
            log.info("【延迟队列】消息发送成功！");
        }else{
            log.info("【延迟队列】delay <= 0，不发送消息：delay={}", delay);
        }
    }
}
