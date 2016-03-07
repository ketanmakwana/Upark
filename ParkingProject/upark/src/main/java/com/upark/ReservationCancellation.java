package com.upark;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.upark.db.DBHelper;
import com.upark.db.models.PaymentDetails;
import com.upark.db.models.Reservation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReservationCancellation extends ActionBarActivity {

    TextView txtPaidAmount;
    TextView txtCancellationAmount;
    TextView txtRefundAmount;
    Button btnConfirm;
    final Reservation reservation  = new Reservation();
    final PaymentDetails pyDetails = new PaymentDetails();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_cancellation);
        txtPaidAmount = (TextView) findViewById(R.id.txtPaidFees);
        txtCancellationAmount = (TextView) findViewById(R.id.txtCancellationAmount);
        txtRefundAmount = (TextView) findViewById(R.id.txtRefundAmount);

        btnConfirm = (Button) findViewById(R.id.btnProceedCancellation);


        Bundle bundle = getIntent().getExtras();


         if(bundle != null){
             String RId = bundle.getString("ReId");
             String UserId =bundle.getString("userId");
             String SpotId  = bundle.getString("spotId");
             String isPaid = bundle.getString("isPaidFor");
             String date = bundle.getString("Date");
             String Amount = bundle.getString("pyAmount");
             String PackageId = bundle.getString("pyPackageId");
             String PDId = bundle.getString("pyDetails");
             String IndexedSpot = bundle.getString("py");
             reservation.setId(RId);
             reservation.setUserId(UserId);
             reservation.setSpotId(SpotId);
             reservation.setDate(date);

             pyDetails.setAmount(Amount);
             pyDetails.setPackageId(PackageId);
             pyDetails.setId(PDId);
             pyDetails.setIndexedSpot(IndexedSpot);
         }

         if(isLate(reservation.getDate())){
             txtPaidAmount.setText(pyDetails.getAmount());
             txtCancellationAmount.setText("$1");
             txtRefundAmount.setText(getRefundAmount(pyDetails.getAmount(),txtCancellationAmount.getText().toString()));
         }else{
             txtPaidAmount.setText(pyDetails.getAmount());
             txtCancellationAmount.setText("$0");
             txtRefundAmount.setText(getRefundAmount(pyDetails.getAmount(),txtCancellationAmount.getText().toString()));

         }
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pyDetails.setRefundAmount(txtRefundAmount.getText().toString());
                pyDetails.setIsCancelled("1");
                reservation.setIsCancelled("1");
                deleteData(reservation.getId());
            }
        });
    }
    private String getRefundAmount(String PaidAmount,String CancellationFees){
        double amount = 0;
        try{
            amount = Double.parseDouble(PaidAmount.replace("$","")) - Double.parseDouble(CancellationFees.replace("$",""));
        }catch(NumberFormatException e){
            amount = 0;
        }
        return  "$"+amount+"";
    }
    public void deleteData(String Id) {
        DBHelper helper = new DBHelper(this);
        helper.openDataBase();
        helper.updateReservationData(reservation);
        helper.openDataBase();
        helper.updatePaymentDetails(pyDetails);
        helper.openDataBase();
        helper.addMessage("Reservation Cancelled!!",reservation.getUserId());
        finish();
    }
    // get current date
    public static String getCurrentDate(){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy h:mm a");
        String the_date = format.format(calendar.getTime());
        return the_date;
    }

    private boolean isLate(String compareToDate){

        Calendar c = Calendar.getInstance();

        Date currentTime2;
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy h:mm a");
        try {
            long t = c.getTimeInMillis();
            currentTime2 = c.getTime();

            Date timeCompare = sdf.parse(compareToDate);
            long diff = currentTime2.getTime() - timeCompare.getTime();
            long diffTime = diff / (60 * 1000) % 60;

            if (diffTime > 15)
            {   // after 15min
                return true;
            } else {
               // before 15min
                return false;
            }
        } catch (Exception e) {
             e.printStackTrace();
        }
        return false;
    }
}
