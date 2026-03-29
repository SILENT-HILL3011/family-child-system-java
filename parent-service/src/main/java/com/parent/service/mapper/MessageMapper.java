package com.parent.service.mapper;

import com.child.common.entity.po.MessageBoard;
import com.child.common.entity.po.MessageComment;
import com.child.common.entity.po.MessageLike;
import com.child.common.entity.vo.MessageBoardVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface MessageMapper {

    @Insert("insert into message_board(message_id,family_id,publisher_id,content,image_url,publish_time,like_count,comment_count) values(#{messageId},#{familyId},#{publisherId},#{content},#{imageUrl},#{publishTime},#{likeCount},#{commentCount})")
    void insertMessage(MessageBoard messageBoard);

    List<MessageBoard> selectMessage(String familyId,  Integer timePeriod);

    @Update("update message_board set like_count = #{likeCount} ,comment_count = #{commentCount} where message_id = #{messageId}")
    void updateMessage(MessageBoardVO likeMessageVO);

    @Insert("insert into message_like(like_id,message_id,user_id,like_time) values(#{likeId},#{messageId},#{userId},#{likeTime})")
    void insertMessageLike(MessageLike messageLike);

    @Select("select * from message_board where message_id = #{messageId}")
    MessageBoard selectMessageById(String messageId);

    @Insert("insert into message_comment(comment_id,message_id,user_id,content,comment_time,reply_to_id) values(#{commentId},#{messageId},#{userId},#{content},#{commentTime},#{replyToId})")
    void insertMessageComment(MessageComment messageComment);


    @Select("select * from message_comment where comment_id = #{commentId}")
    MessageComment selectMessageCommentById(String commentId);

    @Select("select * from message_comment where message_id = #{messageId} order by comment_time ASC")
    List<MessageComment> selectCommentByMessageId(String messageId);

    @Update("update message_board set comment_count = comment_count + 1 where message_id = #{messageId}")
    void updateCommentCount(String messageId);
}
