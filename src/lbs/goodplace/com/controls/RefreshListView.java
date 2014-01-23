package lbs.goodplace.com.controls;

import java.util.Date;

import lbs.goodplace.com.R;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 下拉刷新Listview
 * 
 * @author mcx
 * 
 */
public class RefreshListView extends ListView {
	private Context mContext;

	private static final String TAG = "listview";
	private final static int RELEASE_TO_REFRESH = 0; // 释放刷新
	private final static int PULL_TO_REFRESH = 1; // 下拉刷新
	private final static int REFRESHING = 2; // 正在刷新
	private final static int DONE = 3; // 完成刷新
	private final static int LOADING = 4;
	private final static int CLICK_REFRESH = 5;
	private int mCurState; // 当前的状态

	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 3;

	private LayoutInflater mInflater;

	private LinearLayout mHeadView;
	private TextView mTipsTextview;
	private TextView mLastUpdatedTextView;
	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;

	private RotateAnimation mAnimation;
	private RotateAnimation mReverseAnimation;

	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean mIsRecored;
	private int mHeadViewWidth;
	private int mHeadViewHeight;
	private int mStartY; // 起始坐标值
	private int mFirstItemIndex; // listview显示第一个的位置
	private boolean mIsBack;
	private OnRefreshListener mRefreshListener;
	private boolean mIsRefreshable;
	private boolean mIsClickRefresh = false; // 是否点击刷新标志

	// ===========addfoot=====
	public int mCurrentPage; // 已经加载的页数
	public int mTotalPage; // 总页数
	private volatile boolean mLoadingNextPage = false; // 是否正在加载下一页
	private View mListViewFooterView;

	public RefreshListView(Context context) {
		super(context);
		init();
		mContext = context;
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();

	}

	private void init() {
		// setCacheColorHint(context.getResources().getColor(R.color.transparent));
		mInflater = LayoutInflater.from(mContext);
		setOnScrollListener(mScrollListener);
		addHeadView();
		addListViewFooterView();
	}

