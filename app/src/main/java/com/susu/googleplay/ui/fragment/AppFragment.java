package com.susu.googleplay.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.susu.googleplay.R;
import com.susu.googleplay.bean.AppInfo;
import com.susu.googleplay.http.HttpHelper;
import com.susu.googleplay.http.Url;
import com.susu.googleplay.ui.adapter.AppAdapter;
import com.susu.googleplay.util.CommonUtil;
import com.susu.googleplay.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AppFragment extends BaseFragment{
	private View view;
	private PullToRefreshListView refreshListView;
	private ListView listView;
	
	private ArrayList<AppInfo> list = new ArrayList<AppInfo>();
	private AppAdapter appAdapter;
	@Override
	protected View getSuccessView() {
		view = View.inflate(getActivity(), R.layout.ptr_listview, null);
		
		initPullToRefreshView();
		
		appAdapter = new AppAdapter(getActivity(), list);
		listView.setAdapter(appAdapter);
		
		return view;
	}
	
	/**
	 * 初始化下拉刷新VIew
	 */
	private void initPullToRefreshView(){
		refreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		refreshListView.setMode(PullToRefreshBase.Mode.BOTH);

		listView = refreshListView.getRefreshableView();
		listView.setDividerHeight(0);//隐藏listview的divider
		listView.setSelector(android.R.color.transparent);//隐藏listview默认的selector
		refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if(refreshListView.getCurrentMode()== PullToRefreshBase.Mode.PULL_FROM_START){
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
	}

	@Override
	protected Object requestData() {
		String result = HttpHelper.get(Url.APP+list.size());
		ArrayList<AppInfo> appInfos = (ArrayList<AppInfo>) JsonUtil.parseJsonToList(result, new TypeToken<List<AppInfo>>(){}.getType());
		
		if(appInfos!=null && appInfos.size()>0){
			list.addAll(appInfos);
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					appAdapter.notifyDataSetChanged();
					
					refreshListView.onRefreshComplete();//结束刷新的方法
				}
			});
		}
		
		return list;
	}
	
	/**
	 * 使用JSONObject解析json数据
	 * @param result
	 * @return
	 */
	private ArrayList<AppInfo> parseJson(String result){
		if(TextUtils.isEmpty(result))return null;
		
		ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();
		try {
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				AppInfo appInfo = new AppInfo();
				appInfo.setDes(jsonObject.getString("des"));
				appInfo.setId(jsonObject.getInt("id"));
				appInfo.setSize(jsonObject.getLong("size"));
				appInfo.setStars((float) jsonObject.getDouble("stars"));
				
				appInfos.add(appInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return appInfos;
	}
}
