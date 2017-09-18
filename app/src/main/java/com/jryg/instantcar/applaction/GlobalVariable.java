package com.jryg.instantcar.applaction;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.jryg.instantcar.bean.LoginInfo;
import com.jryg.instantcar.utils.CommonLog;
import com.jryg.instantcar.vollery.OkHttpStack;
import com.squareup.okhttp.OkHttpClient;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.UUID;

public class GlobalVariable {

    public static final GlobalVariable globalVariable = new GlobalVariable();

    public GlobalVariable() {

    }

    public static final GlobalVariable getInstance() {
        return globalVariable;
    }

    public int onScreenWidth;    // 屏幕宽
    public int onScreenHeight;    // 屏幕高
    public float onDensity;
    public float onScreenDensity;
    //登录标识
    public int LoginId = -1;
    //登录信息
    public LoginInfo loginInfo;
    //MD5(Uid+Nid+时间戳+密钥KEY+body加密长度)
    public String Sig = null;
    //同一时间唯一
    public String Nid = null;
    //死数据
//    public String UID = Constants.UID;//测试接口uid
//    //加密密钥
//    public String SECRETKEY = Constants.DEFAULT_SECRETKEY;
    //时间戳
    public String timestamp = null;

    //轮训开关
    public boolean wheelGuard = false;
    public final int requestTime = 15000;

    private Context mContext;
    private RequestQueue mRequestQueue;

    public void init(Context context) {
        mContext = context;
        getScreenData(context);
    }

    public void getScreenData(Context context) {
        // 获取屏幕宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        onScreenWidth = dm.widthPixels;
        onScreenHeight = dm.heightPixels;
        onDensity = dm.density;
        onScreenDensity = dm.scaledDensity;
        CommonLog.d("onScreenWidth：" + onScreenWidth);
        CommonLog.d("onScreenHeight：" + onScreenHeight);
        CommonLog.d("onDensity：" + onDensity);
        CommonLog.d("onScreenDensity：" + onScreenDensity);
    }

    /**
     * 返回Nid
     *
     * @return String
     */
    public String getNid() {
        UUID uuid = UUID.randomUUID();
        Nid = uuid.toString();
        return Nid;
    }

    /**
     * 返回时间戳字符串
     *
     * @return String
     */
    public String getTimestamp() {
        timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        return timestamp;
    }

    /**
     * 获取SIG
     *
     * @param desLength
     * @return
     */
//    public String getSig(int desLength) {
//        CommonLog.d("SECRETKEY:" + SECRETKEY);
//        return MD5.MD5(UID + Nid + timestamp + SECRETKEY + desLength).toLowerCase();
//    }

    /**
     * 获取Mac
     */
    private String getMac() {
        String macSerial = null;
        String str = null;
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    /**
     * @return RequestQueue
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext, new OkHttpStack(new OkHttpClient()));
        }
        mRequestQueue.getCache().clear();
        return mRequestQueue;
    }
}
