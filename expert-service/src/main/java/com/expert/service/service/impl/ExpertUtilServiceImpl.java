package com.expert.service.service.impl;

import com.child.common.constants.Constant;
import com.child.common.entity.po.MessageBoardExpert;
import com.child.common.entity.po.MessageInfo;
import com.child.common.entity.vo.MessageInfoVO;
import com.child.common.utils.StringTools;
import com.expert.service.mapper.MessageBoardExpertMapper;
import com.expert.service.service.ExpertUtilService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExpertUtilServiceImpl implements ExpertUtilService {

    @Resource
    private MessageBoardExpertMapper messageBoardExpertMapper;

    @Override
    public PageInfo<MessageBoardExpert> searchMessage(String expertId, Integer pageNum) {
        List<MessageBoardExpert> list = messageBoardExpertMapper.searchMyMessage(expertId);
        if (pageNum == null){
            pageNum = Constant.NUM_ONE;
        }
        PageHelper.startPage(pageNum, 10);
        return new PageInfo<>(list);
    }

    @Override
    public List<MessageInfoVO> history(String boardId, String expertId) {
        List<MessageInfoVO> list = messageBoardExpertMapper.history(boardId);
        if (list == null){
            return new ArrayList<>();
        }
        for (MessageInfoVO msg : list){
            Boolean isSelf = false;
            if (msg.getPublisherId() != null && expertId != null) {
                isSelf = msg.getPublisherId().equals(expertId);
            }
            msg.setSelf(isSelf);
        }
        return list;
    }

    @Override
    public void apply(String content, String boardId, String expertId) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setMessageId(StringTools.getRandomNumber(Constant.LENGTH_12));
        messageInfo.setBoardId(boardId);
        messageInfo.setText(content);
        messageInfo.setPublisherId(expertId);
        messageInfo.setPublishDate(new Date());
        messageBoardExpertMapper.apply(messageInfo);
    }
}
