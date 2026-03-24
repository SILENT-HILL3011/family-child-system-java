package com.parent.service.mapper;

import com.child.common.entity.po.Family;
import com.child.common.entity.po.Member;
import com.child.common.entity.po.User;
import com.child.common.vo.UserLoginVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;




public interface UserMapper {

    @Select("select * from user_info where phone_number = #{phoneNumber}")
    User selectByPhoneNumber(String phoneNumber);


    @Update("insert into user_info(user_id, phone_number,password,role,have_family) values(#{userId},#{phoneNumber},#{password},#{role},#{haveFamily})")
    void insert(User user);

    User selectById(String id);

    void updateUserInfo(User user);

    @Select("select * from family_info where family_id = #{familyId}")
    Family selectFamilyById(String userId);

    @Insert("insert into family_info(family_id, create_user_id, family_name) values(#{familyId},#{createUserId},#{familyName})")
    void insertFamily(Family family);


    @Insert("insert into member_info(member_id, family_id, member_name,seniority,role,phone) values(#{memberId},#{familyId},#{memberName},#{seniority},#{role},#{phone})")
    void insertMember(Member member);

    @Update("update user_info set have_family = 1 where user_id = #{userId}")
    void updateUserFamily(String userId);
}
