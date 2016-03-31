package com.susu.googleplay.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.susu.googleplay.R;
import com.susu.googleplay.ui.fragment.FragmentFactory;
import com.susu.googleplay.util.CommonUtil;

public class MainPagerAdapter extends FragmentPagerAdapter{
	private String[] tabArr;
	public MainPagerAdapter(FragmentManager fm) {
		super(fm);
		tabArr = CommonUtil.getStringArray(R.array.tab_names);
	}

	@Override
	public Fragment getItem(int position) {
		return FragmentFactory.create(position);
	}

	@Override
	public int getCount() {
		return tabArr.length;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return tabArr[position];
	}

}
