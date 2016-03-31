package com.susu.googleplay.ui.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.susu.googleplay.R;
import com.susu.googleplay.bean.AppInfo;
import com.susu.googleplay.global.GooglePlayApplication;
import com.susu.googleplay.global.ImageLoaderOptions;
import com.susu.googleplay.http.Url;
import com.susu.googleplay.manager.DownloadInfo;
import com.susu.googleplay.manager.DownloadManager;
import com.susu.googleplay.ui.view.ProgressArc;
import com.susu.googleplay.util.CommonUtil;
import com.susu.googleplay.util.LogUtil;

import java.util.ArrayList;

public class HomeAdapter extends BasicAdapter<AppInfo>{
	
	public HomeAdapter(Context context, ArrayList<AppInfo> list) {
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
		holder.resetProgressArc();
		//设置数据
		AppInfo appInfo = list.get(position);
		
		holder.setAppInfo(appInfo);
		
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
	
	
	static class HomeHolder implements DownloadManager.DownloadObserver,OnClickListener{
		ImageView iv_icon;
		TextView tv_name,tv_size,tv_des;
		RatingBar rb_star;
		
		FrameLayout fl_progress_container;
		TextView tv_download_state;
		ProgressArc progressArc;
		
		AppInfo appInfo;
		
		public HomeHolder(View convertView){
			iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			rb_star = (RatingBar) convertView.findViewById(R.id.rb_star);
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			tv_size = (TextView) convertView.findViewById(R.id.tv_size);
			tv_des = (TextView) convertView.findViewById(R.id.tv_des);
			fl_progress_container = (FrameLayout) convertView.findViewById(R.id.fl_progress_container);
			tv_download_state = (TextView) convertView.findViewById(R.id.tv_download_state);
			
			//往fl_progress_container添加进度条
			progressArc = new ProgressArc(GooglePlayApplication.getContext());
			progressArc.setArcDiameter((int) CommonUtil.getDimens(R.dimen.progress_arc_width));//设置直径
			progressArc.setForegroundResource(R.drawable.ic_download);//显示下载图片
			progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//默认是没有进度的
			fl_progress_container.addView(progressArc);
			tv_download_state.setText("下载");
			// 设置进度条的颜色
			progressArc.setProgressColor(GooglePlayApplication.getContext()
								.getResources().getColor(R.color.progress));
			DownloadManager.getInstance().registerDownloadObserver(this);
			
			fl_progress_container.setOnClickListener(this);
			
		}
		
		/**
		 * 重置进度条
		 */
		public void resetProgressArc(){
			progressArc.setForegroundResource(R.drawable.ic_download);//显示下载图片
			progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//默认是没有进度的
			progressArc.setProgress(0, false);
			tv_download_state.setText("下载");
		}
		
		
		public void setAppInfo(AppInfo appInfo){
			this.appInfo = appInfo;
			
			DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(appInfo);
			if(downloadInfo!=null){
				refreshState(downloadInfo);
			}
		}
		
		public static HomeHolder getHolder(View convertView){
			HomeHolder holder = (HomeHolder) convertView.getTag();
			if(holder==null){
				holder = new HomeHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}

		@Override
		public void onDownloadStateChange(final DownloadInfo downloadInfo) {
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					refreshState(downloadInfo);
				}
			});
			
		}
		
		private void refreshState(DownloadInfo downloadInfo){
			if(appInfo==null || appInfo.getId()!=downloadInfo.getId()){
				//说明当前的app和传入downloadInfo不是同一个
				return;
			}
			switch (downloadInfo.getState()) {
			case DownloadManager.STATE_FINISH:
				progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//默认是没有进度的
				progressArc.setForegroundResource(R.drawable.ic_install);
				tv_download_state.setText("安装");
				break;
			case DownloadManager.STATE_PAUSE:
				progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//默认是没有进度的
				progressArc.setForegroundResource(R.drawable.ic_resume);
				tv_download_state.setText("暂停");
				break;
			case DownloadManager.STATE_WAITING:
				progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//默认是没有进度的
				progressArc.setForegroundResource(R.drawable.ic_pause);
				tv_download_state.setText("等待");
				break;
			case DownloadManager.STATE_ERROR:
				progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//默认是没有进度的
				progressArc.setForegroundResource(R.drawable.ic_redownload);
				tv_download_state.setText("重新下载");
				break;
			case DownloadManager.STATE_NONE:
				progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//默认是没有进度的
				progressArc.setForegroundResource(R.drawable.ic_download);
				tv_download_state.setText("下载");
				break;
			}
		}

		@Override
		public void onDownloadProgressChange(final DownloadInfo downloadInfo) {
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					if(appInfo==null || appInfo.getId()!=downloadInfo.getId()){
						//说明当前的app和传入downloadInfo不是同一个
						return;
					}
					float fraction = downloadInfo.getCurrentLength()*100f/downloadInfo.getSize();
					tv_download_state.setText((int)fraction+"%");
					progressArc.setForegroundResource(R.drawable.ic_pause);
					progressArc.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
					LogUtil.e(this, "fraction: "+fraction);
					progressArc.setProgress(fraction/100, false);
				}
			});
		}

		@Override
		public void onClick(View v) {
			DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(appInfo);
			if(downloadInfo==null){
				//直接下载
				DownloadManager.getInstance().download(appInfo);
			}else {
				if(downloadInfo.getState()==DownloadManager.STATE_FINISH){
					//点击应该安装
					DownloadManager.getInstance().intallApk(appInfo);
				}else if (downloadInfo.getState()==DownloadManager.STATE_PAUSE) {
					//应该继续下载
					DownloadManager.getInstance().download(appInfo);
				}else if (downloadInfo.getState()==DownloadManager.STATE_WAITING
						|| downloadInfo.getState()==DownloadManager.STATE_DOWNLOADING) {
					//应该暂停
					DownloadManager.getInstance().pause(appInfo);
				}else if (downloadInfo.getState()==DownloadManager.STATE_ERROR) {
					//应该下载
					DownloadManager.getInstance().download(appInfo);
				}
			}
		}
	}

}
