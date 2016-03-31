package com.susu.googleplay.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableUtil {
	/**
	 * 对应的shape标签
	 * @param rgb
	 * @return
	 */
	public static GradientDrawable generateDrawable(int rgb){
		GradientDrawable drawable = new GradientDrawable();
		drawable.setShape(GradientDrawable.RECTANGLE);//设置矩形
		drawable.setCornerRadius(5);
		drawable.setColor(rgb);
		return drawable;
	}
	/**
	 * 动态生成selector
	 * @param pressed
	 * @param normal
	 * @return
	 */
	public static StateListDrawable generateSelector(Drawable pressed,Drawable normal){
		StateListDrawable stateListDrawable = new StateListDrawable();
		//设置按下的拖
		stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
		//设置默认状态对应的图片
		stateListDrawable.addState(new int[]{}, normal);
		return stateListDrawable;
	}
}
