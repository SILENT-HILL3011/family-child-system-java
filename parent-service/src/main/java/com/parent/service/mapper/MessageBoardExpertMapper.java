package com.parent.service.mapper;

import com.child.common.entity.po.MessageBoard;
import com.child.common.entity.po.MessageBoardExpert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface MessageBoardExpertMapper {

    void insertMessageBoardExpert(MessageBoardExpert messageBoardExpert);

    @Select("select * from message_board_expert where board_id = #{boardId}")
    MessageBoardExpert selectBoardExpertByBoardId(String boardId);

    @Update("update message_board_expert set message_count = #{messageCount} where board_id = #{boardId}")
    void updateMessageBoardExpert(MessageBoardExpert messageBoardExpert);

    @Select("select * from message_board_expert where user_id = #{userId} and is_finished = 0")
    List<MessageBoardExpert> searchMyMessage(String userId);


    @Update("update message_board_expert set is_finished = 1 where board_id = #{boardId}")
    void finish(MessageBoardExpert messageBoardExpert);
}
