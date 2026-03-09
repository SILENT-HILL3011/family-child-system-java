package com.parent.service.service.impl;

import com.child.common.constants.Constant;
import com.child.common.entity.dto.UserLoginDTO;
import com.child.common.entity.po.User;
import com.child.common.entity.vo.ResponseCodeEnum;
import com.child.common.exception.BusinessException;
import com.child.common.utils.StringTools;
import com.child.common.vo.UserLoginVO;
import com.parent.service.mapper.UserMapper;
import com.parent.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

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
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        if (userLoginDTO.getUsername() == null || userLoginDTO.getPassword() == null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        User checkIsExist = userMapper.selectByPhoneNumber(userLoginDTO.getUsername());
        if (checkIsExist == null || !StringTools.getMd5(userLoginDTO.getPassword()).equals(checkIsExist.getPassword())){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setPhoneNumber(checkIsExist.getPhoneNumber());
        userLoginVO.setPassword(checkIsExist.getPassword());
        return userLoginVO;
    }

    @Override
    public User updateUserInfo(User user) {
        return null;
    }


}
