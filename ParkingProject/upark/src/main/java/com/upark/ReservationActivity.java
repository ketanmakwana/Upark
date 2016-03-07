package com.upark;

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
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.upark.Net.InternetConnection;
import com.upark.adapter.ReservationsAdapter;
import com.upark.db.DBHelper;
import com.upark.db.models.Reservation;
import com.upark.model.LoggedInUsers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("deprecation")

public class ReservationActivity extends ActionBarActivity {

    private static final String TAG = "Reservation";

    private static final int ACTION_ADD_RESERVATION = 1;
    ReservationsAdapter adapter;
    ArrayList<Reservation> reservationArrayList;
    ListView list;
    ProgressDialog progressDialog;
    Reservation reservation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        list = (ListView) findViewById(R.id.lstReservation);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please while processing...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        if (isConnectingToInternet(this)) {
            reservation = new Reservation();

            reservation.setUserId(LoggedInUsers.getUserId());
            new ReservationData().execute(new String[]{InternetConnection.RegisteredSpots, "List"});
        } else {
            showMessage("Internet Connection!", "Please check your internet connection!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reservation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.add_new_reservation) {
//            startActivityForResult(new Intent(this, NewReservationActivity.class), ACTION_ADD_RESERVATION);
          Intent intent = new Intent(ReservationActivity.this,Spots.class);
            return true;
        }
        if (id == R.id.homeAsUp) {
            adapter.notifyDataSetChanged();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void getData(String urls, String type) {
        reservationArrayList = new ArrayList<>();

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
            builder.appendQueryParameter("type", type);
            builder.appendQueryParameter("user_id", LoggedInUsers.getUserId());

            if (type.equalsIgnoreCase("Add")) {

                builder.appendQueryParameter("spot_id", reservation.getSpotId());
                builder.appendQueryParameter("reservation_date", reservation.getDate());
                builder.appendQueryParameter("is_paid", reservation.getIsPaid());
                builder.appendQueryParameter("is_cancelled", reservation.getIsCancelled());
                builder.appendQueryParameter("paid_amount", reservation.getPaidAmount());
                builder.appendQueryParameter("paid_return", reservation.getPaidReturnAmount());
                builder.appendQueryParameter("amount_fine_obtained", reservation.getAmountFineObtain());
                builder.appendQueryParameter("entry_time", reservation.getDTReservationIN());
                builder.appendQueryParameter("exit_time", reservation.getDTReservationOUT());

            } else if (type.equalsIgnoreCase("Edit")) {

                builder.appendQueryParameter("spot_id", reservation.getSpotId());
                builder.appendQueryParameter("reservation_date", reservation.getDate());
                builder.appendQueryParameter("is_paid", reservation.getIsPaid());
                builder.appendQueryParameter("is_cancelled", reservation.getIsCancelled());
                builder.appendQueryParameter("paid_amount", reservation.getPaidAmount());
                builder.appendQueryParameter("paid_return", reservation.getPaidReturnAmount());
                builder.appendQueryParameter("amount_fine_obtained", reservation.getAmountFineObtain());
                builder.appendQueryParameter("id", reservation.getId());
                builder.appendQueryParameter("entry_time", reservation.getDTReservationIN());
                builder.appendQueryParameter("exit_time", reservation.getDTReservationOUT());
            }

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
                JSONObject jMainObject = itemArray.getJSONObject(i).getJSONObject("reservation");
                Reservation reservation1 = new Reservation();
                reservation1.setId(jMainObject.getString("id"));
                reservation1.setUserId(jMainObject.getString("user_id"));
                reservation1.setSpotId(jMainObject.getString("spot_id"));
                reservation1.setIsPaid(jMainObject.getString("is_paid"));
                reservation1.setIsCancelled(jMainObject.getString("is_cancelled"));
                reservation1.setPaidAmount(jMainObject.getString("paid_amount"));
                reservation1.setPaidReturnAmount(jMainObject.getString("paid_return"));
                reservation1.setReservationDate(jMainObject.getString("reservation_date"));
                reservation1.setDTReservationIN(jMainObject.getString("entry_time"));
                reservation1.setDTReservationOUT(jMainObject.getString("exit_time"));
                reservation1.setAmountFineObtain(jMainObject.getString("amount_fine_obtained"));
                reservation1.setVSpotName(jMainObject.getString("spot_name"));
                reservationArrayList.add(reservation1);
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

    class ReservationData extends AsyncTask<String, Void, Void> {

        public ReservationData() {
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            getData(params[0], params[1]);
            progressDialog.dismiss();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (reservationArrayList != null && reservationArrayList.size() > 0) {
                adapter = new ReservationsAdapter(ReservationActivity.this, reservationArrayList);
                list.setAdapter(adapter);
            }
        }
    }

    public void setData() {

        DBHelper dbHelper = new DBHelper(this);
        dbHelper.openDataBase();
        String UserName = dbHelper.getUserId(LoggedInUsers.getUserName());
        dbHelper.openDataBase();
        adapter = new ReservationsAdapter(this, dbHelper.getReservationView(UserName));
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ACTION_ADD_RESERVATION && resultCode == RESULT_OK) {
            adapter.notifyDataSetChanged();
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}