package com.susu.googleplay.ui.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.susu.googleplay.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.susu.googleplay.bean.AppInfo;
import com.susu.googleplay.global.ImageLoaderOptions;
import com.susu.googleplay.http.Url;

import java.util.ArrayList;

public class AppAdapter extends BasicAdapter<AppInfo>{
	
	public AppAdapter(Context context, ArrayList<AppInfo> list) {
		super(context, list);
		
	}
	//用来存放已经加载过的图片的url
	private ArrayList<String> loadedImage = new ArrayList<String>();
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = View.inflate(context, R.layout.adapter_home, null);
		}
		HomeHolder holder = HomeHolder.getHolder(convertView);
		//设置数据
		AppInfo appInfo = list.get(position);
		holder.tv_name.setText(appInfo.getName());
		holder.tv_des.setText(appInfo.getDes());
		holder.rb_star.setRating(appInfo.getStars());
		holder.tv_size.setText(Formatter.formatFileSize(context, appInfo.getSize()));
		
		if(!loadedImage.contains(appInfo.getIconUrl())){
			//显示图片
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+appInfo.getIconUrl(), holder.iv_icon, ImageLoaderOptions.fadein_options);
			loadedImage.add(appInfo.getIconUrl());
		}else {
			//不使用动画显示
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+appInfo.getIconUrl(), holder.iv_icon, ImageLoaderOptions.options);
		}
		
		return convertView;
	}
	
	
	static class HomeHolder{
		ImageView iv_icon;
		TextView tv_name,tv_size,tv_des;
		RatingBar rb_star;
		
		public HomeHolder(View convertView){
			iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			rb_star = (RatingBar) convertView.findViewById(R.id.rb_star);
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			tv_size = (TextView) convertView.findViewById(R.id.tv_size);
			tv_des = (TextView) convertView.findViewById(R.id.tv_des);
		}
		
		public static HomeHolder getHolder(View convertView){
			HomeHolder holder = (HomeHolder) convertView.getTag();
			if(holder==null){
				holder = new HomeHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}

}
