package com.expert.service.controller;

import com.child.common.annotation.GlobalInterceptor;
import com.child.common.constants.Constant;
import com.child.common.entity.po.ExpertInfo;
import com.child.common.redis.RedisComponent;
import com.child.common.result.R;
import com.expert.service.service.ExpertService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
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
    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/expertRegister")
    public R expertRegister(@NotEmpty String expertPhone,@NotEmpty String expertPassword){
        expertService.register(expertPhone,expertPassword);
        return R.success();
    }

    @RequestMapping("/expertLogin")
    public R<String> expertLogin(@NotEmpty String expertPhone, @NotEmpty String expertPassword){
        String token = expertService.login(expertPhone,expertPassword);
        return R.success(token);
    }

    @RequestMapping("/updateExpertInfo")
    @GlobalInterceptor(checkLogin = true)
    public R updateExpertInfo(@RequestBody ExpertInfo expertInfo){
        expertService.updateExpertInfo(expertInfo);
        return R.success();
    }

    @RequestMapping("/searchExpertInfo")
    @GlobalInterceptor(checkLogin = true)
    public R<ExpertInfo> searchExpertInfo(){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String expertId = redisComponent.getExpertIdByToken(token);
        ExpertInfo expertInfo = expertService.searchExpertInfo(expertId);
        return R.success(expertInfo);
    }

    @RequestMapping("/createPersonalExamination")
    @GlobalInterceptor(checkLogin = true)
    public R createPersonalExamination(@NotEmpty String examinationTime){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String expertId = redisComponent.getExpertIdByToken(token);
        expertService.createPersonalExamination(expertId,examinationTime);
        return R.success();
    }
}
