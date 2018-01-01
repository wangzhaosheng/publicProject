package com.jryg.instantcar.okhttp;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;

import com.jryg.instantcar.R;
import com.jryg.instantcar.base.BaseActivtiy;
import com.jryg.instantcar.bean.BaseBean;
import com.jryg.instantcar.manager.PromptManager;
import com.jryg.instantcar.utils.CommonLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2017/12/31 0031.
 */
public class OkhttpActivity<T> extends BaseActivtiy {

    Class <T>clazz;
    class A{};
    class Fruit extends A{};
    class Apple extends Fruit{}
    class Orange extends Fruit{}

    @Override
    public int getContentViewId() {
        return R.layout.activity_okhttp;
    }

    @Override
    public void getSaveBundle(Bundle bundle) {
        List<? extends Fruit> list = new ArrayList<>();
    }

    @Override
    public void getLastPageBundle() {
    }

    @Override
    public void initView() {
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void requestNetwork() {

    }


    public void click(View v) {

        HashMap<Object, Object> map = new HashMap<>();
        map.put("areaCode", "010");
        OkhttpUtils.getInstance().sendOkHttpRequest("http://dev.service.call.jryghq.com/config/getcitybycode", map, BaseBean.class, new MyOkhttpCallback() {
            @Override
            public void onSuccess(BaseBean resopnse) {
                CommonLog.d(resopnse.StatusCode + "");
            }

            @Override
            public void onFail(IOException e) {

            }
        });

    }
}
