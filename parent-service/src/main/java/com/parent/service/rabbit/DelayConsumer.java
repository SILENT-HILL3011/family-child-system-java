package com.parent.service.rabbit;

import com.child.common.constants.Constant;
import com.child.common.entity.po.MainBox;
import com.child.common.entity.po.ScheduleInfo;
import com.child.common.entity.po.User;
import com.child.common.utils.DateUtils;
import com.child.common.utils.MailUtil;
import com.child.common.utils.StringTools;
import com.parent.service.mapper.MailBoxMapper;
import com.parent.service.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DelayConsumer {


    private static final Logger log = LoggerFactory.getLogger(DelayConsumer.class);
    @Resource
    private MailBoxMapper mailBoxMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private MailUtil mailUtil;

    @RabbitListener(queues = RabbitDelayConfig.BUSINESS_QUEUE)
    public void receiveRemind(ScheduleInfo schedule) {
        log.info("【消费者】收到延迟消息：{}", schedule);
        try {
            MainBox msg = new MainBox();
            msg.setId(StringTools.getRandomNumber(Constant.LENGTH_12));
            msg.setSendUserId("000000000000");
            msg.setUserId(schedule.getUserId());
            msg.setTitle("【1小时后重要日程提醒】");
            msg.setContent("日程：" + schedule.getWork() + "，时间：" + DateUtils.changeDate2Str(schedule.getDate()));
            msg.setCreateTime(new Date());
            msg.setIsRead(Constant.NO);
            mailBoxMapper.insert(msg);

            User user = userMapper.selectById(schedule.getUserId());
            log.info("【消费者】查询到用户：{}", user);
            log.info("【消费者】用户邮箱：{}", user.getMail());
            if (user != null && user.getMail() != null && !user.getMail().isEmpty()) {
                String title = "【重要日程提醒】";
                String content = "您的重要日程即将开始：\n" + schedule.getWork() + "\n时间：" + DateUtils.changeDate2Str(schedule.getDate());
                log.info("【消费者】开始发送邮件至：{}", user.getMail());

                mailUtil.sendEmail(user.getMail(), title, content);
                log.info("【消费者】邮件发送成功！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
