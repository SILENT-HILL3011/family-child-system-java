package com.parent.service.controller;

import com.alibaba.fastjson2.JSON;
import com.child.common.annotation.GlobalInterceptor;
import com.child.common.constants.Constant;
import com.child.common.entity.po.ChatMessage;
import com.child.common.entity.po.ExpertInfo;
import com.child.common.entity.po.MessageBoardExpert;
import com.child.common.entity.po.UtilInfo;
import com.child.common.entity.vo.MessageInfoVO;
import com.child.common.entity.vo.ToolVO;
import com.child.common.redis.RedisComponent;
import com.child.common.result.R;
import com.github.pagehelper.PageInfo;

import com.parent.service.netty.NettyServerHandler;
import com.parent.service.service.UtilService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/child/util")
@Validated
public class UtilController {

    @Resource
    private UtilService utilService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private RedisComponent redisComponent;



    @RequestMapping("/getList")
    public R<List<String>> getUtilList(@NotNull Integer type){
        return R.success(utilService.getUtilList(type));
    }

    @RequestMapping("/getUtilInfoByName")
    public R<UtilInfo> getUtilInfoByName(@NotEmpty String utilName){
        UtilInfo utilInfo = utilService.getUtilInfoByName(utilName);
        return R.success(utilInfo);
    }

    @RequestMapping("/getExpertByType")
    @GlobalInterceptor(checkLogin = true)
    public R<PageInfo<ExpertInfo>> getExpertByType(@NotNull Integer type, Integer pageNum){
        return R.success(utilService.getExpertByType(type, pageNum));
    }



    @RequestMapping("/consultToExpert")
    public R<String> consultToExpert(@NotEmpty String expertId,@NotEmpty String message,String boardId){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String userId = redisComponent.getUserIdByToken(token);
        String id =  utilService.consultToExpert(userId,expertId,message,boardId);
        ChatMessage msg = new ChatMessage();
        msg.setBoardId(id);
        msg.setFormId(userId);
        msg.setToId(expertId);
        msg.setContent(message);
        msg.setPublishTime(System.currentTimeMillis());
        String json = JSON.toJSONString(msg);
        NettyServerHandler.sendToUser(expertId,json);
        return R.success(id);
    }

    @RequestMapping("/searchMyMessage")
    public R<PageInfo<MessageBoardExpert>> searchMyMessage(Integer pageNum){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String userId = redisComponent.getUserIdByToken(token);
        return R.success(utilService.searchMyMessage(userId,pageNum));
    }

    @RequestMapping("/getMessageInfoByBoardId")
    public R getMessageInfoByBoardId(@NotEmpty String boardId) {
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String userId = redisComponent.getUserIdByToken(token);
        List<MessageInfoVO> list = utilService.selectMessageInfoByBoardId(boardId, userId);
        return R.success(list);
    }

    @RequestMapping("/finishMessage")
    public R finishMessage(@NotEmpty String boardId){
        utilService.finishMessage(boardId);
        return R.success("结束咨询成功");
    }

    @RequestMapping("/recommend")
    @GlobalInterceptor(checkLogin = true)
    public R<List<ToolVO>> recommendTools(){
        List<ToolVO> list = Arrays.asList(

                new ToolVO(
                        "儿童健康问诊",
                        "全平台",
                        "症状自查、疾病判断、药品推荐",
                        "内置知识图谱，智能匹配",
                        "免费"
                ),
                new ToolVO(
                        "过敏原查询",
                        "全平台",
                        "食物过敏风险快速查询",
                        "Neo4j知识图谱，实时关联",
                        "免费"
                ),
                new ToolVO(
                        "营养素查询",
                        "全平台",
                        "食物营养成分展示",
                        "搭配饮食推荐使用",
                        "免费"
                ),
                new ToolVO(
                        "育儿知识库",
                        "全平台",
                        "按年龄、学科获取育儿知识",
                        "内容权威，适合0-6岁儿童",
                        "免费"
                )
        );
        return R.success(list);
    }

}
