package com.parent.service.service;

import com.child.common.entity.po.Member;
import com.child.common.entity.po.User;
import com.github.pagehelper.PageInfo;

public interface UserService {
     void register(String phoneNumber, String password);

     User updateUserInfo(User user);

     String login(String phoneNumber, String  password);

     void createFamily(String userId, String familyName,String seniority);

     void inviteMember(String phoneNumber, String familyId,String seniority,Integer role);

     User getUserInfo(String userId);

    PageInfo<Member> searchMemberList(String userId,Integer pageNum);

    String getFamilyId(String userId);

    void kickOut(String memberId);
}
