package com.expert.service.service.impl;

import com.child.common.constants.Constant;
import com.child.common.entity.po.Examination;
import com.child.common.entity.vo.ResponseCodeEnum;
import com.child.common.exception.BusinessException;
import com.child.common.utils.DateUtils;
import com.child.common.utils.StringTools;
import com.expert.service.entity.po.doctor.DoctorInfo;
import com.expert.service.mapper.DoctorMapper;
import com.expert.service.service.DoctorService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Resource
    private DoctorMapper doctorMapper;

    @Override
    public void updateDoctorInfo(String doctorId, String doctorName,String doctorPhone, String doctorEmail, String hospitalLocation) {
        DoctorInfo doctorInfo = new DoctorInfo();
        doctorInfo.setDoctorId(doctorId);
        doctorInfo.setDoctorName(doctorName);
        doctorInfo.setDoctorEmail(doctorEmail);
        doctorInfo.setHospitalLocation(hospitalLocation);
        doctorInfo.setDoctorPhone(doctorPhone);
        doctorMapper.updateDoctorInfo(doctorInfo);
    }

    @Override
    public void createPersonalExamination(String doctorId, String examinationTime) {
        DoctorInfo doctorInfo = doctorMapper.selectDoctorInfoById(doctorId);
        if (doctorInfo == null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (!DateUtils.isValidDate(examinationTime)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        Examination examination = new Examination();
        examination.setExaminationId(StringTools.getRandomNumber(Constant.LENGTH_12));
        examination.setDoctorId(doctorId);
        examination.setExaminationTime(DateUtils.ChangeStr2Date(examinationTime));
        doctorMapper.insertExamination(examination);
    }
}
