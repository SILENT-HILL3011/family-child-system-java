package com.child.common.entity.po;

import com.alibaba.excel.annotation.ExcelProperty;

public class DailyTimeExcel {

    @ExcelProperty(value = "记录类型", index = 0)
    private Integer time;

    @ExcelProperty(value = "饮食内容", index = 1)
    private String food;

    @ExcelProperty(value = "睡眠时长", index = 2)
    private Integer sleepTime;

    @ExcelProperty(value = "记录时间", index = 3)
    private String recordTime;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public Integer getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Integer sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }
}
