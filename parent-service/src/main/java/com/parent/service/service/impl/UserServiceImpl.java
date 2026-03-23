package com.parent.service.service.impl;

import com.child.common.constants.Constant;
import com.child.common.entity.enums.HaveFamilyEnum;
import com.child.common.entity.enums.MemberRole;
import com.child.common.entity.po.Family;
import com.child.common.entity.po.Member;
import com.child.common.entity.po.User;
import com.child.common.entity.vo.ResponseCodeEnum;
import com.child.common.exception.BusinessException;
import com.child.common.redis.RedisComponent;
import com.child.common.utils.StringTools;
import com.child.common.vo.UserLoginVO;
import com.parent.service.mapper.UserMapper;
import com.parent.service.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisComponent redisComponent;

    @Override
    public void register(String phoneNumber, String password,Integer role) {
        User checkIsExist = userMapper.selectByPhoneNumber(phoneNumber);
        if (checkIsExist != null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        User user = new User();
        user.setUserId(StringTools.getRandomNumber(Constant.LENGTH_12));
        user.setPhoneNumber(phoneNumber);
        user.setPassword(StringTools.getMd5(password));
        user.setRole(role);
        user.setHaveFamily(HaveFamilyEnum.NO.getValue());
        userMapper.insert(user);
    }

    @Override
    public UserLoginVO login(String phoneNumber, String password) {
        User checkIsExist = userMapper.selectByPhoneNumber(phoneNumber);
        if (checkIsExist == null || !StringTools.getMd5(password).equals(checkIsExist.getPassword())){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        String token = StringTools.getMd5(checkIsExist.getUserId()+StringTools.getRandomNumber(Constant.LENGTH_20));
        redisComponent.saveUserLoginToken(token,phoneNumber);
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setUserId(checkIsExist.getUserId());
        userLoginVO.setPhoneNumber(phoneNumber);
        userLoginVO.setPassword(password);
        return userLoginVO;
    }

    @Override
    public void createFamily(String userId,String familyName,String seniority) {
        Family checkIsExist = userMapper.selectFamilyById(userId);
        if (checkIsExist != null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        Family family = new Family();
        family.setFamilyId(StringTools.getRandomNumber(Constant.LENGTH_12));
        family.setCreateUserId(userId);
        family.setFamilyName(familyName);
        userMapper.insertFamily(family);
        userMapper.updateUserFamily(userId);
        User user = userMapper.selectById(userId);
        addMember(userId,family.getFamilyId(),user.getUserName(),seniority, MemberRole.MAIN.getCode(),user.getPhoneNumber());
    }

    @Override
    public void inviteMember(String phoneNumber, String familyId,String seniority,Integer role) {
        User user = userMapper.selectByPhoneNumber(phoneNumber);
        String id = user.getUserId();
        if (user == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600); // 或其他适当的错误码
        }
        addMember(user.getUserId(),familyId,user.getUserName(),seniority, role,user.getPhoneNumber());
    }

    @Override
    public User updateUserInfo(User user) {
        if (user.getUserId() == null || user.getUserId().isEmpty()){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        User checkIsExist = userMapper.selectById(user.getUserId());
        if (checkIsExist == null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        User updateUser = new User();
        updateUser.setUserId(user.getUserId());
        if (user.getUserName() != null){
            updateUser.setUserName(user.getUserName());
        }
        if (user.getAge() != null){
            updateUser.setAge(user.getAge());
        }
        if (user.getSex() != null){
            updateUser.setSex(user.getSex());
        }
        userMapper.updateUserInfo(updateUser);
        return null;
    }

    private void addMember(String memberId,String familyId,String memberName,String seniority,Integer role,String phone){
        Member member = new Member();
        member.setMemberId(memberId);
        member.setFamilyId(familyId);
        member.setMemberName(memberName);
        member.setSeniority(seniority);
        member.setRole(role);
        member.setPhone(phone);
        userMapper.insertMember(member);
    }


}
