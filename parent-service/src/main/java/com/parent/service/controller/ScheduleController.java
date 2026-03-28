package com.parent.service.controller;

import com.child.common.annotation.GlobalInterceptor;
import com.child.common.constants.Constant;
import com.child.common.entity.po.ScheduleInfo;
import com.child.common.redis.RedisComponent;
import com.child.common.result.R;
import com.github.pagehelper.PageInfo;
import com.parent.service.service.ScheduleService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/child/schedule")
@Validated
public class ScheduleController {
    @Resource
    private ScheduleService scheduleService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/createSchedule")
    @GlobalInterceptor(checkLogin = true)
    public R crateSchedule(@NotEmpty String scheduleForm, @NotEmpty String importance, @NotEmpty String date,@NotEmpty String work){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String userId = redisComponent.getUserIdByToken(token);
        scheduleService.createSchedule(userId,scheduleForm,importance,date, work);
        return R.success();
    }

    @RequestMapping("/getScheduleByMonth")
    @GlobalInterceptor(checkLogin = true)
    public R<List<ScheduleInfo>> getScheduleByMonth(@NotEmpty String month){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String userId = redisComponent.getUserIdByToken(token);
        List<ScheduleInfo> scheduleInfoList = scheduleService.getScheduleByMonth(userId,month);
        return R.success(scheduleInfoList);
    }
}
