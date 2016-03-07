package com.upark.db.models;

public class Reservation {


    String TableName;

    String Id;
    String UserId;
    String SpotId;
    String Date;
    String Time;
    String IsPaid;
    String IsCancelled;
    String PaidAmount;
    String PaidReturnAmount;
    String AmountFineObtain;
    String ReservationDate;
    String VSpotName;
    String DTReservationIN;
    String DTReservationOUT;
    String ForHours;


    public Reservation() {

    }

    public Reservation(String id, String userId, String spotId, String date, String time, String isPaid, String isCancelled) {
        Id = id;
        UserId = userId;
        SpotId = spotId;
        Date = date;
        Time = time;
        IsPaid = isPaid;
        IsCancelled = isCancelled;
    }

    public Reservation(String id, String userId, String spotId, String date, String time, String isPaid, String isCancelled, String paidAmount, String paidReturnAmount, String amountFineObtain, String reservationDate, String VSpotName, String DTReservationIN, String DTReservationOUT, String forHours) {
        Id = id;
        UserId = userId;
        SpotId = spotId;
        Date = date;
        Time = time;
        IsPaid = isPaid;
        IsCancelled = isCancelled;
        PaidAmount = paidAmount;
        PaidReturnAmount = paidReturnAmount;
        AmountFineObtain = amountFineObtain;
        ReservationDate = reservationDate;
        this.VSpotName = VSpotName;
        this.DTReservationIN = DTReservationIN;
        this.DTReservationOUT = DTReservationOUT;
        ForHours = forHours;
    }

    public String getReservationDate() {
        return ReservationDate;
    }

    public void setReservationDate(String reservationDate) {
        ReservationDate = reservationDate;
    }

    public String getPaidReturnAmount() {
        return PaidReturnAmount;
    }

    public void setPaidReturnAmount(String paidReturnAmount) {
        PaidReturnAmount = paidReturnAmount;
    }

    public String getAmountFineObtain() {
        return AmountFineObtain;
    }

    public void setAmountFineObtain(String amountFineObtain) {
        AmountFineObtain = amountFineObtain;
    }

    public String getPaidAmount() {
        return PaidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        PaidAmount = paidAmount;
    }

    public String getIsCancelled() {
        return IsCancelled;
    }

    public void setIsCancelled(String isCancelled) {
        IsCancelled = isCancelled;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getSpotId() {
        return SpotId;
    }

    public void setSpotId(String spotId) {
        SpotId = spotId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getIsPaid() {
        return IsPaid;
    }

    public void setIsPaid(String isPaid) {
        IsPaid = isPaid;
    }

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public String getVSpotName() {
        return VSpotName;
    }

    public void setVSpotName(String VSpotName) {
        this.VSpotName = VSpotName;
    }

    public String getDTReservationIN() {
        return DTReservationIN;
    }

    public void setDTReservationIN(String DTReservationIN) {
        this.DTReservationIN = DTReservationIN;
    }

    public String getForHours() {
        return ForHours;
    }

    public void setForHours(String forHours) {
        ForHours = forHours;
    }

    public String getDTReservationOUT() {
        return DTReservationOUT;
    }

    public void setDTReservationOUT(String DTReservationINOUT) {
        this.DTReservationOUT = DTReservationINOUT;
    }
}
