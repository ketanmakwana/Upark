package com.upark.db.models;

public class Packages {


    String TableName;
    String Id;
    String PackageName;
    String AllowedDays;
    String CreatedDate;
    String isVisible;
    String Rate;

    public Packages() {
    }

    public Packages(String tableName, String id, String packageName, String allowedDays, String createdDate, String isVisible, String Rate) {
        TableName = tableName;
        Id = id;
        PackageName = packageName;
        AllowedDays = allowedDays;
        CreatedDate = createdDate;
        this.isVisible = isVisible;
        this.Rate = Rate;
    }

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getAllowedDays() {
        return AllowedDays;
    }

    public void setAllowedDays(String allowedDays) {
        AllowedDays = allowedDays;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }
}
