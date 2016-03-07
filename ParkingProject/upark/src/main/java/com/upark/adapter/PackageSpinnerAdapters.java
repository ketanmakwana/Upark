package com.upark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.upark.R;
import com.upark.db.models.Packages;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PackageSpinnerAdapters extends BaseAdapter {
    ArrayList<Packages> packagesArrayList;
    Context mContext;
    int resourceId;

    public PackageSpinnerAdapters(Context mContext,int resourceId,ArrayList<Packages> packageArrayList){
        this.packagesArrayList = packageArrayList;
        this.mContext = mContext;
        this.resourceId = resourceId;

    }
    @Override
    public int getCount() {
        return packagesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return packagesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view =(LinearLayout) inflater.inflate(resourceId , parent,false);
        TextView txtSpotsName =(TextView) view.findViewById(R.id.txtSpotNameSPItem);
        txtSpotsName.setText(packagesArrayList.get(position).getPackageName());

        return view;
    }
}
