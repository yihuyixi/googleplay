package com.susu.googleplay.manager;


import com.susu.googleplay.bean.AppInfo;

/**
 * 封装下载任务的信息
 * @author Administrator
 *
 */
public class DownloadInfo {
	private int id;
	private String donwloadUrl;
	private int state;//状态
	private long currentLength;//已经下载的长度
	private long size;//总大小
	private String path;//下载apk文件的保存路径
	
	/**
	 * 使用AppInfo生成DownloadInfo
	 */
	public static DownloadInfo create(AppInfo appInfo){
		DownloadInfo downloadInfo = new DownloadInfo();
		downloadInfo.setId(appInfo.getId());
		downloadInfo.setDonwloadUrl(appInfo.getDownloadUrl());
		downloadInfo.setSize(appInfo.getSize());
		downloadInfo.setCurrentLength(0);
		downloadInfo.setState(DownloadManager.STATE_NONE);
		
		// /mnt/sdcard/com.heima63.googleplay/download/有缘网.apk
		downloadInfo.setPath(DownloadManager.DOWNLOAD_DIR+"/"+appInfo.getName()+".apk");
		return downloadInfo;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDonwloadUrl() {
		return donwloadUrl;
	}
	public void setDonwloadUrl(String donwloadUrl) {
		this.donwloadUrl = donwloadUrl;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getCurrentLength() {
		return currentLength;
	}
	public void setCurrentLength(long currentLength) {
		this.currentLength = currentLength;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
