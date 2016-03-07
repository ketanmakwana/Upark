package com.upark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.upark.R;
import com.upark.db.DBHelper;
import com.upark.db.models.PaymentDetails;

import java.util.ArrayList;

public class PaymentDetailsHistoryAdapter extends BaseAdapter {
    ArrayList<PaymentDetails> paymentDetailsArrayList;
    Context mContext;
    int resourceId;


    public PaymentDetailsHistoryAdapter(Context mContext, int resourceId, ArrayList<PaymentDetails> paymentDetailsArrayList) {
        this.paymentDetailsArrayList = paymentDetailsArrayList;
        this.mContext = mContext;
        this.resourceId = resourceId;
    }

    @Override
    public int getCount() {
        return paymentDetailsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return paymentDetailsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.payment_details_history_list_item, parent, false);
        TextView txtPaidFor = (TextView) view.findViewById(R.id.txtPaidFor);
        TextView txtAmount = (TextView) view.findViewById(R.id.txtAmount);
        TextView txtPDDate = (TextView) view.findViewById(R.id.txtPDDate);

        DBHelper dbHelper = new DBHelper(mContext);
        dbHelper.openDataBase();
        if (paymentDetailsArrayList != null && paymentDetailsArrayList.size() > 0) {
            txtPaidFor.setText("Reserved "+dbHelper.getSpotNameById(paymentDetailsArrayList.get(position).getIndexedSpot()));
            txtAmount.setText("Amount Paid : "+paymentDetailsArrayList.get(position).getAmount());
            txtPDDate.setText("Reserved On : "+paymentDetailsArrayList.get(position).getDate());
        }
        return view;
    }
}
