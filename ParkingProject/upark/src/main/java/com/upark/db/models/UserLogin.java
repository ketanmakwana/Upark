package com.upark.db.models;


public class UserLogin {

    String Id;
    String UserName;
    String EmailId;
    String MobileNo;
    String Password;

    public UserLogin() {
    }

    public UserLogin(String id, String userName, String emailId, String mobileNo, String password) {
        Id = id;
        UserName = userName;
        EmailId = emailId;
        MobileNo = mobileNo;
        Password = password;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
