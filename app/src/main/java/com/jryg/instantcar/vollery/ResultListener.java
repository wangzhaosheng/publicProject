package com.jryg.instantcar.vollery;

import com.android.volley.VolleyError;

/**
 * @version V1.0
 * @ClassName: ResultListener
 * @Description: TODO
 * @Author：wangzhaosheng
 * @Date：2016-3-31 上午10:49:26
 */
public interface ResultListener<T> {
    public void getData(T response);

    public void error(VolleyError error);
}
