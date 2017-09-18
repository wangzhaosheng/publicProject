package com.jryg.instantcar.vollery;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.jryg.instantcar.R;
import com.jryg.instantcar.applaction.GlobalVariable;
import com.jryg.instantcar.applaction.JRYGApplaction;
import com.jryg.instantcar.manager.PromptManager;
import com.jryg.instantcar.utils.CommonLog;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.Response.ErrorListener;
import com.android.volley.AuthFailureError;
/**
 * @version V1.0
 * @ClassName: JRYGRequest
 * @Description:
 * @Author：wangzhaosheng
 * @Date：2016-3-30 下午6:09:03
 */
public class JRYGRequest<T> extends Request<T> {

    private String url = null;
    private Class<T> mClazz;
    private Listener<T> mListener;
    private ErrorListener errorListener = null;
    private Map<String, Object> mParams = new HashMap<String, Object>();
    private String body = null;

    public JRYGRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
        // TODO Auto-generated constructor stub
        this.url = url;
    }

    public JRYGRequest(int method, String url, Class<T> clazz, Map<String, Object> params, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.url = url;
        this.mListener = listener;
        this.errorListener = errorListener;
        this.mClazz = clazz;
        this.mParams = params;
        body = getBodyString();
    }

    public JRYGRequest(String url, Class<T> clazz, Map<String, Object> params, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, errorListener);
        this.url = url;
        this.mListener = listener;
        this.errorListener = errorListener;
        this.mClazz = clazz;
        this.mParams = params;
        CommonLog.d("URL:" + url);
        body = getBodyString();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("Charset", "UTF-8");
//        headers.put("Content-Type", "application/json");
//        headers.put(Constants.KEY_NID, GlobalVariable.getInstance().getNid());
//        headers.put(Constants.KEY_UID, GlobalVariable.getInstance().UID);
//        headers.put(Constants.KEY_TIMESTAMP, GlobalVariable.getInstance().getTimestamp());
//        headers.put(Constants.KEY_SIG, GlobalVariable.getInstance().getSig(body.length()));
//        if (!Constants.DEFAULT_SECRETKEY.equals(GlobalVariable.getInstance().SECRETKEY)) {
//            headers.put(Constants.KEY_LOGIN_ID, String.valueOf(GlobalVariable.getInstance().LoginId));
//        }
        CommonLog.d("getHeaders:" + headers.toString());
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (TextUtils.isEmpty(url)) {
            return super.getParams();
        }
        Map<String, String> params = new HashMap<String, String>();
//        params.put(Constants.KEY_BODY, body);// TODO: 2017/6/9 请求体
        // 添加自己所需的全局参数
        CommonLog.d("请求参数：" + params.toString());
        return params;
    }

    /**
     * @return String
     * @Title: getBodyString
     * @Description: 获取参数信息
     */
    private String getBodyString() {
//        CommonLog.d("getIMEI：" + MobileInformation.getIMEI(JRYGApplaction.context));
//        mParams.put(Constants.KEY_DEVICE_UID, MobileInformation.getIMEI(JRYGApplaction.context));
//            if (GlobalVariable.getInstance().LoginId != -1) {
//                mParams.put(Constants.KEY_LOGIN_ID, GlobalVariable.getInstance().LoginId);
//            }

        String jsonString = JSON.toJSONString(mParams);
        CommonLog.d("加密前参数jsonString：" + jsonString);
        try {
//            CommonLog.d("GlobalVariable->SECRETKEY:" + GlobalVariable.getInstance().SECRETKEY);
//            jsonString = DesUtils.encryptThreeDESECB(jsonString, GlobalVariable.getInstance().SECRETKEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CommonLog.d("加密后参数jsonString：" + jsonString);
        return jsonString;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        // TODO Auto-generated method stub
        try {
            CommonLog.d("请求成功:statusCode:" + response.statusCode);
            String jsonStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            CommonLog.d(url + "返回：" + jsonStr);
            return Response.success(JSON.parseObject(jsonStr, mClazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            CommonLog.d("请求失败！");
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        // TODO Auto-generated method stub
        mListener.onResponse(response);
    }

    public void deliverError(VolleyError error) {
        CommonLog.e("请求异常 -> VolleyError"+error.getMessage());
        CommonLog.e("请求异常 -> VolleyError"+error);//TODO  添加的
        if (errorListener != null) {
            errorListener.onErrorResponse(error);
        }
        if (error instanceof NoConnectionError) {
            PromptManager.showToast(JRYGApplaction.context, R.string.network_anomaly);
        }
    }
}
