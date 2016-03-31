package com.susu.googleplay.ui.view;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.susu.googleplay.R;
import com.susu.googleplay.bean.AppInfo;
import com.susu.googleplay.global.ImageLoaderOptions;
import com.susu.googleplay.http.Url;


/**
 * 分离app detail页面的业务逻辑
 * @author Administrator
 *
 */
public class AppInfoUI {
	private ImageView iv_icon;
	private TextView tv_name,tv_donwload_num,tv_version,tv_date,tv_size;
	private RatingBar rb_star;
	private Context context;
	public AppInfoUI(Context context){
		this.context = context;
	}
	
	private AppInfo appInfo;
	public void setData(AppInfo appInfo){
		this.appInfo = appInfo;
		
		
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+appInfo.getIconUrl(), iv_icon, ImageLoaderOptions.fadein_options);
		tv_name.setText(appInfo.getName());
		rb_star.setRating(appInfo.getStars());
		tv_donwload_num.setText("下载："+appInfo.getDownloadNum());
		tv_version.setText("版本："+appInfo.getVersion());
		tv_date.setText("日期："+appInfo.getDate());
		tv_size.setText("大小："+Formatter.formatFileSize(context, appInfo.getSize()));
		
	}
	
	public View getView(){
		View view = View.inflate(context, R.layout.layout_app_detail_info,  null);
		iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
		rb_star = (RatingBar) view.findViewById(R.id.rb_star);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_donwload_num = (TextView) view.findViewById(R.id.tv_donwload_num);
		tv_version = (TextView) view.findViewById(R.id.tv_version);
		tv_date = (TextView) view.findViewById(R.id.tv_date);
		tv_size = (TextView) view.findViewById(R.id.tv_size);
		return view;
	}
}
