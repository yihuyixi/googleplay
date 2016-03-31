package com.susu.googleplay.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.susu.googleplay.R;
import com.susu.googleplay.bean.AppInfo;
import com.susu.googleplay.bean.AppSafe;
import com.susu.googleplay.global.ImageLoaderOptions;
import com.susu.googleplay.http.HttpHelper;
import com.susu.googleplay.http.Url;
import com.susu.googleplay.manager.DownloadInfo;
import com.susu.googleplay.manager.DownloadManager;
import com.susu.googleplay.ui.fragment.ContentPage;
import com.susu.googleplay.util.CommonUtil;
import com.susu.googleplay.util.JsonUtil;

import java.util.ArrayList;

public class AppDetailActivity extends ActionBarActivity implements OnClickListener
		,DownloadManager.DownloadObserver {
	private String packageName;
	private ContentPage contentPage;
	private AppInfo appInfo;
	private ScrollView sv_container;
	//app info模块
	private ImageView iv_icon;
	private TextView tv_name,tv_donwload_num,tv_version,tv_date,tv_size;
	private RatingBar rb_star;
	//app safe模块
	private ImageView iv_safe1,iv_safe2,iv_safe3,iv_safe_arrow;
	private ImageView iv_safe_des1,iv_safe_des2,iv_safe_des3;
	private TextView tv_safe_des1,tv_safe_des2,tv_safe_des3;
	private LinearLayout ll_safe_des_container,ll_app_safe_des2,ll_app_safe_des3;
	private RelativeLayout rl_safe_container;
	private int appSafeHeight;//app描述模块原来的高度
	private boolean isExtendAppSafeDes = false;//app安全描述是否展开
	//app screen模块
	private ImageView iv_screen1,iv_screen2,iv_screen3,iv_screen4,iv_screen5;
	//app des模块
	private TextView tv_des,tv_author;
	private ImageView iv_des_arrow;
	private LinearLayout ll_app_des_container;
	private int appDesMinHeight;//app 描述区域5行时候的高度
	private int appDesMaxHeight;//app 描述区域所有内容的高度
	private boolean isExtendAppDes = false;//app描述模块是否显示完全
	//app download模块
	private ProgressBar pb_progress;
	private Button btn_download;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentPage = new ContentPage(this) {
			@Override
			public Object loadData() {
				return requestData();
			}
			@Override
			public View createSuccessView() {
				View view = View.inflate(AppDetailActivity.this, R.layout.activity_detail, null);
				sv_container = (ScrollView) view.findViewById(R.id.sv_container);
				//1.初始化app info模块
				iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				rb_star = (RatingBar) view.findViewById(R.id.rb_star);
				tv_name = (TextView) view.findViewById(R.id.tv_name);
				tv_donwload_num = (TextView) view.findViewById(R.id.tv_donwload_num);
				tv_version = (TextView) view.findViewById(R.id.tv_version);
				tv_date = (TextView) view.findViewById(R.id.tv_date);
				tv_size = (TextView) view.findViewById(R.id.tv_size);
				//2.初始化app safe模块
				iv_safe1 = (ImageView) view.findViewById(R.id.iv_safe1);
				iv_safe2 = (ImageView) view.findViewById(R.id.iv_safe2);
				iv_safe3 = (ImageView) view.findViewById(R.id.iv_safe3);
				iv_safe_arrow = (ImageView) view.findViewById(R.id.iv_safe_arrow);
				iv_safe_des1 = (ImageView) view.findViewById(R.id.iv_safe_des1);
				iv_safe_des2 = (ImageView) view.findViewById(R.id.iv_safe_des2);
				iv_safe_des3 = (ImageView) view.findViewById(R.id.iv_safe_des3);
				tv_safe_des1 = (TextView) view.findViewById(R.id.tv_safe_des1);
				tv_safe_des2 = (TextView) view.findViewById(R.id.tv_safe_des2);
				tv_safe_des3 = (TextView) view.findViewById(R.id.tv_safe_des3);
				ll_app_safe_des2 = (LinearLayout) view.findViewById(R.id.ll_app_safe_des2);
				ll_app_safe_des3 = (LinearLayout) view.findViewById(R.id.ll_app_safe_des3);
				ll_safe_des_container = (LinearLayout) view.findViewById(R.id.ll_safe_des_container);
				rl_safe_container = (RelativeLayout) view.findViewById(R.id.rl_safe_container);
				//3.初始化app screen模块
				iv_screen1 = (ImageView) view.findViewById(R.id.iv_screen1);
				iv_screen2 = (ImageView) view.findViewById(R.id.iv_screen2);
				iv_screen3 = (ImageView) view.findViewById(R.id.iv_screen3);
				iv_screen4 = (ImageView) view.findViewById(R.id.iv_screen4);
				iv_screen5 = (ImageView) view.findViewById(R.id.iv_screen5);
				//4.初始化app des模块
				tv_des = (TextView) view.findViewById(R.id.tv_des);
				tv_author = (TextView) view.findViewById(R.id.tv_author);
				iv_des_arrow = (ImageView) view.findViewById(R.id.iv_des_arrow);
				ll_app_des_container = (LinearLayout) view.findViewById(R.id.ll_app_des_container);
				//5.初始化app downlaod模块
				pb_progress = (ProgressBar) view.findViewById(R.id.pb_progress);
				btn_download = (Button) view.findViewById(R.id.btn_download);
				btn_download.setOnClickListener(AppDetailActivity.this);
				return view;
			}
		};
		setContentView(contentPage);//将ContentPage作为Activity的界面
		
		
		packageName = getIntent().getStringExtra("packageName");
		
		setActionBar();
		
		//注册下载监听器
		DownloadManager.getInstance().registerDownloadObserver(this);
	}
	/**
	 * 请求数据
	 * @return
	 */
	private Object requestData(){
		String result = HttpHelper.get(String.format(Url.DETAIL, packageName));
		appInfo = JsonUtil.parseJsonToBean(result, AppInfo.class);
		if(appInfo!=null){
			//更新UI
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					refreshUI();
				}
			});
		}
		
		return appInfo;
	}
	/**
	 * 更新UI
	 */
	private void refreshUI(){
		//1. 显示app info 模块
		showAppInfo();
		//2. 显示app safe模块
		showAppSafe();
		//3. 显示app safe模块
		showAppScreen();
		//4. 显示app 描述模块
		showAppDes();
		//5.根据downlaodInfo的state显示不同的文字
		DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(appInfo);
		if(downloadInfo!=null){
			refreshUIByState(downloadInfo);
			
		}
	}
	/**
	 * 显示app info 模块
	 */
	private void showAppInfo(){
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+appInfo.getIconUrl(), iv_icon, ImageLoaderOptions.fadein_options);
		tv_name.setText(appInfo.getName());
		rb_star.setRating(appInfo.getStars());
		tv_donwload_num.setText("下载："+appInfo.getDownloadNum());
		tv_version.setText("版本："+appInfo.getVersion());
		tv_date.setText("日期："+appInfo.getDate());
		tv_size.setText("大小："+Formatter.formatFileSize(this, appInfo.getSize()));
	}
	/**
	 * 显示app safe模块
	 */
	private void showAppSafe(){
		rl_safe_container.setOnClickListener(this);
		ArrayList<AppSafe> safe = appInfo.getSafe();
		AppSafe appSafe1 = safe.get(0);//第一个安全bean
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+appSafe1.getSafeUrl(), iv_safe1, ImageLoaderOptions.fadein_options);
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+appSafe1.getSafeDesUrl(), iv_safe_des1, ImageLoaderOptions.fadein_options);
		tv_safe_des1.setText(appSafe1.getSafeDes());
		//显示第2个和第3个的时候需要判断有没有对应的数据
		if(safe.size()>1){
			AppSafe appSafe2 = safe.get(1);//第一个安全bean
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+appSafe2.getSafeUrl(), iv_safe2, ImageLoaderOptions.fadein_options);
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+appSafe2.getSafeDesUrl(), iv_safe_des2, ImageLoaderOptions.fadein_options);
			tv_safe_des2.setText(appSafe2.getSafeDes());
		}else {
			ll_app_safe_des2.setVisibility(View.GONE);
		}
		if(safe.size()>2){
			AppSafe appSafe3 = safe.get(2);//第一个安全bean
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+appSafe3.getSafeUrl(), iv_safe3, ImageLoaderOptions.fadein_options);
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+appSafe3.getSafeDesUrl(), iv_safe_des3, ImageLoaderOptions.fadein_options);
			tv_safe_des3.setText(appSafe3.getSafeDes());
		}else {
			ll_app_safe_des3.setVisibility(View.GONE);
		}
		
		//1.首先获取ll_safe_des_container原来的高度
		ll_safe_des_container.measure(0, 0);
		appSafeHeight = ll_safe_des_container.getMeasuredHeight();
		
		//2.一开始，让ll_safe_des_container的高度变为0
		ll_safe_des_container.getLayoutParams().height = 0;
		ll_safe_des_container.requestLayout();
		
	}
	
	/**
	 * 显示app screen模块
	 */
	private void showAppScreen(){
		iv_screen1.setOnClickListener(this);
		iv_screen2.setOnClickListener(this);
		iv_screen3.setOnClickListener(this);
		iv_screen4.setOnClickListener(this);
		iv_screen5.setOnClickListener(this);
		
		ArrayList<String> screen = appInfo.getScreen();
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+screen.get(0), iv_screen1, ImageLoaderOptions.fadein_options);
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+screen.get(1), iv_screen2, ImageLoaderOptions.fadein_options);
		ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+screen.get(2), iv_screen3, ImageLoaderOptions.fadein_options);
		//第4个和第5个可能木有，所以要判断
		if(screen.size()>3){
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+screen.get(3), iv_screen4, ImageLoaderOptions.fadein_options);
		}else {
			iv_screen4.setVisibility(View.GONE);
		}
		if(screen.size()>4){
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+screen.get(4), iv_screen5, ImageLoaderOptions.fadein_options);
		}else {
			iv_screen5.setVisibility(View.GONE);
		}
	}
	
	
	
	/**
	 * 显示app 描述模块
	 */
	private void showAppDes(){
		ll_app_des_container.setOnClickListener(this);
		tv_des.setText(appInfo.getDes());
		tv_author.setText(appInfo.getAuthor());
		
		//1.获取tv_des5行时候的高度
		tv_des.setMaxLines(5);//这样就显示5行了
//		tv_des.measure(0, 0); 
		
		tv_des.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				//在用完回调之后立即remove监听，因为只要父view中某个控件的宽高有变化，会仍然执行回调
				tv_des.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				appDesMinHeight = tv_des.getHeight();//获取到app描述模块的最小高度
				
				//2.获取tv_des的总高度
				tv_des.setMaxLines(Integer.MAX_VALUE);//让tv_des显示所有文本
				tv_des.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						//在用完回调之后立即remove监听，因为只要父view中某个控件的宽高有变化，会仍然执行回调
						tv_des.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//						LogUtil.e(this, "height: "+tv_des.getHeight());
						appDesMaxHeight = tv_des.getHeight();//获取到app des全部内容的总高度
						
						//3.将tv_des的height设置为5行的高度
						tv_des.getLayoutParams().height = appDesMinHeight;
						tv_des.requestLayout();
					}
				});
			}
		});
	}
	
	/**
	 * 设置ActionBar
	 */
	private void setActionBar(){
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("应用详情");
		actionBar.setDisplayHomeAsUpEnabled(true);//显示home按钮
		actionBar.setDisplayShowHomeEnabled(true);//设置home按钮可以被点击
	}
	
	/**
	 * 点击ActionBar的home按钮的时候会回调该方法
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home){
			//当我点击的是ActionBar的home按钮的时候才结束当前界面
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onClick(View v) {
		ValueAnimator animator = null;
		switch (v.getId()) {
		case R.id.rl_safe_container:
			if(isExtendAppSafeDes){
				//从appSafeHeight到0
				animator = ValueAnimator.ofInt(appSafeHeight,0);
			}else {
				//从0到appSafeHeight
				animator = ValueAnimator.ofInt(0,appSafeHeight);
			}
			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animator) {
					int animatedValue = (Integer) animator.getAnimatedValue();
//					LogUtil.e(this, "animatedValue:"+animatedValue);
					ll_safe_des_container.getLayoutParams().height = animatedValue;
					ll_safe_des_container.requestLayout();
				}
			});
			animator.addListener(new AppSafeAnimListener());
			animator.setDuration(300);
			animator.start();
			break;
		case R.id.iv_screen1:
			enterImageScaleActivity(0);
			break;
		case R.id.iv_screen2:
			enterImageScaleActivity(1);
			break;
		case R.id.iv_screen3:
			enterImageScaleActivity(2);
			break;
		case R.id.iv_screen4:
			enterImageScaleActivity(3);
			break;
		case R.id.iv_screen5:
			enterImageScaleActivity(4);
			break;
		case R.id.ll_app_des_container:
			if(isExtendAppDes){
				animator = ValueAnimator.ofInt(appDesMaxHeight,appDesMinHeight);
			}else {
				animator = ValueAnimator.ofInt(appDesMinHeight,appDesMaxHeight);
			}
			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animator) {
					int animatedValue = (Integer) animator.getAnimatedValue();
					//将动画当前的值设置为tv_des的高度
					tv_des.getLayoutParams().height = animatedValue;
					tv_des.requestLayout();
					
					//伸展动画的时候整个view需要往上滚动
					if(!isExtendAppDes){
						int dy = tv_des.getHeight()-appDesMinHeight;
//						LogUtil.e(this,"dy: "+dy);
//						sv_container.scrollTo(0, dy);
						sv_container.scrollBy(0, dy);//让ScrollView向上滚动
						
					}
				}
			});
			animator.addListener(new AppDesAnimListener());
			animator.setDuration(300);
			animator.start();
			break;
		case R.id.btn_download:
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
			break;
		}
	}
	/**
	 * 进入大图缩放的界面
	 */
	private void enterImageScaleActivity(int currentItem){
		Intent intent = new Intent(this,ImageScaleActivity.class);
		intent.putStringArrayListExtra("imageUrl", appInfo.getScreen());
		intent.putExtra("currentItem", currentItem);
		startActivity(intent);
	}
	
	/**
	 * AppSafe描述模块的动画监听
	 * @author Administrator
	 *
	 */
	class AppSafeAnimListener implements AnimatorListener{
		@Override
		public void onAnimationCancel(Animator arg0) {
		}
		@Override
		public void onAnimationEnd(Animator arg0) {
			isExtendAppSafeDes = !isExtendAppSafeDes;
//			iv_safe_arrow.setBackgroundResource(isExtendAppSafeDes?R.drawable.arrow_up:R.drawable.arrow_down);
			
			ViewPropertyAnimator.animate(iv_safe_arrow).rotationBy(180).setDuration(300);
//			ViewPropertyAnimator.animate(iv_safe3).rotationBy(180).setDuration(500).setInterpolator(new OvershootInterpolator(2));
//			ViewPropertyAnimator.animate(iv_safe3).rotationBy(180).setDuration(500).setInterpolator(new BounceInterpolator());
//			ViewPropertyAnimator.animate(iv_safe3).translationXBy(20).setDuration(500).setInterpolator(new BounceInterpolator());
//			ViewPropertyAnimator.animate(iv_safe3).scaleXBy(0.2f).setDuration(500).setInterpolator(new BounceInterpolator());
//			ViewPropertyAnimator.animate(iv_safe3).scaleYBy(0.2f).setDuration(500).setInterpolator(new BounceInterpolator());
		}
		@Override
		public void onAnimationRepeat(Animator arg0) {
		}
		@Override
		public void onAnimationStart(Animator arg0) {
		}
	}
	/**
	 * app des 模块的动画监听
	 * @author Administrator
	 *
	 */
	class AppDesAnimListener implements AnimatorListener{
		@Override
		public void onAnimationCancel(Animator arg0) {
		}
		@Override
		public void onAnimationEnd(Animator arg0) {
			isExtendAppDes=!isExtendAppDes;
			iv_des_arrow.setBackgroundResource(isExtendAppDes?R.drawable.arrow_up: R.drawable.arrow_down);
		}
		@Override
		public void onAnimationRepeat(Animator arg0) {
		}
		@Override
		public void onAnimationStart(Animator arg0) {
		}
	}
	
	@Override
	public void onDownloadStateChange(final DownloadInfo downloadInfo) {
		CommonUtil.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				refreshUIByState(downloadInfo);
			}
		});
	}
	
	private void refreshUIByState(DownloadInfo downloadInfo){
		if(appInfo==null || appInfo.getId()!=downloadInfo.getId()){
			//说明当前页面的app不是正在下载的
			return ;
		}
		btn_download.setBackgroundResource(0);
		switch (downloadInfo.getState()) {
		case DownloadManager.STATE_FINISH:
			btn_download.setText("安装");
			break;
		case DownloadManager.STATE_ERROR:
			btn_download.setText("下载错误，重新下载");
			break;
		case DownloadManager.STATE_PAUSE:
			btn_download.setText("继续下载");
			break;
		case DownloadManager.STATE_WAITING:
			btn_download.setText("等待下载");
			break;
		}
		float downloadFraction = downloadInfo.getCurrentLength()*100f/downloadInfo.getSize();
		pb_progress.setProgress((int) downloadFraction);
	}
	
	@Override
	public void onDownloadProgressChange(final DownloadInfo downloadInfo) {
		if(appInfo==null || appInfo.getId()!=downloadInfo.getId()){
			//说明当前页面的app不是正在下载的
			return ;
		}
		CommonUtil.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				btn_download.setBackgroundResource(0);
				float downloadFraction = downloadInfo.getCurrentLength()*100f/downloadInfo.getSize();
				btn_download.setText((int)downloadFraction+"%");
				pb_progress.setProgress((int) downloadFraction);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		DownloadManager.getInstance().unregisterDownloaObserver(this);
	}
}
