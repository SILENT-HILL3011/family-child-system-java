package com.parent.service.service;

import com.child.common.entity.vo.MailBoxVO;
import com.github.pagehelper.PageInfo;

public interface MailBoxService {
    PageInfo<MailBoxVO> searchMailList(String userId, Integer pageNum);

    String readMail(String mailId);

    void readAll(String userId);

    Boolean checkUnReadMails(String userId);
}
