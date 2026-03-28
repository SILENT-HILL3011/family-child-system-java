package com.parent.service.rabbit;

import com.child.common.constants.Constant;
import com.child.common.entity.po.MainBox;
import com.child.common.entity.po.ScheduleInfo;
import com.child.common.utils.DateUtils;
import com.child.common.utils.StringTools;
import com.parent.service.mapper.MailBoxMapper;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DelayConsumer {


    @Resource
    private MailBoxMapper mailBoxMapper;

    @RabbitListener(queues = RabbitDelayConfig.BUSINESS_QUEUE)
    public void receiveRemind(ScheduleInfo schedule) {
        try {
            MainBox msg = new MainBox();
            msg.setId(StringTools.getRandomNumber(Constant.LENGTH_12));
            msg.setSendUserId("000000000000");
            msg.setUserId(schedule.getUserId());
            msg.setTitle("【1小时后重要日程提醒】");
            msg.setContent("日程：" + schedule.getWork() + "，时间：" + DateUtils.changeDate2Str(schedule.getDate()));
            msg.setCreateTime(new Date());

            mailBoxMapper.insert(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
