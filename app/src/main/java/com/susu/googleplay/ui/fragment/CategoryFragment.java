package com.susu.googleplay.ui.fragment;

import android.view.View;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.susu.googleplay.R;
import com.susu.googleplay.bean.Category;
import com.susu.googleplay.bean.CategoryInfo;
import com.susu.googleplay.http.HttpHelper;
import com.susu.googleplay.http.Url;
import com.susu.googleplay.ui.adapter.CategoryAdapter;
import com.susu.googleplay.util.CommonUtil;
import com.susu.googleplay.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;


public class CategoryFragment extends BaseFragment{
	private ArrayList<Category> list = new ArrayList<Category>();
	private ArrayList<Object> categoryList = new ArrayList<Object>();
	private ListView listView;
	private CategoryAdapter adapter;
	@Override
	protected View getSuccessView() {
		listView = (ListView) View.inflate(getActivity(), R.layout.listview, null);
		adapter = new CategoryAdapter(getActivity(), categoryList);
		listView.setAdapter(adapter);
		return listView;
	}

	@Override
	protected Object requestData() {
		String result = HttpHelper.get(Url.CATEGORY+0);
		list = (ArrayList<Category>) JsonUtil.parseJsonToList(result, new TypeToken<List<Category>>(){}.getType());
		if(list!=null && list.size()>0){
			//更新UI
			//整体数据，将title和categoryinfo视为同等级数据，放入新的集合当中
			for (int i = 0; i < list.size(); i++) {
				Category category = list.get(i);
				//1.将title放入集合
				categoryList.add(category.getTitle());
				//2.将categoryinfo放入集合
				ArrayList<CategoryInfo> infos = category.getInfos();
				categoryList.addAll(infos);
			}
			
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					adapter.notifyDataSetChanged();
				}
			});
		}
		
		return list;
	}
}
