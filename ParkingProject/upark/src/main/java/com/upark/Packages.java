package com.upark;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.upark.Net.InternetConnection;
import com.upark.adapter.PackagesAdapter;
import com.upark.model.PackagesModel;

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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Packages extends ActionBarActivity {

    Spinner spPackageName;
    ProgressDialog progressDialog;
    TextView txtAmount;
    EditText edtEnteryDate;
    EditText edtEnteryTime;
    EditText edtExitDate;
    EditText edtExitTime;

    ArrayList<PackagesModel> arrayPackageModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);
        spPackageName = (Spinner) findViewById(R.id.spPackages);
        txtAmount = (TextView) findViewById(R.id.txtAmount);
        edtEnteryDate = (EditText) findViewById(R.id.edtEnteryDate);
        edtEnteryTime = (EditText) findViewById(R.id.edtEnteryTime);
        edtExitDate = (EditText) findViewById(R.id.edtExitDate);
        edtExitTime = (EditText) findViewById(R.id.edtExitTime);
        if (isConnectingToInternet(this)) {
            new GetPackagesFromWeb().execute(new String[]{InternetConnection.Packages + getString(R.string.apikey)});
        }
    }

    private void GetPackageData(String url) {
        arrayPackageModels = new ArrayList<>();

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
            for (int i = 0; i < mainObj.length(); i++) {
                JSONObject jData = mainObj.getJSONObject("package_data");
                PackagesModel models = new PackagesModel();
                models.setId(jData.getString("id"));
                models.setName(jData.getString("package_name"));
                models.setAllowedDays(jData.getString("alllowed_days"));
                models.setRate(jData.getString("rate"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GetPackagesFromWeb extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Packages.this);
            progressDialog.setMessage("Please wait while processing...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            GetPackageData(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (arrayPackageModels.size() > 0 && arrayPackageModels != null) {
                PackagesAdapter adapter = new PackagesAdapter(Packages.this, R.layout.packages_spinner_item, arrayPackageModels);
                spPackageName.setAdapter(adapter);
            }
        }
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

}
