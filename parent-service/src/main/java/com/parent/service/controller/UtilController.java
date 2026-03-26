package com.parent.service.controller;

import com.child.common.constants.Constant;
import com.child.common.entity.po.ExpertInfo;
import com.child.common.entity.po.MessageBoard;
import com.child.common.entity.po.MessageBoardExpert;
import com.child.common.entity.po.UtilInfo;
import com.child.common.result.R;
import com.github.pagehelper.PageInfo;
import com.parent.service.service.UtilService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/child/util")
@Validated
public class UtilController {

    @Resource
    private UtilService utilService;
    @Resource
    private HttpServletRequest request;

    @RequestMapping("/getList")
    public R<List<String>> getUtilList(@NotNull Integer type){
        return R.success(utilService.getUtilList(type));
    }

    @RequestMapping("/getUtilInfoByName")
    public R<UtilInfo> getUtilInfoByName(@NotEmpty String utilName){
        UtilInfo utilInfo = utilService.getUtilInfoByName(utilName);
        return R.success(utilInfo);
    }

    // TODO 实现六个工具类逻辑

    // TODO 育儿知识

    @RequestMapping("/getExpertByType")
    public R<PageInfo<ExpertInfo>> getExpertByType(@NotNull Integer type, Integer pageNum){
        return R.success(utilService.getExpertByType(type, pageNum));
    }



    @RequestMapping("/consultToExpert")
    public R<String> consultToExpert(@NotEmpty String expertId,@NotEmpty String message,String boardId){
//        String userId = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String userId = "911168719308";
        utilService.consultToExpert(userId,expertId,message,boardId);
        return R.success("咨询成功");
    }

    @RequestMapping("/searchMyMessage")
    public R<PageInfo<MessageBoardExpert>> searchMyMessage(@NotEmpty String userId, Integer pageNum){
        return R.success(utilService.searchMyMessage(userId,pageNum));
    }

    @RequestMapping("/finishMessage")
    public R finishMessage(@NotEmpty String boardId){
        utilService.finishMessage(boardId);
        return R.success("结束咨询成功");
    }

}
