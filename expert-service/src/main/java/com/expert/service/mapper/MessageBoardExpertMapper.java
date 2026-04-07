package com.expert.service.mapper;

import com.child.common.entity.po.MessageBoardExpert;
import com.child.common.entity.po.MessageInfo;
import com.child.common.entity.vo.MessageInfoVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MessageBoardExpertMapper {
    @Select("select * from message_board_expert where expert_id = #{expertId} and is_finished = 0")
    List<MessageBoardExpert> searchMyMessage(String expertId);

    @Select("select * from message_info where board_id = #{boardId}")
    List<MessageInfoVO> history(String boardId);

    @Insert("insert into message_info(message_id,board_id,publisher_id,text,publish_date) values(#{messageId},#{boardId},#{publisherId},#{text},#{publishDate})")
    void apply(MessageInfo messageInfo);

    @Select("select * from message_board_expert where board_id = #{boardId}")
    MessageBoardExpert selectBoardByBoardId(String boardId);
}
