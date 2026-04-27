package com.parent.service.mapper;

import com.child.common.entity.po.GrowthTrend;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    @Delete("delete from growth_trend where id = #{id}")
    void deleteById(String id);

    @Select("select * from growth_trend where id = #{id}")
    GrowthTrend selectById(String id);
}
