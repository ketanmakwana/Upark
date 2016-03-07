package com.upark.adapter;

import java.util.ArrayList;

import com.upark.NewReservationActivity;
import com.upark.R;
import com.upark.ReservationActivity;
import com.upark.ReservationCancellation;
import com.upark.ReservationModification;
import com.upark.db.DBHelper;
import com.upark.db.models.PaymentDetails;
import com.upark.db.models.Reservation;
import com.upark.db.models.UserLogin;
import com.upark.model.LoggedInUsers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class ReservationsAdapter extends BaseAdapter {

    private final Context context;
    ArrayList<Reservation> reservations;
    PaymentDetails pyDetailsList;

    @SuppressLint("InflateParams")
    public ReservationsAdapter(Context context, ArrayList<Reservation> reservations) {
        this.context = context;
        this.reservations = reservations;
    }

    public int getCount() {
        return reservations.size();
    }

    @Override
    public Object getItem(int position) {
        return reservations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.item_reservation, parent, false);

        String TitleText = "Reserved " + reservations.get(position).getVSpotName()+ "\n" + reservations.get(position).getReservationDate();
        ((TextView) rowView.findViewById(R.id.title)).setText(TitleText);
        ImageView imgEdit = (ImageView) rowView.findViewById(R.id.edit);
        ImageView imgDelete = (ImageView) rowView.findViewById(R.id.delete);

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(context, ReservationModification.class);
                    intent.putExtra("isEdit", true);
                    intent.putExtra("ReId", reservations.get(position).getId());
                    intent.putExtra("Time", reservations.get(position).getTime());
                    intent.putExtra("isPaidFor", reservations.get(position).getIsPaid());
                    intent.putExtra("Date", reservations.get(position).getDate());
                    intent.putExtra("spotId", reservations.get(position).getSpotId());
                    intent.putExtra("userId", reservations.get(position).getUserId());
                    intent.putExtra("pyAmount", pyDetailsList.getAmount());
                    intent.putExtra("pyDetails", pyDetailsList.getId());
                    intent.putExtra("pyPackageId", pyDetailsList.getPackageId());
                    intent.putExtra("py", pyDetailsList.getIndexedSpot());
                    context.startActivity(intent);

            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReservationCancellation.class);
                intent.putExtra("isEdit", false);
                intent.putExtra("ReId", reservations.get(position).getId());
                intent.putExtra("Time", reservations.get(position).getTime());
                intent.putExtra("isPaidFor", reservations.get(position).getIsPaid());
                intent.putExtra("Date", reservations.get(position).getDate());
                intent.putExtra("spotId", reservations.get(position).getSpotId());
                intent.putExtra("userId", reservations.get(position).getUserId());
                intent.putExtra("pyAmount", pyDetailsList.getAmount());
                intent.putExtra("pyDetails", pyDetailsList.getId());
                intent.putExtra("pyPackageId", pyDetailsList.getPackageId());
                intent.putExtra("py", pyDetailsList.getIndexedSpot());
                context.startActivity(intent);
            }
        });



        return rowView;
    }


}