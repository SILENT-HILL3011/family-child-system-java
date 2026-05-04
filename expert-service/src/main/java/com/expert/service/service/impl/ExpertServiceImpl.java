package com.expert.service.service.impl;

import com.child.common.constants.Constant;
import com.child.common.entity.po.*;
import com.child.common.entity.vo.ExaminationVO;
import com.child.common.entity.vo.ResponseCodeEnum;
import com.child.common.exception.BusinessException;
import com.child.common.redis.RedisComponent;
import com.child.common.utils.DateUtils;
import com.child.common.utils.StringTools;
import com.expert.service.mapper.ExamMapper;
import com.expert.service.mapper.ExpertInfoMapper;
import com.expert.service.service.ExpertService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExpertServiceImpl implements ExpertService {


    @Resource
    private ExpertInfoMapper expertInfoMapper;
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private HttpServletRequest request;
    @Resource
    private ExamMapper examMapper;

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
    public void createPersonalExamination(String expertId, String startTime, String endTime) {
        ExpertInfo expertInfo = expertInfoMapper.selectById(expertId);
        if (expertInfo == null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (!DateUtils.isValidDate(startTime)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (!DateUtils.isValidDate(endTime)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        Examination examination = new Examination();
        examination.setExaminationId(StringTools.getRandomNumber(Constant.LENGTH_12));
        examination.setDoctorId(expertId);
        examination.setStartTime(DateUtils.ChangeStr2DateTime(startTime));
        examination.setEndTime(DateUtils.ChangeStr2DateTime(endTime));
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

    @Override
    public List<ExaminationVO> getMyExamination(String expertId) {
        return examMapper.selectMyExamination(expertId);
    }

    @Override
    public void putExamResult(String expertId, PhysicalExam physicalExam) {
        AppointExamination appointExamination = examMapper.selectAppointExaminationById(physicalExam.getAppointId());
        if (appointExamination == null){
            throw new BusinessException("预约记录不存在");
        }
        PhysicalExam oldReport = examMapper.selectExamReportByReportId(physicalExam.getReportId());
        if (oldReport != null) {
            throw new BusinessException("该预约已提交体检报告，请勿重复提交");
        }
        physicalExam.setChildId(appointExamination.getChildId());
        physicalExam.setDoctor(expertInfoMapper.getExpertNameById(expertId));
        physicalExam.setReportId(StringTools.getRandomNumber(Constant.LENGTH_12));
        physicalExam.setExamDate(appointExamination.getAppointTime());
        examMapper.insertExamReport(physicalExam);

    }
}
