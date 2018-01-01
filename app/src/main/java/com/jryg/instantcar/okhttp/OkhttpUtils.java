package com.jryg.instantcar.okhttp;

import com.alibaba.fastjson.JSON;
import com.jryg.instantcar.bean.BaseBean;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/30 0030.
 */
public class OkhttpUtils<T extends BaseBean> {

    private static OkhttpUtils okhttpUtils;

    private OkhttpUtils() {
    }

    public static OkhttpUtils getInstance() {
        if (okhttpUtils == null) {
            synchronized (OkhttpUtils.class) {
                if (okhttpUtils == null) {
                    okhttpUtils = new OkhttpUtils();
                }
            }
        }
        return okhttpUtils;
    }

    public void sendOkHttpRequest(String url, HashMap<String, String> map, final Class<T> clazz, final MyOkhttpCallback myCallback) {
        Headers headers = new Headers.Builder()
                .add("token", "token")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(getRequestBody(map))
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myCallback.onFail(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                T t = JSON.parseObject(string, clazz);
                myCallback.onSuccess(t);
            }
        });
    }

    private RequestBody getRequestBody(HashMap<String, String> map) {

        FormBody.Builder builder = new FormBody.Builder();
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            builder.add(entry.getKey(), entry.getValue());
        }
        FormBody body = builder.build();
        return body;
    }
}
