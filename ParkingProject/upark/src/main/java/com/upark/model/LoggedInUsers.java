package com.upark.model;


public class LoggedInUsers {

    public static String UserName;
    public static String UserId;

    public static String getUserName() {
        return UserName;
    }

    public static void setUserName(String userName) {
        UserName = userName;
    }

    public static String getUserId() {
        return UserId;
    }

    public static void setUserId(String userId) {
        UserId = userId;
    }
}
