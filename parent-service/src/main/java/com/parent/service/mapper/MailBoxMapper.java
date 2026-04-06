package com.parent.service.mapper;

import com.child.common.entity.po.MainBox;
import com.child.common.entity.vo.MailBoxVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface MailBoxMapper {
    @Insert("insert into main_box(id, send_user_id, title, user_id, content,is_read) values(#{id}, #{sendUserId}, #{title}, #{userId}, #{content},#{isRead})")
    void insert(MainBox mainBox);

    @Select({
            "SELECT",
            "    id,",
            "    send_user_id,",
            "    title,",
            "    create_time,",
            "    is_read",
            "FROM sys_message",
            "WHERE user_id = #{userId} OR user_id IS NULL",
            "ORDER BY",
            "    is_read ASC,",
            "    create_time DESC"
    })
    List<MailBoxVO> searchMailList(String userId, Integer pageNum);

    @Select("select * from sys_message where id = #{mailId}")
    MainBox readMail(String mailId);

    @Update("update sys_message set is_read = 1 where id = #{id}")
    void update(MainBox mainBox);

    @Update("update sys_message set is_read = 1 where user_id = #{userId} and is_read = 0 ")
    void readAll(String userId);
}
