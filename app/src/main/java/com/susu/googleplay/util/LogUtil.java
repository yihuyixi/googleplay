package com.susu.googleplay.util;

import android.util.Log;

/**
 * Log管理工具类
 * @author Administrator
 *
 */
public class LogUtil {
	private static boolean isDebug = true;//默认是开发调试模式，在开发完毕之后将其置为false
	
	/**
	 * 打印i级别
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag,String msg){
		if(isDebug){
			Log.i(tag, msg);
		}
	}
	
	public static void i(Object object,String msg){
		if(isDebug){
			Log.i(object.getClass().getSimpleName(), msg);
		}
	}
	
	
	
	/**
	 * 打印e级别
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag,String msg){
		if(isDebug){
			Log.e(tag, msg);
		}
	}
	
	public static void e(Object object,String msg){
		if(isDebug){
			Log.e(object.getClass().getSimpleName(), msg);
		}
	}
}
