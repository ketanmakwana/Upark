package com.upark.db.models;


public class PaymentDetails {


    String TableName;
    String Id;
    String isPaidFor;
    String Amount;
    String IndexedSpot;
    String PackageId;
    String Date;
    String isCancelled;
    String RefundAmount;
    String UserId;

    public PaymentDetails() {
    }

    public PaymentDetails(String id, String isPaidFor, String amount, String indexedSpot, String packageId, String date, String isCancelled, String refundAmount, String userId) {
        Id = id;
        this.isPaidFor = isPaidFor;
        Amount = amount;
        IndexedSpot = indexedSpot;
        PackageId = packageId;
        Date = date;
        this.isCancelled = isCancelled;
        RefundAmount = refundAmount;
        UserId = userId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(String isCancelled) {
        this.isCancelled = isCancelled;
    }

    public String getRefundAmount() {
        return RefundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        RefundAmount = refundAmount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
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

    public String getIsPaidFor() {
        return isPaidFor;
    }

    public void setIsPaidFor(String isPaidFor) {
        this.isPaidFor = isPaidFor;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getIndexedSpot() {
        return IndexedSpot;
    }

    public void setIndexedSpot(String indexedSpot) {
        IndexedSpot = indexedSpot;
    }

    public String getPackageId() {
        return PackageId;
    }

    public void setPackageId(String packageId) {
        PackageId = packageId;
    }
}
