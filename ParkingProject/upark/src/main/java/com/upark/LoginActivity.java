package com.upark;

import com.upark.db.DBHelper;
import com.upark.model.LoggedInUsers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoginActivity extends Activity {

    Button btnLogin;
    Button btnSignUP;

    EditText edtUserName;
    EditText edtPassword;

    String UserId;
    String loginURL = "http://javawebandroid.com/Admin/api/login.php?accesskey=ups09040342395523423&";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //if user logged in one time then it will go for home acitivity
        AutoLogin();
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUP = (Button) findViewById(R.id.btnSignUP);
        edtUserName = (EditText) findViewById(R.id.txtvUserName);
        edtPassword = (EditText) findViewById(R.id.txtvPassword);

        //Copy database to local storage
//        DBHelper helper = new DBHelper(this);
//        if (helper.checkDataBase() == false) {
//            try {
//                //call custom copy function from DBHelper Class
//                // for copying database
//                helper.copyDataBase();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        // button for do signup for new users
        btnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSignUP();
            }
        });

        //button for do login for existing users
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

    }

    protected void show(String message) {

        new AlertDialog.Builder(this)
                .setMessage(message)
                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    public void AutoLogin() {
        Context mContext = getApplicationContext();
        SharedPreferences sharedPreferences = null;
        sharedPreferences = getSharedPreferences(getString(R.string.prefs_user_login), 0);
        String UserId =sharedPreferences.getString(getString(R.string.prefs_user_id),"");
        String UserName = sharedPreferences.getString(getString(R.string.prefs_uname), "");
        String Password = sharedPreferences.getString(getString(R.string.prefs_pass), "");
        if (UserName != null && UserName != "" && Password != null && Password != "") {
            if (UserName.equalsIgnoreCase("Admin")) {
                isAdmin = true;
            }
            LoggedInUsers.setUserName(UserName);
            LoggedInUsers.setUserId(UserId);
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("user", isAdmin);
            startActivity(intent);
            finish();
        }
    }


    boolean isAdmin = false;

    boolean isEmpty() {
        if (edtUserName.getText().toString().isEmpty()) {
            edtUserName.setError("Please enter email id!");
            return false;
        }
        if (edtPassword.getText().toString().isEmpty()) {
            edtPassword.setError("Please enter password!");

            return false;
        }
        return true;
    }

    public void doLogin() {
//        DBHelper dbHelper = new DBHelper(this);
//        dbHelper.openDataBase();
        //if (dbHelper.isUserExist(edtUserName.getText().toString(), edtPassword.getText().toString())) {
        if (isConnectingToInternet(this)) {
            if (isEmpty()) {
                new AuthenticateLogin().execute(new String[]{loginURL + "email=" + edtUserName.getText().toString() + "&password=" + edtPassword.getText().toString()});
            }
            if (edtUserName.getText().toString().equalsIgnoreCase("Admin")) {
                isAdmin = true;
            }
        }
//        LoggedInUsers.setUserName(edtUserName.getText().toString());
//
//        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//        intent.putExtra("user", isAdmin);
//        startActivity(intent);
//        finish();

//        } else {
//            show("Please Check User Name & Password!!");
//        }

    }

    boolean status = false;

    private void AuthenticateLoginData(String url) {
        int timeout = 30000;
        HttpURLConnection c = null;
        String JSON = null;

        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");

            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            Log.e("JSON", JSON);
            JSONObject jObject = new JSONObject(JSON);
            JSONArray jsonObject = jObject.getJSONArray("data");
            JSONObject mainObj = jsonObject.getJSONObject(0);
            String message = mainObj.getString("status");
            String UserId = mainObj.getString("user_id");
            this.UserId = UserId;
            LoggedInUsers.setUserId(UserId);
            if (message.equalsIgnoreCase("success")) {
                status = true;
            } else {
                status = false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class AuthenticateLogin extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Please wait while processing...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            AuthenticateLoginData(params[0]);
            progressDialog.dismiss();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (status) {
                Context mContext = getApplicationContext();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(getString(R.string.prefs_user_login), 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.prefs_user_id), UserId);
                editor.putString(getString(R.string.prefs_uname), edtUserName.getText().toString());
                editor.putString(getString(R.string.prefs_pass), edtPassword.getText().toString());
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("user", isAdmin);
                startActivity(intent);
                finish();
            } else {

            }
        }
    }

    public void doSignUP() {
        Intent intent = new Intent(LoginActivity.this, SignUp.class);
        startActivity(intent);
    }

    private void disableConnectionReuseIfNecessary() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

// end of code
}
