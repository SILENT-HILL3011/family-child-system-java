package com.parent.service.mapper;

import com.child.common.entity.po.GrowthTrend;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

public interface GrowthTrendMapper {


    void insertGrowthTrend(GrowthTrend growthTrend);

    void updateById(GrowthTrend growthTrend);

    GrowthTrend selectByChildIdAndDate(String childId, Date today);

    List<GrowthTrend> selectByChildIdAndDateRange(
            @Param("childId") String childId,
            @Param("startDate") java.sql.Date startDate,
            @Param("endDate") java.sql.Date endDate
    );
}
