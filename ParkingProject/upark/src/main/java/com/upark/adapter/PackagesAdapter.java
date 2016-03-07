package com.upark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.upark.R;
import com.upark.db.models.ParkingSpots;
import com.upark.model.PackagesModel;

import java.util.ArrayList;

/**
 * Created by Ketan on 07/03/2016.
 */
public class PackagesAdapter extends BaseAdapter {


    ArrayList<PackagesModel> arrayParkingSpots;
    int resourceId;
    Context mContext;

    public PackagesAdapter(Context mContext, int resourceId, ArrayList<PackagesModel> arrayParkingSpots) {
        this.mContext = mContext;
        this.resourceId = resourceId;
        this.arrayParkingSpots = arrayParkingSpots;
    }

    @Override
    public int getCount() {
        return arrayParkingSpots.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayParkingSpots.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(resourceId, parent, false);

        TextView txtSpotsName = (TextView) view.findViewById(R.id.txtSpotNameSPItem);
        txtSpotsName.setText(arrayParkingSpots.get(position).getName());
        return view;
    }
}
