package com.expert.service.mapper;

import com.child.common.entity.po.AppointExamination;
import com.child.common.entity.po.PhysicalExam;
import com.child.common.entity.vo.ExaminationVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ExamMapper {

    List<ExaminationVO> selectMyExamination(String expertId);

    @Select("select * from examination_appoint where appoint_id = #{appointId}")
    AppointExamination selectAppointExaminationById(String appointId);

    @Select("select * from physical_exam where report_id = #{reportId}")
    PhysicalExam selectExamReportByReportId(String reportId);

    void insertExamReport(PhysicalExam physicalExam);
}
