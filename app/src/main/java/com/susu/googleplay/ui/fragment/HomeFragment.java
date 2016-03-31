package com.susu.googleplay.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.susu.googleplay.R;
import com.susu.googleplay.bean.AppInfo;
import com.susu.googleplay.bean.Home;
import com.susu.googleplay.http.HttpHelper;
import com.susu.googleplay.http.Url;
import com.susu.googleplay.ui.activity.AppDetailActivity;
import com.susu.googleplay.ui.adapter.HomeAdapter;
import com.susu.googleplay.ui.adapter.HomeHeaderAdapter;
import com.susu.googleplay.util.CommonUtil;
import com.susu.googleplay.util.JsonUtil;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment{
	
	private ArrayList<AppInfo> list = new ArrayList<AppInfo>();
	private HomeAdapter homeAdapter;
	private PullToRefreshListView refreshListView;
	private ListView listView;
	private View headerView;
	private ViewPager viewPager;
	
	private Home home;
	private View view;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(viewPager.getCurrentItem()+1);//设置下一页
			
			handler.sendEmptyMessageDelayed(0, 2000);
		};
	};
	@Override
	protected View getSuccessView() {
		view = View.inflate(getActivity(), R.layout.ptr_listview, null);
		
		initPullToRefreshView();
		initHeaderView();
		
		homeAdapter = new HomeAdapter(getActivity(), list);
		listView.setAdapter(homeAdapter);
		
		return view;
	}
	/**
	 * 初始化下拉刷新VIew
	 */
	private void initPullToRefreshView(){
		refreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		refreshListView.setMode(Mode.BOTH);

		listView = refreshListView.getRefreshableView();
		listView.setDividerHeight(0);//隐藏listview的divider
		listView.setSelector(android.R.color.transparent);//隐藏listview默认的selector
		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if(refreshListView.getCurrentMode()==Mode.PULL_FROM_START){
					//下拉
					CommonUtil.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							refreshListView.onRefreshComplete();//结束刷新的方法
						}
					});
				}else {
					//上拉，就是加载更多
					contentPage.loadDataAndRefreshPage();//重新请求数据然后刷新view
				}
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
//				LogUtil.e(this, "position: "+position);
				Intent intent = new Intent(getActivity(),AppDetailActivity.class);
				intent.putExtra("packageName", list.get(position-2).getPackageName());
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 初始化headerView
	 */
	private void initHeaderView(){
		headerView = View.inflate(getActivity(), R.layout.layout_home_header, null);
		viewPager = (ViewPager) headerView.findViewById(R.id.viewPager);
		
		//宽高比是2.65(width/height)
		int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
		float height = width/2.65f;//得到对应的高
		viewPager.getLayoutParams().height = (int) height;
		viewPager.requestLayout();//相当于setLayoutParams
		
		listView.addHeaderView(headerView);
	}

	@Override
	protected Object requestData() {
		String result = HttpHelper.get(Url.HOME+list.size());
		
		home = JsonUtil.parseJsonToBean(result, Home.class);
		if(home!=null){
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					if(home.getPicture()!=null && home.getPicture().size()>0){
						//说明是第一页请求，有大图的url数据
						HomeHeaderAdapter headerAdapter = new HomeHeaderAdapter(home.getPicture());
						viewPager.setAdapter(headerAdapter);
						//默认设置中间的某个item
						viewPager.setCurrentItem(home.getPicture().size()*100000);
						
						//延时发送消息
						handler.sendEmptyMessageDelayed(0, 2000);
					}
					
					list.addAll(home.getList());
					homeAdapter.notifyDataSetChanged();
					
					refreshListView.onRefreshComplete();//结束刷新的方法
				}
			});
		}
		
		return home;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//清除所有消息和callback
		handler.removeCallbacksAndMessages(null);
	}
}
