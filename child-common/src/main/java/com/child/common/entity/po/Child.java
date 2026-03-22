package com.child.common.entity.po;

public class Child {

    private String childId;
    private String childName;
    private Integer sex;
    private String familyId;

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    private String healthCondition;
    private String dietaryStatus;
    private StudyCondition studyCondition;

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getHealthCondition() {
        return healthCondition;
    }

    public void setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getDietaryStatus() {
        return dietaryStatus;
    }

    public void setDietaryStatus(String dietaryStatus) {
        this.dietaryStatus = dietaryStatus;
    }

    public StudyCondition getStudyCondition() {
        return studyCondition;
    }

    public void setStudyCondition(StudyCondition studyCondition) {
        this.studyCondition = studyCondition;
    }
}
