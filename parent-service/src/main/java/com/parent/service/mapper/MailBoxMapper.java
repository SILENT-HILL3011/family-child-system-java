package com.parent.service.mapper;

import com.child.common.entity.po.MainBox;
import org.apache.ibatis.annotations.Insert;

public interface MailBoxMapper {
    @Insert("insert into main_box(id, send_user_id, title, user_id, content) values(#{id}, #{sendUserId}, #{title}, #{userId}, #{content})")
    void insert(MainBox mainBox);
}
