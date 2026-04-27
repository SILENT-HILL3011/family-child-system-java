package com.parent.service.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.child.common.constants.Constant;
import com.child.common.entity.enums.ChildVaccineEnum;
import com.child.common.entity.enums.TimePerEnum;
import com.child.common.entity.po.*;
import com.child.common.entity.vo.AvailableTimeVO;
import com.child.common.entity.vo.ChildInfoVO;
import com.child.common.entity.vo.ExaminationVO;
import com.child.common.entity.vo.ResponseCodeEnum;
import com.child.common.exception.BusinessException;
import com.child.common.redis.RedisComponent;
import com.child.common.utils.ChildVaccineUtil;
import com.child.common.utils.DateUtils;
import com.child.common.utils.StringTools;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.parent.service.mapper.*;
import com.parent.service.service.ChildService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChildServiceImpl implements ChildService {

    private static final Logger log = LoggerFactory.getLogger(ChildServiceImpl.class);
    @Resource
    private UserMapper userMapper;
    @Resource
    private ChildMapper childMapper;
    @Resource
    private VaccineRecordMapper vaccineRecordMapper;
    @Resource
    private DailyTimeMapper dailyTimeMapper;
    @Resource
    private GrowthTrendMapper growthTrendMapper;
    @Resource
    private RedisComponent redisComponent;

    @Override
    public void addChild(String familyId, String childName, Integer sex, String idNumber, String birthDate) {
        Family checkIsExist = userMapper.selectFamilyById(familyId);
        if (checkIsExist == null) {
            throw new BusinessException("家庭不存在");
        }
        Child check = childMapper.selectByNameAndFamilyId(childName, familyId);
        if (check != null) {
            throw new BusinessException("儿童不存在");
        }
        Child child = new Child();
        child.setChildName(childName);
        child.setSex(sex);
        child.setChildId(StringTools.getRandomNumber(Constant.LENGTH_12));
        child.setFamilyId(familyId);
        child.setIdNumber(idNumber);
        child.setBirthDate(DateUtils.ChangeStr2Date(birthDate));
        child.setAge(DateUtils.getAgeFromBirthDate(birthDate));
        childMapper.insert(child);
        redisComponent.saveChildInfo(child.getChildId(), JSON.toJSONString(child));
    }

    @Override
    public void updateChildInfo(Child child) {
        Child check = childMapper.selectById(child.getChildId());
        if (check == null) {
            throw new BusinessException("儿童不存在");
        }
        if (child.getChineseWordCount() != null) {
            check.setChineseWordCount(child.getChineseWordCount());
        }
        if (child.getEnglishWordCount() != null) {
            check.setEnglishWordCount(child.getEnglishWordCount());
        }
        if (child.getPoetryCount() != null) {
            check.setPoetryCount(child.getPoetryCount());
        }
        if (child.getHeight() != null) {
            check.setHeight(child.getHeight());
        }
        if (child.getWeight() != null) {
            check.setWeight(child.getWeight());
        }
        if (child.getHeadCirc() != null) {
            check.setHeadCirc(child.getHeadCirc());
        }
        if (child.getAge() != null) {
            check.setAge(child.getAge());
        }
        check.setRecordDate(new Date());
        childMapper.update(check);
        redisComponent.saveChildInfo(child.getChildId(), JSON.toJSONString(check));
        redisComponent.clearChildListCache();
    }

    @Override
    public void deleteChild(String childId) {
        Child child = childMapper.selectById(childId);
        if (child == null) {
            throw new BusinessException("儿童不存在");
        }
        childMapper.deleteById(childId);
        redisComponent.deleteChildInfo(childId);
        redisComponent.clearChildListCache();
    }

    @Override
    public VaccineRecord searchVaccine(String childId) {
        Child child = childMapper.selectById(childId);
        if (child == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_602);
        }
        Child newChild = childMapper.selectChildFromVaccineRecord(childId);
        if (newChild == null) {
            childMapper.insertVaccineRecord(childId);
            return null;
        }
        return childMapper.selectVaccineRecord(childId);
    }

    @Override
    public void updateVaccine(String childId, String vaccine) {
        Optional<ChildVaccineEnum> vaccineEnumOpt = ChildVaccineUtil.getEnumByVaccineDesc(vaccine);
        if (vaccineEnumOpt.isEmpty()) {
            throw new BusinessException("未匹配到对应的疫苗类型：" + vaccine);
        }
        VaccineRecord record = vaccineRecordMapper.selectByChildId(childId);
        if (record == null) {
            record = new VaccineRecord();
            record.setChildId(childId);
        }
        Date now = new Date();
        String vaccineType = ChildVaccineUtil.getVaccineTypeFromDesc(vaccine);
        Integer needleNum = ChildVaccineUtil.getNeedleNumFromDesc(vaccine);
        updateRecordByVaccineType(record, vaccineType, needleNum, now);
        if (record.getChildId() != null && vaccineRecordMapper.selectByChildId(childId) != null) {
            vaccineRecordMapper.updateByChildId(record);
        } else {
            vaccineRecordMapper.insert(record);
        }
    }

    @Override
    public List<String> searchVaccineThisYear(String childId) {
        Child child = childMapper.selectById(childId);
        if (child == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_602);
        }
        Integer ageByMonth = child.getAge();
        VaccineRecord record = vaccineRecordMapper.selectByChildId(childId);
        if (record == null) {
            record = new VaccineRecord(); // 无记录 = 全部未接种
        }
        List<ChildVaccineEnum> allShouldVaccine = Arrays.stream(ChildVaccineEnum.values())
                .filter(v -> v.getMonths() <= ageByMonth)
                .collect(Collectors.toList());
        List<String> result = new ArrayList<>();
        for (ChildVaccineEnum vaccineEnum : allShouldVaccine) {
            boolean isNotDone = !isVaccineDone(record, vaccineEnum);
            if (isNotDone) {
                result.add(vaccineEnum.getVaccine()); // 返回疫苗名称
            }
        }

        return result;
    }


    @Override
    public Examination appointExamination(String childId, String examinationId, String startTime) {
        Examination examination = childMapper.selectExamById(examinationId);
        if (examination == null) {
            throw new BusinessException("体检不存在");
        }
        AppointExamination appointment = childMapper.selectAppointByExamId(examinationId);
        if (appointment != null) {
            throw new BusinessException("该体检已预约");
        }
        AppointExamination appointmentExamination = new AppointExamination();
        appointmentExamination.setAppointId(StringTools.getRandomNumber(Constant.LENGTH_12));
        appointmentExamination.setChildId(childId);
        appointmentExamination.setExaminationId(examinationId);
        appointmentExamination.setAppointTime(DateUtils.ChangeStr2DateTime(startTime));
        childMapper.insertAppoint(appointmentExamination);
        return examination;
    }

    @Override
    public void recordFood(String childId, Integer time, String food) {
        DailyTime dailyTime = new DailyTime();
        dailyTime.setDailyId(StringTools.getRandomNumber(Constant.LENGTH_12));
        dailyTime.setChildId(childId);
        dailyTime.setTime(time);
        dailyTime.setFood(food);
        dailyTime.setRecordTime(new Date());
        dailyTimeMapper.insert4Food(dailyTime);
    }

    @Override
    public void recordSleep(String childId, Integer time, Integer sleepTime) {
        DailyTime dailyTime = new DailyTime();
        dailyTime.setDailyId(StringTools.getRandomNumber(Constant.LENGTH_12));
        dailyTime.setChildId(childId);
        dailyTime.setTime(time);
        dailyTime.setSleepTime(sleepTime);
        dailyTime.setRecordTime(new Date());
        dailyTimeMapper.insert4Sleep(dailyTime);
    }

    @Override
    public PageInfo<DailyTime> searchLive(String childId, Integer pageNum) {
        if (pageNum == null) {
            pageNum = Constant.NUM_ONE;
        }
        List<DailyTime> dailyTimeListOfWeek = dailyTimeMapper.selectWeeklyRecordsByChildId(childId);
        PageHelper.startPage(pageNum, 10);
        return new PageInfo<>(dailyTimeListOfWeek);
    }

    @Override
    public List<ChildInfoVO> searchChildInfo(String familyId) {

        String json = redisComponent.getChildList(familyId);
        if (json != null) {
            return JSON.parseObject(json, new TypeReference<List<ChildInfoVO>>() {
            });
        }
        List<ChildInfoVO> childInfoVOList = childMapper.selectChildInfo(familyId);
        redisComponent.saveChildList(familyId, JSON.toJSONString(childInfoVOList));
        return childInfoVOList;
    }

    @Override
    public Child searchChildById(String childId) {
        Child child = childMapper.selectById(childId);
        if (child == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_602);
        }
        return child;
    }

    @Override
    public void exportLive(String childId, HttpServletResponse response) throws Exception {
        PageInfo<DailyTime> pageInfo = searchLive(childId, 9999);
        List<DailyTime> dataList = pageInfo.getList();

        List<DailyTimeExcel> excelList = new ArrayList<>();
        for (DailyTime d : dataList) {
            DailyTimeExcel e = new DailyTimeExcel();
            e.setTime(d.getTime());
            e.setFood(d.getFood());
            e.setSleepTime(d.getSleepTime());
            e.setRecordTime(d.getRecordTime() == null ? "" : d.getRecordTime().toString());
            excelList.add(e);
        }
        response.reset(); // 清空所有之前的响应
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("生活记录", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), DailyTimeExcel.class)
                .sheet("生活记录")
                .doWrite(excelList);
    }

    @Override
    public void updateGrowthRecord(String childId, Integer height, Integer weight, Integer headCirc) {
        Child child = childMapper.selectById(childId);
        if (child == null) {
            throw new BusinessException("儿童信息不存在");
        }
        child.setHeight(height);
        child.setWeight(weight);
        child.setHeadCirc(headCirc);
        child.setRecordDate(new Date());
        childMapper.update(child);
    }

    @Override
    public void recordGrowth(GrowthTrend growthTrend) {
        String childId = growthTrend.getChildId();
        if (childId == null || childId.isBlank()) {
            throw new BusinessException("儿童ID不能为空");
        }
        Child child = childMapper.selectById(childId);
        if (child == null) {
            throw new BusinessException("儿童信息不存在");
        }
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        GrowthTrend todayRecord = growthTrendMapper.selectByChildIdAndDate(childId, today);
        if (todayRecord != null) {
            if (growthTrend.getHeight() != null) {
                todayRecord.setHeight(growthTrend.getHeight());
            }
            if (growthTrend.getWeight() != null) {
                todayRecord.setWeight(growthTrend.getWeight());
            }
            if (growthTrend.getHeadCirc() != null) {
                todayRecord.setHeadCirc(growthTrend.getHeadCirc());
            }
            Integer addCn = growthTrend.getChineseWordCount();
            if (addCn != null && addCn > 0) {
                todayRecord.setChineseWordCount((todayRecord.getChineseWordCount() == null ? 0 : todayRecord.getChineseWordCount()) + addCn);
            }
            Integer addEn = growthTrend.getEnglishWordCount();
            if (addEn != null && addEn > 0) {
                todayRecord.setEnglishWordCount((todayRecord.getEnglishWordCount() == null ? 0 : todayRecord.getEnglishWordCount()) + addEn);
            }
            Integer addPoem = growthTrend.getPoetryCount();
            if (addPoem != null && addPoem > 0) {
                todayRecord.setPoetryCount((todayRecord.getPoetryCount() == null ? 0 : todayRecord.getPoetryCount()) + addPoem);
            }
            growthTrendMapper.updateById(todayRecord);
        } else {
            growthTrend.setId(StringTools.getRandomNumber(Constant.LENGTH_12));
            growthTrend.setRecordDate(today); // 存入 DATE 类型
            growthTrendMapper.insertGrowthTrend(growthTrend);
        }
        if (growthTrend.getHeight() != null) {
            child.setHeight(growthTrend.getHeight());
        }
        if (growthTrend.getWeight() != null) {
            child.setWeight(growthTrend.getWeight());
        }
        if (growthTrend.getHeadCirc() != null) {
            child.setHeadCirc(growthTrend.getHeadCirc());
        }
        Integer addChinese = growthTrend.getChineseWordCount();
        if (addChinese != null && addChinese > 0) {
            Integer old = child.getChineseWordCount() == null ? 0 : child.getChineseWordCount();
            child.setChineseWordCount(old + addChinese);
        }
        Integer addEnglish = growthTrend.getEnglishWordCount();
        if (addEnglish != null && addEnglish > 0) {
            Integer old = child.getEnglishWordCount() == null ? 0 : child.getEnglishWordCount();
            child.setEnglishWordCount(old + addEnglish);
        }
        Integer addPoetry = growthTrend.getPoetryCount();
        if (addPoetry != null && addPoetry > 0) {
            Integer old = child.getPoetryCount() == null ? 0 : child.getPoetryCount();
            child.setPoetryCount(old + addPoetry);
        }
        child.setRecordDate(today);
        childMapper.update(child);
    }

    @Override
    public List<GrowthTrend> searchGrowth(String childId, Integer days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        java.sql.Date start = java.sql.Date.valueOf(startDate);
        java.sql.Date end = java.sql.Date.valueOf(endDate);
        Child child = childMapper.selectById(childId);
        if (child == null) {
            throw new BusinessException("儿童信息不存在");
        }
        return growthTrendMapper.selectByChildIdAndDateRange(childId, start, end);
    }

    @Override
    public void updateGrowthTrend(GrowthTrend growthTrend) {
        if (growthTrend.getId() == null || growthTrend.getId().isBlank()) {
            throw new BusinessException("数据id不能为空");
        }
        if (growthTrend.getChildId() == null || growthTrend.getChildId().isBlank()) {
            throw new BusinessException("儿童ID不能为空");
        }
        GrowthTrend oldRecord = growthTrendMapper.selectById(growthTrend.getId());
        if (oldRecord == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        Child child = childMapper.selectById(growthTrend.getChildId());
        if (child == null) {
            throw new BusinessException("儿童信息不存在");
        }
        if (growthTrend.getHeight() != null) {
            oldRecord.setHeight(growthTrend.getHeight());
        }
        if (growthTrend.getWeight() != null) {
            oldRecord.setWeight(growthTrend.getWeight());
        }
        if (growthTrend.getHeadCirc() != null) {
            oldRecord.setHeadCirc(growthTrend.getHeadCirc());
        }
        if (growthTrend.getChineseWordCount() != null) {
            oldRecord.setChineseWordCount(growthTrend.getChineseWordCount());
        }
        if (growthTrend.getEnglishWordCount() != null) {
            oldRecord.setEnglishWordCount(growthTrend.getEnglishWordCount());
        }
        if (growthTrend.getPoetryCount() != null) {
            oldRecord.setPoetryCount(growthTrend.getPoetryCount());
        }
        growthTrendMapper.updateById(oldRecord);
        if (oldRecord.getHeight() != null) {
            child.setHeight(oldRecord.getHeight());
        }
        if (oldRecord.getWeight() != null) {
            child.setWeight(oldRecord.getWeight());
        }
        if (oldRecord.getHeadCirc() != null) {
            child.setHeadCirc(oldRecord.getHeadCirc());
        }
        child.setChineseWordCount(oldRecord.getChineseWordCount());
        child.setEnglishWordCount(oldRecord.getEnglishWordCount());
        child.setPoetryCount(oldRecord.getPoetryCount());
        child.setRecordDate(new java.sql.Date(System.currentTimeMillis()));
        childMapper.update(child);
    }

    @Override
    public List<AvailableTimeVO> loadFreeTime(String examinationId) {
        Examination examination = childMapper.selectExamById(examinationId);
        if (examination == null) {
            throw new BusinessException("体检信息不存在");
        }
        List<AppointExamination> appointments = childMapper.selectAppointExamination(examinationId);
        List<Integer> timeStatus = new ArrayList<>();
        Date startDate = examination.getStartTime();
        Date endDate = examination.getEndTime();
        int startHour = DateUtils.getHour(startDate);
        int endHour = DateUtils.getHour(endDate);
        int totalHour = DateUtils.getHourDiff(startDate, endDate);
        for (int i = 0; i < totalHour; i++) {
            timeStatus.add(1);
        }
        for (AppointExamination appointment : appointments){
            int appointHour = DateUtils.getHour(appointment.getAppointTime());
            int index = appointHour - startHour;
            if (index >= 0 && index < timeStatus.size()){
                timeStatus.set(index, 0);
            }
        }
        List<Integer> availableHours = new ArrayList<>();
        for (int i = 0; i < timeStatus.size(); i++) {
            if (timeStatus.get(i) == 1) {
                availableHours.add(startHour + i);
            }
        }
        AvailableTimeVO vo = new AvailableTimeVO();
        vo.setDoctorId(examination.getDoctorId());
        vo.setDoctorName(examination.getDoctorName());
        vo.setDate(new SimpleDateFormat("yyyy-MM-dd").format(examination.getStartTime()));
        vo.setAvailableHours(availableHours);
        return Collections.singletonList(vo);

    }

    @Override
    public List<Examination> loadExamination() {
        LocalDateTime now = LocalDateTime.now();
        return childMapper.selectAvailableExamination(now);
    }

    @Override
    public List<ExaminationVO> findMyExamination(String familyId) {
        return childMapper.findMyExamination(familyId);
    }

    @Override
    public void deleteGrowthRecord(String id) {
        growthTrendMapper.deleteById(id);
    }

    @Override
    public void cancelExamination(String appointId) {
        childMapper.deleteExamination(appointId);
    }

    @Override
    public void updateFood(String childId, String recordTime, Integer time, String food) {
        childMapper.updateFood(childId, recordTime, time, food);
    }

    @Override
    public void updateSleep(String childId, String recordTime, Integer time, Integer sleepTime) {
        childMapper.updateSleep(childId, recordTime, time, sleepTime);
    }

    @Override
    public void deleteLiveRecord(String dailyId) {
        dailyTimeMapper.deleteLiveRecord(dailyId);
    }

    private int getValue(Integer num) {
        return num == null ? 0 : num;
    }

    private int getNeedTimes(ChildVaccineEnum vaccineEnum) {
        String[] split = vaccineEnum.name().split("_");
        return Integer.parseInt(split[1]);
    }

    private boolean isVaccineDone(VaccineRecord record, ChildVaccineEnum vaccineEnum) {
        String name = vaccineEnum.name();
        int needTimes = getNeedTimes(vaccineEnum);
        if (name.startsWith("HBV")) {
            return getValue(record.getHBVTimes()) >= needTimes;
        }
        if (name.startsWith("BCGV")) {
            return getValue(record.getBCGVTimes()) >= needTimes;
        }
        if (name.startsWith("OPV")) {
            return getValue(record.getOPVTimes()) >= needTimes;
        }
        if (name.startsWith("DTaP")) {
            return getValue(record.getDTaPTimes()) >= needTimes;
        }
        if (name.startsWith("HAV")) {
            return getValue(record.getHAVTimes()) >= needTimes;
        }
        if (name.startsWith("MMR")) {
            return getValue(record.getMMRTimes()) >= needTimes;
        }
        if (name.startsWith("JEVL")) {
            return getValue(record.getJEVLTimes()) >= needTimes;
        }
        if (name.startsWith("GACPV") || name.startsWith("GACMV")) {
            return getValue(record.getGACPVTimes()) >= needTimes;
        }
        if (name.startsWith("DT")) {
            return getValue(record.getDTTimes()) >= needTimes;
        }
        return false;
    }

    /**
     * 根据疫苗类型更新对应字段
     *
     * @param record        接种记录
     * @param vaccineType   疫苗类型（如乙肝、卡介苗）
     * @param needleNum     针次
     * @param inoculateTime 接种时间
     */
    private void updateRecordByVaccineType(VaccineRecord record, String vaccineType, Integer needleNum, Date inoculateTime) {
        switch (vaccineType) {
            case "卡介苗":
                record.setBCGVTimes(needleNum);
                record.setBCGLastTime(inoculateTime);
                break;
            case "乙肝":
                record.setHBVTimes(needleNum);
                record.setHBVLastTime(inoculateTime);
                break;
            case "脊髓灰质炎":
                record.setOPVTimes(needleNum);
                record.setOPVLastTime(inoculateTime);
                break;
            case "百白破":
                record.setDTaPTimes(needleNum);
                record.setDTaPLastTime(inoculateTime);
                break;
            case "麻腮风":
                record.setMMRTimes(needleNum);
                record.setMMRLastTime(inoculateTime);
                break;
            case "乙脑":
                record.setJEVLTimes(needleNum);
                record.setJEVLLastTime(inoculateTime);
                break;
            case "A群流脑":
                record.setGACPVTimes(needleNum);
                record.setGACPVLastTime(inoculateTime);
                break;
            case "甲肝":
                record.setHAVTimes(needleNum);
                record.setHAVLastTime(inoculateTime);
                break;
            case "白破":
                record.setDTTimes(needleNum);
                record.setDTLastTime(inoculateTime);
                break;
            case "AC群流脑":
                record.setGACPVTimes(needleNum);
                record.setGACPVLastTime(inoculateTime);
                break;
            default:
                throw new BusinessException("不支持的疫苗类型：" + vaccineType);
        }
    }

}
