package com.upark;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.upark.db.DBHelper;

import java.io.IOException;

public class Splash extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler mHandler = new Handler();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DBHelper helper = new DBHelper(Splash.this);

                if (helper.checkDataBase() == false) {
                    try {
                        //call custom copy function from DBHelper Class
                        // for copying database
                        helper.copyDataBase();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Intent intent = new Intent(Splash.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);


    }

}
