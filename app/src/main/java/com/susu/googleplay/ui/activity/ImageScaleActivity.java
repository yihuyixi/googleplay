package com.susu.googleplay.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.susu.googleplay.R;
import com.susu.googleplay.ui.adapter.ImageScaleAdapter;

import java.util.ArrayList;

public class ImageScaleActivity extends Activity{
	private ViewPager viewPager;
	private ImageScaleAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_scale);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		ArrayList<String> list = getIntent().getStringArrayListExtra("imageUrl");
		int currentItem = getIntent().getIntExtra("currentItem", 0);
		adapter = new ImageScaleAdapter(list);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(currentItem);
		
	}
}
