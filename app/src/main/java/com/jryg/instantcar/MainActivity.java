package com.jryg.instantcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jryg.instantcar.download.DownLoadActivity;
import com.jryg.instantcar.okhttp.OkhttpActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void downloadFile(View v) {
        startActivity(new Intent(this, DownLoadActivity.class));
    }

    public void okhttp(View v) {
        startActivity(new Intent(this, OkhttpActivity.class));
    }
}
