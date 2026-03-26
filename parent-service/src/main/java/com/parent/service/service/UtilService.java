package com.parent.service.service;

import com.child.common.entity.po.ExpertInfo;
import com.child.common.entity.po.UtilInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface UtilService {
    List<String> getUtilList(Integer type);

    UtilInfo getUtilInfoByName(String utilName);

    PageInfo<ExpertInfo> getExpertByType(Integer type, Integer pageNum);

    void consultToExpert(String userId, String expertId, String message,String boardId);
}
