package com.jryg.instantcar.download;

/**
 * Created by wangzhaosheng on 2017/12/6.
 */

public interface DownLoadListener {

    void onSuccess();

    void onFail();

    void onPause();

    void onCancel();

    void onProgress(int progress);

}
