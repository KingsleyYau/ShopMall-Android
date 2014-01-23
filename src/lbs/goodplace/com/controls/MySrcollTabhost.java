package lbs.goodplace.com.controls;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.R;
import lbs.goodplace.com.controls.myinterface.onFlingListener;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 可滑动的TABHOST控件
 * @author zhaojunjie
 *
 */
public class MySrcollTabhost extends LinearLayout {
	// 变量
	private Context mContext;
	private List<View> mIconViews = new ArrayList<View>();
	private View.OnClickListener mSelectTabListener;
	private int mSelectIndex = 0;	//选中索引
	private int mForwardIndex = 0;	//之前选中的页面索引
	private int mTabTempIndex = 0 ;		 //保存这次点击的项索引
	private List<SelectTabListenerObj> SelectTabListenerObjList = new ArrayList<MySrcollTabhost.SelectTabListenerObj>();	//分页点击事件
	// 控件
	private MyViewGroup mViewGroup;
	private LinearLayout mLayout_bottom;

	public MySrcollTabhost(Context context) {
		super(context);
		inintView(context);
	}

	public MySrcollTabhost(Context context, AttributeSet attrs) {
		super(context, attrs);
		inintView(context);
	}

	private void inintView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.mysrcolltabhost_view, this);

		mContext = context;
		// 控件
		mViewGroup = (MyViewGroup) findViewById(R.id.myViewGroup);
		mViewGroup.setScrollable(false);
		mViewGroup.SetonFlingListener(new onFlingListener() {

			@Override
			public boolean onFling() {
				//选中第几页
				mSelectIndex = mViewGroup.getCurrentScreenIndex();
				//保存上一次选中的索引
				if(mSelectIndex != mTabTempIndex){
					mForwardIndex = mTabTempIndex;
					mTabTempIndex = mSelectIndex;
				}
				//向应外部设置的向应事件
//				if(mSelectTabListener !=null){
//					mSelectTabListener.onClick(mIconViews.get(mSelectIndex));
//				}
				if(mIconViews.size()>0){
					mIconViews.get(mSelectIndex).setFocusableInTouchMode(true);
					mIconViews.get(mSelectIndex).requestFocus();
					mIconViews.get(mSelectIndex).setFocusableInTouchMode(false);
				}
				return false;
			}
		});
		
		mLayout_bottom = (LinearLayout)findViewById(R.id.srcollhostLayout_bottom);
		
	}

	/**
	 * 添加VIEW到控件中
	 * @param child tab页面
	 * @param tabicon tabicon
	 */
	public void addView(View child, View tabicon) {
		//保存在变量中
		if(tabicon == null){
			mIconViews.add(new View(mContext));
		}else{
			mIconViews.add(tabicon);
		}
		
		//添加到控件中
		mViewGroup.addView(child);
		tabicon.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.FILL_PARENT, 1.0f));
		mLayout_bottom.addView(tabicon);
		//为ICON添加点击事件
		for(int i = 0 ;i < mIconViews.size(); i++){
			final int index = i;
			mIconViews.get(i).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//切换页面
					for(int i = 0 ; i < SelectTabListenerObjList.size(); i ++){
						if(SelectTabListenerObjList.get(i).index == index){
							SelectTabListenerObjList.get(i).OnClickListener.onClick(v);
							Log.i("zjj", "点击分页第:" + index);
//							v.postDelayed(new Runnable() {
//								
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//									mViewGroup.scrollToScreen(index,1);
//								}
//							}, 500);
							return;
						}
					}
					
					mViewGroup.scrollToScreen(index,1);
				}
			});
		}
		//默认指定第一项
		mViewGroup.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mIconViews.get(0).setFocusableInTouchMode(true);
				mIconViews.get(0).requestFocus();
				mIconViews.get(0).setFocusableInTouchMode(false);
			}
		},500);
		
	}
	
	/**
	 * 重定义某个分页切换时的点击事件
	 * @param onClickListener
	 */
	public void AddOnSelectTabListener(int index,View.OnClickListener onClickListener){
//		mSelectTabListener = onClickListener;
		SelectTabListenerObj item = new SelectTabListenerObj(index, onClickListener);
		SelectTabListenerObjList.add(item);
	}

	/**
	 * 返回选中索引
	 * @return
	 */
	public int getSelectIndex(){
		return mSelectIndex;
	}
	
	/**
	 * 返回上一次选中页面索引
	 * @return
	 */
	public int getForwardIndex(){
		return mForwardIndex;
	}
	
	/**
	 * 跳转到指定页面
	 * @param index
	 */
	public void SetCurrentTab(int index){
		mViewGroup.scrollToScreen(index,1);
	}
	
	/**
	 * 分页点击事件
	 * @author zhaojunjie
	 *
	 */
	private class SelectTabListenerObj{
		public int index;
		public View.OnClickListener OnClickListener;
		
		public SelectTabListenerObj(int index, View.OnClickListener onClickListener){
			this.index = index;
			this.OnClickListener = onClickListener;
		}
	}
	
	/**
	 * 设置某一页的数目
	 * @param index
	 */
	public void setTabviewNum(int index,int sum){
		if(index > -1 && index < mIconViews.size()){
			View v = mIconViews.get(index);
			TextView tv = (TextView)v.findViewById(R.id.tabview_number_txtv);
			if(sum > 0){
				tv.setText(sum + "");
				tv.setVisibility(View.VISIBLE);
			}else{
				tv.setVisibility(View.GONE);
			}
		}
	}
}
