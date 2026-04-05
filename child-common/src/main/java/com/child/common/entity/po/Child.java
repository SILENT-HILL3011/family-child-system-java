package com.child.common.entity.po;

import java.util.Date;

public class Child {

    private String childId;
    private String childName;
    private Integer sex;
    private String familyId;
    private String idNumber;
    private Integer chineseWordCount;
    private Integer englishWordCount;
    private Integer poetryCount;
    private Integer age;
    private Integer height;
    private Integer weight;
    private Integer headCirc;
    private Date recordDate;
    private Date birthDate;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

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

}
