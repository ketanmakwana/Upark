package com.upark.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.upark.R;
import com.upark.db.models.ParkingSpots;

import java.util.ArrayList;

public class ParkingSpotsAdapter extends BaseAdapter{

    ArrayList<ParkingSpots> items;
    Context mContext;
    int position;
    int resourceId;

    public ParkingSpotsAdapter(Context context,int resource,ArrayList<ParkingSpots> spots){
        this.mContext = context;
        this.resourceId = resource;
        this.items = spots;
    }
    @Override
    public int getCount() {
        int size =0 ;
        try{
            size = items.size();
        }catch (NullPointerException e){
            size = 0;
        }
        return size;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        view = (RelativeLayout) inflater.inflate(resourceId,parent,false);

        TextView txtSpotName = (TextView) view.findViewById(R.id.txtSpotNameItem);
        //TextView txtIsVisible = (TextView) view.findViewById(R.id.txtIsVisibleItem);

        if(items.size() > 0 && items != null){
            txtSpotName.setText(items.get(position).getSpotName());
          //  String isVisible = items.get(position).getIsVisible();
        }

        return view;
    }
}
