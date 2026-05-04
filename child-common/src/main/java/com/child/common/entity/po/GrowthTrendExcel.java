package com.child.common.entity.po;

import com.alibaba.excel.annotation.ExcelProperty;

public class GrowthTrendExcel {

    @ExcelProperty("记录日期")
    private String recordDate;
    @ExcelProperty("身高(cm)")
    private Integer height;
    @ExcelProperty("体重(kg)")
    private Integer weight;
    @ExcelProperty("头围(cm)")
    private Integer headCirc;
    @ExcelProperty("中文词汇量")
    private Integer chineseWordCount;
    @ExcelProperty("英文词汇量")
    private Integer englishWordCount;
    @ExcelProperty("诗歌数量")
    private Integer poetryCount;

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public Integer getPoetryCount() {
        return poetryCount;
    }

    public void setPoetryCount(Integer poetryCount) {
        this.poetryCount = poetryCount;
    }

    public Integer getEnglishWordCount() {
        return englishWordCount;
    }

    public void setEnglishWordCount(Integer englishWordCount) {
        this.englishWordCount = englishWordCount;
    }

    public Integer getChineseWordCount() {
        return chineseWordCount;
    }

    public void setChineseWordCount(Integer chineseWordCount) {
        this.chineseWordCount = chineseWordCount;
    }

    public Integer getHeadCirc() {
        return headCirc;
    }

    public void setHeadCirc(Integer headCirc) {
        this.headCirc = headCirc;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
