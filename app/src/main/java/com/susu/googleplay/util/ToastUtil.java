package com.susu.googleplay.util;


import android.widget.Toast;

import com.susu.googleplay.global.GooglePlayApplication;

public class ToastUtil {
	private static Toast toast;
	/**
	 * 能够连续弹吐司，不用等上个消失
	 * @param text
	 */
	public static void showToast(String text){
		if(toast==null){
			toast = Toast.makeText(GooglePlayApplication.getContext(), text, 0);
		}
		toast.setText(text);
		toast.show();
	}
}
