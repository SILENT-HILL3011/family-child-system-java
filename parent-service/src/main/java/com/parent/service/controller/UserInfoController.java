package com.parent.service.controller;

import com.child.common.annotation.GlobalInterceptor;
import com.child.common.annotation.RequirePrimaryCaregiver;
import com.child.common.constants.Constant;
import com.child.common.entity.po.Member;
import com.child.common.entity.po.User;
import com.child.common.redis.RedisComponent;
import com.child.common.result.R;
import com.child.common.entity.vo.UserLoginVO;
import com.github.pagehelper.PageInfo;
import com.parent.service.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/child/user")
@Validated
public class UserInfoController {
    @Resource
    private UserService userService;
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private HttpServletRequest request;


    @RequestMapping("/login")
    public R<String> login(@NotEmpty String phoneNumber, @NotEmpty String password){
        String token =  userService.login(phoneNumber, password);
        return R.success(token);
    }

    @RequestMapping("/register")
    public R<UserLoginVO> register(@NotEmpty String phoneNumber, @NotEmpty String password){
        userService.register(phoneNumber, password);
        return R.success();
    }

    @RequestMapping("/createFamily")
    @GlobalInterceptor(checkLogin = true)
    public R createFamily(@NotEmpty String familyName,@NotEmpty String seniority){
        String userId = redisComponent.getUserIdByToken(request.getHeader(Constant.TOKEN_HEADER_KEY));
        userService.createFamily(userId,familyName,seniority);
        return R.success();
    }

    @RequestMapping("/updateUserInfo")
    @GlobalInterceptor(checkLogin = true)
    public R<User> updateUserInfo(@RequestBody User user){
        return R.success(userService.updateUserInfo(user));
    }

    @PostMapping("/inviteMember")
    @GlobalInterceptor(checkLogin = true)
    @RequirePrimaryCaregiver
    public R inviteMember(@NotEmpty String phoneNumber,@NotEmpty String familyId,@NotEmpty String seniority,@NotNull Integer role){
        userService.inviteMember(phoneNumber,familyId,seniority,role);
        return R.success();
    }

    @RequestMapping("/getUserInfo")
    @GlobalInterceptor(checkLogin = true)
    public R<User> getUserInfo(){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String userId = redisComponent.getUserIdByToken(token);
        return R.success(userService.getUserInfo(userId));
    }

    @RequestMapping("/searchMemberList")
    @GlobalInterceptor(checkLogin = true)
    public R<PageInfo<Member>> searchMemberList(Integer pageNum){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String userId = redisComponent.getUserIdByToken(token);
        return R.success(userService.searchMemberList(userId,pageNum));
    }

    @RequestMapping("/kickOut")
    @GlobalInterceptor(checkLogin = true)
    @RequirePrimaryCaregiver
    public R kickOut(@NotEmpty String memberId){
        userService.kickOut(memberId);
        return R.success();
    }



    @RequestMapping("/getFamilyId")
    @GlobalInterceptor(checkLogin = true)
    public R<String> getFamilyId(){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String userId = redisComponent.getUserIdByToken(token);
        return R.success(userService.getFamilyId(userId));
    }

}
