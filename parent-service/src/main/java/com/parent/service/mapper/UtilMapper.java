package com.parent.service.mapper;

import com.child.common.entity.po.*;
import com.child.common.entity.vo.MessageInfoVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UtilMapper {
    @Select("select util_name from util_info where type = #{type}")
    List<String> getUtilByType(Integer type);

    @Select("select * from util_info where util_name = #{utilName}")
    UtilInfo getUtilInfoByName(String utilName);

    @Select("select * from expert_info where expert_type = #{type}")
    List<ExpertInfo> getExpertByType(String type);

    @Select("select * from message_board_expert where board_id = #{boardId}")
    MessageBoardExpert selectBoardExpertByBoardId(String boardId);


    @Insert("insert into message_info(message_id,board_id,publisher_id,text,publish_date) values(#{messageId},#{boardId},#{publisherId},#{text},#{publishDate})")
    void insertMessageInfo(MessageInfo messageInfo);

    @Select("SELECT " +
            "mi.*, " +
            "mbe.expert_id, " +
            "mbe.user_id " +
            "FROM message_info mi " +
            "JOIN message_board_expert mbe ON mi.board_id = mbe.board_id " +
            "WHERE mi.board_id = #{boardId} " +
            "ORDER BY mi.publish_date ASC")
    List<MessageInfoVO> selectMessageInfoByBoardId(String boardId);
}
