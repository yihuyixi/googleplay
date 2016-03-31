package com.susu.googleplay.global;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.susu.googleplay.R;

public class ImageLoaderOptions {
	//不带动画的options
	public static DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_default)
			.showImageForEmptyUri(R.drawable.ic_default)
			.showImageOnFail(R.drawable.ic_default).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true)
			.displayer(new SimpleBitmapDisplayer()).build();
	
	//带动画的options
	public static DisplayImageOptions fadein_options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.ic_default)
	.showImageForEmptyUri(R.drawable.ic_default)
	.showImageOnFail(R.drawable.ic_default).cacheInMemory(true)
	.cacheOnDisk(true).considerExifParams(true)
	.displayer(new FadeInBitmapDisplayer(500)).build();
	// .displayer(new RoundedBitmapDisplayer(5)).build();;
}
