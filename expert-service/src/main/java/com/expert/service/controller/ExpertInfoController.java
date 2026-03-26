package com.expert.service.controller;

import com.child.common.entity.po.ExpertInfo;
import com.child.common.result.R;
import com.expert.service.service.ExpertService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expert/info")
@Validated
public class ExpertInfoController {

    @Resource
    private ExpertService expertService;

    @RequestMapping("/expertRegister")
    public R expertRegister(@NotEmpty String expertPhone,@NotEmpty String expertPassword){
        expertService.register(expertPhone,expertPassword);
        return R.success();
    }

    @RequestMapping("/expertLogin")
    public R expertLogin(@NotEmpty String expertPhone, @NotEmpty String expertPassword){
        expertService.login(expertPhone,expertPassword);
        return R.success();
    }

    @RequestMapping("/updateExpertInfo")
    public R updateExpertInfo(@RequestBody ExpertInfo expertInfo){
        expertService.updateExpertInfo(expertInfo);
        return R.success();
    }
}
