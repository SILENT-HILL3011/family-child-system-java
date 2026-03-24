package com.parent.service.controller;

import com.child.common.result.R;
import com.parent.service.service.ScheduleService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/child/schedule")
@Validated
public class ScheduleController {
    @Resource
    private ScheduleService scheduleService;

    @RequestMapping("/createSchedule")
    public R crateSchedule(@NotEmpty String userId, @NotEmpty String scheduleForm, @NotEmpty String importance, @NotNull Integer date){
        scheduleService.crateSchedule(userId,scheduleForm,importance,date);
        return R.success();
    }
}
