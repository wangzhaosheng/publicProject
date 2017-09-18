package com.jryg.instantcar.vollery;

import android.content.Context;


import com.jryg.instantcar.bean.BaseBean;
import com.jryg.instantcar.manager.ActivityManager;
import com.jryg.instantcar.manager.PromptManager;
import com.jryg.instantcar.utils.Utils;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Map;

/**
 * @version V1.0
 * @ClassName: JRYGRequestManager
 * @Description: TODO
 * @Author：wangzhaosheng
 * @Date：2016-3-31 上午10:55:39
 */
public class JRYGRequestManager<T extends BaseBean> {

    private static final JRYGRequestManager jRYGRequestManager = new JRYGRequestManager();

    private JRYGRequestManager() {

    }

    public static JRYGRequestManager getInstance() {
        return jRYGRequestManager;
    }




    public void requestNetwork(RequestQueue requestQueue, final Context context, Class<T> clazz, String url, String tag, Map<String, Object> params, final ResultListener<T> resultListener) {
        if (!Utils.isNetworkAvailable(context)) {
            PromptManager.showToast(context, "请检查网络！");
            resultListener.error(null);
            return;
        }
        JRYGRequest<T> request = new JRYGRequest<T>(url, clazz, params, new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                if (response.StatusCode == 200) {
                    resultListener.getData(response);
                } else if (response.StatusCode == 801) {
                    PromptManager.showToast(context, "帐号在其它客户端登录！");
//                    context.startActivity(new Intent(context, RegisteredLoginOptionActivity.class));// TODO: 2017/6/9 回登录界面 
                    ActivityManager.getInstance().logout(context);
                } else {
                    resultListener.error(null);
                    PromptManager.showToast(context, response.Message);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultListener.error(error);
                PromptManager.showToast(context, "请求失败！");
            }
        });
        request.setTag(tag);
        requestQueue.add(request);
    }

    /**
     * 不处理
     *
     * @param requestQueue
     * @param context
     * @param clazz
     * @param url
     * @param tag
     * @param params
     * @param resultListener
     */
    public void requestNetworkRrror(RequestQueue requestQueue, final Context context, Class<T> clazz, String url, String tag, Map<String, Object> params, final ResultListener<T> resultListener) {
        if (!Utils.isNetworkAvailable(context)) {
            PromptManager.showToast(context, "请检查网络！");
            resultListener.error(null);
            return;
        }
        JRYGRequest<T> request = new JRYGRequest<T>(url, clazz, params, new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                if (response.StatusCode == 801) {
                    PromptManager.showToast(context, "帐号在其它客户端登录！");
//                    context.startActivity(new Intent(context, RegisteredLoginOptionActivity.class));
                    ActivityManager.getInstance().logout(context);
                } else {
                    resultListener.getData(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultListener.error(error);
                PromptManager.showToast(context, "请求失败！");
            }
        });
        request.setTag(tag);
        requestQueue.add(request);
    }
}
