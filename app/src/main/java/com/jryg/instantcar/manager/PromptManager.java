package com.jryg.instantcar.manager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.widget.Toast;

import com.jryg.instantcar.R;


/**
 * @version V1.0
 * @ClassName: PromptManager
 * @Description: 提示类
 * @Author：wangzhaosheng
 * @Date：2016-3-31 上午10:51:31
 */
public class PromptManager {

    private static ProgressDialog dialog;

    /**
     * 提示消息无回调监听
     *
     * @param context
     * @param msgResId 消息资源ID
     */
    public static void showResDialog(Context context, int msgResId) {
        showResDialog(context, msgResId, null);
    }

    /**
     * 提示消息无回调监听
     *
     * @param context
     * @param msg     消息体字符串
     */
    public static void showTextDialog(Context context, String msg) {
        String title = context.getString(R.string.title_reminder);
        showTextDialog(context, title, msg, null);
    }

    /**
     * 用于无回调确认提示框
     *
     * @param context
     * @param titleResId 标题资源ID
     * @param msgResId   消息资源ID
     */
    public static void showResDialog(Context context, int titleResId, int msgResId) {
        showResDialog(context, titleResId, msgResId, null);
    }

    /**
     * 用于无回调确认提示框
     *
     * @param context
     * @param title   标题字符串
     * @param msg     消息体字符串
     */
    public static void showTextDialog(Context context, String title, String msg) {
        showTextDialog(context, title, msg, null);
    }

    /**
     * 用于固定TITEL单一按钮回调事件提示框
     *
     * @param context
     * @param msgResId 消息资源ID
     * @param listener 按钮回调监听
     */
    public static void showResDialog(Context context, int msgResId, OnClickListener listener) {
        int titleResId = R.string.title_reminder;
        showResDialog(context, titleResId, msgResId, listener);
    }

    /**
     * 用于固定TITEL单一按钮回调事件提示框
     *
     * @param context
     * @param msg      消息体字符串
     * @param listener 按钮回调监听
     */
    public static void showTextDialog(Context context, String msg, OnClickListener listener) {
        String title = context.getString(R.string.title_reminder);
        showTextDialog(context, title, msg, listener);
    }

    /**
     * 用于单一按钮回调事件提示框
     *
     * @param context
     * @param titleResId 标题资源ID
     * @param msgResId   消息资源ID
     * @param listener   按钮回调监听
     */
    public static void showResDialog(Context context, int titleResId, int msgResId, OnClickListener listener) {
        int btnResId = R.string.confirm;
        showResDialog(context, titleResId, msgResId, btnResId, listener);
    }

    /**
     * 用于单一按钮回调事件提示框
     *
     * @param context
     * @param title    标题字符串
     * @param msg      消息体字符串
     * @param listener 按钮回调监听
     */
    public static void showTextDialog(Context context, String title, String msg, OnClickListener listener) {
        String confirm = context.getString(R.string.confirm);
        showTextDialog(context, title, msg, confirm, listener);
    }

    /**
     * 用于单一按钮回调事件提示框
     *
     * @param context
     * @param titleResId 标题资源ID
     * @param msgResId   消息资源ID
     * @param btnResId   按钮文字资源ID
     * @param listener   按钮回调监听
     */
    public static void showResDialog(Context context, int titleResId, int msgResId, int btnResId, OnClickListener listener) {
        showResDialog(context, titleResId, msgResId, btnResId, listener, -1);
    }

    /**
     * 用于提示单一按钮并且有回调监听的
     *
     * @param context
     * @param title    标题字符串
     * @param msg      消息体字符串
     * @param btnStr   取消按钮文字
     * @param listener 按钮回调监听
     */
    public static void showTextDialog(Context context, String title, String msg, String btnStr, OnClickListener listener) {
        showTextDialog(context, title, msg, btnStr, listener, null);
    }

    /**
     * 用于提示取消没有回调监听的
     *
     * @param context
     * @param titleResId      标题资源ID -1代表无些按钮
     * @param msgResId        消息资源ID -1代表无些按钮
     * @param confirmResId    确定按钮文字资源ID -1代表无些按钮
     * @param confirmListener 确定按钮回调监听
     * @param cancelResId     取消按钮文字资源ID -1代表无些按钮
     */
    public static void showResDialog(Context context, int titleResId, int msgResId, int confirmResId, OnClickListener confirmListener,
                                     int cancelResId) {
        showResDialog(context, titleResId, msgResId, confirmResId, confirmListener, cancelResId, null);
    }

    /**
     * 用于提示取消没有回调监听的
     *
     * @param context
     * @param title           标题字符串
     * @param msg             消息体字符串
     * @param confirm         确定按钮文字
     * @param confirmListener 确定按钮监听
     * @param cancel          取消按钮文字
     */
    public static void showTextDialog(Context context, String title, String msg, String confirm, OnClickListener confirmListener,
                                      String cancel) {
        showTextDialog(context, title, msg, confirm, confirmListener, cancel, null);
    }

    /**
     * 用于回调事件提示框
     *
     * @param context
     * @param titleResId      标题资源ID -1代表无些按钮
     * @param msgResId        消息资源ID -1代表无些按钮
     * @param confirmResId    确定按钮文字资源ID -1代表无些按钮
     * @param confirmListener 确定按钮回调监听
     * @param cancelResId     取消按钮文字资源ID -1代表无些按钮
     * @param cancelListener  取消按钮回调监听
     */
    public static void showResDialog(Context context, int titleResId, int msgResId, int confirmResId, OnClickListener confirmListener,
                                     int cancelResId, OnClickListener cancelListener) {
        String title = null;
        if (titleResId != -1) {
            title = context.getResources().getString(titleResId);
        }
        String msg = null;
        if (msgResId != -1) {
            msg = context.getResources().getString(msgResId);
        }
        String confirm = null;
        if (confirmResId != -1) {
            confirm = context.getResources().getString(confirmResId);
        }
        String cancel = null;
        if (cancelResId != -1) {
            cancel = context.getResources().getString(cancelResId);
        }
        showTextDialog(context, title, msg, confirm, confirmListener, cancel, cancelListener);
    }

    /**
     * 最终调用提示框
     *
     * @param context
     * @param title           标题字符串
     * @param msg             消息体字符串
     * @param confirm         确定按钮文字
     * @param confirmListener 确定按钮监听
     * @param cancel          取消按钮文字
     * @param cancelListener  取消按钮监听
     */
    public static void showTextDialog(Context context, String title, String msg, String confirm, OnClickListener confirmListener,
                                      String cancel, OnClickListener cancelListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            alertDialog.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            alertDialog.setMessage(msg);
        }
        if (!TextUtils.isEmpty(confirm) || confirmListener != null) {
            alertDialog.setPositiveButton(confirm, confirmListener);
        }
        if (!TextUtils.isEmpty(cancel) || cancelListener != null) {
            alertDialog.setNegativeButton(cancel, cancelListener);
        }
        alertDialog.show();
    }

    public static void showSimpleProgressDialog(Context context, String text) {
        dialog = new ProgressDialog(context);
        dialog.setTitle(R.string.hint);
        dialog.setMessage(text);
        dialog.setCancelable(false);
        dialog.show();
    }

    private static Toast toast;

    public static void showToast(Context context, String msg) { //里面的封装意义   连续弹出toast的时候不重复弹出  只弹出一次  后面的弹
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

}