	public void addHeadView() {

		mHeadView = (LinearLayout) mInflater.inflate(R.layout.head, null);
		mArrowImageView = (ImageView) mHeadView
				.findViewById(R.id.head_arrowImageView);
		mArrowImageView.setMinimumWidth(70);
		mArrowImageView.setMinimumHeight(50);
		mProgressBar = (ProgressBar) mHeadView
				.findViewById(R.id.head_progressBar);
		mTipsTextview = (TextView) mHeadView
				.findViewById(R.id.head_tipsTextView);
		mLastUpdatedTextView = (TextView) mHeadView
				.findViewById(R.id.head_lastUpdatedTextView);

		measureView(mHeadView);
		mHeadViewHeight = mHeadView.getMeasuredHeight();
		mHeadViewWidth = mHeadView.getMeasuredWidth();

		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
		mHeadView.invalidate();

//		Log.i("lch3", "width:" + mHeadViewWidth + " height:" + mHeadViewHeight);

		addHeaderView(mHeadView, null, false);

		// 设置listview点击事件
		mHeadView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurState = REFRESHING;
				changeHeaderViewByState();
				onRefresh();
			}
		});

		mAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mAnimation.setInterpolator(new LinearInterpolator());
		mAnimation.setDuration(250);
		mAnimation.setFillAfter(true);

		mReverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseAnimation.setInterpolator(new LinearInterpolator());
		mReverseAnimation.setDuration(200);
		mReverseAnimation.setFillAfter(true);

		mCurState = DONE;
		mIsRefreshable = false;
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (!mIsClickRefresh) {
			if (mIsRefreshable) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 当前最上一行是第一行的 和没有被记录过的。
					if (mFirstItemIndex == 0 && !mIsRecored) {
						mIsRecored = true;
						mStartY = (int) event.getY(); // 记录点击时的Y坐标
						Log.v(TAG, "在down时候记录当前位置");
					}
					break;

				case MotionEvent.ACTION_UP:
					if (mCurState != REFRESHING && mCurState != LOADING) {
						if (mCurState == DONE) {
							// 什么都不做
						}
						if (mCurState == PULL_TO_REFRESH) {
							mCurState = DONE;
							changeHeaderViewByState();

							Log.v(TAG, "由下拉刷新状态，到done状态");
						}
						if (mCurState == RELEASE_TO_REFRESH) {
							mCurState = REFRESHING;
							changeHeaderViewByState();
							onRefresh();

							Log.v(TAG, "由松开刷新状态，到done状态");
						}
					}
					mIsRecored = false; // 释放记录完成一次下拉
					mIsBack = false;
					break;

				case MotionEvent.ACTION_MOVE:
					int tempY = (int) event.getY();

					// 防止第一次点击没有接收到
					if (!mIsRecored && mFirstItemIndex == 0) {
						Log.v(TAG, "在move时候记录下位置");
						mIsRecored = true;
						mStartY = tempY;
					}

					if (mCurState != REFRESHING && mIsRecored
							&& mCurState != LOADING) {

						// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动

						// 可以松手去刷新了
						if (mCurState == RELEASE_TO_REFRESH) {

							setSelection(0);

							// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
							if (((tempY - mStartY) / RATIO < mHeadViewHeight)
									&& (tempY - mStartY) > 0) {
								mCurState = PULL_TO_REFRESH;
								changeHeaderViewByState();

								Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
							}
							// 一下子推到顶了
							else if (tempY - mStartY <= 0) {
								mCurState = DONE;
								changeHeaderViewByState();

								Log.v(TAG, "由松开刷新状态转变到done状态");
							}
							// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
							else {
								// 不用进行特别的操作，只用更新paddingTop的值就行了
							}
						}
						// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
						if (mCurState == PULL_TO_REFRESH) {

							setSelection(0);

							// 下拉到可以进入RELEASE_TO_REFRESH的状态
							if ((tempY - mStartY) / RATIO >= mHeadViewHeight) {
								mCurState = RELEASE_TO_REFRESH;
								mIsBack = true;
								changeHeaderViewByState();

								Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
							}
							// 上推到顶了
							else if (tempY - mStartY <= 0) {
								mCurState = DONE;
								changeHeaderViewByState();

								Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
							}
						}

						// done状态下
						if (mCurState == DONE) {
							if (tempY - mStartY > 0) {
								mCurState = PULL_TO_REFRESH;
								changeHeaderViewByState();
							}
						}

						// 更新headView的size
						if (mCurState == PULL_TO_REFRESH) {
							mHeadView.setPadding(0, -1 * mHeadViewHeight
									+ (tempY - mStartY) / RATIO, 0, 0);

						}

						// 更新headView的paddingTop
						if (mCurState == RELEASE_TO_REFRESH) {
							mHeadView.setPadding(0, (tempY - mStartY) / RATIO
									- mHeadViewHeight, 0, 0);
						}

					}

					break;
				}
			}
		}

		return super.onTouchEvent(event);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		switch (mCurState) {
		case RELEASE_TO_REFRESH:
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
			mTipsTextview.setVisibility(View.VISIBLE);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.startAnimation(mAnimation);
			mTipsTextview.setText(R.string.pull_to_refresh_release_label);

			Log.v(TAG, "当前状态，松开刷新");
			break;

		case PULL_TO_REFRESH:
			mProgressBar.setVisibility(View.GONE);
			mTipsTextview.setVisibility(View.VISIBLE);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (mIsBack) {
				mIsBack = false;
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mReverseAnimation);

				mTipsTextview.setText(R.string.pull_to_refresh_pull_label);
			} else {
				mTipsTextview.setText(R.string.pull_to_refresh_pull_label);
			}
			Log.v(TAG, "当前状态，下拉刷新");
			break;

		case REFRESHING:
			mHeadView.setPadding(0, 0, 0, 0);
			mProgressBar.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);
			mTipsTextview.setText(R.string.pull_to_refresh_refreshing_label);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);

			Log.v(TAG, "当前状态,正在刷新...");
			break;

		case DONE:
			mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
			mProgressBar.setVisibility(View.GONE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setImageResource(R.drawable.arrow);
			mTipsTextview.setText(R.string.pull_to_refresh_pull_label);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);

			Log.v(TAG, "当前状态，done");
			break;

		// 点击刷新
		case CLICK_REFRESH:
			mHeadView.setPadding(0, 0, 0, 0);
			mProgressBar.setVisibility(View.GONE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);
			mTipsTextview.setText(R.string.pull_to_refresh_tap_label);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);
			break;
		}
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.mRefreshListener = refreshListener;
		mIsRefreshable = true;
	}

	public interface OnRefreshListener {
		public void onRefreshData();
		
		public void loadNextPageData();
	}

	public void onRefreshComplete() {
		if (mIsClickRefresh) {
			if (getAdapter().getCount() > 1 + getFooterViewsCount()) {
				hideHeadView();
			} else {
				setHeadViewVisible();
			}
		} else {
			mCurState = DONE;
			mLastUpdatedTextView.setText(mContext.getResources().getString(
					R.string.pull_to_refresh_latest_label)
					+ new Date().toLocaleString());
			changeHeaderViewByState();
		}
	}

	private void onRefresh() {
		if (mRefreshListener != null) {
			mRefreshListener.onRefreshData();
		}
	}

	// 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
	private void measureView(View child) {
		ViewGroup.LayoutParams params = child.getLayoutParams();
		if (params == null) {
			params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0,
				params.width);
		int lpHeight = params.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter) {
		mLastUpdatedTextView.setText(mContext.getResources().getString(
				R.string.pull_to_refresh_latest_label)
				+ new Date().toLocaleString());
		// if(!getClearRefresh()){
		// if(adapter.getCount()>0){
		// hideHeadView();
		// }else{
		// setHeadViewVisible();
		// }
		// }
		super.setAdapter(adapter);
	}

	/**
	 * 显示head，代表没有数据，点击可以刷新
	 */
	public void setHeadViewVisible() {
		mIsClickRefresh = true;
		mCurState = CLICK_REFRESH;
		mHeadView.setClickable(true);
		changeHeaderViewByState();
	}

	public void hideHeadView() {
		mIsClickRefresh = false;
		mCurState = DONE;
		mHeadView.setClickable(false);
		changeHeaderViewByState();
	}

	/**
	 * listview滑动监听器
	 */
	private OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			// 如果是滑到底部，加载下一页
			case OnScrollListener.SCROLL_STATE_IDLE: {
				if (view.getLastVisiblePosition() >= (view.getCount() - 1)) {
					loadNextPage();
				}

				if (view.getLastVisiblePosition() >= (view.getCount() - 1) && mCurrentPage >= mTotalPage) {
//					Toast.makeText(getContext(), "最后一页", Toast.LENGTH_SHORT).show();
					hideListViewFooter();
					// removeFooterView(mListViewFooterView);
				}

				// 列表停止滚动时
				// 找出列表可见的第一项和最后一项
				int start = view.getFirstVisiblePosition();
				int end = view.getLastVisiblePosition();
				// 如果有添加HeaderView，要减去
				ListView lisView = null;
				if (view instanceof ListView) {
					lisView = (ListView) view;
				}
				if (lisView != null) {
					int headViewCount = lisView.getHeaderViewsCount();
					start -= headViewCount;
					end -= headViewCount;
				}
				if (end >= view.getCount()) {
					end = view.getCount() - 1;
				}
				// 对图片控制器进行位置限制设置
				AsyncImageManager.getInstance().setLimitPosition(start, end);
				// 然后解锁通知加载
				AsyncImageManager.getInstance().unlock();
			}
				break;
			case OnScrollListener.SCROLL_STATE_FLING: {
				// 列表在滚动，图片控制器加锁
				AsyncImageManager.getInstance().lock();
			}
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: {
				// 列表在滚动，图片控制器加锁
				AsyncImageManager.getInstance().lock();
			}
				break;
			default:
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			mFirstItemIndex = firstVisibleItem;
		}
	};

	/**
	 * <br>
	 * 功能简述:显示底部加载框 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	public void addListViewFooterView() {
		if (mListViewFooterView == null) {
			mListViewFooterView = mInflater.inflate(
					R.layout.listview_foot_load, null);
		}
		this.addFooterView(mListViewFooterView);
	}

	/**
	 * <br>
	 * 功能简述:设置当前页和总页数 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param totalPage
	 * @param currentPage
	 */
	public void setPageData(int totalPage, int currentPage) {
		Log.i("lch", "totalPage:" + totalPage);
		Log.i("lch", "currentPage:" + currentPage);

		mTotalPage = totalPage;
		mCurrentPage = currentPage;
		if (currentPage >= totalPage) {
			if (mListViewFooterView != null && getFooterViewsCount() > 0) {
				removeFooterView(mListViewFooterView);
			}
		}
		
		else if (currentPage == 1 && currentPage < totalPage) {
			if (mListViewFooterView != null && getFooterViewsCount() == 0) {
				addFooterView(mListViewFooterView);
			}
		}
	}

	/**
	 * <br>
	 * 功能简述:显示底部加载框 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	public void showListViewFooter() {
		log("showListViewFooter---");
		if (mListViewFooterView != null) {
			mListViewFooterView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * <br>
	 * 功能简述:显示底部加载框 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	public void hideListViewFooter() {
		if (mListViewFooterView != null) {
			mListViewFooterView.setVisibility(View.GONE);
		}
	}

	/**
	 * 取下一页的数据
	 */
	private void loadNextPage() {
		log("loadNextPage()----");
		if (mCurrentPage >= mTotalPage) {
			mLoadingNextPage = false;
			return;
		}
		if (mLoadingNextPage || mCurState != DONE) {
			log("正在加载中。。。");
			return;
		}

		 showListViewFooter();

		if (mRefreshListener != null) {
			mLoadingNextPage = true;
			mRefreshListener.loadNextPageData();
		}
	}

	/**
	 * <br>
	 * 功能简述:设置已经加载完毕，可以给下一次刷新 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	public void setLoadMoreFinsh() {
		mLoadingNextPage = false;
	}

	public void log(String string) {
		Log.i("lch1", string);
	}
}
