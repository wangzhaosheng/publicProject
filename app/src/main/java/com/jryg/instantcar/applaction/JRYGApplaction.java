package com.jryg.instantcar.applaction;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.jryg.instantcar.R;
import com.jryg.instantcar.utils.FileUtils;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;


/**
 * @version V1.0
 * @ClassName: JRYGApplaction
 * @Description: TODO
 * @Author：wangzhaosheng
 * @Date：2016-3-31 上午10:54:49
 */
public class JRYGApplaction extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context = this.getApplicationContext();
        GlobalVariable.getInstance().init(context);
        initImageLoader();
        clearData();
        initFile();
//        initPush();
    }




    /**
     * @return void
     * @Title: initImageLoader
     * @Description: TODO
     */
    private void initImageLoader() {
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_background)
                .showImageOnFail(R.drawable.default_background)
                .cacheInMemory(true).cacheOnDisk(true)
                .resetViewBeforeLoading(true).considerExifParams(false)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800)
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(10)
//                .diskCache(
//                        new UnlimitedDiscCache(StorageUtils.getCacheDirectory(
//                                this, true)))
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new BaseImageDownloader(this))
                .imageDecoder(new BaseImageDecoder(false))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .defaultDisplayImageOptions(imageOptions).build();
        ImageLoader.getInstance().init(config);
    }

    public void clearData() {
        ImageLoader.getInstance().stop();
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().resume();
    }

    /**
     * 初始化目录文件
     */
    private void initFile() {
        FileUtils.init(context);
        //本地文件扫描
        FileUtils.fileScanRunable(context);
    }

    /**
     * 极光推送
     *
     * @return
     */
//    private void initPush() {
//        CommonLog.d("推送初始化数据！");
//        if (CommonLog.LOG_LEVEL > Log.DEBUG) {
//            JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
//        } else {
//            JPushInterface.setDebugMode(false);
//        }
//        JPushInterface.init(this);            // 初始化 JPush
//        String registrationID = JPushInterface.getRegistrationID(getApplicationContext());
//        CommonLog.d("registrationID:" + registrationID);
//        ShareDataUtils.put(context, Constants.KEY_REGISTRATION_ID, registrationID);
//        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getApplicationContext());
//        builder.statusBarDrawable = R.drawable.jpush_notification_icon;
//        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
//                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
//        builder.notificationDefaults = Notification.DEFAULT_SOUND
//                | Notification.DEFAULT_VIBRATE
//                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
//        JPushInterface.setPushNotificationBuilder(1, builder);
//        JPushInterface.setLatestNotificationNumber(getApplicationContext(), 3);
//    }


}
