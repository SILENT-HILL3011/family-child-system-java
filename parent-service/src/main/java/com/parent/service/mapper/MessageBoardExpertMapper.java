package com.parent.service.mapper;

import com.child.common.entity.po.MessageBoardExpert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface MessageBoardExpertMapper {

    void insertMessageBoardExpert(MessageBoardExpert messageBoardExpert);

    @Select("select * from message_board_expert where board_id = #{boardId}")
    MessageBoardExpert selectBoardExpertByBoardId(String boardId);

    @Update("update message_board_expert set message_count = #{messageCount} where board_id = #{boardId}")
    void updateMessageBoardExpert(MessageBoardExpert messageBoardExpert);
}
