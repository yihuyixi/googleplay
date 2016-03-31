package com.susu.googleplay.ui.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.susu.googleplay.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.susu.googleplay.global.GooglePlayApplication;
import com.susu.googleplay.global.ImageLoaderOptions;
import com.susu.googleplay.http.Url;
import com.susu.googleplay.lib.photoview.PhotoViewAttacher;

import java.util.ArrayList;

public class ImageScaleAdapter extends PagerAdapter{
	private ArrayList<String> list;
	public ImageScaleAdapter(ArrayList<String> list){
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view==object;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = View.inflate(GooglePlayApplication.getContext(), R.layout.adapter_image_scale, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
		
		final PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
		
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+list.get(position), imageView, ImageLoaderOptions.fadein_options,new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
			}
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				attacher.update();//应该在ImageView加载完图片的时候,更新ImageView
			}
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
			}
		});
		
		container.addView(view);
		return view;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		super.destroyItem(container, position, object);
		container.removeView((View) object);
	}

}
