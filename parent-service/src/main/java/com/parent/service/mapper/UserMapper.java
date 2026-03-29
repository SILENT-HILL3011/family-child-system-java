package com.parent.service.mapper;

import com.child.common.entity.po.Family;
import com.child.common.entity.po.Member;
import com.child.common.entity.po.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface UserMapper {

    @Select("select * from user_info where phone_number = #{phoneNumber}")
    User selectByPhoneNumber(String phoneNumber);


    @Update("insert into user_info(user_id, phone_number,password,have_family) values(#{userId},#{phoneNumber},#{password},#{haveFamily})")
    void insert(User user);

    User selectById(String id);

    void updateUserInfo(User user);

    @Select("select * from family_info where family_id = #{familyId}")
    Family selectFamilyById(String userId);

    @Insert("insert into family_info(family_id, create_user_id, family_name) values(#{familyId},#{createUserId},#{familyName})")
    void insertFamily(Family family);


    @Insert("insert into member_info(member_id, family_id, member_name,seniority,phone,role) values(#{memberId},#{familyId},#{memberName},#{seniority},#{phone},#{role})")
    void insertMember(Member member);

    @Update("update user_info set have_family = 1 where user_id = #{userId}")
    void updateUserFamily(String userId);

    @Select("select * from member_info where member_id = #{memberId}")
    Member selectFamilyByMemberId(String memberId);

    @Select("select * from member_info where family_id = (select family_id from member_info where member_id = #{userId})")
    List<Member> selectMemberList(String userId);

    @Select("select member_name from member_info where member_id = #{publisherId}")
    String selectMemberName(String publisherId);

    @Select("select family_id from member_info where member_name = #{userName}")
    String selectFamilyIdByName(String userName);

    @Select("select * from member_info where phone = #{phone}")
    Member selectMemberByPhone(String phone);

    @Update("update member_info set role = #{role} where phone = #{phone}")
    void updateRole(Member member);

    @Select("select count(*) from member_info where family_id = #{familyId} and role = 1")
    int countPrimaryByFamilyId(String familyId);
}
