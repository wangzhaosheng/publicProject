package com.jryg.instantcar.download;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wangzhaosheng on 2017/12/6.
 * 异步断点下载文件类  传入的参数是下载url
 */

public class DownLoadTask extends AsyncTask<String, Integer, Integer> {

    public static final int TYPE_SUCCESS = 1;
    public static final int TYPE_FAILED = 2;
    public static final int TYPE_PAUSED = 3;
    public static final int TYPE_CANCEL = 4;

    private boolean isCancel;
    private boolean isPause;

    private DownLoadListener listener;
    private InputStream inputStream;

    public DownLoadTask(DownLoadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... strings) {

        RandomAccessFile savedFile = null;
        File file = null;
        try {
            String downLoadUrl = strings[0];
            String fileName = downLoadUrl.substring(downLoadUrl.lastIndexOf("/"));
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(path + fileName);
            long downLoadedLength = 0;
            if (file.exists()) {
                downLoadedLength = file.length();
            }
            long contentLength = getContentLength(downLoadUrl);
            if (contentLength == 0) {
                return TYPE_FAILED;
            }
            if (contentLength == downLoadedLength) {  //已经下载完成
                return TYPE_SUCCESS;
            }

            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    //断点下载  这个表示从已经下载的字节开始下
                    .addHeader("RANGE", "bytes=" + downLoadedLength + "-")
                    .url(downLoadUrl).build();
            Response response = httpClient.newCall(request).execute();
            if (response != null) {
                inputStream = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                savedFile.seek(downLoadedLength);//跳过已下载的字节
                byte[] b = new byte[1024];
                int len = 0;
                int totalLenth = 0;
                while ((len = inputStream.read(b)) != -1) {
                    if (isCancel) {
                        return TYPE_CANCEL;
                    }
                    if (isPause) {
                        return TYPE_PAUSED;
                    }
                    totalLenth += len;
                    savedFile.write(b, 0, len);
                    publishProgress((int) ((totalLenth + downLoadedLength) * 100 / contentLength));//计算已经下载的百分比
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (savedFile != null) {
                    savedFile.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (isCancel && file != null) {
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    private int lastProgress;

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFail();
                break;
            case TYPE_CANCEL:
                listener.onCancel();
                break;
            case TYPE_PAUSED:
                listener.onPause();
                break;

            default:

                break;
        }
    }

    private long getContentLength(String downLoadUrl) throws IOException {
        Request request = new Request.Builder()
                .url(downLoadUrl)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        if (response != null) {
            long contentLength = response.body().contentLength();
            return contentLength;
        }
        return 0;
    }

    /**
     * 暂停下载
     */
    public void pauseDownload() {
        isPause = true;
    }
    /**
     * 取消下载
     */
    public void cancelDownload() {
        isCancel = true;
    }

}
