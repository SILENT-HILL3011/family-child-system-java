package com.expert.service.controller;

import com.alibaba.fastjson2.JSON;
import com.child.common.annotation.GlobalInterceptor;
import com.child.common.constants.Constant;
import com.child.common.entity.po.ChatMessage;
import com.child.common.entity.po.MessageBoardExpert;
import com.child.common.entity.po.MessageInfo;
import com.child.common.entity.vo.MessageInfoVO;
import com.child.common.redis.RedisComponent;
import com.child.common.result.R;
import com.expert.service.mapper.MessageBoardExpertMapper;
import com.expert.service.service.ExpertUtilService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/expert/util")
@Validated
public class ExpertUtilController {


    @Resource
    private RedisComponent redisComponent;
    @Resource
    private HttpServletRequest request;
    @Resource
    private ExpertUtilService expertUtilService;
    @Resource
    private MessageBoardExpertMapper messageBoardExpertMapper;
    @Resource
    private RestTemplate restTemplate;

    @RequestMapping("/searchMessage")
    @GlobalInterceptor(checkLogin = true)
    public R<PageInfo<MessageBoardExpert>> searchMessage(Integer pageNum){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String expertId = redisComponent.getExpertIdByToken(token);
        return R.success(expertUtilService.searchMessage(expertId,pageNum));
    }

    @RequestMapping("/history")
    @GlobalInterceptor(checkLogin = true)
    public R<List<MessageInfoVO>> history(@NotEmpty String boardId){
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String expertId = redisComponent.getExpertIdByToken(token);
        return R.success(expertUtilService.history(boardId,expertId));
    }

    @RequestMapping("/apply")
    @GlobalInterceptor(checkLogin = true)
    public R apply(@NotEmpty String content,@NotEmpty String boardId) throws Exception{
        String token = request.getHeader(Constant.TOKEN_HEADER_KEY);
        String expertId = redisComponent.getExpertIdByToken(token);
        MessageBoardExpert board = messageBoardExpertMapper.selectBoardByBoardId(boardId);
        String userId = board.getUserId();
        expertUtilService.apply(content,boardId,expertId);
        ChatMessage msg = new ChatMessage();
        msg.setBoardId(boardId);
        msg.setFormId(expertId);
        msg.setToId(userId);
        msg.setContent(content);
        msg.setPublishTime(System.currentTimeMillis());
        String json = JSON.toJSONString(msg);
        restTemplate.postForObject("http://localhost:8000/netty/push?userId=" + userId + "&msg=" + URLEncoder.encode(json, "UTF-8"),
                null, String.class);
        return R.success();
    }
}
