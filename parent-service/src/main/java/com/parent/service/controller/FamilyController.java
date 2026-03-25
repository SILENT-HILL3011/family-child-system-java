package com.parent.service.controller;

import com.child.common.entity.po.TaskInfo;
import com.child.common.entity.vo.MessageBoardVO;
import com.child.common.result.R;
import com.parent.service.service.FamilyService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/child/family")
@Validated
public class FamilyController {

    @Resource
    private FamilyService familyService;

    @RequestMapping("/publishTask")
    public R publishTask(@NotEmpty String publisherId, @NotEmpty String taskName, @NotEmpty String publishDate){
        familyService.publishTask(publisherId,taskName,publishDate);
        return R.success();
    }

    @RequestMapping("/acceptTask")
    public R<TaskInfo> acceptTask(@NotEmpty String publisherId, @NotEmpty String receiverId,@NotEmpty String taskName){
        TaskInfo taskInfo = familyService.acceptTask(publisherId,receiverId,taskName);
        return R.success(taskInfo);
    }

    @RequestMapping("/searchTask")
    public R<List<TaskInfo>> searchTask(@NotEmpty String publisherId){
        List<TaskInfo> taskInfos = familyService.searchTask(publisherId);
        return R.success(taskInfos);
    }

    @RequestMapping("/finishTask")
    public R<TaskInfo> finishTask(@NotEmpty String receiverId,@NotEmpty String taskName){
        TaskInfo taskInfo = familyService.finishTask(receiverId,taskName);
        return R.success(taskInfo);
    }

    @RequestMapping("/publishMessage")
    public R publishMessage(@NotEmpty String publisherId,String content,String imageUrl){
        familyService.publishMessage(publisherId,content,imageUrl);
        return R.success();
    }

    @RequestMapping("/searchMessage")
    public R<List<MessageBoardVO>> searchMessage(@NotEmpty String familyId,@NotEmpty String publisherId,Integer timePeriod){
        List<MessageBoardVO> messageBoardVOList = familyService.searchMessage(familyId,publisherId,timePeriod);
        return R.success(messageBoardVOList);
    }

    @RequestMapping("/likeMessage")
    public R<MessageBoardVO> likeMessage(@NotEmpty String familyId,@NotEmpty String publisherId,Integer timePeriod){
        MessageBoardVO messageBoardVO = familyService.likeMessage(familyId,publisherId,timePeriod);
        return R.success(messageBoardVO);
    }
}
