package com.upark;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;

import com.upark.Net.InternetConnection;
import com.upark.adapter.ParkingSpotsAdapter;
import com.upark.db.DBHelper;
import com.upark.db.models.ParkingSpots;
import com.upark.db.models.Reservation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

// Class for display spots and add new spots

public class Spots extends ActionBarActivity {

    protected final String TAG = "Spot";
    GridView gridView;

    boolean isEdit = false;
    AlertDialog altDialog;

    ProgressDialog progressDialog;
    ArrayList<ParkingSpots> spotsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spots);

        gridView = (GridView) findViewById(R.id.gvSpotNames);
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please wait while processing...");

        //setData();
        if (isConnectingToInternet(this)) {
            new SpotData().execute(new String[]{InternetConnection.Spots + getString(R.string.apikey)});
        } else {
            showMessage("Internet Connection", "Please check your internet connection!");
        }
        Date date = new Date();
        String dateFormat = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        final String todayDate = simpleDateFormat.format(date);
        ParkingSpots parkingSpots = new ParkingSpots();


    }

    // save data to database
    public void saveData(ParkingSpots spots) {
        DBHelper dbHelper = new DBHelper(Spots.this);
        dbHelper.openDataBase();
        if (dbHelper.checkSpotName(spots.getSpotName()) == false) {
            if (!isEdit) {
                dbHelper.addNewSpot(spots);
                viewMessage("Data Added!", "Data Saved Successfully!");
            } else {
                dbHelper.updateSpots(spots);
                viewMessage("Data Updated", "Data Updated Successfully!");
                isEdit = false;
            }
        } else {
            viewMessage("Error!", "Duplication Name Not Allowed!");
        }
    }

    protected void getData(String urls) {
        spotsArrayList = new ArrayList<>();

        int TimeOut = 25000;
        HttpURLConnection c = null;
        String JSON = null;

        try {

            URL u = new URL(urls);
            c = (HttpURLConnection) u.openConnection();
            c.setReadTimeout(30000);
            c.setConnectTimeout(30000);
            c.setRequestMethod("POST");
            c.setDoInput(true);
            c.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder();
            builder.appendQueryParameter("accesskey", getString(R.string.apikey));


            String query = builder.build().getEncodedQuery();
            OutputStream os = c.getOutputStream();

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            c.connect();

            disableConnectionReuseIfNecessary();
            int status = c.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    JSON = sb.toString();
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                            null, ex);
                }
            }
        }

        try {
            Log.i(TAG, JSON);
            JSONObject jObject = new JSONObject(JSON);
            JSONArray itemArray = jObject.getJSONArray("data");
            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject jMainObject = itemArray.getJSONObject(i).getJSONObject("spot_data");
                ParkingSpots parkingSpots = new ParkingSpots();
                parkingSpots.setId(jMainObject.getString("id"));
                parkingSpots.setSpotName(jMainObject.getString("spot_name"));
                spotsArrayList.add(parkingSpots);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error!", e.toString());
        }
    }

    public void disableConnectionReuseIfNecessary() {
        // Work around pre-Froyo bugs in HTTP connection reuse.
        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    public boolean isConnectingToInternet(Context c) {
        ConnectivityManager connectivity = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    private void showMessage(String title, String messsage) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(messsage);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    class SpotData extends AsyncTask<String, Void, Void> {

        public SpotData() {
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            getData(params[0]);
            progressDialog.show();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (spotsArrayList.size() > 0 && spotsArrayList != null) {
                ParkingSpotsAdapter adapter = new ParkingSpotsAdapter(Spots.this, R.layout.spots_list_item, spotsArrayList);
                gridView.setAdapter(adapter);
            }

        }
    }

    //// delete spot data from database
//    public void deleteData(String id) {
//        DBHelper dbHelper = new DBHelper(Spots.this);
//        dbHelper.openDataBase();
//        dbHelper.deleteSpotsData(id);
//        setData();
//    }
// view Alert Messages
    public void viewMessage(String Title, String Message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(Title);
        dialog.setMessage(Message);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
//    ArrayList<ParkingSpots> spots;
//// set data from database to list
//    public void setData() {
//        DBHelper dbHelper = new DBHelper(this);
//        dbHelper.openDataBase();
//        spots = dbHelper.getAllSpots();
//        if (spots != null) {
//            ParkingSpotsAdapter adapter = new ParkingSpotsAdapter(Spots.this, R.layout.spots_list_item, spots);
//            gridView.setAdapter(adapter);
//        }
//    }
}
