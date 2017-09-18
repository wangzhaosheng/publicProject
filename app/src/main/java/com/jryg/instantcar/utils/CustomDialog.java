package com.jryg.instantcar.utils;

/**
 * Created by wangzhaosheng on 2016/10/28.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jryg.instantcar.R;


/**
 * 自定义加载
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context, R.style.DialogTheme);
        // 设置内容
        setContentView(R.layout.dialog_layout);
        setCanceledOnTouchOutside(true);//设置点击dialog外面都可以取消dialog
        setCancelable(true);//设置点击返回键能取消
        // 设置窗口属性
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 设置宽度、高度、密度、对齐方式
        float density = getDensity(context);
        params.width = (int) (80 * density);
        params.height = (int) (80 * density);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    public CustomDialog(Context context, int width, int height) {
        super(context, R.style.DialogTheme);
        // 设置内容
        setContentView(R.layout.dialog_layout);
        setCanceledOnTouchOutside(true);//设置点击dialog外面都可以取消dialog
        setCancelable(true);//设置点击返回键都能取消
        // 设置窗口属性
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 设置宽度、高度、密度、对齐方式
        float density = getDensity(context);
        params.width = (int) (width * density);
        params.height = (int) (height * density);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    /**
     * 显示文本框
     *
     * @param txt
     */
    public void showText(String txt) {
        TextView tv = (TextView) findViewById(R.id.message);
        tv.setVisibility(View.VISIBLE);
        tv.setText(txt);
    }

    /**
     * 获取显示密度
     *
     * @param context
     * @return
     */
    public float getDensity(Context context) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        return dm.density;
    }
}
