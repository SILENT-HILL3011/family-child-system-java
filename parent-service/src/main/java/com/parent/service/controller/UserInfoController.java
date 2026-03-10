package com.parent.service.controller;

import com.child.common.annotation.GlobalInterceptor;
import com.child.common.entity.po.User;
import com.child.common.entity.vo.CheckCodeVO;
import com.child.common.redis.RedisComponent;
import com.child.common.result.R;
import com.child.common.vo.UserLoginVO;
import com.parent.service.service.UserService;
import com.wf.captcha.ArithmeticCaptcha;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/child/user")
@Validated
public class UserInfoController {
    @Resource
    private UserService userService;
    @Resource
    private RedisComponent redisComponent;


    @RequestMapping("/login")
    public R<UserLoginVO> login( @NotEmpty String phoneNumber, @NotEmpty String password){
        UserLoginVO userLoginVO = userService.login(phoneNumber, password);
        return R.success(userLoginVO);
    }

    @RequestMapping("/register")
    public R<UserLoginVO> register(@NotEmpty String phoneNumber, @NotEmpty String password){
        userService.register(phoneNumber, password);
        return R.success();
    }

    @RequestMapping("/createFamily")
    public R createFamily(@NotEmpty String userId, List<String> childNames){
        return R.success();
    }

    @RequestMapping("/updateUserInfo")
    @GlobalInterceptor(checkLogin = true)
    public R<User> updateUserInfo(@RequestBody User user){
        return R.success(userService.updateUserInfo(user));
    }
}
