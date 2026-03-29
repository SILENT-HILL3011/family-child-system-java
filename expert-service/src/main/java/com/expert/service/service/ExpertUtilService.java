package com.expert.service.service;

import com.child.common.entity.po.MessageBoardExpert;
import com.child.common.entity.po.MessageInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ExpertUtilService {
    PageInfo<MessageBoardExpert> searchMessage(String expertId, Integer pageNum);

    List<MessageInfo> history(String boardId);

    void apply(String content, String boardId, String expertId);
}
