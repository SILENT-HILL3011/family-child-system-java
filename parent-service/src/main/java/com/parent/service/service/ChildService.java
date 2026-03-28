package com.parent.service.service;

import com.child.common.entity.po.Child;
import com.child.common.entity.po.DailyTime;
import com.child.common.entity.po.Examination;
import com.child.common.entity.po.VaccineRecord;
import com.child.common.entity.vo.ChildInfoVO;
import com.child.common.entity.vo.GrowthConditionVO;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ChildService {
    void addChild(String familyId, String childName, Integer sex,String idNumber);

    void updateChildInfo(Child child);

    GrowthConditionVO getGrowthInfo(String childId);

    VaccineRecord searchVaccine(String childId);

    void updateVaccine(String childId, String vaccine);

    List<String> searchVaccineThisYear(String childId);

    Examination appointExamination(String childId, String doctorId);

    void recordFood(String childId, Integer time, String food);

    void recordSleep(String childId, Integer time, Integer sleepTime);

    PageInfo<DailyTime> searchLive(String childId,Integer pageNum);

    PageInfo<ChildInfoVO> searchChildInfo(String familyId, Integer pageNum);

    Child searchChildById(String childId);

    void exportLive(String childId, HttpServletResponse response)throws Exception;
}
