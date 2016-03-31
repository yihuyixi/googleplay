package com.susu.googleplay.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.susu.googleplay.util.CommonUtil;

public abstract class BaseFragment extends Fragment{
	protected ContentPage contentPage;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(contentPage==null){
//			LogUtil.e(this, "创建ContentPage");
			contentPage = new ContentPage(getActivity()) {
				@Override
				public Object loadData() {
					return requestData();
				}
				
				@Override
				public View createSuccessView() {
					return getSuccessView();
				}
			};
		}else {
//			LogUtil.e(this, "使用已经创建的ContentPage");
			//将contentPage从父view中移除
			CommonUtil.removeSelfFromParent(contentPage);
		}
		return contentPage;
	}
	
	/**
	 * 每个子fragment去实现对应的成功View
	 * @return
	 */
	protected abstract View getSuccessView();
	
	/**
	 * 每个子fragment去实现请求数据的过程
	 * @return
	 */
	protected abstract Object requestData();
}
