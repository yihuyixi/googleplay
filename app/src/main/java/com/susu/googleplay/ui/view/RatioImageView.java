package com.susu.googleplay.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 根据指定的宽高比(ratio),动态设置自己的高度
 * @author Administrator
 *
 */
public class RatioImageView extends ImageView{
	private float ratio;//宽高比
	public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RatioImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		ratio = attrs.getAttributeFloatValue("http://schemas.android.com/apk/res/res-auto", "ratio", 0f);
	}

	public RatioImageView(Context context) {
		super(context);
	}
	
	/**
	 * MeasureSpec: 测量规则，由size和mode组成
	 * size ： 是具体的大小
	 * mode : 测量模式
	 *      MeasureSpec.AT_MOST:对应的是wrap_content
	 *      MeasureSpec.EXACTLY:对应的是具体的dp值，match_parent
	 *      MeasureSpec.UNSPECIFIED:未定义的，多用于adapter的测量中
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//1.求出ImageView的宽
		int width = MeasureSpec.getSize(widthMeasureSpec);//获取的imageView的宽
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);//获取宽度的测量模式
//		LogUtil.e(this, "width: "+width  + " widthMode :"+widthMode);
		//如果ImageView的宽是match_parent或者具体的dp值，而且ratio!=0
		if(widthMode==MeasureSpec.EXACTLY && getRatio()!=0){
			float height = width/getRatio();//根据宽高比计算出高度
			
			heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);
		}
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public float getRatio() {
		return ratio;
	}

	public void setRatio(float ratio) {
		this.ratio = ratio;
	}
	
}
