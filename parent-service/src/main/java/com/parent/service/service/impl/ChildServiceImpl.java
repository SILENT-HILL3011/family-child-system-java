package com.parent.service.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.child.common.constants.Constant;
import com.child.common.entity.enums.ChildVaccineEnum;
import com.child.common.entity.enums.TimePerEnum;
import com.child.common.entity.po.*;
import com.child.common.entity.vo.ChildInfoVO;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChildServiceImpl implements ChildService {

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
    @Value("${file.upload-path}")
    private String uploadPath;
    @Resource
    private RedisComponent redisComponent;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addChild(String familyId, String childName, Integer sex,String idNumber,String birthDate) {
        Family checkIsExist = userMapper.selectFamilyById(familyId);
        if (checkIsExist == null){
            throw new BusinessException("家庭不存在");
        }
        Child check = childMapper.selectByNameAndFamilyId(childName,familyId);
        if (check != null){
            throw new BusinessException("儿童不存在");
        }
        Child child = new Child();
        child.setChildName(childName);
        child.setSex(sex);
        child.setChildId(StringTools.getRandomNumber(Constant.LENGTH_12));
        child.setFamilyId(familyId);
        child.setIdNumber(idNumber);
        child.setBirthDate(DateUtils.ChangeStr2Date(birthDate));
        childMapper.insert(child);
    }

    @Override
    public void updateChildInfo(Child child) {
        Child check = childMapper.selectById(child.getChildId());
        if (check == null){
            throw new BusinessException("儿童不存在");
        }
        if (child.getChineseWordCount() != null){
            check.setChineseWordCount(child.getChineseWordCount());
        }
        if (child.getEnglishWordCount() != null){
            check.setEnglishWordCount(child.getEnglishWordCount());
        }
        if (child.getPoetryCount() != null){
            check.setPoetryCount(child.getPoetryCount());
        }
        if (child.getHeight() != null){
            check.setHeight(child.getHeight());
        }
        if (child.getWeight() != null){
            check.setWeight(child.getWeight());
        }
        if (child.getHeadCirc() != null){
            check.setHeadCirc(child.getHeadCirc());
        }
        if (child.getAge() != null){
            check.setAge(child.getAge());
        }
        check.setRecordDate(new Date());
        childMapper.update(check);
        redisComponent.saveChildInfo(child.getChildId(), JSON.toJSONString(check));
        redisComponent.clearChildListCache();
    }

    @Override
    public VaccineRecord searchVaccine(String childId) {
        Child child = childMapper.selectById(childId);
        if (child == null){
            throw new BusinessException(ResponseCodeEnum.CODE_602);
        }
        Child newChild = childMapper.selectChildFromVaccineRecord(childId);
        if (newChild == null){
            childMapper.insertVaccineRecord(childId);
            return null;
        }
        return childMapper.selectVaccineRecord(childId);
    }

    @Override
    public void updateVaccine(String childId, String vaccine) {
        Optional<ChildVaccineEnum> vaccineEnumOpt = ChildVaccineUtil.getEnumByVaccineDesc(vaccine);
        if (vaccineEnumOpt.isEmpty()){
            throw new BusinessException("未匹配到对应的疫苗类型：" + vaccine);
        }
        VaccineRecord record = vaccineRecordMapper.selectByChildId(childId);
        if (record == null) {
            record = new VaccineRecord();
            record.setChildId(childId);
        }
        Date now = new Date();
        String vaccineType = ChildVaccineUtil.getVaccineTypeFromDesc(vaccine);
        Integer needleNum = ChildVaccineUtil.getNeedleNumFromDesc(vaccine) ;
        updateRecordByVaccineType(record,vaccineType,needleNum,now);
        if (record.getChildId() != null && vaccineRecordMapper.selectByChildId(childId) != null) {
            vaccineRecordMapper.updateByChildId(record);
        } else {
            vaccineRecordMapper.insert(record);
        }
    }

    @Override
    public List<String> searchVaccineThisYear(String childId) {
        Child child = childMapper.selectById(childId);
        if (child == null){
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
    public Examination appointExamination(String childId, String doctorId) {
        Examination examination = childMapper.selectExaminationByDoctorId(doctorId);
        if (examination == null){
            return null;
        }
        Examination newExamination = new Examination();
        newExamination.setExaminationId(StringTools.getRandomNumber(Constant.LENGTH_12));
        newExamination.setChildId(childId);
        newExamination.setChecked(Constant.IS);
        newExamination.setDoctorId(doctorId);
        newExamination.setExaminationTime(new Date());
        childMapper.updateExamination(newExamination);
        return newExamination;
    }

    @Override
    public void recordFood(String childId, Integer time, String food) {
        DailyTime dailyTime = new DailyTime();
        dailyTime.setChildId(childId);
        dailyTime.setTime(TimePerEnum.getDescByCode(time));
        dailyTime.setFood(food);
        dailyTime.setRecordTime(new Date());
        dailyTimeMapper.insert4Food(dailyTime);
    }

    @Override
    public void recordSleep(String childId, Integer time, Integer sleepTime) {
        DailyTime dailyTime = new DailyTime();
        dailyTime.setChildId(childId);
        dailyTime.setTime(TimePerEnum.getDescByCode(time));
        dailyTime.setSleepTime(sleepTime);
        dailyTime.setRecordTime(new Date());
        dailyTimeMapper.insert4Sleep(dailyTime);
    }

    @Override
    public PageInfo<DailyTime> searchLive(String childId,Integer pageNum) {
        if (pageNum == null){
            pageNum = Constant.NUM_ONE;
        }
        List<DailyTime> dailyTimeListOfWeek = dailyTimeMapper.selectWeeklyRecordsByChildId(childId);
        PageHelper.startPage(pageNum, 10);
        return new PageInfo<>(dailyTimeListOfWeek);
    }

    @Override
    public PageInfo<ChildInfoVO> searchChildInfo(String familyId, Integer pageNum) {
        if (pageNum == null){
            pageNum = Constant.NUM_ONE;
        }
        String json = redisComponent.getChildList(familyId, pageNum);
        if (json != null) {
            return JSON.parseObject(json, PageInfo.class);
        }
        List<ChildInfoVO> childInfoVOList = childMapper.selectChildInfo(familyId);
        PageHelper.startPage(pageNum, 10);
        PageInfo<ChildInfoVO> pageInfo = new PageInfo<>(childInfoVOList);
        redisComponent.saveChildList(familyId, pageNum, JSON.toJSONString(pageInfo));
        return pageInfo;
    }

    @Override
    public Child searchChildById(String childId) {
        Child child = childMapper.selectById(childId);
        if (child == null){
            throw new BusinessException(ResponseCodeEnum.CODE_602);
        }
        return child;
    }

    @Override
    public void exportLive(String childId, HttpServletResponse response)throws Exception {
        PageInfo<DailyTime> pageInfo = searchLive(childId, 9999);
        List<DailyTime> dataList = pageInfo.getList();

        // 2. 转换为 Excel 实体
        List<DailyTimeExcel> excelList = new ArrayList<>();
        for (DailyTime d : dataList) {
            DailyTimeExcel e = new DailyTimeExcel();
            e.setTime(d.getTime());
            e.setFood(d.getFood());
            e.setSleepTime(d.getSleepTime());
            e.setRecordTime(d.getRecordTime() == null ? "" : d.getRecordTime().toString());
            excelList.add(e);
        }

        // ====================== 核心：正确响应头 ======================
        response.reset(); // 清空所有之前的响应
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String fileName = URLEncoder.encode("生活记录", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        // 3. 写出 Excel
        EasyExcel.write(response.getOutputStream(), DailyTimeExcel.class)
                .sheet("生活记录")
                .doWrite(excelList);
    }

    @Override
    public void updateGrowthRecord(String childId,Integer height,Integer weight,Integer headCirc){
        Child child = childMapper.selectById(childId);
        if (child == null){
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
        if (child == null){
            throw new BusinessException("儿童信息不存在");
        }
        return growthTrendMapper.selectByChildIdAndDateRange(childId, start, end);
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

        // 按疫苗类型判断已接种次数 >= 需要针次
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
     * @param record 接种记录
     * @param vaccineType 疫苗类型（如乙肝、卡介苗）
     * @param needleNum 针次
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
                // 注：原PO中AC群流脑字段复用了GACPV，需确认是否新增字段，此处先复用
                record.setGACPVTimes(needleNum);
                record.setGACPVLastTime(inoculateTime);
                break;
            default:
                throw new BusinessException("不支持的疫苗类型：" + vaccineType);
        }
    }

}
