package com.susu.googleplay.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.susu.googleplay.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.susu.googleplay.bean.CategoryInfo;
import com.susu.googleplay.global.ImageLoaderOptions;
import com.susu.googleplay.http.Url;

import java.util.ArrayList;

public class CategoryAdapter extends BasicAdapter<Object>{

	public CategoryAdapter(Context context, ArrayList<Object> list) {
		super(context, list);
	}
	private final int ITEM_TITLE = 0;//title类型的item
	private final int ITEM_INFO = 1;//info类型的item
	
	/**
	 * 返回item的类型数量
	 */
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	/**
	 * 返回对应position的item类型
	 */
	@Override
	public int getItemViewType(int position) {
		Object object = list.get(position);
		if(object instanceof String){
			return ITEM_TITLE;//title类型
		}else {
			return ITEM_INFO;//info类型
		}
//		return super.getItemViewType(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int itemViewType = getItemViewType(position);
		switch (itemViewType) {
		case ITEM_TITLE:
			if(convertView==null){
				convertView = View.inflate(context, R.layout.adapter_category_title, null);
			}
			TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			String title = (String) list.get(position);
			tv_title.setText(title);
			break;
		case ITEM_INFO:
			if(convertView==null){
				convertView = View.inflate(context, R.layout.adapter_category_info, null);
			}
			CategoryInfoHolder holder = CategoryInfoHolder.getHolder(convertView);
			CategoryInfo categoryInfo = (CategoryInfo) list.get(position);
			holder.tv_name1.setText(categoryInfo.getName1());
			ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+categoryInfo.getUrl1(), holder.iv_image1, ImageLoaderOptions.fadein_options);
			//显示2和3，因为2和3可能没有，所以要判断
			if(!TextUtils.isEmpty(categoryInfo.getUrl2())){
				holder.ll_info2.setVisibility(View.VISIBLE);//恢复为显示
				holder.tv_name2.setText(categoryInfo.getName2());
				ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+categoryInfo.getUrl2(), holder.iv_image2, ImageLoaderOptions.fadein_options);
			}else {
				holder.ll_info2.setVisibility(View.INVISIBLE);
			}
			
			if(!TextUtils.isEmpty(categoryInfo.getUrl3())){
				holder.ll_info3.setVisibility(View.VISIBLE);//恢复为显示
				holder.tv_name3.setText(categoryInfo.getName3());
				ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+categoryInfo.getUrl3(), holder.iv_image3, ImageLoaderOptions.fadein_options);
			}else {
				holder.ll_info3.setVisibility(View.INVISIBLE);
			}
			break;
		}
		return convertView;
	}
	
	
	static class CategoryInfoHolder{
		ImageView iv_image1,iv_image2,iv_image3;
		TextView tv_name1,tv_name2,tv_name3;
		LinearLayout ll_info2,ll_info3;
		
		public CategoryInfoHolder(View convertView){
			iv_image1 = (ImageView) convertView.findViewById(R.id.iv_image1);
			iv_image2 = (ImageView) convertView.findViewById(R.id.iv_image2);
			iv_image3 = (ImageView) convertView.findViewById(R.id.iv_image3);
			tv_name1 = (TextView) convertView.findViewById(R.id.tv_name1);
			tv_name2 = (TextView) convertView.findViewById(R.id.tv_name2);
			tv_name3 = (TextView) convertView.findViewById(R.id.tv_name3);
			ll_info2 = (LinearLayout) convertView.findViewById(R.id.ll_info2);
			ll_info3 = (LinearLayout) convertView.findViewById(R.id.ll_info3);
		}
		
		public static CategoryInfoHolder getHolder(View convertView){
			CategoryInfoHolder holder = (CategoryInfoHolder) convertView.getTag();
			if(holder==null){
				holder = new CategoryInfoHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}

}
