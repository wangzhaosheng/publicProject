package com.jryg.instantcar.download;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jryg.instantcar.R;

/**
 * wangzhaosheng
 * 2017/12/7.
 * DownLoadActivity
 */

public class DownLoadActivity extends AppCompatActivity {

    private DownLoadService.DownloadBinder binder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        Intent intent = new Intent(this, DownLoadService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (DownLoadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    public void startDown(View view) {
        binder.startDownload("https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe");
    }

    public void pauseDown(View view) {
        binder.pauseDownload();
    }

    public void cancelDown(View view) {
        binder.cancelDownload();
    }
}
