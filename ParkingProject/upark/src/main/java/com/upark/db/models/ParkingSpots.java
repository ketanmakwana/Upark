package com.upark.db.models;

public class ParkingSpots {


    String TableName;
    String Id;
    String SpotName;
    String CreatedDate;
    String isVisible;

    public ParkingSpots() {
    }

    public ParkingSpots(String tableName, String id, String spotName, String createdDate, String isVisible) {
        TableName = tableName;
        Id = id;
        SpotName = spotName;
        CreatedDate = createdDate;
        this.isVisible = isVisible;
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

    public String getSpotName() {
        return SpotName;
    }

    public void setSpotName(String spotName) {
        SpotName = spotName;
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
}
