package com.upark;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.upark.db.DBHelper;
import com.upark.db.models.Packages;
import com.upark.db.models.ParkingSpots;
import com.upark.db.models.PaymentDetails;
import com.upark.db.models.Reservation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReservationModification extends ActionBarActivity {


    // spinner components
    Spinner spSpots;
    Spinner spPackages;

    //Arraylist for spots & packages
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> itemsPackages  = new ArrayList<>();

    //TextView for Amount
    TextView tvAmount;

    //Button For Save Data
    Button btnDone;


    boolean isEdit = false;

    String setSpotId;
    String setPackageId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_modification);

        // Declaration for used components
        spSpots = (Spinner) findViewById(R.id.spSpots);
        spPackages = (Spinner) findViewById(R.id.spPackages);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        btnDone = (Button) findViewById(R.id.btnSaveReservationModification);

        //fill data to spinner components


        //get Extras from previous activity
        Bundle bundle = getIntent().getExtras();
        final Reservation reservation = new Reservation();
        final PaymentDetails pyDetails = new PaymentDetails();


        //check if bundle data is not null
        if(bundle!= null) {
            //set data to Model classes
            isEdit = bundle.getBoolean("isEdit");
            reservation.setId(bundle.getString("ReId"));
            reservation.setUserId(bundle.getString("userId"));
            reservation.setSpotId(bundle.getString("spotId"));
            reservation.setIsPaid(bundle.getString("isPaidFor"));
            reservation.setDate(bundle.getString("Date"));

            pyDetails.setAmount(bundle.getString("pyAmount"));
            pyDetails.setPackageId(bundle.getString("pyPackageId"));
            pyDetails.setId(bundle.getString("pyDetails"));
            pyDetails.setIndexedSpot(bundle.getString("py"));

        }
        setData(reservation.getSpotId(),pyDetails.getPackageId());
        tvAmount.setText(pyDetails.getAmount());

        // set On Item Clicking action for Packages
        spPackages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DBHelper helper = new DBHelper(ReservationModification.this);
                helper.openDataBase();
                setPackageId = helper.getPackageIdByName(itemsPackages.get(position));
                tvAmount.setText(helper.getPackageAmountById(setPackageId));
                pyDetails.setAmount(tvAmount.getText().toString());
                pyDetails.setPackageId(setPackageId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // set On Item Clicking action for Spots
        spSpots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DBHelper helper = new DBHelper(ReservationModification.this);
                helper.openDataBase();
                setSpotId = helper.getSpotId(items.get(position));
                reservation.setVSpotName(items.get(position));
                reservation.setSpotId(setSpotId);
                pyDetails.setIndexedSpot(setSpotId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //save data on done
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData(reservation,pyDetails);
            }
        });
    }

    //update reservation and payment details data to database
    private void UpdateData(Reservation reservation,PaymentDetails pyDetails){
        if(reservation != null) {
            DBHelper helper = new DBHelper(this);
            helper.openDataBase();
            helper.updateReservationData(reservation);
            helper.updatePaymentDetails(pyDetails);
            helper.openDataBase();
            helper.addMessage("Reservation Modification Successfull!! For \n"+reservation.getVSpotName(),reservation.getUserId());
            finish();
        }
    }

    // get current date
    public static String getCurrentDate(){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy h:mm a");
        String the_date = format.format(calendar.getTime());
        return the_date;
    }

    // set whole data to spinner components
    public void setData(String SpotId,String PackageId) {
        DBHelper helper = new DBHelper(this);
        helper.openDataBase();

        ArrayList<ParkingSpots> spots = helper.getAllSpots();

        int spSelectedSpotIndex = 0 ;
        for(int i = 0 ; i < spots.size();i++){
            items.add(spots.get(i).getSpotName());
            if(spots.get(i).getId().contains(SpotId)){
                spSelectedSpotIndex = i;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.list_row_item, items);
        spSpots.setAdapter(adapter);
        spSpots.setSelection(spSelectedSpotIndex);

        int spSelectedPackageIndex = 0;
        ArrayList<Packages> packages = helper.getAllPackages();
        for(int i = 0 ; i < packages.size();i++){
            itemsPackages.add(packages.get(i).getPackageName());
            if(packages.get(i).getId().contains(PackageId)){
                spSelectedPackageIndex = i;
            }
        }

        ArrayAdapter<String> adapters = new ArrayAdapter<String>(
                getApplicationContext(),R.layout.list_row_item, itemsPackages);
        spPackages.setAdapter(adapters);
        spPackages.setSelection(spSelectedPackageIndex);
    }

    // end of code
}
