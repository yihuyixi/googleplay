package com.susu.googleplay.global;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class GooglePlayApplication extends Application{
	private static Context context;//全局Context变量
	private static Handler mainHandler;//主线程的handler
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		context = this;
		mainHandler = new Handler();
		
		
//		new Thread(){
//			public void run() {
//				Looper.prepare();//创建looper对象
//				Looper.loop();//开启looper循环
//				mainHandler = new Handler();
//			};
//		}.start();
		
		initImageLoader(context);
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}
	
	public static Context getContext(){
		return context;
	}
	
	public static Handler getHandler(){
		return mainHandler;
	}
}	
