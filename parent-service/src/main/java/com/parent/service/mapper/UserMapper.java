package com.parent.service.mapper;

import com.child.common.entity.po.User;
import com.child.common.vo.UserLoginVO;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


public interface UserMapper {

    @Select("select * from user_info where phone_number = #{phoneNumber}")
    User selectByPhoneNumber(String phoneNumber);


    @Update("insert into user_info(user_id, phone_number,password) values(#{userId},#{phoneNumber},#{password})")
    void insert(UserLoginVO userLoginVO);

    User selectById(String id);

    void updateUserInfo(User user);
}
