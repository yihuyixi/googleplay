package com.susu.googleplay.ui.activity;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

import com.susu.googleplay.R;
import com.susu.googleplay.lib.PagerSlidingTab;
import com.susu.googleplay.ui.adapter.MainPagerAdapter;
import com.susu.googleplay.util.LogUtil;


public class MainActivity extends ActionBarActivity {
	private DrawerLayout drawLayout;
	private PagerSlidingTab pagerSlidingTab;
	private ViewPager viewPager;
	private ActionBarDrawerToggle drawerToggle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initActionBar();
	}
	/**
	 * 初始化View
	 */
	private void initView(){
		drawLayout = (DrawerLayout) findViewById(R.id.drawLayout);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		pagerSlidingTab = (PagerSlidingTab) findViewById(R.id.pagerSlidingTab);
		
		viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
		//绑定pagerSlidingTab和ViewPager
		pagerSlidingTab.setViewPager(viewPager);
	}
	
	/**
	 * 初始化ActionBar
	 */
	private void initActionBar(){
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.ic_launcher);
		actionBar.setTitle(getString(R.string.app_name));
		actionBar.setDisplayHomeAsUpEnabled(true);//显示home按钮
		actionBar.setDisplayShowHomeEnabled(true);//设置home按钮可以被点击
		//关联ActionBar和DrawLayout
		drawerToggle = new ActionBarDrawerToggle(this, drawLayout, R.drawable.ic_drawer_am, 0, 0);
		drawerToggle.syncState();//将ActionBar的状态和drawLayout同步起来
		
		//给3条线增加动画
		drawLayout.setDrawerListener(new DrawerListener() {
			@Override
			public void onDrawerStateChanged(int newState) {
				LogUtil.e(this, "newState: "+newState);
				drawerToggle.onDrawerStateChanged(newState);
			}
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				LogUtil.e(this, "slideOffset: "+slideOffset);
				drawerToggle.onDrawerSlide(drawerView, slideOffset);
			}
			@Override
			public void onDrawerOpened(View drawerView) {
				LogUtil.e(this, "onDrawerOpened: ");
				drawerToggle.onDrawerOpened(drawerView);
			}
			@Override
			public void onDrawerClosed(View drawerView) {
				LogUtil.e(this, "onDrawerClosed: ");
				drawerToggle.onDrawerClosed(drawerView);
			}
		});
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//点击ActionBar的home按钮可以弹出DrawLayout
		drawerToggle.onOptionsItemSelected(item);
		return super.onOptionsItemSelected(item);
	}
	

}
