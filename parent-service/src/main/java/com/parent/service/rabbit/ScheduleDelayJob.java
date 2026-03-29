package com.parent.service.rabbit;

import com.child.common.constants.Constant;
import com.child.common.entity.po.MainBox;
import com.child.common.entity.po.ScheduleInfo;
import com.child.common.utils.DateUtils;
import com.child.common.utils.StringTools;
import com.parent.service.mapper.MailBoxMapper;
import com.parent.service.mapper.ScheduleMapper;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleDelayJob {

    @Resource
    private ScheduleMapper scheduleMapper;
    @Resource
    private MailBoxMapper mailBoxMapper;

    @Scheduled(cron = "0 0 0 * * ?")
    public void generateTomorrowRemind() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = cal.getTime();
        List<ScheduleInfo> list = scheduleMapper.selectTomorrowImportantSchedule(tomorrow);
        for (ScheduleInfo info : list){
            MainBox mainBox = new MainBox();
            mainBox.setId(StringTools.getRandomNumber(Constant.LENGTH_12));
            mainBox.setSendUserId(Constant.SYSTEM_ID);
            mainBox.setUserId(info.getUserId());
            mainBox.setTitle(Constant.SCHEDULE_REMIND);
            mainBox.setContent(Constant.TOMORROW_SCHEDULE + info.getWork() + Constant.SCHEDULE_TIME + DateUtils.changeDate2Str(info.getDate()));
            mainBox.setIsRead(Constant.NO);
            mailBoxMapper.insert(mainBox);
        }
    }
}
