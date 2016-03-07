package com.upark;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.upark.adapter.PaymentDetailsHistoryAdapter;
import com.upark.db.DBHelper;
import com.upark.db.models.Packages;
import com.upark.db.models.PaymentDetails;
import com.upark.db.models.Reservation;
import com.upark.model.LoggedInUsers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.DialogInterface.BUTTON_POSITIVE;

public class PaymentConfirmation extends ActionBarActivity {


    Spinner spPackages;
    TextView txtDate;
    TextView txtTime;
    TextView txtAmount;

    Button btnPaynow;

    ArrayList<Packages> packagesArrayList;
    String SpotName = "";
    String UserId;

    String Dates = null;
    String Time = null;
    String CurrDate = null;
    String setPackageId;
    String strPackageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);


        spPackages = (Spinner) findViewById(R.id.spPackageName);
        txtAmount = (TextView) findViewById(R.id.txtAmounts);
        txtDate = (TextView ) findViewById(R.id.txtDates);
        txtTime = (TextView) findViewById(R.id.txtTimes);
        btnPaynow = (Button) findViewById(R.id.btnPaynow);

        DBHelper dbHelper = new DBHelper(this);
        dbHelper.openDataBase();

        UserId = dbHelper.getUserId(LoggedInUsers.getUserName());

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            SpotName = bundle.getString("SpotName");
        }

        DBHelper helper = new DBHelper(this);
        helper.openDataBase();
        packagesArrayList = helper.getAllPackages();
        final ArrayList<String> packages= new ArrayList<>();

        for( int i = 0 ; i < packagesArrayList.size(); i++){
            packages.add(packagesArrayList.get(i).getPackageName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.list_row_item, packages);
        spPackages.setAdapter(adapter);

        spPackages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DBHelper helper = new DBHelper(PaymentConfirmation.this);
                helper.openDataBase();
                strPackageName = packages.get(position);
                setPackageId = helper.getPackageIdByName(packages.get(position));
                txtAmount.setText(helper.getPackageAmountById(setPackageId));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        btnPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayNow();
            }
        });
    }

    private void PayNow(){
        DBHelper dbHelper = new DBHelper(PaymentConfirmation.this);
        dbHelper.openDataBase();
        String SpotId = dbHelper.getSpotId(SpotName);
        dbHelper.openDataBase();
        // Check spot is regestered or not
        if (!dbHelper.isSpotRegistered(SpotId, UserId)) {
            makePayment(SpotId);
        } else {
            // alert for spot already reserved
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(PaymentConfirmation.this);
            alertDialog.setTitle("Error!");
            alertDialog.setMessage("Spot Already Reserved!");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
        }
    }
    public void makePayment(final String SpotId) {

        //   if (Dates != null && !Dates.equals("") && Time != null && !Time.equals("")) {

                DBHelper dbHelper = new DBHelper(PaymentConfirmation.this);
                dbHelper.openDataBase();
                // Check spot is regestered or not
                if (!dbHelper.isSpotRegistered(SpotId, UserId)) {
                        /*do reservation entery on db*/
                    dbHelper.openDataBase();
                    Reservation reservation = new Reservation();
                    reservation.setIsPaid("1");
                    reservation.setSpotId(SpotId);
                    reservation.setDate(txtDate.getText().toString() + " " + CurrDate);
                    Log.e("Dates", "" + Dates + " " + CurrDate);
                    reservation.setTime(getCurrentDate());
                    reservation.setUserId(UserId);
                    reservation.setIsCancelled("0");
                    dbHelper.addNewReservation(reservation);

                    String ReservationId = dbHelper.getReservationId();
                    String Amount = txtAmount.getText().toString();


    				/*do paymentdetails entery on db*/
                    dbHelper.openDataBase();
                    PaymentDetails paymentDetails = new PaymentDetails();
                    paymentDetails.setIndexedSpot(SpotId);
                    paymentDetails.setAmount(Amount);
                    paymentDetails.setPackageId(setPackageId);
                    paymentDetails.setIsPaidFor(ReservationId);
                    paymentDetails.setIsCancelled("0");
                    paymentDetails.setRefundAmount("0");
                    paymentDetails.setDate(getCurrentDate());
                    paymentDetails.setUserId(UserId);

                    dbHelper.addNewPaymentDetails(paymentDetails);

                    dbHelper.openDataBase();
                    dbHelper.addMessage("Reservation Successfull! For\n" + SpotName, UserId);
                    Intent intent = new Intent(PaymentConfirmation.this, Payment.class);
                    intent.putExtra("PackageName", "Car_Spot_" + SpotId + "_For_Package_" + strPackageName);
                    intent.putExtra("Amount", Amount);
                    startActivity(intent);
                    finish();
                    CurrDate = "";
                    Dates = "";
                } else {
                    // alert for spot already reserved
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PaymentConfirmation.this);
                    alertDialog.setTitle("Error!");
                    alertDialog.setMessage("Spot Already Reserved!");
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }

    }

    public void showTimePicker() {
        TimePickerDialog mTimePicker;

        Calendar mCurrentTime = Calendar.getInstance();
        final int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(PaymentConfirmation.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                updateTime(hourOfDay, minute);
                txtTime.setText(CurrDate);
            }
        }, hour, minute, DateFormat.is24HourFormat(PaymentConfirmation.this));
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    public void showDatePickerDialog() {
        DatePickerDialog mDatePicker;
        Calendar mCurrentDate = Calendar.getInstance();
        int mYear = mCurrentDate.get(Calendar.YEAR);
        int mMonth = mCurrentDate.get(Calendar.MONTH);
        int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

        mDatePicker = new DatePickerDialog(PaymentConfirmation.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;
                txtDate.setText(monthOfYear + "/" + dayOfMonth + "/" + year);
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.setCanceledOnTouchOutside(false);
        mDatePicker.show();
    }

    //get system current date
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy h:mm a");
        String the_date = format.format(calendar.getTime());
        return the_date;
    }


    private void updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        CurrDate = aTime;
    }

    public class GetPackagesFromWeb extends AsyncTask<String,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
