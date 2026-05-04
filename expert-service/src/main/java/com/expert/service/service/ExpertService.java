package com.expert.service.service;


import com.child.common.entity.po.ExpertInfo;
import com.child.common.entity.po.PhysicalExam;
import com.child.common.entity.vo.ExaminationVO;

import java.util.List;

public interface ExpertService {

    void register(String expertPhone, String expertPassword);

    String login(String expertPhone, String expertPassword);

    void updateExpertInfo(ExpertInfo expertInfo);

    void createPersonalExamination(String expertId, String startTime,String endTime);

    ExpertInfo searchExpertInfo(String expertId);

    List<ExaminationVO> getMyExamination(String expertId);

    void putExamResult(String expertId, PhysicalExam physicalExam);
}
