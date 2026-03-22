package com.parent.service.mapper;

import com.child.common.entity.po.Child;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface ChildMapper {
    @Select("select * from child_info where child_name = #{childName} and family_id = #{familyId}")
    Child selectByNameAndFamilyId(String childName, String familyId);

    @Insert("insert into child_info(child_id, child_name, family_id, sex) values(#{childId},#{childName},#{familyId},#{sex})")
    void insert(Child child);
}
