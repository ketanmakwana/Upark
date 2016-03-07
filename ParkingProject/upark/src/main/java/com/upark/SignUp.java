package com.upark;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.upark.Net.InternetConnection;
import com.upark.db.DBHelper;
import com.upark.db.models.UserLogin;

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
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUp extends ActionBarActivity {

    private final String TAG = "SignUP";
    Button btnDone;
    EditText edtUserName;
    EditText edtEmailId;
    EditText edtMobileNo;
    EditText edtPassword;
    EditText edtAddress;
    boolean status;
    boolean isAlready;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnDone = (Button) findViewById(R.id.btnSignUPDone);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtEmailId = (EditText) findViewById(R.id.edtEmailId);
        edtMobileNo = (EditText) findViewById(R.id.edtMobileNo);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtAddress = (EditText) findViewById(R.id.edtAddress);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please while processing...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty()){
                saveUserDetails();
                doLogin();
                }
            }
        });


    }
    private void doLogin(){
        finish();
    }
    private boolean isEmpty() {
        if (edtUserName.getText().toString().isEmpty() || edtEmailId.getText().toString().isEmpty() || edtMobileNo.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {

            if (edtUserName.getText().toString().isEmpty()) {
                edtUserName.setError("Please Enter UserName!");
                return false;
            }
            if (edtEmailId.getText().toString().isEmpty()) {
                edtEmailId.setError("Please Enter Email Id!");
                return false;
            }
            if (edtMobileNo.getText().toString().isEmpty()) {
                edtMobileNo.setError("Please Enter Mobile No.!");
                return false;
            }
            if (edtPassword.getText().toString().isEmpty()) {
                edtPassword.setError("Please Enter Password");
                return false;
            }

            return false;
        }
        return true;

    }

    private void saveUserDetails() {

        DBHelper helper = new DBHelper(this);
        helper.openDataBase();

        if(!helper.isUserExist(edtUserName.getText().toString(),edtPassword.getText().toString())){


            if(isConnectingToInternet(this)){
                new SignUpDetails().execute(new String[]{InternetConnection.SignUP});
            }else{
                showMessage("Interne Connection!","Please check your internet connection");
            }

            Context mContext = getApplicationContext();

            SharedPreferences sharedPreferences = mContext.getSharedPreferences(getString(R.string.prefs_user_key), 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.prefs_uname), edtUserName.getText().toString());
            editor.putString(getString(R.string.prefs_email), edtEmailId.getText().toString());
            editor.putString(getString(R.string.prefs_mobile), edtMobileNo.getText().toString());
            editor.putString(getString(R.string.prefs_pass), edtPassword.getText().toString());

            editor.commit();

        }else{


        }
    }
    protected void getData(String urls) {
        //username
        //pass
        //email
        //mobile
        //address
        //city
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
            builder.appendQueryParameter("username", edtUserName.getText().toString());
            builder.appendQueryParameter("pass", edtPassword.getText().toString());
            builder.appendQueryParameter("email", edtEmailId.getText().toString());
            builder.appendQueryParameter("mobile", edtMobileNo.getText().toString());
            builder.appendQueryParameter("address",edtAddress.getText().toString());
            builder.appendQueryParameter("city", "toronto");

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
            Log.i(TAG,JSON);
            JSONObject jObject = new JSONObject(JSON);
            JSONArray itemArray = jObject.getJSONArray("data");
            String Status = itemArray.getJSONObject(0).getJSONObject("user_data").getString("status");

            if(Status.equalsIgnoreCase("success")){
                status = true;
                isAlready = false;
            }else if(Status.equalsIgnoreCase("fail")){
                status = false;
                isAlready = false;
            }else{
                status= false;
                isAlready = true;
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
    private void showMessage(String title,String messsage){
        AlertDialog.Builder alertDialog = new  AlertDialog.Builder(this);
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


    class SignUpDetails extends  AsyncTask<String ,Void, Void>{

        public SignUpDetails() {
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            getData(params[0]);
            progressDialog.dismiss();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(status == true){
                // user success
            }else{

                if(isAlready == true){
                    // user Already
                }else{
                    // User REgistration error
                }
            }
        }
    }

}
