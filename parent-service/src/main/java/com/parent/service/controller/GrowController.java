package com.parent.service.controller;

import com.child.common.result.R;
import com.parent.service.service.ChildService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/child/grow")
@Validated
public class GrowController {

    @Resource
    private ChildService childService;

    @RequestMapping("/addChild")
    public R addChild(@NotEmpty String familyId, @NotEmpty String childName, @NotNull Integer sex){
        childService.addChild(familyId,childName,sex);
        return R.success();
    }
    @RequestMapping("/updateChildInfo")
    public R updateChildInfo(@NotEmpty String childId){

        return R.success();
    }
}
