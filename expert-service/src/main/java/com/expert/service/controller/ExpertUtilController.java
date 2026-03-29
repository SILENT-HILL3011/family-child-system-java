package com.expert.service.controller;

import com.child.common.annotation.GlobalInterceptor;
import com.child.common.constants.Constant;
import com.child.common.entity.po.MessageBoardExpert;
import com.child.common.entity.po.MessageInfo;
import com.child.common.redis.RedisComponent;
import com.child.common.result.R;
import com.expert.service.service.ExpertUtilService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/expert/util")
@Validated
public class ExpertUtilController {


    @Resource
    private RedisComponent redisComponent;
    @Resource
    private HttpServletRequest request;
    @Resource
    private ExpertUtilService expertUtilService;

    @RequestMapping("/searchMessage")
    @GlobalInterceptor(checkLogin = true)
    public R<PageInfo<MessageBoardExpert>> searchMessage(Integer pageNum){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String expertId = redisComponent.getExpertIdByToken(token);
        return R.success(expertUtilService.searchMessage(expertId,pageNum));
    }

    @RequestMapping("/history")
    @GlobalInterceptor(checkLogin = true)
    public R<List<MessageInfo>> history(@NotEmpty String boardId){
        return R.success(expertUtilService.history(boardId));
    }

    @RequestMapping("/apply")
    @GlobalInterceptor(checkLogin = true)
    public R apply(@NotEmpty String content,@NotEmpty String boardId){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String expertId = redisComponent.getExpertIdByToken(token);
        expertUtilService.apply(content,boardId,expertId);
        return R.success();
    }
}
