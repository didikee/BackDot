package com.inno.backdot.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

import com.inno.backdot.R;

public class HelpActivity extends AppCompatActivity {

    private WebView mWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        init();
    }

    private void init() {
        mWeb = ((WebView) findViewById(R.id.web));
        String type = getIntent().getStringExtra("type");
        if ("help".equals(type)){
            mWeb.loadUrl("file:///android_asset/help/help.html");
        }else if ("me".equals(type)){
            mWeb.loadUrl("file:///android_asset/me/me.html");
        }else {
            Toast.makeText(HelpActivity.this,"WTF!!!",Toast.LENGTH_SHORT).show();
        }


    }



}
