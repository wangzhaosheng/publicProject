package com.jryg.instantcar.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jryg.instantcar.R;
import com.jryg.instantcar.manager.PromptManager;

import java.io.File;

/**
 * Created by wangzhaosheng on 2017/12/7.
 * 下载文件的服务
 */

public class DownLoadService extends Service {

    private DownloadBinder downloadBinder = new DownloadBinder();
    private String downloadUrl;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return downloadBinder;
    }

    DownLoadTask downLoadTask;

    public class DownloadBinder extends Binder {

        public void startDownload(String url) {
            if (downLoadTask == null) {
                downloadUrl = url;
                downLoadTask = new DownLoadTask(listener);
                downLoadTask.execute(downloadUrl);
                startForeground(1, getNotifaction("Downloading...", 0));
                PromptManager.showToast(mContext, "开始下载");
            }
        }

        public void cancelDownload() {
            if (downLoadTask != null) { //正在下载的时候   在下载流程中删除文件
                downLoadTask.cancelDownload();
            } else { //取消下载的时候需要删除文件
                if (downloadUrl != null) {
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(path + fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                    getNotifactionManager().cancel(1);
                    stopForeground(true);
                    PromptManager.showToast(mContext, "取消下载");
                }
            }
        }

        public void pauseDownload() {
            if (downLoadTask != null) {
                downLoadTask.pauseDownload();
            }
        }
    }

    private Notification getNotifaction(String title, int progress) {
        Intent intent = new Intent(this, DownLoadActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        if (progress > 0) {
            builder.setProgress(100, progress, false);
            builder.setContentText(progress + "%");
        }
        return builder.build();
    }

    private DownLoadListener listener = new DownLoadListener() {
        @Override
        public void onSuccess() {
            downLoadTask = null;
            //下载成功将前台服务通知关闭  并添加一个下载成功的通知
            stopForeground(true);
            getNotifactionManager().notify(1, getNotifaction("Download success", -1));
            PromptManager.showToast(mContext, "下载成功");
        }

        @Override
        public void onFail() {
            downLoadTask = null;
            //下载成功将前台服务通知关闭  并添加一个下载成功的通知
            stopForeground(true);
            getNotifactionManager().notify(1, getNotifaction("Download fail", -1));
            PromptManager.showToast(mContext, "下载失败");
        }

        @Override
        public void onPause() {
            downLoadTask = null;
            PromptManager.showToast(mContext, "下载暂停");
        }

        @Override
        public void onCancel() {
            downLoadTask = null;
            stopForeground(true);
            PromptManager.showToast(mContext, "下载取消");
        }

        @Override
        public void onProgress(int progress) {
            getNotifactionManager().notify(1, getNotifaction("Downloading...", progress));
        }
    };

    private NotificationManager getNotifactionManager() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        return manager;
    }
}
