package com.susu.googleplay.ui.adapter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BasicAdapter<T> extends BaseAdapter{
	protected Context context;
	protected ArrayList<T> list;
	//alt+shift+s ->o
	public BasicAdapter(Context context, ArrayList<T> list) {
		super();
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public Object getItem(int position) {
		return null;
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
