package com.parent.service.service.impl;

import com.child.common.constants.Constant;
import com.child.common.entity.enums.ChildVaccineEnum;
import com.child.common.entity.enums.TimePerEnum;
import com.child.common.entity.po.*;
import com.child.common.entity.vo.GrowthConditionVO;
import com.child.common.entity.vo.ResponseCodeEnum;
import com.child.common.exception.BusinessException;
import com.child.common.utils.ChildVaccineUtil;
import com.child.common.utils.StringTools;
import com.parent.service.mapper.ChildMapper;
import com.parent.service.mapper.DailyTimeMapper;
import com.parent.service.mapper.UserMapper;
import com.parent.service.mapper.VaccineRecordMapper;
import com.parent.service.service.ChildService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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

    @Override
    public void addChild(String familyId, String childName, Integer sex,String idNumber) {
        Family checkIsExist = userMapper.selectFamilyById(familyId);
        if (checkIsExist == null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        Child check = childMapper.selectByNameAndFamilyId(childName,familyId);
        if (check != null){
            throw new BusinessException(ResponseCodeEnum.CODE_601);
        }
        Child child = new Child();
        child.setChildName(childName);
        child.setSex(sex);
        child.setChildId(StringTools.getRandomNumber(Constant.LENGTH_12));
        child.setFamilyId(familyId);
        child.setIdNumber(idNumber);
        childMapper.insert(child);
    }

    @Override
    public void updateChildInfo(Child child) {
        Child check = childMapper.selectById(child.getChildId());
        if (check == null){
            throw new BusinessException(ResponseCodeEnum.CODE_602);
        }
        if (child.getHealthCondition() != null){
            check.setHealthCondition(child.getHealthCondition());
        }
        if (child.getDietaryStatus() != null){
            check.setDietaryStatus(child.getDietaryStatus());
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
        if (child.getStatus() != null){
            check.setStatus(child.getStatus());
        }
        if (child.getAge() != null){
            check.setAge(child.getAge());
        }
        childMapper.update(check);
    }

    @Override
    public GrowthConditionVO getGrowthInfo(String childId) {
        Child child = childMapper.selectById(childId);
        if (child == null){
            throw new BusinessException(ResponseCodeEnum.CODE_602);
        }
        GrowthConditionVO growthConditionVO = new GrowthConditionVO();
        if(child.getChineseWordCount() != null){
            growthConditionVO.setChineseWordCount(child.getChineseWordCount());
        }
        if(child.getEnglishWordCount() != null){
            growthConditionVO.setEnglishWordCount(child.getEnglishWordCount());
        }
        if(child.getPoetryCount() != null){
            growthConditionVO.setPoetryCount(child.getPoetryCount());
        }
        if(child.getHealthCondition() != null){
            growthConditionVO.setHealthCondition(child.getHealthCondition());
        }
        if(child.getDietaryStatus() != null){
            growthConditionVO.setDietaryStatus(child.getDietaryStatus());
        }
        if (child.getStatus() != null){
            growthConditionVO.setStatus(child.getStatus());
        }
        return growthConditionVO;
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
    public List<DailyTime> searchLive(String childId) {
        List<DailyTime> dailyTimeListOfWeek = dailyTimeMapper.selectWeeklyRecordsByChildId(childId);
        if (dailyTimeListOfWeek == null){
            return new ArrayList<>();
        }
        return dailyTimeListOfWeek;
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
