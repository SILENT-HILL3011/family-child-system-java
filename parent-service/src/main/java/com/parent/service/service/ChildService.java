package com.parent.service.service;

import com.child.common.entity.po.*;
import com.child.common.entity.vo.ChildInfoVO;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface ChildService {
    void addChild(String familyId, String childName, Integer sex,String idNumber,String birthDate);

    void updateChildInfo(Child child);


    VaccineRecord searchVaccine(String childId);

    void updateVaccine(String childId, String vaccine);

    List<String> searchVaccineThisYear(String childId);

    Examination appointExamination(String childId, String doctorId,String startTime);

    void recordFood(String childId, Integer time, String food);

    void recordSleep(String childId, Integer time, Integer sleepTime);

    PageInfo<DailyTime> searchLive(String childId,Integer pageNum);

    PageInfo<ChildInfoVO> searchChildInfo(String familyId, Integer pageNum);

    Child searchChildById(String childId);

    void exportLive(String childId, HttpServletResponse response)throws Exception;

    void updateGrowthRecord(String childId,Integer height,Integer weight,Integer headCirc);


    void recordGrowth(GrowthTrend growthTrend);

    List<GrowthTrend> searchGrowth(String childId, Integer days);

    List<Examination> loadExamination();
}
