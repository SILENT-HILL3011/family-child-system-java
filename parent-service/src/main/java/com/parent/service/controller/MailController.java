package com.parent.service.controller;

import com.child.common.annotation.GlobalInterceptor;
import com.child.common.constants.Constant;
import com.child.common.entity.po.MainBox;
import com.child.common.entity.vo.MailBoxVO;
import com.child.common.redis.RedisComponent;
import com.child.common.result.R;
import com.github.pagehelper.PageInfo;
import com.parent.service.mapper.MailBoxMapper;
import com.parent.service.service.MailBoxService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/child/mail")
public class MailController {

    @Resource
    private MailBoxService mailBoxService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/searchMailList")
    @GlobalInterceptor(checkLogin = true)
    public R<PageInfo<MailBoxVO>> searchMailList(Integer pageNum){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String userId = redisComponent.getUserIdByToken(token);
        return R.success(mailBoxService.searchMailList(userId,pageNum));
    }

    @RequestMapping("/openMail")
    @GlobalInterceptor(checkLogin = true)
    public R<String> openMail(@NotEmpty String mailId){
        return R.success(mailBoxService.readMail(mailId));
    }

    @RequestMapping("/readAll")
    @GlobalInterceptor(checkLogin = true)
    public R readAll(){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String userId = redisComponent.getUserIdByToken(token);
        mailBoxService.readAll(userId);
        return R.success();
    }

    @RequestMapping("/checkUnReadMails")
    @GlobalInterceptor(checkLogin = true)
    public R<Boolean> checkUnReadMails(){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String userId = redisComponent.getUserIdByToken(token);
        return R.success(mailBoxService.checkUnReadMails(userId));
    }
}
