package com.parent.service.mapper;

import com.child.common.entity.po.VaccineRecord;

public interface VaccineRecordMapper {
    VaccineRecord selectByChildId(String childId);

    void updateByChildId(VaccineRecord record);

    void insert(VaccineRecord record);
}
