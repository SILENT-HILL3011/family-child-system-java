package com.parent.service.service;

import com.child.common.entity.po.*;
import com.child.common.entity.vo.AvailableTimeVO;
import com.child.common.entity.vo.ChildInfoVO;
import com.child.common.entity.vo.ExaminationVO;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface ChildService {
    void addChild(String familyId, String childName, Integer sex,String idNumber,String birthDate);

    void updateChildInfo(Child child);


    VaccineRecord searchVaccine(String childId);

    void updateVaccine(String childId, String vaccine);

    List<String> searchVaccineThisYear(String childId);

    Examination appointExamination(String childId, String examinationId, String startTime);

    void recordFood(String childId, Integer time, String food);

    void recordSleep(String childId, Integer time, Integer sleepTime);

    PageInfo<DailyTime> searchLive(String childId,Integer pageNum);

    List<ChildInfoVO> searchChildInfo(String familyId);

    Child searchChildById(String childId);

    void exportLive(String childId, HttpServletResponse response)throws Exception;

    void updateGrowthRecord(String childId,Integer height,Integer weight,Integer headCirc);


    void recordGrowth(GrowthTrend growthTrend);

    List<GrowthTrend> searchGrowth(String childId, Integer days);

    List<Examination> loadExamination();

    void deleteChild(String childId);

    void deleteGrowthRecord(String id);

    void cancelExamination(String appointId);

    void deleteLiveRecord(String dailyId);

    void updateFood(String childId, String recordTime, Integer time, String food);

    void updateSleep(String childId, String recordTime, Integer time, Integer sleepTime);

    void updateGrowthTrend(GrowthTrend growthTrend);

    List<ExaminationVO> findMyExamination(String familyId);

    List<AvailableTimeVO> loadFreeTime(String examinationId);
}
