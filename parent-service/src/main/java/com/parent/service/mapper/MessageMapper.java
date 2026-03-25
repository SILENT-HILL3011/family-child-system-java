package com.parent.service.mapper;

import com.child.common.entity.po.MessageBoard;
import com.child.common.entity.po.MessageLike;
import com.child.common.entity.vo.MessageBoardVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface MessageMapper {

    @Insert("insert into message_board(message_id,family_id,publisher_id,content,image_url,publish_time,like_count,comment_count) values(#{messageId},#{familyId},#{publisherId},#{content},#{imageUrl},#{publishTime},#{likeCount},#{commentCount})")
    void insertMessage(MessageBoard messageBoard);

    List<MessageBoard> selectMessage(String familyId, String publisherId, Integer timePeriod);

    @Update("update message_board set like_count = #{likeCount} where message_id = #{messageId}")
    void updateMessage(MessageBoardVO likeMessageVO);

    @Insert("insert into message_like(like_id,message_id,user_id,like_time) values(#{likeId},#{messageId},#{userId},#{likeTime})")
    void insertMessageLike(MessageLike messageLike);
}
