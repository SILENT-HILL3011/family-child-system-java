package com.parent.service.controller;

import com.child.common.annotation.GlobalInterceptor;
import com.child.common.entity.po.TaskInfo;
import com.child.common.entity.vo.MessageBoardVO;
import com.child.common.result.R;
import com.github.pagehelper.PageInfo;
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
    @GlobalInterceptor(checkLogin = true)
    public R publishTask(@NotEmpty String publisherId, @NotEmpty String taskName, @NotEmpty String publishDate){
        familyService.publishTask(publisherId,taskName,publishDate);
        return R.success();
    }

    @RequestMapping("/acceptTask")
    @GlobalInterceptor(checkLogin = true)
    public R<TaskInfo> acceptTask(@NotEmpty String publisherId, @NotEmpty String receiverId,@NotEmpty String taskName){
        TaskInfo taskInfo = familyService.acceptTask(publisherId,receiverId,taskName);
        return R.success(taskInfo);
    }

    @RequestMapping("/searchTask")
    @GlobalInterceptor(checkLogin = true)
    public R<List<TaskInfo>> searchTask(@NotEmpty String publisherId){
        List<TaskInfo> taskInfos = familyService.searchTask(publisherId);
        return R.success(taskInfos);
    }

    @RequestMapping("/finishTask")
    @GlobalInterceptor(checkLogin = true)
    public R<TaskInfo> finishTask(@NotEmpty String receiverId,@NotEmpty String taskName){
        TaskInfo taskInfo = familyService.finishTask(receiverId,taskName);
        return R.success(taskInfo);
    }

    @RequestMapping("/publishMessage")
    @GlobalInterceptor(checkLogin = true)
    public R publishMessage(@NotEmpty String publisherId,String content,String imageUrl){
        familyService.publishMessage(publisherId,content,imageUrl);
        return R.success();
    }

    @RequestMapping("/searchMessage")
    @GlobalInterceptor(checkLogin = true)
    public R<PageInfo<MessageBoardVO>> searchMessageByPage(@NotEmpty String familyId, @NotEmpty String publisherId,
                                                     Integer timePeriod,Integer pageNum){
        PageInfo<MessageBoardVO> messageBoardVOList = familyService.searchMessageByPage(familyId,publisherId,timePeriod,pageNum);
        return R.success(messageBoardVOList);
    }

    @RequestMapping("/likeMessage")
    @GlobalInterceptor(checkLogin = true)
    public R<MessageBoardVO> likeMessage(@NotEmpty String messageId){
        MessageBoardVO messageBoardVO = familyService.likeMessage(messageId);
        return R.success(messageBoardVO);
    }

    @RequestMapping("/applyToMessage")
    @GlobalInterceptor(checkLogin = true)
    public R<MessageBoardVO> applyToMessage(@NotEmpty String messageId,@NotEmpty String content){
        MessageBoardVO messageBoardVO = familyService.applyToMessage(messageId,content);
        return R.success(messageBoardVO);
    }

    @RequestMapping("/searchComment")
    @GlobalInterceptor(checkLogin = true)
    public R<List<String>> searchComment(@NotEmpty String messageId){
        List<String> comments = familyService.searchComment(messageId);
        return R.success(comments);
    }

    @RequestMapping("/applyToComment")
    @GlobalInterceptor(checkLogin = true)
    public R applyToComment(@NotEmpty String commentId,@NotEmpty String content){
        familyService.applyToComment(commentId,content);
        return R.success();
    }
}
