package com.jryg.guide.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.jryg.guide.inter.ImageCompressListener;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtils {
    /**
     * 获取图片
     *
     * @param context
     * @param uri
     * @return
     */
    public static Bitmap getBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        String path = getPathFromUri(context, uri);
        int orientation = readPictureDegree(path);// 获取旋转角度
        bitmap = revitionImageSize(path);
        if (Math.abs(orientation) > 0) {
            bitmap = rotaingImageView(orientation, bitmap);// 旋转图片
        }
        return bitmap;
    }

    /**
     * 根据uri获取图片路径
     *
     * @param mContext
     * @param contentUri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static String getPathFromUri(Context mContext, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(mContext, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * 获取图片的旋转角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 创建图片路径
     *
     * @return
     */
    public static String createImageUrl() {
        String fileName = getPhotoFileName();
        return (Environment.getExternalStorageDirectory() + Constants.FILE_PATH_IMAGE_DIR + fileName);
    }

    // 使用系统当前日期加以调整作为照片的名称
    private static String getPhotoFileName() {
//        Date date = new Date(System.currentTimeMillis());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
//        return dateFormat.format(date) + ".png";
        return "IMG"+System.currentTimeMillis()+".png";
    }

    /**
     * 保存图片到本地
     *
     * @param bm
     * @param picUrl
     */
    public static void saveBitmap(Bitmap bm, String picUrl) {
        Log.e("", "保存图片");
        try {
            File f = new File(picUrl);
            if (f.exists()) {
                f.delete();
            }
            if (bm != null) {
                FileOutputStream out = new FileOutputStream(f);
                bm.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片压缩
     *
     * @param path
     * @return
     */
    public static Bitmap revitionImageSize(String path) {
        BufferedInputStream bufferedInputStream = null;
        Bitmap bitmap = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(path)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(bufferedInputStream, null, options);
            bufferedInputStream.close();
            int i = 0;
            while (true) {
                if ((options.outWidth >> i <= 1000)
                        && (options.outHeight >> i <= 1000)) {
                    bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(path)));
                    options.inSampleSize = (int) Math.pow(2.0D, i);
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(bufferedInputStream, null, options);
                    break;
                }
                i += 1;
            }
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeFile(path);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 压缩到100K
     *
     * @param imagePath
     * @return
     */
    public static String compressImage(String imagePath) {
        Bitmap bitmap = revitionImageSize(imagePath);
        int orientation = readPictureDegree(imagePath);// 获取旋转角度
        if (Math.abs(orientation) > 0) {
            bitmap = rotaingImageView(orientation, bitmap);// 旋转图片
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 5;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        String imageurl = ImageUtils.createImageUrl();
        ImageUtils.saveBitmap(bitmap, imageurl);
        bitmap.recycle();
        bitmap = null;
        return imageurl;
    }

    /**
     * 图片压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 2048) {    //循环判断如果压缩后图片是否大于2M,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inPreferredConfig = Bitmap.Config.RGB_565;
        bfo.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, bfo);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * Get image from newwork
     *
     * @param path The path of image
     * @return byte[]
     * @throws Exception
     */
    public static byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return readStream(inStream);
        }
        return null;
    }

    /**
     * Get data from stream
     *
     * @param inStream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }


    public static InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }
}
