package com.child.common.entity.po;

public class User {
    private String id;
    private String phoneNumber;
    private String password;
    private String userName;
    private Integer age;
    private Integer sex;
    private Boolean haveFamily;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Boolean getHaveFamily() {
        return haveFamily;
    }

    public void setHaveFamily(Boolean haveFamily) {
        this.haveFamily = haveFamily;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
