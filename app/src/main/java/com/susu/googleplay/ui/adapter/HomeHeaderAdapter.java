package com.susu.googleplay.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.susu.googleplay.global.GooglePlayApplication;
import com.susu.googleplay.global.ImageLoaderOptions;
import com.susu.googleplay.http.Url;

import java.util.ArrayList;

public class HomeHeaderAdapter extends PagerAdapter{
	private ArrayList<String> list;
	public HomeHeaderAdapter(ArrayList<String> list){
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
//		return list.size();
	}
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view==object;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageView = new ImageView(GooglePlayApplication.getContext());
		imageView.setScaleType(ScaleType.FIT_XY);//s设置图片的四周和imageView对齐
		
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+list.get(position%list.size()), imageView, ImageLoaderOptions.fadein_options);
		
		container.addView(imageView);
		return imageView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		super.destroyItem(container, position, object);
		container.removeView((View) object);
	}

}
