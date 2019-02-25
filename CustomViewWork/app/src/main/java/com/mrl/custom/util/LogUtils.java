package com.mrl.custom.util;

import android.util.Log;

import com.mrl.custom.BuildConfig;


/**
 * @Description: Log统一管理类
 */
public class LogUtils {

	private LogUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static final String TAG = "Jeagine";

	// 下面四个是默认tag的函数
	public static void i(String msg) {
		if (BuildConfig.DEBUG && msg!=null){
			Log.i(TAG, msg);
		}
	}

	public static void d(String msg) {
		if (BuildConfig.DEBUG && msg!=null)
			Log.d(TAG, msg);
	}

	public static void e(String msg) {
		if (BuildConfig.DEBUG && msg!=null)
			Log.e(TAG, msg);
	}

	public static void v(String msg) {
		if (BuildConfig.DEBUG && msg!=null)
			Log.v(TAG, msg);
	}
	
	public static void w(String msg) {
		if (BuildConfig.DEBUG && msg!=null)
			Log.w(TAG, msg);
	}

	// 下面是传入自定义tag的函数
	public static void i(String tag, String msg) {
		if (BuildConfig.DEBUG && msg!=null && tag != null){
			Log.i(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (BuildConfig.DEBUG  && msg!=null && tag != null)
			Log.d(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (BuildConfig.DEBUG  && msg!=null && tag != null)
			Log.e(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (BuildConfig.DEBUG  && msg!=null && tag != null)
			Log.v(tag, msg);
	}
	
	public static void w(String tag, String msg) {
		if (BuildConfig.DEBUG  && msg!=null && tag != null)
			Log.w(tag, msg);
	}
	
	/** 以级别为 e 的形式输出LOG信息和Throwable */
	public static void e(Throwable tr) {
		if (BuildConfig.DEBUG && tr!=null) {
			Log.e(TAG, "", tr);
		}
	}

}
