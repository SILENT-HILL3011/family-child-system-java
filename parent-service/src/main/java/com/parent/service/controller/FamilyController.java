package com.parent.service.controller;

import com.child.common.annotation.GlobalInterceptor;
import com.child.common.annotation.RequirePrimaryCaregiver;
import com.child.common.constants.Constant;
import com.child.common.entity.po.MessageComment;
import com.child.common.entity.po.TaskInfo;
import com.child.common.entity.vo.MessageBoardVO;
import com.child.common.redis.RedisComponent;
import com.child.common.result.R;
import com.github.pagehelper.PageInfo;
import com.parent.service.service.FamilyService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/publishTask")
    @GlobalInterceptor(checkLogin = true)
    @RequirePrimaryCaregiver
    public R publishTask(@NotEmpty String taskName, @NotEmpty String publishDate){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String publisherId = redisComponent.getUserIdByToken(token);
        familyService.publishTask(publisherId,taskName,publishDate);
        return R.success();
    }

    @RequestMapping("/acceptTask")
    @GlobalInterceptor(checkLogin = true)
    public R<TaskInfo> acceptTask(@NotEmpty String publisherId, @NotEmpty String taskName){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String receiverId = redisComponent.getUserIdByToken(token);
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
    public R<TaskInfo> finishTask(@NotEmpty String taskName){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String receiverId = redisComponent.getUserIdByToken(token);
        TaskInfo taskInfo = familyService.finishTask(receiverId,taskName);
        return R.success(taskInfo);
    }

    @RequestMapping("/searchMyTask")
    @GlobalInterceptor(checkLogin = true)
    public R<List<TaskInfo>> searchMyTask(){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String userId = redisComponent.getUserIdByToken(token);
        List<TaskInfo> taskInfos = familyService.searchMyTask(userId);
        return R.success(taskInfos);
    }


    @RequestMapping("/publishMessage")
    @GlobalInterceptor(checkLogin = true)
    public R publishMessage(String content,String imageUrl){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String publisherId = redisComponent.getUserIdByToken(token);
        familyService.publishMessage(publisherId,content,imageUrl);
        return R.success();
    }

    @RequestMapping("/searchMessage")
    @GlobalInterceptor(checkLogin = true)
    public R<List<MessageBoardVO>> searchMessage(@NotEmpty String familyId
                                                     ,Integer timePeriod){
        List<MessageBoardVO> messageBoardVOList = familyService.searchMessageByPage(familyId,timePeriod);
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
    public R<List<MessageComment>> searchComment(@NotEmpty String messageId){
        List<MessageComment> comments = familyService.searchComment(messageId);
        return R.success(comments);
    }

    @RequestMapping("/applyToComment")
    @GlobalInterceptor(checkLogin = true)
    public R<String> applyToComment(@NotEmpty String commentId,@NotEmpty String content){
        String id = familyService.applyToComment(commentId,content);
        return R.success(id);
    }

    @RequestMapping("/changeRole")
    @GlobalInterceptor(checkLogin = true)
    @RequirePrimaryCaregiver
    public R changeRole(@NotEmpty String phoneNumber,@NotNull Integer role){
        familyService.changeRole(phoneNumber,role);
        return R.success();
    }
}
