package com.jryg.instantcar.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jryg.instantcar.inter.BaseActivityInterface;
import com.jryg.instantcar.manager.ActivityManager;
import com.jryg.instantcar.utils.CustomDialog;

/**
 * @version V1.0
 * @ClassName:BaseActivtiy
 * @Description:没有标题栏的基类
 * @Author：wangzhaosheng
 * @Date：2017/6/12.
 */

public abstract class BaseActivtiy extends FragmentActivity implements BaseActivityInterface {

    protected Activity mActivity;
    protected Context mContext;
    protected CustomDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActivityManager.getInstance().addActivity(this);
        setContentView(getContentViewId());
        init(savedInstanceState);
    }

    /**
     * Description: 初始化
     * Date  2017/6/12
     * Author: wangzhaosheng
     */
    private void init(Bundle bundle) {
        mActivity = this;
        mContext = getApplicationContext();
        dialog=new CustomDialog(this);
        //获取mActivity保存状态
        getSaveBundle(bundle);
        //获取上页数据
        getLastPageBundle();
        //初始化findviewbyid
        initView();
        //设置监听
        setListener();
        //初始化数据
        initData();
        //访问网络
        requestNetwork();
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//        MobclickAgent.onPause(this);
//        MobclickAgent.onPageStart("SplashScreen");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        MobclickAgent.onResume(this);
//        MobclickAgent.onPageStart("SplashScreen");
    }


    @Override
    protected void onDestroy() {
        dismissProgressDialog();//这个要放在前面  销毁界面前必须让dialog消失
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);//这个防止mActivity直接回到home页后  没有走onKeyDown中的removeActivity方法
        if (mActivity != null) {
            mActivity = null;
        }
        if (mContext != null) {
            mContext = null;
        }
    }

    private void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityManager.getInstance().removeActivity(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public String getRString(int resId) {
        return getResources().getString(resId);
    }

    public String getRString(int resId, String txt) {
        return getResources().getString(resId, txt);
    }

    public int getResColor(int resId) {
        return ContextCompat.getColor(this, resId);
    }

}
