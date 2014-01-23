package lbs.goodplace.com.controls;



import lbs.goodplace.com.controls.myinterface.onFlingListener;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class MyViewGroup extends ViewGroup {

	private static final String TAG = "scroller";

	private Scroller scroller;

	private int currentScreenIndex;

	private GestureDetector gestureDetector;
	
	private onFlingListener mFlingListener;

	private ScrollToScreenCallback scrollToScreenCallback;
	
	private boolean scrollable = true;

	public void setScrollable(boolean scrollable) {
		this.scrollable = scrollable;
	}

	public void setScrollToScreenCallback(
			ScrollToScreenCallback scrollToScreenCallback) {
		this.scrollToScreenCallback = scrollToScreenCallback;
	}

	// 设置一个标志位，防止底层的onTouch事件重复处理UP事件
	private boolean fling;

	public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public MyViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public MyViewGroup(Context context) {
		super(context);
		initView(context);
	}

	private void initView(final Context context) {
		this.scroller = new Scroller(context);

		this.gestureDetector = new GestureDetector(new OnGestureListener() {

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				if(scrollable){
					if ((distanceX > 0 && currentScreenIndex < getChildCount() - 1)// 防止移动过最后一页
							|| (distanceX < 0 && getScrollX() > 0)) {// 防止向第一页之前移动
						scrollBy((int) distanceX, 0);
					}
				}
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e) {
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				Log.d(TAG, "min velocity >>>"+ ViewConfiguration.get(context).getScaledMinimumFlingVelocity()+ " current velocity>>" + velocityX);
				if(scrollable){
					if (Math.abs(velocityX) > ViewConfiguration.get(context)
							.getScaledMinimumFlingVelocity()) {// 判断是否达到最小轻松速度，取绝对值的
						if (velocityX > 0 && currentScreenIndex > 0) {
							Log.d(TAG, ">>>>fling to left");
							fling = true;
							scrollToScreen(currentScreenIndex - 1);
						} else if (velocityX < 0
								&& currentScreenIndex < getChildCount() - 1) {
							Log.d(TAG, ">>>>fling to right");
							fling = true;
							scrollToScreen(currentScreenIndex + 1);
						}
					}
				}
//				if(mFlingListener!=null)
//					mFlingListener.onFling();
				
				return true;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
	}
	

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) { 
		Log.d(TAG, ">>left: " + left + " top: " + top + " right: " + right
				+ " bottom:" + bottom);

		/**
		 * 设置布局，将子视图顺序横屏排列
		 */
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			child.setVisibility(View.VISIBLE);
//			child.measure(right - left, bottom - top);
			child.layout(i * getWidth(), 0, getWidth() + i * getWidth(),
					getHeight());

		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), 0);
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			if (!fling) {
				snapToDestination();
			}
			fling = false;
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * 切换到指定屏
	 * 有动画
	 * @param whichScreen
	 */
	public void scrollToScreen(int whichScreen) {
		if (getFocusedChild() != null && whichScreen != currentScreenIndex
				&& getFocusedChild() == getChildAt(currentScreenIndex)) {
			getFocusedChild().clearFocus();
		}

		final int delta = whichScreen * getWidth() - getScrollX();
		scroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta)*2);	//最后一个参数是速度,越小越快
		invalidate();

		currentScreenIndex = whichScreen;
		if (scrollToScreenCallback != null) {
			scrollToScreenCallback
					.callback(currentScreenIndex);
		}
		
		if(mFlingListener!=null)
			mFlingListener.onFling();
	}
	
	/**
	 * 切换到指定屏
	 * 无动画
	 * @param whichScreen
	 * @param speed 速度,0:没滑动效果,数值越大越慢
	 */
	public void scrollToScreen(int whichScreen,int speed) {
		if (getFocusedChild() != null && whichScreen != currentScreenIndex
				&& getFocusedChild() == getChildAt(currentScreenIndex)) {
			getFocusedChild().clearFocus();
		}

		final int delta = whichScreen * getWidth() - getScrollX();
		scroller.startScroll(getScrollX(), 0, delta, 0,Math.abs(delta) * speed);
		invalidate();
		
		currentScreenIndex = whichScreen;
		if (scrollToScreenCallback != null) {
			scrollToScreenCallback
					.callback(currentScreenIndex);
		}
		
		if(mFlingListener!=null)
			mFlingListener.onFling();
		
	}

	/**
	 * 
	 */
	public void scrollToNextPage(){
		if(currentScreenIndex < getChildCount() - 1)
			scrollToScreen(currentScreenIndex + 1);
	}
	
	/**
	 * 
	 */
	public void scrollToForward(){
		if(currentScreenIndex > 0)
			scrollToScreen(currentScreenIndex - 1);
	}
	
	/**
	 * 根据当前x坐标位置确定切换到第几屏
	 */
	private void snapToDestination() {
		scrollToScreen((getScrollX() + (getWidth() / 2)) / getWidth());
	}

	interface ScrollToScreenCallback {
		public void callback(int currentIndex);
	}
	
	/**
	 * 切换tab后事件
	 * @param l
	 */
	public void SetonFlingListener(onFlingListener l){
		mFlingListener = l;
	}
	
	/**
	 * 取当前页码
	 * @return
	 */
	public int getCurrentScreenIndex() {
		return currentScreenIndex;
	}
}
