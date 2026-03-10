package com.parent.service.service.impl;

import com.child.common.constants.Constant;
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

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisComponent redisComponent;

    @Override
    public UserLoginVO register(String phoneNumber, String password) {
        User checkIsExist = userMapper.selectByPhoneNumber(phoneNumber);
        if (checkIsExist != null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setPhoneNumber(phoneNumber);
        userLoginVO.setPassword(StringTools.getMd5(password));
        userLoginVO.setUserId(StringTools.getRandomNumber(Constant.LENGTH_12));
        userMapper.insert(userLoginVO);
        return userLoginVO;
    }

    @Override
    public UserLoginVO login(String phoneNumber, String password) {
        User checkIsExist = userMapper.selectByPhoneNumber(phoneNumber);
        if (checkIsExist == null || !StringTools.getMd5(password).equals(checkIsExist.getPassword())){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        String token = StringTools.getMd5(checkIsExist.getId()+StringTools.getRandomNumber(Constant.LENGTH_20));
        redisComponent.saveUserLoginToken(token,phoneNumber);
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setUserId(checkIsExist.getId());
        userLoginVO.setPhoneNumber(phoneNumber);
        userLoginVO.setPassword(password);
        return userLoginVO;
    }

    @Override
    public User updateUserInfo(User user) {
        if (user.getId() == null || user.getId().isEmpty()){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        User checkIsExist = userMapper.selectById(user.getId());
        if (checkIsExist == null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        if (user.getUserName() != null){
            updateUser.setUserName(user.getUserName());
        }
        if (user.getAge() != null){
            updateUser.setAge(user.getAge());
        }
        if (user.getSex() != null){
            updateUser.setSex(user.getSex());
        }
        if (user.getHaveFamily() != null){
            updateUser.setHaveFamily(user.getHaveFamily());
        }
        userMapper.updateUserInfo(updateUser);
        return null;
    }


}
