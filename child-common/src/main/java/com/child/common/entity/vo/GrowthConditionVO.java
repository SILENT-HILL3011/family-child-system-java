package com.child.common.entity.vo;

public class GrowthConditionVO {
    private String healthCondition;
    private String dietaryStatus;
    private Integer status;
    private Integer chineseWordCount;
    private Integer englishWordCount;
    private Integer poetryCount;

    public String getHealthCondition() {
        return healthCondition;
    }

    public void setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
    }

    public String getDietaryStatus() {
        return dietaryStatus;
    }

    public void setDietaryStatus(String dietaryStatus) {
        this.dietaryStatus = dietaryStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getChineseWordCount() {
        return chineseWordCount;
    }

    public void setChineseWordCount(Integer chineseWordCount) {
        this.chineseWordCount = chineseWordCount;
    }

    public Integer getEnglishWordCount() {
        return englishWordCount;
    }

    public void setEnglishWordCount(Integer englishWordCount) {
        this.englishWordCount = englishWordCount;
    }

    public Integer getPoetryCount() {
        return poetryCount;
    }

    public void setPoetryCount(Integer poetryCount) {
        this.poetryCount = poetryCount;
    }
}
