package com.susu.googleplay.ui.fragment;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.susu.googleplay.R;
import com.susu.googleplay.util.CommonUtil;

import java.util.List;

/**
 * 页面加载管理类，根据不同的状态显示不同的view
 * @author Administrator
 *
 */
public abstract class ContentPage extends FrameLayout{
	
	private View loadingView;//加载中的view
	private View errorView;//加载失败的view
	private View emptyView;//加载数据为空的view
	private View successView;//加载成功的view

	private PageState mState = PageState.STATE_LOADING;//默认是加载中的状态
	/**
	 * 定义页面状态常量
	 * @author Administrator
	 *
	 */
	public enum PageState{
		STATE_LOADING(0),//加载中的状态
		STATE_SUCCESS(1),//加载成功的状态
		STATE_ERROR(2),//加载失败的状态
		STATE_EMPTY(3);//加载数据为空的状态
		
		private int value;
		PageState(int value){
			this.value = value;
		}
		public int getValue(){
			return value;
		}
	}
	
	public ContentPage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPage();
	}
	public ContentPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPage();
	}
	public ContentPage(Context context) {
		super(context);
		initPage();
	}
	
	/**
	 * 初始化Page
	 */
	private void initPage(){
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		//天然往ContentPage中添加4个状态对应的view，然后根据不同状态，显示不同的view
		//1.添加LoadingView
		if(loadingView==null){
			loadingView = View.inflate(getContext(), R.layout.page_loading, null);
		}
		addView(loadingView, params);
		//2.添加ErrorView
		if(errorView==null){
			errorView = View.inflate(getContext(), R.layout.page_error, null);
			Button btn_reload = (Button) errorView.findViewById(R.id.btn_reload);
			btn_reload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mState = PageState.STATE_LOADING;
					showPage();
					//重新加载
					loadDataAndRefreshPage();
				}
			});
		}
		addView(errorView, params);
		//3.添加EmptyView
		if(emptyView==null){
			emptyView = View.inflate(getContext(), R.layout.page_empty, null);
		}
		addView(emptyView, params);
		//4.添加SuccessView
		if(successView==null){
			successView = createSuccessView();
		}
		if(successView==null){
			throw new IllegalArgumentException("The method createSuccessView() can not return null!");
		}else {
			addView(successView, params);
		}
		
		//5.根据不同的state显示不同的view
		showPage();
		
		//6.请求数据然后刷新View
		loadDataAndRefreshPage();
	}
	
	/**
	 * 请求服务器的数据，然后根据加载的数据刷新View
	 */
	public void loadDataAndRefreshPage(){
		new Thread(){
			public void run() {
				//模拟请求服务器的延时过程
				SystemClock.sleep(1500);
				
				//1.获取加载完成的数据
				Object result = loadData();
				//2.根据数据判断当前page的状态
				mState = checkData(result);
				
				//3.根据最新state，刷新View
				CommonUtil.runOnUIThread(new Runnable() {
					@Override
					public void run() {
						showPage();
					}
				});
				
			}
		}.start();
	}
	
	/**
	 * 根据数据检查对应的状态
	 * @return
	 */
	private PageState checkData(Object result){
		if(result!=null){
			if(result instanceof List){
				List list = (List) result;
				if(list.size()==0){
					return PageState.STATE_EMPTY;//加载数据为空
				}else {
					return PageState.STATE_SUCCESS;//加载成功
				}
			}else {
				return PageState.STATE_SUCCESS;//加载成功
			}
		}else {
			return PageState.STATE_ERROR;//加载失败
		}
	}
	
	/** 
	 * 根据不同的state显示不同的view
	 */
	private void showPage(){
		loadingView.setVisibility(mState==PageState.STATE_LOADING?View.VISIBLE:View.INVISIBLE);
		errorView.setVisibility(mState==PageState.STATE_ERROR?View.VISIBLE:View.INVISIBLE);
		emptyView.setVisibility(mState==PageState.STATE_EMPTY?View.VISIBLE:View.INVISIBLE);
		successView.setVisibility(mState==PageState.STATE_SUCCESS?View.VISIBLE:View.INVISIBLE);
	}
	
	/**
	 * 每个界面的成功view都不一样，应该由每个界面自己提供
	 * @return
	 */
	public abstract View createSuccessView();
	
	/**
	 * 由于每个界面加载数据的过程不一样，我只需要关系它加载回来之后的数据，然后根据数据刷新View
	 * @return
	 */
	public abstract Object loadData();
	
}
