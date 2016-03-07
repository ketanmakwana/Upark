package com.upark.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.renderscript.RenderScript;
import android.util.Log;

import com.upark.R;
import com.upark.db.models.Packages;
import com.upark.db.models.ParkingSpots;
import com.upark.db.models.PaymentDetails;
import com.upark.db.models.Reservation;
import com.upark.db.models.UserLogin;
import com.upark.model.LoggedInUsers;

public class DBHelper extends SQLiteOpenHelper {

    protected String DB_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath()
            + "/Android/data/com.upark/";
    protected final static String DB_NAME = "up.db";
    protected final static int DB_VERSION = 1;
    protected static SQLiteDatabase db;
    protected final Context context;

    //Table for users
    protected final String MSTR_USERS = "mstr_users";
    protected final String u_id = "id";
    protected final String u_name = "user_name";
    protected final String u_emaild = "email_id";
    protected final String u_mobile_no = "mobile_no";
    protected final String u_password = "password";

    //Table for Reservation
    protected final String MSTR_RESERVATION = "mstr_reservation";
    protected final String RESERVATION_VIEW = "ReservationManager";
    protected final String r_id = "id";
    protected final String r_user_id = "user_id";
    protected final String r_spot_id = "spot_id";
    protected final String r_date = "date";
    protected final String r_time = "time";
    protected final String r_is_paid = "is_paid";
    protected final String r_isCancelled = "isCancelled";
    protected final String r_v_SpotName = "spot_name";

    // Table for Packages
    protected final String MSTR_PACKAGES = "mstr_packages";
    protected final String p_id = "id";
    protected final String p_package_name = "package_name";
    protected final String p_allowed_days = "allowed_days";
    protected final String p_created_date = "created_date";
    protected final String p_is_visible = "is_visible";
    protected final String p_rate = "rate";

    // Table for Spots
    protected final String MSTR_SPOTS = "mstr_spots";
    protected final String s_Id = "id";
    protected final String s_spot_name = "spot_name";
    protected final String s_create_date = "created_date";
    protected final String s_is_Visible = "is_visible";

    // Table PaymentDetails
    protected final String MSTR_PAYMENT_DETAILS = "tbl_payment_details";
    protected final String pd_Id = "id";
    protected final String pd_isPaidFor = "is_paid_for";
    protected final String pd_amount = "amount";
    protected final String pd_indexedSpot = "indexed_spot";
    protected final String pd_packageId = "package_id";
    protected final String pd_c_date = "c_date";
    protected final String pd_isCancelled = "isCancelled";
    protected final String pd_refund_amount = "refund_amount";
    protected final String pd_userId = "user_id";

