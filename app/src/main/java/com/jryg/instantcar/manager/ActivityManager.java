package com.jryg.instantcar.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @version V1.0
 * @ClassName: ActivityManager
 * @Description: TODO
 * @Author：wangzhaosheng
 * @Date：2016-3-31 上午10:51:23
 */
public class ActivityManager {

    private static final ActivityManager activityManager = new ActivityManager();

    private ActivityManager() {

    }

    public static final ActivityManager getInstance() {
        return activityManager;
    }

    private List<Activity> activityList = new ArrayList<Activity>();

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 返回当前Activity
     *
     * @return
     */
    public Activity getActivity() {
        Activity activity = null;
        if (activityList != null && activityList.size() > 0) {
            activity = activityList.get(activityList.size() - 1);
        }
        return activity;
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
        activity.finish();
    }

    public void exit() {
        if (activityList.size() > 0) {
            for (Activity activity : activityList) {
                activity.finish();
            }
            activityList.clear();
            System.gc();

        }
    }

    /**
     * 登录
     *
     * @param context
     * @param loginInfo
     */
//    public void login(Context context, LoginInfo loginInfo) {
//        GlobalVariable.getInstance().loginInfo = loginInfo;
//        GlobalVariable.getInstance().SECRETKEY = loginInfo.SecretKey;
//        GlobalVariable.getInstance().LoginId = loginInfo.LoginId;
//        Intent intent = new Intent(context, HomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        //如果不是推送过来的直接启动否则在主页启动环信
//        if (ActivityManager.getInstance().getActivity() != null) {
//            initEm(ActivityManager.getInstance().getActivity(), loginInfo);
//        } else {
//            intent.putExtra(Constants.KEY_EM_FLAG, true);
//        }
//        context.startActivity(intent);
//    }




    /**
     * 退出
     */
    public void logout(Context context) {
        clearActivity();
//        GlobalVariable.getInstance().wheelGuard = false;
//        GlobalVariable.getInstance().LoginId = -1;
//        GlobalVariable.getInstance().loginInfo = null;
//        GlobalVariable.getInstance().SECRETKEY = Constants.DEFAULT_SECRETKEY;
    }

    public void clearActivity() {
        if (activityList.size() > 0) {
            for (Activity activity : activityList) {
                activity.finish();
            }
            activityList.clear();
            System.gc();
        }
    }
}
