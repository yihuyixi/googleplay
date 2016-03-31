package com.susu.googleplay.manager;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.susu.googleplay.bean.AppInfo;
import com.susu.googleplay.global.GooglePlayApplication;
import com.susu.googleplay.http.HttpHelper;
import com.susu.googleplay.http.Url;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * 下载管理类
 * @author Administrator
 *
 */
public class DownloadManager {
	// /mnt/sdcard/com.heima63.googleplay/download
	public static String DOWNLOAD_DIR = Environment.getExternalStorageDirectory()+"/"+
				GooglePlayApplication.getContext().getPackageName()+"/download";
	//定义下载状态常量
	public static final int STATE_NONE = 0;//
	public static final int STATE_DOWNLOADING = 1;//下载中
	public static final int STATE_PAUSE = 2;//暂停的状态
	public static final int STATE_WAITING = 3;//等待的状态
	public static final int STATE_FINISH = 4;//下载完成
	public static final int STATE_ERROR = 5;//下载失败
	
	//使用单例模式
	private static DownloadManager mInstance = new DownloadManager();
	public static DownloadManager getInstance(){
		return mInstance;
	}
	private DownloadManager(){
		//初始化下载目录
		File file = new File(DOWNLOAD_DIR);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	//下载监听的集合，因为有多个界面需求同时更新UI,也就是多个界面都需要设置监听器
	private ArrayList<DownloadObserver> observerList = new ArrayList<DownloadObserver>();
	//用来保存下载任务信息
	private HashMap<Integer, DownloadInfo> downloadInfoMap = new HashMap<Integer, DownloadInfo>();
	//用来保存下载任务，方便暂停的时候找到该任务，然后将其从线程池中移除
	private HashMap<Integer, DownloadTask> downloadTaskMap = new HashMap<Integer, DownloadTask>();
	
	/**
	 * 获取downloadInfo
	 * @param appInfo
	 * @return
	 */
	public DownloadInfo getDownloadInfo(AppInfo appInfo){
		return downloadInfoMap.get(appInfo.getId());
	}
	
	/**
	 * 下载的方法
	 * @param appInfo
	 */
	public void download(AppInfo appInfo){
		//1.获取下载任务的信息
		DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
		if(downloadInfo==null){
			downloadInfo = DownloadInfo.create(appInfo);//创建下载任务
			downloadInfoMap.put(downloadInfo.getId(), downloadInfo);//将下载任务信息维护起来
		}
		//2.根据下载任务的state来判断，是否能够开始下载：以下情况下可以:none,pause,error
		if(downloadInfo.getState()==STATE_NONE || downloadInfo.getState()==STATE_PAUSE
			|| downloadInfo.getState()==STATE_ERROR){
			//可以开始下载,创建下载任务，并且将任务维护起来
			DownloadTask downloadTask = new DownloadTask(downloadInfo);//创建任务，
			downloadTaskMap.put(downloadInfo.getId(), downloadTask);
			
			//将downloadInfo的状态更改，同时通知所有监听器
			downloadInfo.setState(STATE_WAITING);
			notifyDownloadStateChange(downloadInfo);//通知监听器更新UI
			
			ThreadPoolManager.getInstance().execute(downloadTask);//将下载任务交给线程池管理
		}
	}
	
	/**
	 * 下载任务
	 * @author Administrator
	 *
	 */
	class DownloadTask implements Runnable{
		private DownloadInfo downloadInfo;
		public DownloadTask(DownloadInfo downloadInfo){
			this.downloadInfo =downloadInfo;
		}
		@Override
		public void run() {
			//3.一旦run方法执行，意味着开始下载了
			downloadInfo.setState(STATE_DOWNLOADING);
			notifyDownloadStateChange(downloadInfo);//通知监听器更新UI
			
			//4.开始真正下载，两种情况:a.从头开始下载    b.断点下载
			HttpHelper.HttpResult result = null;
			File file = new File(downloadInfo.getPath());
			if(!file.exists() || file.length()!=downloadInfo.getCurrentLength()){
				//从头下载或者文件无效的情况
				file.delete();//删除无效文件
				downloadInfo.setCurrentLength(0);//重置已经下载的长度
				
				String url = String.format(Url.DOWNLOAD, downloadInfo.getDonwloadUrl());
				result = HttpHelper.download(url);
			}else {
				//断点下载的情况
				String url = String.format(Url.BREAK_DOWNLOAD, downloadInfo.getDonwloadUrl(),downloadInfo.getCurrentLength());
				result = HttpHelper.download(url);
			}
			
			//5.获取流对象，进行读写文件
			if(result!=null && result.getInputStream()!=null){
				//说明真正获取到了数据，开始文件的读写
				InputStream is = result.getInputStream();
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file, true);
					
					byte[] buffer = new byte[1024*8];//8k的缓冲区
					int len = -1;
					while((len=is.read(buffer))!=-1 && downloadInfo.getState()==STATE_DOWNLOADING){
						fos.write(buffer, 0, len);
						fos.flush();
						//更新currentLength,通知进度更新
						downloadInfo.setCurrentLength(downloadInfo.getCurrentLength()+len);
						notifyDownloadProgressChange(downloadInfo);//通知监听器进度更新
					}
				} catch (Exception e) {
					e.printStackTrace();
					//属于下载失败的情况
					file.delete();//删除无效文件
					downloadInfo.setCurrentLength(0);//重置已经下载的长度
					downloadInfo.setState(STATE_ERROR);
					notifyDownloadStateChange(downloadInfo);//通知监听器更新UI
				} finally{
					//关闭流和链接
					result.close();
					try {
						if(fos!=null)fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				//6.for循环结束走到这里，a.下载完成     b.暂停      c.判断是否下载出错
				if(file.length()==downloadInfo.getSize() && downloadInfo.getState()==STATE_DOWNLOADING){
					//进入下载完成
					downloadInfo.setState(STATE_FINISH);
					notifyDownloadStateChange(downloadInfo);//通知监听器更新UI
				}else if (downloadInfo.getState()==STATE_PAUSE) {
					notifyDownloadStateChange(downloadInfo);//通知监听器更新UI
				}else if (file.length()!=downloadInfo.getCurrentLength()) {
					//由于不明原因，文件保存有误
					//属于下载失败的情况
					file.delete();//删除无效文件
					downloadInfo.setCurrentLength(0);//重置已经下载的长度
					downloadInfo.setState(STATE_ERROR);
					notifyDownloadStateChange(downloadInfo);//通知监听器更新UI
				}
				
			}else {
				//属于下载失败的情况
				file.delete();//删除无效文件
				downloadInfo.setCurrentLength(0);//重置已经下载的长度
				downloadInfo.setState(STATE_ERROR);
				notifyDownloadStateChange(downloadInfo);//通知监听器更新UI
			}
			
			//task结束后，则需要将其冲downloadTaskMap中移除
			downloadTaskMap.remove(downloadInfo.getId());
		}
		
	}

	/**
	 * 暂停的方法
	 */
	public void pause(AppInfo appInfo){
		DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
		if(downloadInfo!=null){
			//将状态更改为暂停
			downloadInfo.setState(STATE_PAUSE);
			notifyDownloadStateChange(downloadInfo);
			
			//将暂停的task移除线程池，为其他任务留出资源
			DownloadTask downloadTask = downloadTaskMap.get(downloadInfo.getId());
			ThreadPoolManager.getInstance().remove(downloadTask);
		}
	}
	
	/**
	 * 安装apk的方法
	 * @param appInfo
	 */
	public void intallApk(AppInfo appInfo){
		/*<intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:scheme="content" />
        <data android:scheme="file" />
        <data android:mimeType="application/vnd.android.package-archive" />
        </intent-filter>*/
		DownloadInfo downloadInfo = downloadInfoMap.get(appInfo.getId());
		if(downloadInfo!=null){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://"+downloadInfo.getPath()), "application/vnd.android.package-archive");
			GooglePlayApplication.getContext().startActivity(intent);
		}
	}
	
	/**
	 * 注册下载监听
	 * @param downloadObserver
	 */
	public void registerDownloadObserver(DownloadObserver downloadObserver){
		if(!observerList.contains(downloadObserver)){
			observerList.add(downloadObserver);
		}
	}
	
	/**
	 * 取消注册监听
	 * @param downloadObserver
	 */
	public void unregisterDownloaObserver(DownloadObserver downloadObserver){
		if(observerList.contains(downloadObserver)){
			observerList.remove(downloadObserver);
		}
	}
	
	/**
	 * 通知下载状态改变
	 */
	public void notifyDownloadStateChange(DownloadInfo downloadInfo){
		for (DownloadObserver observer : observerList) {
			observer.onDownloadStateChange(downloadInfo);
		}
	}
	
	/**
	 * 通知下载进度改变
	 */
	public void notifyDownloadProgressChange(DownloadInfo downloadInfo){
		for (DownloadObserver observer : observerList) {
			observer.onDownloadProgressChange(downloadInfo);
		}
	}
	
	
	/**
	 * 下载监听
	 * @author Administrator
	 *
	 */
	public interface DownloadObserver{
		void onDownloadStateChange(DownloadInfo downloadInfo);
		void onDownloadProgressChange(DownloadInfo downloadInfo);
	}
	
}
