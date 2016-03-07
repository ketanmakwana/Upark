package com.upark;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.webkit.WebView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Payment extends ActionBarActivity {


    WebView webView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        webView = (WebView) findViewById(R.id.webView);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String url = null;
            try {
                url = "http://usingjava.com/Upark/index.php?productname="+ URLEncoder.encode(bundle.getString("PackageName"), "UTF-8") + "&amount=" + URLEncoder.encode(bundle.getString("Amount"),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            webView.loadUrl(url);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
