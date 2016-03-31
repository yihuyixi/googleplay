package com.susu.googleplay.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FlowLayout extends ViewGroup{
	private int horizontalSpacing = 12;//子view之间的水平间距
	private int verticalSpacing = 12;//行与行的间距
	
	//用于存放所有的line
	private ArrayList<Line> lineList = new ArrayList<Line>();
	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public FlowLayout(Context context) {
		super(context);
	}
	/**
	 * 设置水平间距
	 * @param horizontalSpacing
	 */
	public void setHorizontalSpacing(int horizontalSpacing){
		this.horizontalSpacing = horizontalSpacing;
	}
	/**
	 * 设置竖直间距
	 * @param verticalSpacing
	 */
	public void setVerticalSpacing(int verticalSpacing){
		this.verticalSpacing = verticalSpacing;
	}
	
	/**
	 * 遍历所有子view,对所有的子view进行分行
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//1.获取整个view的宽度
		int width = MeasureSpec.getSize(widthMeasureSpec);//
		//2.获取比较的总宽度,即除去左右2边的padding值的宽度
		int noPaddingWidth = width-getPaddingLeft()-getPaddingRight();
		
		//3.开始遍历子view，进行分行操作
		Line line = null;
		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);
			//为了保证能够获取到view的宽高
			childView.measure(0, 0);
			
			//创建line对象
			if(line==null){
				line = new Line();
			}
			//如果当前line中没有子view,则直接放入，因为要保证每行至少有一个子view
			if(line.getViewList().size()==0){
				line.add(childView);
			}else if (line.getWidth()+horizontalSpacing+childView.getMeasuredWidth()>noPaddingWidth) {
				//换行,但是要保存之前的line
				lineList.add(line);
				
				//将当前child放入新的line中
				line = new Line();
				line.add(childView);
				
				//如果childView是最后一个，也会造成当前line丢失,所以要判断
				if(i==(getChildCount()-1)){
					lineList.add(line);
				}
			}else{
				//加入到本行
				line.add(childView);
				
				//如果childView是最后一个，也会造成当前line丢失,所以要判断
				if(i==(getChildCount()-1)){
					lineList.add(line);
				}
			}
		}
		line = null;
		
		//for循环一旦结束，lineList就存放了所有的line
		//我们需要计算出所有line的高度，然后申请这么大的高度
		int height = getPaddingTop()+getPaddingBottom();
		for (int i = 0; i < lineList.size(); i++) {
			height += lineList.get(i).getHeight();//加上每行的高度
		}
		height += (lineList.size()-1)*verticalSpacing;//加上所有的垂直间距
		
//		LogUtil.e(this, "lineSize: "+lineList.size()  +"  height: "+height);
		
		setMeasuredDimension(width, height);//设置或者向父view申请这么大的宽高
	}
	/**
	 * 对所有的子view进行摆放,按照lineList中的line进行摆放
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		
		for (int i = 0; i < lineList.size(); i++) {
			Line line = lineList.get(i);//取出当前line
			
			if(i>0){
				paddingTop += lineList.get(i-1).getHeight()+verticalSpacing;
			}
			
			ArrayList<View> viewList = line.getViewList();//获取line的所有子view
			//1.计算出留白的值
			float remainSpacing = getMeasuredWidth()-getPaddingLeft()-getPaddingRight()-line.getWidth();
			//2.将留白评价分给每个子view的宽度
			float perSpacing = remainSpacing/viewList.size();
			for (int j = 0; j < viewList.size(); j++) {
				//3.将得到留白值加到childView的宽度上面
				View childView = viewList.get(j);
				int widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childView.getMeasuredWidth()+perSpacing),MeasureSpec.EXACTLY);
				childView.measure(widthMeasureSpec, 0);//重新设置宽高
				
				if(j==0){
					//第一个子view,
					childView.layout(paddingLeft, paddingTop, paddingLeft+childView.getMeasuredWidth()
							, paddingTop+childView.getMeasuredHeight());
				}else {
					View preChildView =  viewList.get(j-1);//获取前一个子view
					int left = preChildView.getRight()+horizontalSpacing;//当前left等于前个子view的right+horizontalSpacing
					int top = preChildView.getTop();//当前line的top都和第一个子view的top相等
					childView.layout(left, top,left+childView.getMeasuredWidth(),preChildView.getBottom());
				}
			}
		}
	}
	
	/**
	 * 封装每行的所有子view
	 * @author Administrator
	 *
	 */
	class Line{
		private ArrayList<View> viewList;//用于存放所有的子view
		private int width;//用于记录所有子view的宽度，还包括子view之间的水平间距
		private int height;//用于记录本行的高度
		
		public Line(){
			viewList = new ArrayList<View>();
		}
		/**
		 * 将子view加入到当前line中
		 * @param child
		 */
		public void add(View child){
			if(!viewList.contains(child)){
				//更新width和height
				if(viewList.size()==0){
					width = child.getMeasuredWidth();
				}else {
					//原来的宽+当前child的宽+horizontalSpacing
					width += child.getMeasuredWidth() + horizontalSpacing;//
				}
				//height要取子view中高度最大的那个
				height = Math.max(height, child.getMeasuredHeight());
				
				viewList.add(child);
			}
		}
		
		public ArrayList<View> getViewList(){
			return viewList;
		}
		
		public int getWidth(){
			return width;
		}
		
		public int getHeight(){
			return height;
		}
	}
	
}
