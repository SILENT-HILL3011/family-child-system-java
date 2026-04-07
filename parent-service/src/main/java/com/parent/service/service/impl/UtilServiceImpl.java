package com.parent.service.service.impl;

import com.child.common.constants.Constant;
import com.child.common.entity.enums.ExpertTypeEnum;
import com.child.common.entity.po.*;
import com.child.common.entity.vo.MessageInfoVO;
import com.child.common.utils.StringTools;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.parent.service.mapper.MessageBoardExpertMapper;
import com.parent.service.mapper.UtilMapper;
import com.parent.service.service.UtilService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class UtilServiceImpl implements UtilService {

    @Resource
    private UtilMapper utilMapper;
    @Resource
    private MessageBoardExpertMapper messageBoardExpertMapper;

    @Override
    public List<String> getUtilList(Integer type) {
        List<String> utilNameList = utilMapper.getUtilByType(type);
        if (utilNameList == null) {
            return null;
        }
        return utilNameList;
    }

    @Override
    public UtilInfo getUtilInfoByName(String utilName) {
        UtilInfo utilInfo = utilMapper.getUtilInfoByName(utilName);
        if (utilInfo != null) {
            return utilInfo;
        }
        return null;
    }

    @Override
    public PageInfo<ExpertInfo> getExpertByType(Integer type, Integer pageNum) {
        List<ExpertInfo> expertInfoList = utilMapper.getExpertByType(ExpertTypeEnum.getDescByCode(type));
        if (expertInfoList == null) {
            return null;
        }
        if (pageNum == null){
            pageNum = Constant.NUM_ONE;
        }
        PageHelper.startPage(pageNum, 10);
        return PageInfo.of(expertInfoList);
    }


    @Override
    public String consultToExpert(String userId, String expertId, String message, String boardId) {
        MessageBoardExpert messageBoardExpert = null;
        if (boardId != null && !boardId.isEmpty()) {
            messageBoardExpert = utilMapper.selectBoardExpertByBoardId(boardId);
        }
        if (messageBoardExpert == null) {
            messageBoardExpert = new MessageBoardExpert();
            messageBoardExpert.setBoardId(StringTools.getRandomNumber(Constant.LENGTH_12));
            messageBoardExpert.setExpertId(expertId);
            messageBoardExpert.setUserId(userId);
            messageBoardExpert.setIsFinished(Constant.NO);
            messageBoardExpert.setMessageCount(0);
            messageBoardExpertMapper.insertMessageBoardExpert(messageBoardExpert);
        }
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setMessageId(StringTools.getRandomNumber(Constant.LENGTH_12));
        messageInfo.setBoardId(messageBoardExpert.getBoardId());
        messageInfo.setText(message);
        messageInfo.setPublisherId(userId);
        messageInfo.setPublishDate(new Date());
        utilMapper.insertMessageInfo(messageInfo);
        messageBoardExpert.setMessageCount(messageBoardExpert.getMessageCount() + 1);
        messageBoardExpertMapper.updateMessageBoardExpert(messageBoardExpert);
        return messageBoardExpert.getBoardId();
    }

    @Override
    public PageInfo<MessageBoardExpert> searchMyMessage(String userId,Integer pageNum) {
        if (pageNum == null){
            pageNum = Constant.NUM_ONE;
        }
        List<MessageBoardExpert> messageBoardList = messageBoardExpertMapper.searchMyMessage(userId);
        PageHelper.startPage(pageNum, 10);
        return PageInfo.of(messageBoardList);
    }

    @Override
    public void finishMessage(String boardId) {
        MessageBoardExpert messageBoardExpert = utilMapper.selectBoardExpertByBoardId(boardId);
        messageBoardExpert.setIsFinished(Constant.IS);
        messageBoardExpertMapper.finish(messageBoardExpert);
    }

    @Override
    public List<MessageInfoVO> selectMessageInfoByBoardId(String boardId,String userId) {
        List<MessageInfoVO> messageInfoVOList = utilMapper.selectMessageInfoByBoardId(boardId);
        if (messageInfoVOList == null) {
            return null;
        }
        for (MessageInfoVO vo : messageInfoVOList){
            vo.setSelf(vo.getPublisherId().equals(userId));
        }
        messageInfoVOList.sort(Comparator.comparing(MessageInfoVO::getPublishDate));
        return messageInfoVOList;
    }
}
