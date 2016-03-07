package com.upark.adapter;

import com.upark.Payment;
import com.upark.PaymentConfirmation;
import com.upark.R;
import com.upark.db.DBHelper;
import com.upark.db.models.Packages;
import com.upark.db.models.ParkingSpots;
import com.upark.db.models.PaymentDetails;
import com.upark.db.models.Reservation;
import com.upark.db.models.UserLogin;
import com.upark.model.LoggedInUsers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.DatePickerDialog.*;


public class CarParkAdapter extends BaseAdapter {


    Activity context;
    ArrayList<ParkingSpots> slots;
    String UserId;

    @SuppressLint("InflateParams")
    public CarParkAdapter(Activity context, ArrayList<ParkingSpots> slots) {

        this.context = context;
        this.slots = slots;
        DBHelper dbHelper = new DBHelper(context);
        dbHelper.openDataBase();
        UserId = dbHelper.getUserId(LoggedInUsers.getUserName());
    }

    public int getCount() {

        return slots.size();
    }

    @Override
    public Object getItem(int position) {
        return slots.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.item_car_park_slot, parent, false);

        final int position = pos;
        final String SpotName = slots.get(position).getSpotName();
        ((TextView) rowView.findViewById(R.id.title)).setText(SpotName);

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PaymentConfirmation.class);
                intent.putExtra("SpotName",slots.get(position).getSpotName());
//                intent.putExtra("user_id",)
                context.startActivity(intent);
            }
        });
        return rowView;
    }

//    public GetSpotsFromWeb extends AsyncTask<String,Void,Void>{
//
//    }



}