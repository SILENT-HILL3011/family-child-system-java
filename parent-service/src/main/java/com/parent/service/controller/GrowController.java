package com.parent.service.controller;

import com.child.common.annotation.GlobalInterceptor;
import com.child.common.entity.enums.DailyTimeEnum;
import com.child.common.entity.po.*;
import com.child.common.entity.vo.ChildInfoVO;
import com.child.common.entity.vo.ResponseCodeEnum;
import com.child.common.exception.BusinessException;
import com.child.common.result.R;
import com.github.pagehelper.PageInfo;
import com.parent.service.service.ChildService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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
    public R addChild(@NotEmpty String familyId, @NotEmpty String childName, @NotNull Integer sex, @NotEmpty String idNumber, @NotEmpty String birthDate){
        childService.addChild(familyId,childName,sex,idNumber,birthDate);
        return R.success();
    }

    @RequestMapping("/searchChildInfo")
    @GlobalInterceptor(checkLogin = true)
    public R<PageInfo<ChildInfoVO>> searchChildInfo(@NotEmpty String familyId, Integer pageNum){
        return R.success(childService.searchChildInfo(familyId,pageNum));
    }

    @RequestMapping("/searchChildById")
    @GlobalInterceptor(checkLogin = true)
    public R<Child> searchChildById(@NotEmpty String childId){
        Child child = childService.searchChildById(childId);
        return R.success(child);
    }

    @RequestMapping("/updateChildInfo")
    @GlobalInterceptor(checkLogin = true)
    public R updateChildInfo(@RequestBody Child child){
        childService.updateChildInfo(child);
        return R.success();
    }

    @RequestMapping("/deleteChild")
    @GlobalInterceptor(checkLogin = true)
    public R deleteChild(@NotEmpty String childId){
        childService.deleteChild(childId);
        return R.success();
    }

    @RequestMapping("/recordGrowth")
    @GlobalInterceptor(checkLogin = true)
    public R recordGrowth(@RequestBody GrowthTrend growthTrend){
        childService.recordGrowth(growthTrend);
        return R.success();
    }

    @RequestMapping("/searchGrowth")
    @GlobalInterceptor(checkLogin = true)
    public R<List<GrowthTrend>> searchGrowth(@NotEmpty String childId,Integer days){
        List<GrowthTrend> growthTrends = childService.searchGrowth(childId, days);
        return R.success(growthTrends);
    }

    @RequestMapping("/deleteGrowthRecord")
    @GlobalInterceptor(checkLogin = true)
    public R deleteGrowthRecord(@NotEmpty String id){
        childService.deleteGrowthRecord(id);
        return R.success();
    }


    @RequestMapping("/searchVaccine")
    @GlobalInterceptor(checkLogin = true)
    public R<VaccineRecord> searchVaccine(@NotEmpty String childId){
        VaccineRecord vaccineRecord = childService.searchVaccine(childId);
        return R.success(vaccineRecord);
    }

    @RequestMapping("/updateVaccine")
    @GlobalInterceptor(checkLogin = true)
    public R updateVaccine(@NotEmpty String childId,@NotEmpty String vaccine){
        childService.updateVaccine(childId,vaccine);
        return R.success();
    }

    @RequestMapping("/searchVaccineThisYear")
    @GlobalInterceptor(checkLogin = true)
    public R<List<String>> searchVaccineThisYear(@NotEmpty String childId){
        List<String> notDoneVaccine = childService.searchVaccineThisYear(childId);
        return R.success(notDoneVaccine);
    }

    @RequestMapping("/appointExamination")
    @GlobalInterceptor(checkLogin = true)
    public R<Examination> appointExamination(@NotEmpty String childId,@NotEmpty String doctorId,@NotEmpty String startTime){
        Examination examination = childService.appointExamination(childId,doctorId,startTime);
        return R.success(examination);
    }

    @RequestMapping("/searchExamination")
    @GlobalInterceptor(checkLogin = true)
    public R<List<Examination>> searchExamination(){
        return R.success(childService.loadExamination());
    }

    @RequestMapping("/cancelExamination")
    @GlobalInterceptor(checkLogin = true)
    public R cancelExamination(@NotEmpty String examinationId){
        childService.cancelExamination(examinationId);
        return R.success();
    }




    @RequestMapping("/recordLive")
    @GlobalInterceptor(checkLogin = true)
    public R recordLive(@NotEmpty String childId,@NotEmpty String time,String food,Integer sleepTime){
        Integer timeCode = DailyTimeEnum.getCodeByTime(time);
        if (timeCode == 1 || timeCode == 2 || timeCode == 4){
            childService.recordFood(childId,timeCode,food);
        } else if (timeCode == 3 || timeCode == 5){
            childService.recordSleep(childId,timeCode,sleepTime);
        } else {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        return R.success();
    }

    @RequestMapping("/searchLive")
    @GlobalInterceptor(checkLogin = true)
    public R<PageInfo<DailyTime>> searchLive(@NotEmpty String childId,Integer pageNum){
        return R.success(childService.searchLive(childId,pageNum));
    }

    @RequestMapping("/updateLiveRecord")
    @GlobalInterceptor(checkLogin = true)
    public R updateLiveRecord(@NotEmpty String childId,@NotEmpty String recordTime,@NotNull Integer time,String food,Integer sleepTime){
        if (time == 1 || time == 2 || time == 4) {
            childService.updateFood(childId, recordTime, time, food);
        } else if (time == 3 || time == 5) {
            childService.updateSleep(childId, recordTime, time, sleepTime);
        } else {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        return R.success();
    }

    @RequestMapping("/deleteLiveRecord")
    @GlobalInterceptor(checkLogin = true)
    public R deleteLiveRecord(@NotEmpty String dailyId){
        childService.deleteLiveRecord(dailyId);
        return R.success();
    }


    @RequestMapping("/exportLive")
    public void exportLive(@NotEmpty String childId, HttpServletResponse  response)throws Exception{
        childService.exportLive(childId, response);
    }

    @PostMapping("/updateGrowthRecord")
    @GlobalInterceptor(checkLogin = true)
    public R updateGrowthRecord(
            @NotEmpty String childId,
            @NotNull Integer height,
            @NotNull Integer weight,
            @NotNull Integer headCirc
    ){
        childService.updateGrowthRecord(childId, height, weight, headCirc);
        return R.success();
    }

}
