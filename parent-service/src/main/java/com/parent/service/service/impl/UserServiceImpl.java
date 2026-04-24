package com.parent.service.service.impl;

import com.child.common.constants.Constant;
import com.child.common.entity.enums.HaveFamilyEnum;
import com.child.common.entity.enums.MemberRoleEnum;
import com.child.common.entity.po.Family;
import com.child.common.entity.po.Member;
import com.child.common.entity.po.User;
import com.child.common.entity.vo.ResponseCodeEnum;
import com.child.common.exception.BusinessException;
import com.child.common.redis.RedisComponent;
import com.child.common.utils.RequestHolder;
import com.child.common.utils.StringTools;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.parent.service.mapper.UserMapper;
import com.parent.service.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisComponent redisComponent;

    @Override
    public void register(String phoneNumber, String password) {
        User checkIsExist = userMapper.selectByPhoneNumber(phoneNumber);
        if (checkIsExist != null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        User user = new User();
        user.setUserId(StringTools.getRandomNumber(Constant.LENGTH_12));
        user.setPhoneNumber(phoneNumber);
        user.setPassword(StringTools.getMd5(password));
        user.setHaveFamily(HaveFamilyEnum.NO.getValue());
        userMapper.insert(user);
    }

    @Override
    public String login(String phoneNumber, String password) {
        String ip = RequestHolder.getIp();
        if (redisComponent.isIpLocked(ip)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        User checkIsExist = userMapper.selectByPhoneNumber(phoneNumber);
        if (checkIsExist == null || !StringTools.getMd5(password).equals(checkIsExist.getPassword())) {
            int failCount = redisComponent.getLoginFailCount(ip) + 1;
            redisComponent.saveLoginFailCount(ip, failCount);
            if (failCount >= 3) {
                redisComponent.lockIp(ip);
            }
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        redisComponent.clearLoginFail(ip);
        String token = StringTools.getMd5(checkIsExist.getUserId() + StringTools.getRandomNumber(Constant.LENGTH_20));
        redisComponent.saveUserLoginToken(token, checkIsExist.getUserId());
        Member member = userMapper.selectMemberByPhone(phoneNumber);
        if (member != null) {
            redisComponent.save(Constant.REDIS_ROLE_KEY + token, member.getRole().toString());
            redisComponent.save(Constant.REDIS_FAMILY_KEY + token, member.getFamilyId());
        }
        return token;
    }

    @Override
    @Transactional
    public void createFamily(String userId,String familyName,String seniority) {
        User user = userMapper.selectById(userId);
        if (user.getHaveFamily() == 1){
            throw new BusinessException("家庭已存在");
        }
        Family family = new Family();
        family.setFamilyId(StringTools.getRandomNumber(Constant.LENGTH_12));
        family.setCreateUserId(userId);
        family.setFamilyName(familyName);
        userMapper.insertFamily(family);
        addMember(userId,family.getFamilyId(),user.getUserName(),seniority, MemberRoleEnum.MAIN.getCode(),user.getPhoneNumber(),user.getAvatar());
        user.setHaveFamily(Constant.IS);
        userMapper.updateUserInfo(user);
    }

    @Override
    public void inviteMember(String phoneNumber, String familyId,String seniority,Integer role) {
        User user = userMapper.selectByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (user.getHaveFamily() == 1){
            throw new BusinessException("用户已加入家庭");
        }
        addMember(user.getUserId(),familyId,user.getUserName(),seniority, role,user.getPhoneNumber(),user.getAvatar());
    }

    @Override
    public User getUserInfo(String userId) {
        User user = userMapper.selectById(userId);
        if (user == null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        return user;
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
        if (user.getAddress() != null){
            updateUser.setAddress(user.getAddress());
        }
        if (user.getMail() != null){
            updateUser.setMail(user.getMail());
        }
        if (user.getAvatar() != null){
            updateUser.setAvatar(user.getAvatar());
        }
        userMapper.updateUserInfo(updateUser);
        return null;
    }

    @Override
    public PageInfo<Member> searchMemberList(String userId,Integer pageNum) {
        if (pageNum == null){
            pageNum = Constant.NUM_ONE;
        }
        PageHelper.startPage(pageNum, 10);
        List<Member> memberList = userMapper.selectMemberList(userId);
        return new PageInfo<>(memberList);
    }

    @Override
    public String getFamilyId(String userId) {
        Member member = userMapper.selectMemberByUserId(userId);
        return member == null ? null : member.getFamilyId();
    }

    @Override
    @Transactional
    public void kickOut(String memberId) {
        userMapper.kickOut(memberId);
        userMapper.outFamily(memberId);
    }


    @Transactional
    public void addMember(String memberId, String familyId, String memberName, String seniority, Integer role, String phone,String avatar){
        Member member = new Member();
        member.setMemberId(memberId);
        member.setFamilyId(familyId);
        member.setMemberName(memberName);
        member.setSeniority(seniority);
        member.setRole(role);
        member.setPhone(phone);
        member.setAvatar(avatar);
        userMapper.insertMember(member);
        userMapper.haveFamily(memberId);
    }


}
