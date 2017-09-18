package com.jryg.instantcar.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileUtils {

    /**
     * MB  单位B
     */
    private static int MB = 1024 * 1024;

    /**
     * 描述：SD卡是否能用.
     *
     * @return true 可用,false不可用
     */
    public static boolean isCanUseSD() {
        try {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 计算sdcard上的剩余空间
     */
    public static int freeSpaceOnSD() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return (int) sdFreeMB;
    }

    /**
     * 判断文件是否可写
     *
     * @param path
     * @return
     */
    public static boolean isWriteable(String path) {
        try {
            if (path != null && !"".equals(path)) {
                return false;
            }
            File f = new File(path);
            return f.exists() && f.canWrite();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 初始化文件
     */
    public static void init(Context context) {
        createCatalog(context, Constants.FILE_PATH_IMAGE_DIR);
        createCatalog(context, Constants.FILE_PATH_voice_DIR);
        createCatalog(context, Constants.FILE_PATH_APK);
        createCatalog(context, Constants.FILE_PATH_EXCEPTION_DIR);
        createCatalog(context, Constants.FILE_PATH_FRESCO_IMAGE_CACHE);
    }

    /**
     * 创建缓存目录
     *
     * @param path
     */
    private static void createCatalog(Context context, String path) {
        if (isCanUseSD()) {
            File file = new File(Environment.getExternalStorageDirectory(), path);
            if (!file.exists()) {
                CommonLog.d("创建目录：" + file.getAbsolutePath());
                file.mkdirs();
            } else {
                deleteFolderFile(context, file.getAbsolutePath(), false);
                CommonLog.d("目录已存在：" + file.getAbsolutePath());
            }
        }
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param filePath
     * @return
     */
    private static void deleteFolderFile(Context context, String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(context, files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[]{file.getPath()});
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void fileScanRunable(final Context context) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                fileScan(context);
            }
        });
    }

    /**
     * 扫描指定文件夹Android4.4中，则会抛出异常MediaScannerConnection.scanFile可以解决
     */
    public static void fileScan(Context context) {
        try {
            if (Build.VERSION.SDK_INT < 19) {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                        + Environment.getExternalStorageDirectory())));
            } else {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                        + Environment.getExternalStorageDirectory())));
                MediaScannerConnection.scanFile(context, new String[]{new File(Environment.getExternalStorageDirectory().toString()).getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveException(Throwable ex, SimpleArrayMap<String, String> option) {
        if (ex == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (option != null && option.size() > 0) {
            int len = option.size();
            for (int i = 0; i < len; i++) {
                String key = option.keyAt(i);
                String value = option.valueAt(i);
                sb.append(key);
                sb.append("===");
                sb.append(value);
                sb.append("\n");
            }
        }
        sb.append("\n");
        FileOutputStream fos = null;
        try {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            printWriter.close();

            String result = writer.toString();
            sb.append(result);
            long timestamp = System.currentTimeMillis();

            String fileName = Constants.CRASH_FILE_NAME_PREFFIX + timestamp + ".log";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                String chashDir = Environment.getExternalStorageDirectory() + Constants.FILE_PATH_EXCEPTION_DIR + "/exception/";
                CommonLog.d("异常目录chashDir:" + chashDir);
                String filePath = chashDir + fileName;
                File dir = new File(chashDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                fos = new FileOutputStream(filePath);

                fos.write(sb.toString().getBytes());
            }


            CommonLog.d("Crash error!!!!" + ex);
        } catch (Exception e) {
            CommonLog.d("an error occured while writing file..." + e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    CommonLog.d("an error occured while cloase fileout stream..." + e);
                }
            }
        }
    }
}
