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
        taskInfo.setPublishDate(DateUtils.ChangeStr2Date(publishDate));
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
    public List<TaskInfo> searchTask(@NotEmpty String publisherId) {
        List<TaskInfo> taskInfoList = taskMapper.selectByPublisherId(publisherId);
        if (taskInfoList == null){
            return null;
        }
        return taskInfoList.stream()
                .filter(taskInfo -> taskInfo.getIsFinished().equals(Constant.NO))
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
        messageMapper.insertMessage(messageBoard);
    }

    @Override
    public List<MessageBoardVO> searchMessage(String familyId, String publisherId, Integer timePeriod) {
        if (timePeriod == null){
            timePeriod = Constant.NUM_ONE;
        }
        if (timePeriod != 1 && timePeriod != 7 && timePeriod != 30) {
            throw new BusinessException("时间段错误");
        }
        Member member = userMapper.selectFamilyByMemberId(publisherId);
        Family family = userMapper.selectFamilyById(familyId);
        List<MessageBoard> messageBoards = messageMapper.selectMessage(familyId, publisherId, timePeriod);
        List<MessageBoardVO> messageBoardVOList = new ArrayList<>();
        for (MessageBoard messageBoard : messageBoards) {
            MessageBoardVO messageBoardVO = new MessageBoardVO();
            messageBoardVO.setMessageId(messageBoard.getMessageId());
            messageBoardVO.setFamilyName(family.getFamilyName());
            messageBoardVO.setPublisherName(member.getMemberName());
            messageBoardVO.setContent(messageBoard.getContent());
            messageBoardVO.setImageUrl(messageBoard.getImageUrl());
            messageBoardVO.setPublishTime(messageBoard.getPublishTime());
            messageBoardVO.setLikeCount(messageBoard.getLikeCount());
            messageBoardVO.setCommentCount(messageBoard.getCommentCount());
            messageBoardVOList.add(messageBoardVO);
        }
        return messageBoardVOList;
    }

    @Override
    public MessageBoardVO likeMessage(String familyId, String publisherId, Integer timePeriod) {
        List<MessageBoardVO> messageBoardVOList = searchMessage(familyId, publisherId, timePeriod);
        MessageBoardVO likeMessageVO = messageBoardVOList.get(0);
        likeMessageVO.setLikeCount(likeMessageVO.getLikeCount() + 1);
        messageMapper.updateMessage(likeMessageVO);
        MessageLike messageLike = new MessageLike();
        messageLike.setLikeId(StringTools.getRandomNumber(Constant.LENGTH_12));
        messageLike.setMessageId(likeMessageVO.getMessageId());
        messageLike.setUserId(publisherId);
        messageLike.setLikeTime(new Date());
        messageMapper.insertMessageLike(messageLike);
        return likeMessageVO;

        // TODO 重复点赞是取消点赞
    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void autoFinishExpiredTask(){
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
