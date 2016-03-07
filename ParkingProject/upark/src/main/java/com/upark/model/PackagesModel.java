package com.upark.model;

/**
 * Created by Ketan on 06/03/2016.
 */
public class PackagesModel {

    String Id;
    String Name;
    String Rate;
    String AllowedDays;

    public PackagesModel() {
    }

    public PackagesModel(String id, String name, String rate, String allowedDays) {
        Id = id;
        Name = name;
        Rate = rate;
        AllowedDays = allowedDays;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getAllowedDays() {
        return AllowedDays;
    }

    public void setAllowedDays(String allowedDays) {
        AllowedDays = allowedDays;
    }
}
