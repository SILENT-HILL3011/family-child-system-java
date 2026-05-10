package com.parent.service.service;

import com.child.common.entity.po.MessageComment;
import com.child.common.entity.po.TaskInfo;
import com.child.common.entity.vo.MessageBoardVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface FamilyService {
    void publishTask(String publisherId, String taskName, String publishDate);

    TaskInfo acceptTask(String publisherId, String receiverId,String taskName);

    TaskInfo finishTask(String receiverId, String taskName);

    List<TaskInfo> searchTask(String familyId);

    void publishMessage(String publisherId, String content, String imageUrl);

    List<MessageBoardVO> searchMessageByPage(String familyId, Integer timePeriod);

    MessageBoardVO likeMessage(String messageId);

    MessageBoardVO applyToMessage(String messageId, String content);

    List<MessageComment> searchComment(String messageId);

    String applyToComment(String commentId, String content);

    List<TaskInfo> searchMyTask(String userId);


    void changeRole(String phoneNumber, Integer role);

}
