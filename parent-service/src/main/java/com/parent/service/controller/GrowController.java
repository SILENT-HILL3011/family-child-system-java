package com.parent.service.controller;

import com.child.common.annotation.GlobalInterceptor;
import com.child.common.entity.po.Child;
import com.child.common.entity.po.DailyTime;
import com.child.common.entity.po.Examination;
import com.child.common.entity.po.VaccineRecord;
import com.child.common.entity.vo.GrowthConditionVO;
import com.child.common.entity.vo.ResponseCodeEnum;
import com.child.common.exception.BusinessException;
import com.child.common.result.R;
import com.parent.service.service.ChildService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/child/grow")
@Validated
public class GrowController {

    @Resource
    private ChildService childService;

    @RequestMapping("/addChild")
    @GlobalInterceptor(checkLogin = true)
    public R addChild(@NotEmpty String familyId, @NotEmpty String childName, @NotNull Integer sex,@NotEmpty String idNumber){
        childService.addChild(familyId,childName,sex,idNumber);
        return R.success();
    }
    @RequestMapping("/updateChildInfo")
    @GlobalInterceptor(checkLogin = true)
    public R updateChildInfo(@RequestBody Child child){
        childService.updateChildInfo(child);
        return R.success();
    }

    @RequestMapping("/getGrowthInfo")
    @GlobalInterceptor(checkLogin = true)
    public R<GrowthConditionVO> getGrowthInfo(@NotEmpty String childId){
        return R.success(childService.getGrowthInfo(childId));
    }

    @RequestMapping("/searchVaccine")
    public R<VaccineRecord> searchVaccine(@NotEmpty String childId){
        VaccineRecord vaccineRecord = childService.searchVaccine(childId);
        return R.success(vaccineRecord);
    }

    @RequestMapping("/updateVaccine")
    public R updateVaccine(@NotEmpty String childId,@NotEmpty String vaccine){
        childService.updateVaccine(childId,vaccine);
        return R.success();
    }

    @RequestMapping("/searchVaccineThisYear")
    public R<List<String>> searchVaccineThisYear(@NotEmpty String childId){
        List<String> notDoneVaccine = childService.searchVaccineThisYear(childId);
        return R.success(notDoneVaccine);
    }

    @RequestMapping("/appointExamination")
    public R<Examination> appointExamination(@NotEmpty String childId,@NotEmpty String doctorId){
        Examination examination = childService.appointExamination(childId,doctorId);
        return R.success(examination);
    }

    @RequestMapping("/recordLive")
    public R recordLive(@NotEmpty String childId,@NotNull Integer time,String food,Integer sleepTime){
        if (time == 1 || time == 2 || time == 4){
            childService.recordFood(childId,time,food);
        } else if (time == 3 || time == 5){
            childService.recordSleep(childId,time,sleepTime);
        } else {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        return R.success();
    }

    @RequestMapping("/searchLive")
    public R<List<DailyTime>> searchLive(@NotEmpty String childId){
        List<DailyTime> dailyTimes = childService.searchLive(childId);
        return R.success(dailyTimes);
    }

}
