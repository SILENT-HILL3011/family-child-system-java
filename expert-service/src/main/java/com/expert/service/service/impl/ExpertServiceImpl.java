package com.expert.service.service.impl;

import com.child.common.constants.Constant;
import com.child.common.entity.po.Examination;
import com.child.common.entity.po.ExpertInfo;
import com.child.common.entity.po.MainBox;
import com.child.common.entity.vo.ResponseCodeEnum;
import com.child.common.exception.BusinessException;
import com.child.common.redis.RedisComponent;
import com.child.common.utils.DateUtils;
import com.child.common.utils.StringTools;
import com.expert.service.mapper.ExpertInfoMapper;
import com.expert.service.service.ExpertService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ExpertServiceImpl implements ExpertService {


    @Resource
    private ExpertInfoMapper expertInfoMapper;
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private HttpServletRequest request;

    @Override
    public void register(String expertPhone, String expertPassword) {
        ExpertInfo expertInfo = expertInfoMapper.selectByPhoneNumber(expertPhone);
        if (expertInfo != null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        expertInfo = new ExpertInfo();
        expertInfo.setExpertId(StringTools.getRandomNumber(Constant.LENGTH_12));
        expertInfo.setExpertPhone(expertPhone);
        expertInfo.setExpertPassword(StringTools.getMd5(expertPassword));
        expertInfoMapper.register(expertInfo);
    }

    @Override
    public String login(String expertPhone, String expertPassword) {
        ExpertInfo expertInfo = expertInfoMapper.selectByPhoneNumber(expertPhone);
        if (expertInfo == null){
            throw new BusinessException(ResponseCodeEnum.CODE_601);
        }
        if (!expertInfo.getExpertPassword().equals(StringTools.getMd5(expertPassword))) {
            throw new BusinessException(ResponseCodeEnum.CODE_602);
        }
        String token = StringTools.getMd5(expertInfo.getExpertId()+StringTools.getRandomNumber(Constant.LENGTH_20));
        redisComponent.saveUserLoginToken(token,expertInfo.getExpertId());
        return token;
    }

    @Override
    public void updateExpertInfo(ExpertInfo expertInfo) {
        String expertId = redisComponent.getExpertIdByToken(request.getHeader(Constant.TOKEN_HEADER_KEY));
        if (!expertId.equals(expertInfo.getExpertId())){
            throw new BusinessException(ResponseCodeEnum.CODE_603);
        }
        expertInfoMapper.updateExpertInfo(expertInfo);
    }

    @Override
    public void createPersonalExamination(String expertId, String examinationTime) {
        ExpertInfo expertInfo = expertInfoMapper.selectById(expertId);
        if (expertInfo == null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (!DateUtils.isValidDate(examinationTime)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        Examination examination = new Examination();
        examination.setExaminationId(StringTools.getRandomNumber(Constant.LENGTH_12));
        examination.setDoctorId(expertId);
        examination.setExaminationTime(DateUtils.ChangeStr2DateTime(examinationTime));
        expertInfoMapper.insertExamination(examination);
        MainBox mainBox = new MainBox();
        mainBox.setId(StringTools.getRandomNumber(Constant.LENGTH_12));
        mainBox.setSendUserId(expertId);
        mainBox.setTitle("体检通知");
        mainBox.setContent("您有一份体检结果，请及时查看");
        mainBox.setCreateTime(new Date());
        mainBox.setIsRead(Constant.NO);
        expertInfoMapper.insertMailOfExamination(mainBox);
    }

    @Override
    public ExpertInfo searchExpertInfo(String expertId) {
        ExpertInfo expertInfo = expertInfoMapper.selectById(expertId);
        if (expertInfo == null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        return expertInfo;
    }
}
