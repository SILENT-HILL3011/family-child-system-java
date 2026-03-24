package com.expert.service.controller;

import com.child.common.result.R;
import com.expert.service.service.DoctorService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Validated
@RequestMapping("/expert/doctor")
public class DoctorController {

    @Resource
    private DoctorService doctorService;

    @RequestMapping("/updateDoctorInfo")
    public R updateDoctorInfo(@NotEmpty String doctorId, String doctorName,String doctorPhone
                              ,String doctorEmail,String hospitalLocation){
        doctorService.updateDoctorInfo(doctorId,doctorName,doctorPhone,doctorEmail,hospitalLocation);
        return R.success();
    }

    @RequestMapping("/createPersonalExamination")
    public R createPersonalExamination(@NotEmpty String doctorId,@NotEmpty String examinationTime){
        doctorService.createPersonalExamination(doctorId,examinationTime);
        return R.success();
    }
}
