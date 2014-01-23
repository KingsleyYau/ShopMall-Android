package lbs.goodplace.com.View;

import lbs.goodplace.com.R;
import lbs.goodplace.com.controls.myinterface.ListViewLoadMoreListener;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

public class MyListView extends ListView {
	public int mCurrentPage; //已经加载的页数
	public int mTotalPage; //总页数
	private volatile boolean mLoadingNextPage = false; //是否正在加载下一页
	private View mListViewFooterView;
	private ListViewLoadMoreListener mLoadMoreListener;

	public Context mContext;

	public MyListView(Context context) {
		super(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		addListViewFooterView();
		setOnScrollListener(mScrollListener);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	
	
	/**
	 * listview滑动监听器
	 */
	private OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			// 如果是滑到底部，加载下一页
				case OnScrollListener.SCROLL_STATE_IDLE : {
					log("最后显示位置:" + view.getLastVisiblePosition());
					log("listview总数-1:" + (view.getCount() - 1));

					if (view.getLastVisiblePosition() >= (view.getCount() - 1)) {
						loadNextPage();
					}

					if (view.getLastVisiblePosition() >= (view.getCount() - 1) && mCurrentPage >= mTotalPage) {
						Toast.makeText(getContext(), "最后一页", Toast.LENGTH_SHORT).show();
						hideListViewFooter();
//						removeFooterView(mListViewFooterView);
					}

					//列表停止滚动时
					//找出列表可见的第一项和最后一项
					int start = view.getFirstVisiblePosition();
					int end = view.getLastVisiblePosition();
					//如果有添加HeaderView，要减去
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
					//对图片控制器进行位置限制设置
					AsyncImageManager.getInstance().setLimitPosition(start, end);
					//然后解锁通知加载
					AsyncImageManager.getInstance().unlock();
				}
					break;
				case OnScrollListener.SCROLL_STATE_FLING : {
					//列表在滚动，图片控制器加锁
					AsyncImageManager.getInstance().lock();
				}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL : {
					//列表在滚动，图片控制器加锁
					AsyncImageManager.getInstance().lock();
				}
					break;
				default :
					break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
				int totalItemCount) {
		}
	};
	
	/**
	 * <br>功能简述:设置加载更多数据监听器
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param listener
	 */
	public void setLoadMoreListener(ListViewLoadMoreListener listener){
		mLoadMoreListener = listener;
	}

	/**
	 * <br>功能简述:显示底部加载框
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	public void addListViewFooterView(){
		if (mListViewFooterView == null) {
			mListViewFooterView = LayoutInflater.from(mContext).inflate(R.layout.listview_foot_load, null);
		}
		this.addFooterView(mListViewFooterView);
	}
	
	/**
	 * <br>功能简述:设置当前页和总页数
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param totalPage
	 * @param currentPage
	 */
	public void setPageData(int totalPage, int currentPage){
		mTotalPage = totalPage;
		mCurrentPage = currentPage;
	}
	
	
	/**
	 * <br>功能简述:显示底部加载框
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	public void showListViewFooter(){
		if (mListViewFooterView != null) {
			mListViewFooterView.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * <br>功能简述:显示底部加载框
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	public void hideListViewFooter(){
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
		if (mLoadingNextPage) {
			return;
		}
		
//		showListViewFooter();
		
		if (mLoadMoreListener != null) {
			mLoadingNextPage = true;
			mLoadMoreListener.listViewLoadMore();
		}
	}
	
	/**
	 * <br>功能简述:设置已经加载完毕，可以给下一次刷新
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	public void setLoadMoreFinsh(){
		mLoadingNextPage = false;
	}
	
	public void log(String string){
		Log.i("lch1", string);
	}
	
}
