package com.susu.googleplay.ui.fragment;

import android.view.View;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.susu.googleplay.R;
import com.susu.googleplay.bean.Subject;
import com.susu.googleplay.http.HttpHelper;
import com.susu.googleplay.http.Url;
import com.susu.googleplay.ui.adapter.SubjectAdapter;
import com.susu.googleplay.util.CommonUtil;
import com.susu.googleplay.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;


public class SubjectFragment extends BaseFragment{
	private View view;
	private PullToRefreshListView refreshListView;
	private ListView listView;
	private SubjectAdapter subjectAdapter;
	
	private ArrayList<Subject> list = new ArrayList<Subject>();
	@Override
	protected View getSuccessView() {
		view = View.inflate(getActivity(), R.layout.ptr_listview, null);
		
		initPullToRefreshView();
		
		subjectAdapter = new SubjectAdapter(getActivity(), list);
		listView.setAdapter(subjectAdapter);
		
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
	}

	@Override
	protected Object requestData() {
		String result = HttpHelper.get(Url.SUBJECT+list.size());
		ArrayList<Subject> subjects = (ArrayList<Subject>) JsonUtil.parseJsonToList(result, new TypeToken<List<Subject>>(){}.getType());
		
		if(subjects!=null && subjects.size()>0){
			list.addAll(subjects);
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					//更新adapter
					subjectAdapter.notifyDataSetChanged();
					
					refreshListView.onRefreshComplete();//结束刷新的方法
				}
			});
		}
		
		return list;
	}
}
