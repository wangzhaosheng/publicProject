package com.jryg.instantcar.inter;

import android.os.Bundle;

/**
 * @version V1.0
 * @ClassName:
 * @Description:
 * @Author：wangzhaosheng
 * @Date：2017/6/12.
 */

public interface BaseActivityInterface {

    /**
     *
     * @Title: getContentViewId
     * @Description: 设置布局ID
     * @return int
     *
     */
    int getContentViewId();

    /**
     *
     * @Title: getSaveBundle
     * @Description: 获取保存状态
     * @param bundle
     * @return void
     *
     */
    void getSaveBundle(Bundle bundle);

    /**
     *
     * @Title: getLastPageBundle
     * @Description: 获取上个页面传递过来的Bundle数据
     * @return void
     *
     */
    void getLastPageBundle();

    /**
     *
     * @Title: initView
     * @Description: 初始化View
     * @return void
     *
     */
    void initView();

    /**
     *
     * @Title: setListener
     * @Description: 设置页面按下监听
     * @return void
     *
     */
    void setListener();

    /**
     *
     * @Title: initData
     * @Description: 初始化控件数据
     * @return void
     *
     */
    void initData();

    /**
     *
     * @Title: requestNetwork
     * @Description: 请求网络
     * @return void
     *
     */
    void requestNetwork();


}
