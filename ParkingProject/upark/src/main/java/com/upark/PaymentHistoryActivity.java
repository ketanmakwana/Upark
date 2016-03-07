package com.upark;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.upark.adapter.PaymentDetailsHistoryAdapter;
import com.upark.db.DBHelper;
import com.upark.db.models.PaymentDetails;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class PaymentHistoryActivity extends ActionBarActivity {

	ArrayList<PaymentDetails> arrayPaymentDetails;
	ListView lstView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_history);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//getSupportActionBar().setSubtitle(getString(R.string.payment_history));
		lstView = (ListView) findViewById(R.id.lstPaymentHistory);
		lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
		setData();
	}
	public void setData(){
		DBHelper helper = new DBHelper(this);
		helper.openDataBase();
		arrayPaymentDetails  = helper.getAllPaymentDetails();

		if(arrayPaymentDetails != null ){
			PaymentDetailsHistoryAdapter adapter = new PaymentDetailsHistoryAdapter(this,R.layout.payment_details_history_list_item,arrayPaymentDetails);
			lstView.setAdapter(adapter);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.homeAsUp) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}