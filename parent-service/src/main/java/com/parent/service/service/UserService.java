package com.parent.service.service;

import com.child.common.entity.po.User;
import com.child.common.vo.UserLoginVO;

public interface UserService {
     UserLoginVO register(String phoneNumber, String password);

     User updateUserInfo(User user);

     UserLoginVO login(String phoneNumber, String password);
}
