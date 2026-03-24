package com.parent.service.controller;

import com.child.common.annotation.GlobalInterceptor;
import com.child.common.entity.po.Child;
import com.child.common.entity.po.VaccineRecord;
import com.child.common.entity.vo.GrowthConditionVO;
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


}
