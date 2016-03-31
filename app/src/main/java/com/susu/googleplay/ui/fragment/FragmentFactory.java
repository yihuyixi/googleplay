package com.susu.googleplay.ui.fragment;

import java.util.HashMap;

import android.support.v4.app.Fragment;

public class FragmentFactory {
	//fragment的缓存
	private static HashMap<Integer, Fragment> fragmentCache = new HashMap<Integer, Fragment>();
//	static SparseArray<Fragment> sparseArray;
	/**
	 * 根据position生产不同的fragment
	 * @param position
	 * @return
	 */
	public static Fragment create(int position){
		Fragment fragment = fragmentCache.get(position);
		if(fragment==null){
			switch (position) {
			case 0:
				fragment = new HomeFragment();
				break;
			case 1:
				fragment = new AppFragment();
				break;
			case 2:
				fragment = new GameFragment();
				break;
			case 3:
				fragment = new SubjectFragment();
				break;
			case 4:
				fragment = new RecommendFragment();
				break;
			case 5:
				fragment = new CategoryFragment();
				break;
			case 6:
				fragment = new HotFragment();
				break;
			}
			fragmentCache.put(position, fragment);
		}
		return fragment;
	}
}
