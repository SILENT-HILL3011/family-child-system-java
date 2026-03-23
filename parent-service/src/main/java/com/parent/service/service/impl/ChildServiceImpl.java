package com.parent.service.service.impl;

import com.child.common.constants.Constant;
import com.child.common.entity.po.Child;
import com.child.common.entity.po.Family;
import com.child.common.entity.vo.GrowthConditionVO;
import com.child.common.entity.vo.ResponseCodeEnum;
import com.child.common.exception.BusinessException;
import com.child.common.utils.StringTools;
import com.parent.service.mapper.ChildMapper;
import com.parent.service.mapper.UserMapper;
import com.parent.service.service.ChildService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ChildServiceImpl implements ChildService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private ChildMapper childMapper;
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
}
