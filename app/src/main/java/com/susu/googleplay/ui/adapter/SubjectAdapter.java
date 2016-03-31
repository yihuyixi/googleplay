package com.susu.googleplay.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.susu.googleplay.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.susu.googleplay.bean.Subject;
import com.susu.googleplay.global.ImageLoaderOptions;
import com.susu.googleplay.http.Url;
import com.susu.googleplay.ui.view.RatioImageView;

import java.util.ArrayList;


public class SubjectAdapter extends BasicAdapter<Subject>{

	public SubjectAdapter(Context context, ArrayList<Subject> list) {
		super(context, list);
	}
	
	private ArrayList<String> loadedUrl = new ArrayList<String>();
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = View.inflate(context, R.layout.adapter_subject, null);
		}
		SubjectHolder holder = SubjectHolder.getHolder(convertView);
		
		Subject subject = list.get(position);
		holder.tv_des.setText(subject.getDes());
		
		if(loadedUrl.contains(subject.getUrl())){
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+subject.getUrl(), holder.iv_image, ImageLoaderOptions.options);
		}else {
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+subject.getUrl(), holder.iv_image, ImageLoaderOptions.fadein_options);
			loadedUrl.add(subject.getUrl());
		}
		
		
		return convertView;
	}
	
	static class SubjectHolder{
		RatioImageView iv_image;
		TextView tv_des;
		
		public SubjectHolder(View convertView){
			iv_image = (RatioImageView) convertView.findViewById(R.id.iv_image);
			tv_des = (TextView) convertView.findViewById(R.id.tv_des);
		}
		
		public static SubjectHolder getHolder(View convertView){
			SubjectHolder subjectHolder = (SubjectHolder) convertView.getTag();
			if(subjectHolder==null){
				subjectHolder = new SubjectHolder(convertView);
				convertView.setTag(subjectHolder);
			}
			return subjectHolder;
		}
	}

}
