package com.susu.googleplay.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.susu.googleplay.global.GooglePlayApplication;


public class CommonUtil {
	/**
	 * 在主线程中运行
	 * @param r
	 */
	public static void runOnUIThread(Runnable r){
		GooglePlayApplication.getHandler().post(r);
	}
	
	/**
	 * 获取resources对象
	 * @return
	 */
	public static Resources getResources(){
		return GooglePlayApplication.getContext().getResources();
	}
	
	/**
	 * 获取字符串
	 * @param resId
	 * @return
	 */
	public static String getString(int resId){
		return getResources().getString(resId);
	}
	
	public static Drawable getDrawable(int resId){
		return getResources().getDrawable(resId);
	}
	
	/**
	 * 获取dimes值
	 * @param resId
	 * @return
	 */
	public static float getDimens(int resId){
		return getResources().getDimension(resId);
	}
	
	/**
	 * 获取字符串的数组
	 * @param resId
	 * @return
	 */
	public static String[] getStringArray(int resId){
		return getResources().getStringArray(resId);
	}
	
	/**
	 * 将指定的child从其父view中移除
	 * @param child
	 */
	public static void removeSelfFromParent(View child){
		if(child!=null){
			ViewParent parent = child.getParent();
			if(parent instanceof ViewGroup){
				ViewGroup group = (ViewGroup) parent;
				group.removeView(child);//将childView从父view中移除
			}
		}
	}
}