    // Table Message
    protected final String MSTR_MESSAGES = "mstr_messages";
    protected final String m_id= "id";
    protected final String m_message = "message";
    protected final String m_reletedTo = "releted_to_reservation_id";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }
    // crate new database
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        SQLiteDatabase db_Read = null;

        if (dbExist) {
        } else {
            db_Read = this.getReadableDatabase();
            db_Read.close();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }
    //check database is exist
    public boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }
    //Copy Database from assests to LocalDirectory path
    public void copyDataBase() throws IOException {
        InputStream myInput = context.getAssets().open(DB_NAME);

        File file = new File(DB_PATH);
        file.mkdirs();

        String outFileName = DB_PATH + DB_NAME;

        OutputStream myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
    // Open existing database

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void close() {
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Spots
    public ArrayList<ParkingSpots> getAllSpots() {
        ArrayList<ParkingSpots> dataArrays = new ArrayList<ParkingSpots>();
        Cursor cursor = null;
        boolean isFound = false;
        try {
            cursor = db.query(MSTR_SPOTS, new String[]{s_Id, s_spot_name, s_create_date, s_is_Visible}, null, null, null, null, null);
            int Size = cursor.getCount();

            cursor.moveToFirst();
            int stop = 0;
            if (!cursor.isAfterLast()) {
                int i = 0;
                do {
                    ParkingSpots mParkingSpots = new ParkingSpots();
                    mParkingSpots.setId(cursor.getString(0));
                    mParkingSpots.setSpotName(cursor.getString(1));
                    mParkingSpots.setCreatedDate(cursor.getString(2));
                    mParkingSpots.setIsVisible(cursor.getString(3));
                    dataArrays.add(mParkingSpots);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
        }
        return dataArrays;
    }

    public String getSpotNameById(String Id) {
        Cursor cursor = null;
        String isFound = "";
        try {
            //
            cursor =
                    db.query(MSTR_SPOTS, new String[]{s_spot_name}, s_Id + "=" + Id, null, null, null, null);
            int Size = cursor.getCount();
            cursor.moveToFirst();
            int stop = 0;
            if (!cursor.isAfterLast()) {
                int i = 0;
                do {
                    isFound = cursor.getString(0);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
        }
        return isFound;
    }

    public ArrayList<Reservation> getReservationView(String UserId){
        ArrayList<Reservation> dataArrays = new ArrayList<Reservation>();
        Cursor cursor = null;
        try {
            cursor = db.query(RESERVATION_VIEW, new String[]{r_id, r_user_id,
                    r_date,  r_spot_id, r_v_SpotName}, r_user_id + " = " + UserId, null, null, null, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    Reservation reservation = new Reservation();
                    reservation.setId(cursor.getString(0));
                    reservation.setUserId(cursor.getString(1));
                    reservation.setDate(cursor.getString(2));
                    reservation.setSpotId(cursor.getString(3));
                    reservation.setVSpotName(cursor.getString(4));
                    dataArrays.add(reservation);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
        }
        return dataArrays;
    }
    public String getSpotId(String spot_name) {
        Cursor cursor = null;
        String isFound = "";
        try {
            //
            cursor =
                    db.query(MSTR_SPOTS, new String[]{s_Id}, " spot_name='" + spot_name + "'", null, null, null, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    String SpotId = cursor.getString(0);
                    isFound = SpotId;
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
        }
        return isFound;

    }

    public boolean isSpotRegistered(String Id,String UserId) {
        Cursor cursor = null;
        boolean isFound = false;
        try {
            cursor =
                    db.query(MSTR_RESERVATION, new String[]{r_spot_id},
                            r_spot_id + "=" + Id + " AND user_id =" +UserId +" AND isCancelled = '0' ", null, null, null, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    isFound = true;
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
        }
        return isFound;
    }

    public boolean checkSpotName(String spot_name) {
        Cursor cursor = null;
        boolean isFound = false;
        try {
            //
            cursor =
                    db.query(MSTR_SPOTS, new String[]{s_Id}, "spot_name='" + spot_name + "'", null, null, null, null);
            int Size = cursor.getCount();
            cursor.moveToFirst();
            int stop = 0;
            if (!cursor.isAfterLast()) {
                int i = 0;
                do {
                    return true;
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
        }
        return false;
    }


    // Packages
    public ArrayList<Packages> getAllPackages() {
        ArrayList<Packages> dataArrays = new ArrayList<Packages>();
        Cursor cursor = null;
        boolean isFound = false;
        try {
            cursor = db.query(MSTR_PACKAGES, new String[]{p_id, p_package_name, p_allowed_days, p_created_date, p_is_visible, p_rate}, null, null, null, null, null);
            int Size = cursor.getCount();

            cursor.moveToFirst();
            int stop = 0;
            if (!cursor.isAfterLast()) {
                int i = 0;
                do {
                    Packages mPackages = new Packages();
                    mPackages.setId(cursor.getString(0));
                    mPackages.setPackageName(cursor.getString(1));
                    mPackages.setAllowedDays(cursor.getString(2));
                    mPackages.setCreatedDate(cursor.getString(3));
                    mPackages.setIsVisible(cursor.getString(4));
                    mPackages.setRate(cursor.getString(5));
                    dataArrays.add(mPackages);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
        }
        return dataArrays;
    }

    public String getPackageAmountById(String Id) {
        String PackageAmount = "";
        Cursor cursor = null;
        boolean isFound = false;
        try {
            cursor = db.query(MSTR_PACKAGES, new String[]{p_rate}, p_id + " = " + Id, null, null, null, null);
            int Size = cursor.getCount();
            cursor.moveToFirst();
            int stop = 0;
            if (!cursor.isAfterLast()) {
                int i = 0;
                do {
                    PackageAmount = cursor.getString(0);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
        }
        return PackageAmount;
    }

    public String getPackageIdByName(String Name) {
        String PackageId = "";
        Cursor cursor = null;
        boolean isFound = false;
        try {
            cursor = db.query(MSTR_PACKAGES, new String[]{p_id}, p_package_name + "= '" + Name + "'", null, null, null, null);
            int Size = cursor.getCount();
            cursor.moveToFirst();
            int stop = 0;
            if (!cursor.isAfterLast()) {
                int i = 0;
                do {
                    PackageId = cursor.getString(0);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return PackageId;
    }

    public void addNewPackages(Packages packages) {
        ContentValues value = new ContentValues();
        value.put(p_id, packages.getId());
        value.put(p_package_name, packages.getPackageName());
        value.put(p_allowed_days, packages.getAllowedDays());
        value.put(p_created_date, packages.getCreatedDate());
        value.put(p_is_visible, packages.getIsVisible());
        value.put(p_rate, packages.getRate());
        try {
            db.insert(MSTR_PACKAGES, null, value);
        } catch (Exception ex) {
        }
    }

    public void updatePackageData(Packages packages) {
        ContentValues value = new ContentValues();
        value.put(p_package_name, packages.getPackageName());
        value.put(p_allowed_days, packages.getAllowedDays());
        value.put(p_created_date, packages.getCreatedDate());
        value.put(p_is_visible, packages.getIsVisible());
        value.put(p_rate, packages.getRate());
        try {
            db.update(MSTR_PACKAGES, value, p_id + "=" + packages.getId(), null);
        } catch (Exception e) {
        }
    }

    public void deletePackageData(String id) {
        try {
            db.delete(MSTR_PACKAGES, p_id + "=" + id, null);
        } catch (Exception e) {
        }
    }

    // Resrevation

    public ArrayList<Reservation> getAllReservation() {
        ArrayList<Reservation> dataArrays = new ArrayList<Reservation>();
        Cursor cursor = null;
        try {
            cursor = db.query(MSTR_RESERVATION, new String[]{r_id, r_user_id,
                    r_date, r_is_paid, r_spot_id, r_time, r_isCancelled}, null, null, null, null, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    Reservation reservation = new Reservation();
                    reservation.setId(cursor.getString(0));
                    reservation.setUserId(cursor.getString(1));
                    reservation.setDate(cursor.getString(2));
                    reservation.setIsPaid(cursor.getString(3));
                    reservation.setSpotId(cursor.getString(4));
                    reservation.setTime(cursor.getString(5));
                    reservation.setIsCancelled(cursor.getString(6));
                    dataArrays.add(reservation);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
        }
        return dataArrays;
    }

    public void addNewReservation(Reservation reservation) {
        ContentValues value = new ContentValues();
        value.put(r_id, reservation.getId());
        value.put(r_spot_id, reservation.getSpotId());
        value.put(r_user_id, reservation.getUserId());
        value.put(r_date, reservation.getDate());
        value.put(r_is_paid, reservation.getIsPaid());
        value.put(r_time, reservation.getTime());
        value.put(r_isCancelled,reservation.getIsCancelled());

        try {
            db.insert(MSTR_RESERVATION, null, value);
        } catch (Exception ex) {
        }
    }

    public String getReservationId() {
        Cursor cursor = null;
        String ReservationId = "";
        try {

            cursor = db.query(MSTR_RESERVATION, new String[]{r_id}, null, null, null, null, r_id + " DESC");
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                ReservationId = cursor.getString(0);
            }
            cursor.close();
        } catch (SQLException e) {
        }
        return ReservationId;
    }

    public void updateReservationData(Reservation reservation) {
        ContentValues value = new ContentValues();
        if (reservation.getSpotId() != null && !reservation.getSpotId().isEmpty()) {
            value.put(r_spot_id, reservation.getSpotId());
        }
        if (reservation.getUserId() != null && !reservation.getUserId().isEmpty()) {
            value.put(r_user_id, reservation.getUserId());
        }
        if (reservation.getDate() != null && !reservation.getDate().isEmpty()) {
            value.put(r_date, reservation.getDate());
        }
        if (reservation.getIsPaid() != null && !reservation.getIsPaid().isEmpty()) {
            value.put(r_is_paid, reservation.getIsPaid());
        }
        if (reservation.getTime() != null && !reservation.getTime().isEmpty()) {
            value.put(r_time, reservation.getTime());
        }
        if(reservation.getIsCancelled() != null && !reservation.getIsCancelled().isEmpty()){
            value.put(r_isCancelled,reservation.getIsCancelled());
        }
        try {
            db.update(MSTR_RESERVATION, value, r_id + "=" + reservation.getId(), null);
        } catch (Exception e) {
        }
    }


    public void deleteReservationData(String id) {
        try {
            db.delete(MSTR_RESERVATION, r_id + "=" + id, null);
        } catch (Exception e) {
        }
    }

    // Users
    public void addUserData(UserLogin userLogin) {

        ContentValues value = new ContentValues();

        value.put(u_name, userLogin.getUserName());
        value.put(u_emaild, userLogin.getEmailId());
        value.put(u_mobile_no, userLogin.getMobileNo());
        value.put(u_password, userLogin.getPassword());

        try {
            db.insert(MSTR_USERS, null, value);
        } catch (Exception ex) {
        }

    }
    public String getUserId(String Name){
        Cursor cursor = null;
        String UserId = null;
        try {
            //
            cursor = db.query(MSTR_USERS, new String[]{u_id},
                    u_name + "='" + Name+"'",
                    null, null, null, null);

            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    UserId = cursor.getString(0);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return UserId;
    }
    public boolean isUserExist(String UserName, String Password) {
        Cursor cursor = null;
        boolean isFound = false;
        try {
            //
            cursor = db.query(MSTR_USERS, new String[]{u_id},
                    u_name + "='" + UserName + "' AND " +
                            "" + u_password + "= '" + Password + "'",
                    null, null, null, null);

            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    return true;
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
        }
        return false;
    }

    // ---- Parking Spots
    public void addNewSpot(ParkingSpots spots) {
        ContentValues value = new ContentValues();
        value.put(s_Id, spots.getId());
        value.put(s_spot_name, spots.getSpotName());
        value.put(s_create_date, spots.getCreatedDate());
        value.put(s_is_Visible, spots.getIsVisible());
        try {
            db.insert(MSTR_SPOTS, null, value);
        } catch (Exception ex) {
        }
    }


    public void updateSpots(ParkingSpots spots) {
        ContentValues value = new ContentValues();
        value.put(s_spot_name, spots.getSpotName());
        value.put(s_is_Visible, spots.getIsVisible());
        try {
            db.update(MSTR_SPOTS, value, s_Id + "=" + spots.getId(), null);
        } catch (Exception e) {
        }
    }


    public void deleteSpotsData(String id) {
        try {
            db.delete(MSTR_SPOTS, s_Id + "=" + id, null);
        } catch (Exception e) {
        }
    }

    // ----Payment Details
    public ArrayList<PaymentDetails> getAllPaymentDetails() {
        ArrayList<PaymentDetails> dataArrays = new ArrayList<>();
        Cursor cursor = null;
        boolean isFound = false;
        try {
            cursor = db.query(MSTR_PAYMENT_DETAILS, new String[]{pd_Id, pd_isPaidFor, pd_packageId, pd_amount, pd_indexedSpot, pd_c_date, pd_isCancelled, pd_refund_amount}, null, null, null, null, null);
            int Size = cursor.getCount();

            cursor.moveToFirst();
            int stop = 0;
            if (!cursor.isAfterLast()) {
                int i = 0;
                do {
                    PaymentDetails mPaymentDetails = new PaymentDetails();
                    mPaymentDetails.setId(cursor.getString(0));
                    mPaymentDetails.setIsPaidFor(cursor.getString(1));
                    mPaymentDetails.setPackageId(cursor.getString(2));
                    mPaymentDetails.setAmount(cursor.getString(3));
                    mPaymentDetails.setIndexedSpot(cursor.getString(4));
                    mPaymentDetails.setDate(cursor.getString(5));
                    mPaymentDetails.setIsCancelled(cursor.getString(6));
                    mPaymentDetails.setRefundAmount(cursor.getString(7));
                    dataArrays.add(mPaymentDetails);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
        }
        return dataArrays;
    }

    public PaymentDetails getAllPaymentDetailsByResId(String Id,String UserId){
        PaymentDetails mPaymentDetails  = null;
        Cursor cursor = null;
        boolean isFound = false;
        try {
            cursor = db.query(MSTR_PAYMENT_DETAILS, new String[]{pd_Id, pd_isPaidFor,
                    pd_packageId, pd_amount, pd_indexedSpot,
                    pd_c_date}, pd_isPaidFor + "=" + Id + " AND user_id = " + UserId, null, null, null, null);
            int Size = cursor.getCount();

            cursor.moveToFirst();

            if (!cursor.isAfterLast()) {

                do {
                    String sId = cursor.getString(0);
                    String isPaidFor = cursor.getString(1);
                    String PackageId  = cursor.getString(2);
                    String Amount = cursor.getString(3);
                    String IndexedSpotId= cursor.getString(4);
                    String Date = cursor.getString(5);
                    mPaymentDetails = new PaymentDetails();

                    mPaymentDetails.setId(sId);
                    mPaymentDetails.setIsPaidFor(isPaidFor);
                    mPaymentDetails.setPackageId(PackageId);
                    mPaymentDetails.setAmount(Amount);
                    mPaymentDetails.setIndexedSpot(IndexedSpotId);
                    mPaymentDetails.setDate(Date);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
        }
        return mPaymentDetails;
    }




    public void addNewPaymentDetails(PaymentDetails paymentDetails) {
        ContentValues value = new ContentValues();
        value.put(pd_Id, paymentDetails.getId());
        value.put(pd_indexedSpot, paymentDetails.getIndexedSpot());
        value.put(pd_amount, paymentDetails.getAmount());
        value.put(pd_isPaidFor, paymentDetails.getIsPaidFor());
        value.put(pd_packageId, paymentDetails.getPackageId());
        value.put(pd_c_date, paymentDetails.getDate());
        value.put(pd_userId, paymentDetails.getUserId());
        value.put(pd_isCancelled,paymentDetails.getIsCancelled());
        value.put(pd_refund_amount,paymentDetails.getRefundAmount());
        try {
            db.insert(MSTR_PAYMENT_DETAILS, null, value);
        } catch (Exception ex) {
        }
    }

    public void updatePaymentDetails(PaymentDetails paymentDetails) {
        ContentValues value = new ContentValues();
        if (paymentDetails.getIndexedSpot() != null && !paymentDetails.getIndexedSpot().isEmpty()) {
            value.put(pd_indexedSpot, paymentDetails.getIndexedSpot());
        }
        if (paymentDetails.getAmount() != null && !paymentDetails.getAmount().isEmpty()) {
            value.put(pd_amount, paymentDetails.getAmount());
        }
        if (paymentDetails.getIsPaidFor() != null && !paymentDetails.getIsPaidFor().isEmpty()) {
            value.put(pd_isPaidFor, paymentDetails.getIsPaidFor());
        }
        if (paymentDetails.getPackageId() != null && !paymentDetails.getPackageId().isEmpty()) {
            value.put(pd_packageId, paymentDetails.getPackageId());
        }
        if(paymentDetails.getIsCancelled() != null && !paymentDetails.getIsCancelled().isEmpty()){
            value.put(pd_isCancelled,paymentDetails.getIsCancelled());
        }
        if(paymentDetails.getRefundAmount()!= null && !paymentDetails.getRefundAmount().isEmpty()){
            value.put(pd_refund_amount,paymentDetails.getRefundAmount());
        }
        try {
            db.update(MSTR_PAYMENT_DETAILS, value, s_Id + "=" + paymentDetails.getId(), null);
        } catch (Exception e) {
        }
    }


    public void deletePaymentDetails(String Id) {
        try {
            db.delete(MSTR_PAYMENT_DETAILS, pd_Id + "=" + Id, null);
        } catch (Exception e) {
        }
    }

    // Messaeges

    public void addMessage(String Messages,String UserId){
        ContentValues value = new ContentValues();
        value.put(m_message,Messages);
        value.put("user_id",UserId);
        try {
            db.insert(MSTR_MESSAGES, null, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<String> getMessages(String UserId) {
        ArrayList<String> dataArrays = new ArrayList<>();
        Cursor cursor = null;
        boolean isFound = false;
        try {
            cursor = db.query(MSTR_MESSAGES, new String[]{m_message}, "user_id="  + UserId, null, null, null, null);
            cursor.moveToFirst();
            int stop = 0;
            if (!cursor.isAfterLast()) {
                int i = 0;
                do {
                    dataArrays.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
        }
        return dataArrays;

    }
}