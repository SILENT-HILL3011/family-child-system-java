package com.parent.service.service.impl;

import com.child.common.constants.Constant;
import com.child.common.entity.po.TaskInfo;
import com.child.common.exception.BusinessException;
import com.child.common.utils.DateUtils;
import com.child.common.utils.StringTools;
import com.parent.service.mapper.TaskMapper;
import com.parent.service.service.FamilyService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FamilyServiceImpl implements FamilyService {

    @Resource
    private TaskMapper taskMapper;
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
