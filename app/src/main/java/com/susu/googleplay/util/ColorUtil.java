package com.susu.googleplay.util;

import java.util.Random;

import android.graphics.Color;

public class ColorUtil {
	/**
	 * 生成漂亮的颜色
	 * @return
	 */
	public static int generateBeautifulColor(){
		Random random = new Random();
		//为了让生成的颜色不至于太黑或者太白，所以对3个颜色的值进行限定
		int red = random.nextInt(150)+50;//50-200
		int green = random.nextInt(150)+50;//50-200
		int blue = random.nextInt(150)+50;//50-200
		int color = Color.rgb(red, green, blue);//使用r,g,b混合生成一种新的颜色
		return color;
	}
}
