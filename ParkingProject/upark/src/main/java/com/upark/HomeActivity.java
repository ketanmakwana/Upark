package com.upark;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends Activity {


    boolean isAdmin = false;
 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

		//check if user is admin or not
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            isAdmin =  bundle.getBoolean("user");
        }
	}
	
	public void makeReservation (View v){

        startActivity(new Intent(this, ReservationActivity.class).putExtra("user",isAdmin));

	}
	
	public void openMessages (View v){
		startActivity(new Intent(this, MessageActivity.class).putExtra("user",isAdmin));
	}
	
	public void openPaymentHistory (View v){
		startActivity(new Intent(this, PaymentHistoryActivity.class).putExtra("user",isAdmin));
	}
	
	public void logOut (View v){
        Context mContext = getApplicationContext();
        SharedPreferences preferences = mContext.getSharedPreferences(getString(R.string.prefs_user_login), 0);
        SharedPreferences.Editor editor = preferences.edit().clear();
        editor.remove(getString(R.string.prefs_user_login));
        editor.commit();

        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();

	}
	

	
    @Override
    protected void onResume() {
        super.onResume();

    }
 
    @Override
    public void onPause() {
        super.onPause();
    }
 
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
