package com.parent.service.controller;

import com.child.common.entity.dto.UserLoginDTO;
import com.child.common.entity.po.User;
import com.child.common.result.R;
import com.child.common.vo.UserLoginVO;
import com.parent.service.service.UserService;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/child/user")
@Validated
public class UserLoginController {
    @Autowired
    private UserService userService;
//    @RequestMapping("/login")
//    public R<UserLoginVO> login(@NotEmpty String phoneNumber, @NotEmpty String password){
//        UserLoginVO userLoginVO = userService.login();
//        return R.success(userLoginVO);
//    }

    @RequestMapping("/register")
    public R<UserLoginVO> register(@NotEmpty String phoneNumber, @NotEmpty String password){
        UserLoginVO userLoginVO = userService.register(phoneNumber, password);
        return R.success(userLoginVO);
    }

    @RequestMapping("/updateUserInfo")
    public R<User> updateUserInfo(@RequestBody User user){
        return R.success(userService.updateUserInfo(user));
    }
}
