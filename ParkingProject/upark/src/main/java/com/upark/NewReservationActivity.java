package com.upark;

import com.upark.adapter.CarParkAdapter;
import com.upark.db.DBHelper;
import com.upark.db.models.ParkingSpots;
import com.upark.model.LoggedInUsers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class NewReservationActivity extends ActionBarActivity {

    GridView list;

    boolean isAdmin;

    boolean isEdit = false;

// Make reservation with gridview spot selection
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reservation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(getString(R.string.add_reservation));

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            isAdmin = bundle.getBoolean("user");
        }

        list = (GridView) findViewById(R.id.list);
        DBHelper helper = new DBHelper(this);
        helper.openDataBase();
        ArrayList<ParkingSpots> spots = helper.getAllSpots();
        list.setAdapter(new CarParkAdapter(this, spots));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_spot_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.homeAsUp:
                Intent homeIntent = new Intent(this, HomeActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                break;
        }
        if (id == R.id.homeAsUp) {

            finish();
            return true;
        }
        if (id == R.id.add_new_spot) {
            Intent intent
                    = new Intent(NewReservationActivity.this, Spots.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}