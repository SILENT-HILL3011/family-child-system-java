package com.parent.service.service.impl;

import com.child.common.constants.Constant;
import com.child.common.entity.po.MainBox;
import com.child.common.entity.vo.MailBoxVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.parent.service.mapper.MailBoxMapper;
import com.parent.service.service.MailBoxService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailBoxServiceImpl implements MailBoxService {

    @Resource
    private MailBoxMapper mailBoxMapper;

    @Override
    public PageInfo<MailBoxVO> searchMailList(String userId,Integer pageNum) {
        if (pageNum == null){
            pageNum = Constant.NUM_ONE;
        }
        List<MailBoxVO> list = mailBoxMapper.searchMailList(userId,pageNum);
        PageHelper.startPage(pageNum, 10);
        return new PageInfo<>(list);
    }

    @Override
    public String readMail(String mailId) {
        MainBox mainBox = mailBoxMapper.readMail(mailId);
        mainBox.setIsRead(Constant.IS);
        mailBoxMapper.update(mainBox);
        return mainBox.getContent();
    }

    @Override
    public void readAll(String userId) {
        mailBoxMapper.readAll(userId);
    }

    @Override
    public Boolean checkUnReadMails(String userId) {
        List<MailBoxVO> unreadList  = mailBoxMapper.findUnreadMails(userId);
        return unreadList != null && !unreadList.isEmpty();
    }
}
