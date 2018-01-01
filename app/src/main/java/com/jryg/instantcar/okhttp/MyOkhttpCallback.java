package com.jryg.instantcar.okhttp;

import com.jryg.instantcar.bean.BaseBean;

import java.io.IOException;

/**
 * Created by Administrator on 2017/12/31 0031.
 */
public interface MyOkhttpCallback<T extends BaseBean> {

    void onSuccess(T resopnse);

    void onFail(IOException e);

}
