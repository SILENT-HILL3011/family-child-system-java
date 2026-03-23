package com.parent.service.service;

import com.child.common.entity.po.Child;
import com.child.common.entity.vo.GrowthConditionVO;

public interface ChildService {
    void addChild(String familyId, String childName, Integer sex,String idNumber);

    void updateChildInfo(Child child);

    GrowthConditionVO getGrowthInfo(String childId);
}
