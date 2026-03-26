package com.expert.service.service;


import com.child.common.entity.po.ExpertInfo;

public interface ExpertService {

    void register(String expertPhone, String expertPassword);

    void login(String expertPhone, String expertPassword);

    void updateExpertInfo(ExpertInfo expertInfo);
}
