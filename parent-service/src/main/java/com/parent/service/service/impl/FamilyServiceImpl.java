package com.parent.service.service.impl;

import com.child.common.constants.Constant;
import com.child.common.entity.po.*;
import com.child.common.entity.vo.MessageBoardVO;
import com.child.common.exception.BusinessException;
import com.child.common.utils.DateUtils;
import com.child.common.utils.StringTools;
import com.parent.service.mapper.MessageMapper;
import com.parent.service.mapper.TaskMapper;
import com.parent.service.mapper.UserMapper;
import com.parent.service.service.FamilyService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FamilyServiceImpl implements FamilyService {

    @Resource
    private TaskMapper taskMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private MessageMapper messageMapper;

    @Override
    public void publishTask(String publisherId, String taskName, String publishDate) {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskId(StringTools.getRandomNumber(Constant.LENGTH_12));
        taskInfo.setPublisherId(publisherId);
        taskInfo.setTaskName(taskName);
        taskInfo.setPublishDate(DateUtils.ChangeStr2DateTime(publishDate));
        taskInfo.setIsAccepted(Constant.NO);
        taskInfo.setIsFinished(Constant.NO);
        taskMapper.insertTask(taskInfo);
    }

    @Override
    public TaskInfo acceptTask(String publisherId, String receiverId,String taskName) {
        List<TaskInfo> taskInfoList = taskMapper.selectByPublisherId(publisherId);
        if (taskInfoList.isEmpty()){
            throw new BusinessException("任务不存在");
        }
        TaskInfo updateTaskInfo = null;
        for (TaskInfo taskInfo : taskInfoList){
            if (taskInfo.getTaskName().equals(taskName)){
                updateTaskInfo = taskInfo;
                updateTaskInfo.setIsAccepted(Constant.IS);
                updateTaskInfo.setReceiverId(receiverId);
                taskMapper.updateTask(updateTaskInfo);
                break;
            }
        }
        return updateTaskInfo;
    }

    @Override
    public TaskInfo finishTask(String receiverId, String taskName) {
        TaskInfo taskInfo = taskMapper.selectByReceiverIdAndTaskName(receiverId, taskName);
        if (taskInfo == null){
            throw new BusinessException("任务不存在");
        }
        if (taskInfo.getIsFinished().equals(Constant.IS)){
            throw new BusinessException("任务已完成");
        }
        taskInfo.setIsFinished(Constant.IS);
        taskMapper.updateTask(taskInfo);
        return taskInfo;
    }

    @Override
    public List<TaskInfo> searchTask(@NotEmpty String familyId) {
        List<String> memberofRole = userMapper.selectMemberByRole(familyId);
        List<TaskInfo> allTasks = new ArrayList<>();
        for (String memberId : memberofRole) {
            List<TaskInfo> tasks = taskMapper.selectByPublisherId(memberId);
            if (tasks != null && !tasks.isEmpty()) {
                allTasks.addAll(tasks);
            }
        }
        return allTasks.stream()
                .filter(task -> task.getIsFinished().equals(Constant.NO))
                .collect(Collectors.toList());
    }



    @Override
    public void publishMessage(String publisherId, String content, String imageUrl) {
        Member member = userMapper.selectFamilyByMemberId(publisherId);
        if (member == null){
            throw new BusinessException("用户未加入家庭");
        }
        MessageBoard messageBoard = new MessageBoard();
        messageBoard.setMessageId(StringTools.getRandomNumber(Constant.LENGTH_12));
        messageBoard.setFamilyId(member.getFamilyId());
        messageBoard.setPublisherId(publisherId);
        messageBoard.setContent(content);
        messageBoard.setImageUrl(imageUrl);
        messageBoard.setPublishTime(new Date());
        messageBoard.setLikeCount(Constant.NUM_ZERO);
        messageBoard.setCommentCount(Constant.NUM_ZERO);
        messageBoard.setAvatar(userMapper.getAvatarByUserId(publisherId));
        messageMapper.insertMessage(messageBoard);
    }

    @Override
    public List<MessageBoardVO> searchMessageByPage(String familyId, Integer timePeriod) {
        if (timePeriod == null){
            timePeriod = Constant.NUM_ONE;
        }
        if (timePeriod != 1 && timePeriod != 7 && timePeriod != 30) {
            throw new BusinessException("时间段错误");
        }

        List<MessageBoard> messageBoards = messageMapper.selectMessage(familyId, timePeriod);
        List<MessageBoardVO> messageBoardVOList = new ArrayList<>();
        for (MessageBoard messageBoard : messageBoards) {
            MessageBoardVO messageBoardVO = new MessageBoardVO();
            messageBoardVO.setMessageId(messageBoard.getMessageId());
            String memberName = userMapper.selectMemberName(messageBoard.getPublisherId());
            messageBoardVO.setMemberName(memberName);
            messageBoardVO.setContent(messageBoard.getContent());
            messageBoardVO.setPublishTime(messageBoard.getPublishTime());
            messageBoardVO.setLikeCount(messageBoard.getLikeCount());
            messageBoardVO.setCommentCount(messageBoard.getCommentCount());
            messageBoardVO.setAvatar(messageBoard.getAvatar());
            messageBoardVO.setImageUrl(messageBoard.getImageUrl());
            messageBoardVOList.add(messageBoardVO);
        }
        return messageBoardVOList;
    }

    @Override
    public MessageBoardVO likeMessage(String messageId) {
        MessageBoard likeMessageBoard = messageMapper.selectMessageById(messageId);
        if (likeMessageBoard == null){
            throw new BusinessException("消息不存在");
        }
        Member member = userMapper.selectFamilyByMemberId(likeMessageBoard.getPublisherId());
        MessageBoardVO likeMessageVO = new MessageBoardVO();
        likeMessageVO.setMessageId(likeMessageBoard.getMessageId());
        likeMessageVO.setContent(likeMessageBoard.getContent());
        likeMessageVO.setPublishTime(likeMessageBoard.getPublishTime());
        likeMessageVO.setLikeCount(likeMessageBoard.getLikeCount() + 1);
        likeMessageVO.setCommentCount(likeMessageBoard.getCommentCount());
        messageMapper.updateMessage(likeMessageVO);
        MessageLike messageLike = new MessageLike();
        messageLike.setLikeId(StringTools.getRandomNumber(Constant.LENGTH_12));
        messageLike.setMessageId(likeMessageVO.getMessageId());
        messageLike.setUserId(likeMessageBoard.getPublisherId());
        messageLike.setLikeTime(new Date());
        messageMapper.insertMessageLike(messageLike);
        return likeMessageVO;
        // TODO 重复点赞是取消点赞
    }

    @Override
    public MessageBoardVO applyToMessage(String messageId, String content) {
        MessageBoard likeMessageBoard = messageMapper.selectMessageById(messageId);
        if (likeMessageBoard == null){
            throw new BusinessException("消息不存在");
        }
        Member member = userMapper.selectFamilyByMemberId(likeMessageBoard.getPublisherId());
        Family family = userMapper.selectFamilyById(likeMessageBoard.getFamilyId());
        MessageBoardVO applyMessageVO = new MessageBoardVO();
        applyMessageVO.setMessageId(likeMessageBoard.getMessageId());
        applyMessageVO.setContent(likeMessageBoard.getContent());
        applyMessageVO.setPublishTime(likeMessageBoard.getPublishTime());
        applyMessageVO.setLikeCount(likeMessageBoard.getLikeCount());
        applyMessageVO.setCommentCount(likeMessageBoard.getCommentCount() + 1);
        messageMapper.updateMessage(applyMessageVO);
        MessageComment messageComment = new MessageComment();
        messageComment.setCommentId(StringTools.getRandomNumber(Constant.LENGTH_12));
        messageComment.setMessageId(likeMessageBoard.getMessageId());
        messageComment.setContent(content);
        messageComment.setCommentTime(new Date());
        messageComment.setUserId(likeMessageBoard.getPublisherId());
        messageMapper.insertMessageComment(messageComment);
        return applyMessageVO;
    }

    @Override
    public List<MessageComment> searchComment(String messageId) {

        List<MessageComment> messageCommentList = messageMapper.selectCommentByMessageId(messageId);
        for (MessageComment messageComment : messageCommentList) {
            Member member = userMapper.selectFamilyByMemberId(messageComment.getUserId());
            messageComment.setContent(member.getMemberName() + ":" + messageComment.getContent());
        }
        return messageCommentList;
    }

    @Override
    public String applyToComment(String commentId, String content) {
        MessageComment messageComment = messageMapper.selectMessageCommentById(commentId);
        if (messageComment == null){
            throw new BusinessException("评论不存在");
        }
        MessageComment applyComment = new MessageComment();
        applyComment.setUserId(messageComment.getUserId());
        applyComment.setCommentId(StringTools.getRandomNumber(Constant.LENGTH_12));
        applyComment.setMessageId(messageComment.getMessageId());
        applyComment.setContent(content);
        applyComment.setCommentTime(new Date());
        applyComment.setReplyToId(messageComment.getCommentId());
        messageMapper.insertMessageComment(applyComment);
        messageMapper.updateCommentCount(messageComment.getMessageId());
        return applyComment.getReplyToId();
    }

    @Override
    public List<TaskInfo> searchMyTask(String userId) {
        List<TaskInfo> taskInfoList = taskMapper.selectByReceiverId(userId);
        if (taskInfoList == null){
            return new ArrayList<>();
        }
        return taskInfoList;
    }

    @Override
    public void changeRole(String phoneNumber, Integer role) {
        Member member = userMapper.selectMemberByPhone(phoneNumber);
        member.setRole(role);
        userMapper.updateRole(member);
    }



    @Scheduled(cron = "0 0 0 * * ?")
    public void autoFinishExpiredTask(){
        List<TaskInfo> tasks = taskMapper.selectUnfinishedExpiredTasks();
        if (tasks.isEmpty()){
            return;
        }
        List<String> taskIds = tasks.stream()
                .map(TaskInfo::getTaskId)
                .collect(Collectors.toList());
        taskMapper.batchUpdateToFinished(taskIds);
    }
}
