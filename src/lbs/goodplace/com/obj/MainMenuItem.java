package lbs.goodplace.com.obj;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.view.View;

public class MainMenuItem implements Serializable {
	private int id;
	private int picResId;
	private String name;
	private int count = 0;	//数量
	private boolean mCanDel = false;	//删除按钮是否显示
	private View.OnClickListener mOnClickDelListener;
	private Bitmap mBitmap = null;
	private String mIconUrl = "";
	public boolean isShowBottom = true;
	private String bottomNum1 = "0";	//底部显示的数字1
	private String bottomNum2 = "0";	//底部显示的数字2
	
	public String getmIconUrl() {
		return mIconUrl;
	}
	public void setmIconUrl(String mIconUrl) {
		this.mIconUrl = mIconUrl;
	}
	public Bitmap getmBitmap() {
		return mBitmap;
	}
	public void setmBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}
	public boolean CanDel() {
		return mCanDel;
	}
	/**
	 * 删除按钮是否显示
	 * @param mCanDel
	 */
	public void setCanDel(boolean mCanDel) {
		this.mCanDel = mCanDel;
	}
	
	public View.OnClickListener getonClickDelListener(){
		return mOnClickDelListener;
	}
	public void setonClickDelListener(View.OnClickListener l){
		mOnClickDelListener = l;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	} 
	
	public int getCount() {
		return count;
	}
	/**
	 * 设置消息数目
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}
	public int getPicResId() {
		return picResId;
	}
	public void setPicResId(int picResId) {
		this.picResId = picResId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBottomNum1() {
		return bottomNum1;
	}
	public void setBottomNum1(String bottomNum1) {
		this.bottomNum1 = bottomNum1;
	}
	public String getBottomNum2() {
		return bottomNum2;
	}
	public void setBottomNum2(String bottomNum2) {
		this.bottomNum2 = bottomNum2;
	}
	
	
}
