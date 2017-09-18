package com.jryg.instantcar.utils;

import android.util.Log;

/**
 * 
 * @ClassName: CommonLog
 * @Description: 日志类
 * @Author：wangzhaosheng
 * @Date：2016-3-31 上午10:51:27
 * @version V1.0
 *
 */
public class CommonLog {

	public static int LOG_LEVEL = Log.ERROR;

	public static String LOG_TAG = "JRYG_LOG";

	public static void d(String tag, String msg) {
		if (LOG_LEVEL > Log.DEBUG) {
			Log.i(tag, msg);
		}
	}

	public static void d(String msg) {
		if (LOG_LEVEL > Log.DEBUG) {
			Log.i(LOG_TAG, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (LOG_LEVEL > Log.INFO) {
			Log.i(tag, msg);
		}
	}

	public static void i(String msg) {
		if (LOG_LEVEL > Log.INFO) {
			Log.i(LOG_TAG, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (LOG_LEVEL > Log.ERROR) {
			Log.d(tag, msg);
		}
	}

	public static void e(String msg) {
		if (LOG_LEVEL > Log.DEBUG) {
			Log.e(LOG_TAG, msg);
		}
	}

}
