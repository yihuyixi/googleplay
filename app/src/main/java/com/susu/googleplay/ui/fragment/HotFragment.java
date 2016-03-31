package com.susu.googleplay.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.susu.googleplay.http.HttpHelper;
import com.susu.googleplay.http.Url;
import com.susu.googleplay.ui.view.FlowLayout;
import com.susu.googleplay.util.ColorUtil;
import com.susu.googleplay.util.CommonUtil;
import com.susu.googleplay.util.DrawableUtil;
import com.susu.googleplay.util.JsonUtil;
import com.susu.googleplay.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class HotFragment extends BaseFragment{

	private ScrollView scrollView;
	private FlowLayout flowLayout;

	@Override
	protected View getSuccessView() {
		scrollView = new ScrollView(getActivity());
		flowLayout = new FlowLayout(getActivity());
		flowLayout.setPadding(10, 10, 10, 10);
		scrollView.addView(flowLayout);
		return scrollView;
	}

	@Override
	protected Object requestData() {
		String result = HttpHelper.get(Url.HOT+0);
		final ArrayList<String> list = (ArrayList<String>) JsonUtil.parseJsonToList(result, new TypeToken<List<String>>(){}.getType());
		
		CommonUtil.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				//根据多少个字符串，往flowLayout里面加入多少个子view
				for (int i = 0; i < list.size(); i++) {
					final TextView textView = new TextView(getActivity());
					textView.setText(list.get(i));
					textView.setTextColor(Color.WHITE);
					textView.setGravity(Gravity.CENTER);
					textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
					textView.setPadding(8, 5, 8, 5);
					Drawable normal = DrawableUtil.generateDrawable(ColorUtil.generateBeautifulColor());
					Drawable pressed = DrawableUtil.generateDrawable(Color.GRAY);
					textView.setBackgroundDrawable(DrawableUtil.generateSelector(pressed, normal));
					
					textView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							ToastUtil.showToast(textView.getText().toString());
						}
					});
					
					flowLayout.addView(textView);
				}
			}
		});
		
		
		return list;
	}
}
