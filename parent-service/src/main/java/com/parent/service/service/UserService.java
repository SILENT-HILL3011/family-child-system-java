package com.parent.service.service;

import com.child.common.entity.po.User;
import com.child.common.vo.UserLoginVO;

import java.util.List;

public interface UserService {
     void register(String phoneNumber, String password,Integer role);

     User updateUserInfo(User user);

     UserLoginVO login(String phoneNumber, String password);

     void createFamily(String userId, String familyName,String seniority);

     void inviteMember(String phoneNumber, String familyId,String seniority,Integer role);
}
