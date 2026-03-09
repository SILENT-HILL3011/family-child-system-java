package com.child.common.vo;

public class UserLoginVO {
    private String userId;
    private String phoneNumber;
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return "UserLoginVO{" +
                "username='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
