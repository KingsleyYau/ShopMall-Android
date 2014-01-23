package com.lee.beautifulgirl.imageBrows;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.lee.beautifulgirl.imageBrows.ImageBrowserScaleView.ScaleTouchListener;
import com.untils.toolproject.effector.SubScreenContainer;
import com.untils.toolproject.effector.SubScreenEffector;
import com.untils.toolsproject.scroller.ScreenScroller;
import com.untils.toolsproject.scroller.ScreenScrollerListener;

/**
 * 
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  yangguanxiang
 * @date  [2012-11-16]
 */
public class ImageScroller extends ViewGroup implements ScreenScrollerListener, SubScreenContainer {

	private ScreenScroller mScroller;
	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;
	private final static int TOUCH_SCROLL_SLOP = 60;
	private int mLastScreen = 0;
	private boolean mRespondMove = true; // 是否响应移位
	// 上次触屏离开的x坐标
	private float mLastMotionX;
	// 当前触屏状态
	private int mTouchState = TOUCH_STATE_REST;
	// 注意：创建滑动的动画，不能删除
	private SubScreenEffector mEffector;
	private Context mContext;
	private ImageScrollerListener mImageScrollerListener;

	public ImageScroller(Context context) {
		super(context);
		mContext = context;
		mScroller = new ScreenScroller(context, this);
		mScroller.setBackgroundAlwaysDrawn(true);
		mScroller.setMaxOvershootPercent(0);
		mScroller.setDuration(200);
		mScroller.setInterpolator(new DecelerateInterpolator(1.5f));
		mEffector = new SubScreenEffector(mScroller);
		mEffector.setDrawQuality(SubScreenEffector.DRAW_QUALITY_LOW);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		int childLeft = 0;
		final int childWidth = r - l;
		int childHeight = 0;
		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				childHeight = child.getMeasuredHeight();
				child.layout(childLeft, 0, childLeft + childWidth, childHeight);
				childLeft += childWidth;
			}
		}
		// 设置总屏数
		mScroller.setScreenCount(getChildCount());
		ScreenScroller.setCycleMode(this, true);
	}

	@Override
	public ScreenScroller getScreenScroller() {

		return mScroller;
	}

	@Override
	public void setScreenScroller(ScreenScroller scroller) {
		mScroller = scroller;

	}

	@Override
	public void onFlingIntercepted() {

	}

	@Override
	public void onScrollStart() {

	}

	@Override
	public void onFlingStart() {

		View focusedChild = getFocusedChild();
		if (focusedChild != null && mScroller.getDstScreen() != getCurrentScreen()) {
			focusedChild.clearFocus();
		}
	}

	@Override
	public void onScrollChanged(int newScroll, int oldScroll) {

	}

	@Override
	public void onScreenChanged(int newScreen, int oldScreen) {
		mLastScreen = oldScreen;
		if (mImageScrollerListener != null) {
			mImageScrollerListener.onScreenChanged(newScreen, oldScreen);
		}
	}

	public int getCurrentScreen() {
		return mScroller.getCurrentScreen();
	}

	@Override
	public void onScrollFinish(int currentScreen) {
		if (mImageScrollerListener != null) {
			mImageScrollerListener.onScrollFinish(currentScreen);
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		mScroller.invalidateScroll();
		if (!mScroller.isFinished()) {
			mScroller.onDraw(canvas);
		} else {
			super.dispatchDraw(canvas);
		}
	}

	@Override
	public void computeScroll() {
		mScroller.computeScrollOffset();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		ImageBrowserScaleView scaleView = getCurrentScaleView();
		int mode = scaleView.getMode();
		final int action = ev.getAction() & MotionEvent.ACTION_MASK;
		if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}
		final float x = ev.getX();
		switch (action) {
			case MotionEvent.ACTION_DOWN : {
				mLastMotionX = x;
				mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
				mRespondMove = true;
				break;
			}
			case MotionEvent.ACTION_MOVE : {
				final int xoffset = (int) (x - mLastMotionX);
				if (Math.abs(xoffset) > TOUCH_SCROLL_SLOP && mRespondMove) {
					mTouchState = TOUCH_STATE_SCROLLING;
					if (mode == ScaleTouchListener.NONE || scaleView.isOverEdge()) {
						mScroller.onTouchEvent(ev, MotionEvent.ACTION_DOWN);
					}
				}
				break;
			}
			case MotionEvent.ACTION_CANCEL :
				mTouchState = TOUCH_STATE_SCROLLING;
				break;
			case MotionEvent.ACTION_UP : {
				mTouchState = TOUCH_STATE_REST;
				break;
			}
			default :
				break;
		}
		if (ev.getPointerCount() == 2) {
			mTouchState = TOUCH_STATE_SCROLLING;
		}
		return mTouchState != TOUCH_STATE_REST;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		ImageBrowserScaleView scaleView = getCurrentScaleView();
		if (scaleView != null && mScroller.isFinished()) {
			scaleView.onScale(event);
		}
		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		ImageBrowserScaleView scaleView = getCurrentScaleView();
		if (scaleView.getMode() != ScaleTouchListener.NONE
				|| (scaleView.isScaled() && !scaleView.isOverEdge())
				|| event.getPointerCount() == 2) {
			return true;
		}
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
			case MotionEvent.ACTION_DOWN :
				mScroller.onTouchEvent(event, action);
				break;

			case MotionEvent.ACTION_MOVE :
				mScroller.onTouchEvent(event, action);
				break;

			case MotionEvent.ACTION_UP :
				mScroller.onTouchEvent(event, action);
				mTouchState = TOUCH_STATE_REST;
				break;
			case MotionEvent.ACTION_CANCEL :
				mTouchState = TOUCH_STATE_REST;
				break;

			default :
				break;

		}
		return true;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		mScroller.setScreenSize(w, h);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int size = getChildCount();
		for (int i = 0; i < size; i++) {
			View childView = getChildAt(i);
			if (childView == null || childView.getLayoutParams() == null) {
				continue;
			}
			if (childView.getVisibility() != GONE) {
				childView.measure(widthMeasureSpec, heightMeasureSpec);
			}
		}
	}

	@Override
	public void drawScreen(Canvas canvas, int screen) {
		View child = getChildAt(screen);
		if (null != child) {
			child.draw(canvas);
		}
	}

	public View getCurrentView() {
		View view = null;
		if (mScroller != null) {
			view = this.getChildAt(mScroller.getDstScreen());
		}
		return view;
	}

	public void addScreenView(View view) {
		// 限制只能加载3屏
		if (getChildCount() >= 3) {
			return;
		}
		this.addView(view);
	}

	public void gotoScreen(int dstScreen, int duration, boolean noElastic) {
		mScroller.gotoScreen(dstScreen, duration, noElastic);
	}

	public int getLastScreen() {
		return mLastScreen;
	}

	@Override
	public void drawScreen(Canvas canvas, int screen, int alpha) {

	}

	/**
	 * 
	 * <br>类描述:
	 * <br>功能详细描述:
	 * 
	 * @author  yangguanxiang
	 * @date  [2012-11-16]
	 */
	public interface ImageScrollerListener {
		public void onScrollFinish(int currentScreen);

		public void onScreenChanged(int newScreen, int oldScreen);
	}

	public void setOnImageScrollerListener(ImageScrollerListener listener) {
		mImageScrollerListener = listener;
	}

	public ImageBrowserScaleView getCurrentScaleView() {
		FrameLayout frameLayout = (FrameLayout) getCurrentView();
		if (frameLayout != null) {
			ImageBrowserScaleView scaleView = (ImageBrowserScaleView) frameLayout.getChildAt(0);
			return scaleView;
		}
		return null;
	}

	public ImageBrowserScaleView getScaleView(int index) {
		FrameLayout frameLayout = (FrameLayout) getChildAt(index);
		if (frameLayout != null) {
			ImageBrowserScaleView scaleView = (ImageBrowserScaleView) frameLayout.getChildAt(0);
			return scaleView;
		}
		return null;
	}
	public void recyle() {
		mImageScrollerListener = null;
		mEffector = null;
	}
}
