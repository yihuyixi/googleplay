package com.susu.googleplay.ui.fragment;

import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.susu.googleplay.http.HttpHelper;
import com.susu.googleplay.http.Url;
import com.susu.googleplay.lib.randomlayout.StellarMap;
import com.susu.googleplay.util.ColorUtil;
import com.susu.googleplay.util.CommonUtil;
import com.susu.googleplay.util.JsonUtil;
import com.susu.googleplay.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RecommendFragment extends BaseFragment{

	private StellarMap stellarMap;
	private ArrayList<String> list;

	@Override
	protected View getSuccessView() {
		stellarMap = new StellarMap(getActivity());
		stellarMap.setInnerPadding(15, 15, 15, 15);
		return stellarMap;
	}

	@Override
	protected Object requestData() {
		String result = HttpHelper.get(Url.RECOMMEND+0);
		list = (ArrayList<String>) JsonUtil.parseJsonToList(result, new TypeToken<List<String>>(){}.getType());
		if(list!=null &&list.size()>0){
			//更新UI
			CommonUtil.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					stellarMap.setAdapter(new MyAdapter());
					//默认不加载数据,设置默认显示第几组的数据
					stellarMap.setGroup(0, true);//
					stellarMap.setRegularity(15, 15);//设置x和y方向的view密度，但是这个值不能太大，也不能太小
				}
			});
		}
		
		return list;
	}
	
	class MyAdapter implements StellarMap.Adapter{
		/**
		 * 返回几组数据
		 */
		@Override
		public int getGroupCount() {
			return 3;
		}
		/**
		 * 返回每组多少数据
		 */
		@Override
		public int getCount(int group) {
			return list.size()/3;
		}
		/**
		 * group:当前第几组
		 * position:当前组中的position
		 */
		@Override
		public View getView(int group, int position, View convertView) {
			final TextView textView = new TextView(getActivity());
			int listPosition = group*getCount(group) + position;
//			LogUtil.e(this, "group: "+group  +"  position:"+position);
			textView.setText(list.get(listPosition));
			
			//1.设置随机大小的字体
			Random random = new Random();
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, random.nextInt(8)+14);//14-21
			//2.上随机色,
 			textView.setTextColor(ColorUtil.generateBeautifulColor());
 			
 			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ToastUtil.showToast(textView.getText().toString());
				}
			});
 			
			return textView;
		}
		/**
		 * 并没有什么乱用
		 */
		@Override
		public int getNextGroupOnPan(int group, float degree) {
			return 0;
		}
		/**
		 * 缩放完下组加载哪组的数据
		 * group:当前组
		 */
		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			//0->1->2->0
			return (group+1)%getGroupCount();
		}
		
	}
}
