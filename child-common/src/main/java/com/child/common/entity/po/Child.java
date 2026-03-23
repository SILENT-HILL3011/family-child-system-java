package com.child.common.entity.po;

public class Child {

    private String childId;
    private String childName;
    private Integer sex;
    private String familyId;
    private String idNumber;
    private Integer status;
    private Integer chineseWordCount;
    private Integer englishWordCount;
    private Integer poetryCount;
    private String healthCondition;
    private String dietaryStatus;

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
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
}
