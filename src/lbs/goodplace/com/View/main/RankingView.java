package lbs.goodplace.com.View.main;

import java.util.ArrayList;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.adapter.MyAdapter;
import lbs.goodplace.com.View.adapter.RankFatherListAdapter;
import lbs.goodplace.com.View.adapter.RankListAdapter;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.controls.RefreshListView;
import lbs.goodplace.com.controls.RefreshListView.OnRefreshListener;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.obj.Commentsituation;
import lbs.goodplace.com.obj.Recommendtags;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;
import lbs.goodplace.com.obj.ShopRankingChildInfo;
import lbs.goodplace.com.obj.ShopRankingInfo;
import lbs.goodplace.com.obj.Signsituation;
import lbs.goodplace.com.obj.parser.ShopListParser;
import lbs.goodplace.com.obj.parser.ShopRankingParser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 排行榜窗口
 * @author zhaojunjie
 *
 */
public class RankingView extends Activity implements View.OnClickListener{
	public Context mContext;
	
	public PopupWindow pwsort;
	public RankFatherListAdapter mRankFatherListAdapter;
	
	
	public TextView mTitle;
	public ImageView mBtnBack; //返回按钮
	public Button mBtn1;
	public Button mBtn2;
	public Button mBtn3;
	public Button mBtn4; //更多排名
	public ArrayList<Button> mBtnList; //排行榜4按钮队列
	
	public static final int LOAD_RANK_SUCCESS = 0; //请求排行版列表成功
	public static final int LOAD_RANK_FAILE = 1; //请求排行版列表失败
	public ArrayList<ShopRankingInfo> mRankingList; // 商家排行版列表	
	public ArrayList<ShopRankingChildInfo> mRankingChildList = new ArrayList<ShopRankingChildInfo>();
	
	
	public ListView mRankListView; //排行旁listview
	public RankListAdapter mRankListAdapter;
	
	
	public int mRankId = -1;
	
	public int mCurPage = 1;
	public int mNextPage = 1;
	public static final int LOAD_SHOP_INFO_SUCCESS = 2;
	public static final int LOAD_SHOP_INFO_FAILE = 3;

	public RefreshListView mListView;
	public MyAdapter mMyAdapter;
	public ArrayList<ShopModule> mDataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ranking_view);
		mContext = this;
		
		initBtn(); //初始化排行榜几个按钮
		
		initRankListView(); //初始化排行版榜列表
		initListView();
		requestRankingList();
//		loadData(mNextPage);
	}

	/**
	 * <br>功能简述:请求排行版列表
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	private void requestRankingList(){
		byte[] postData = JsonRequestManage.getRankingListPostData();
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_SHOP_RANKING_TYEP, postData, new ShopRankingParser(),
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_RANK_SUCCESS;
							mHandler.sendMessage(msg);
							Log.i("lch", "请求成功");
						} else {
							mHandler.sendEmptyMessage(LOAD_RANK_FAILE);
							Log.i("lch", "请求失败");
						}
					}
				});
	}
	
	
	public void requestShopInfo(int curpage) {
		byte[] postData = JsonRequestManage.getShopListOfRankPostData(mRankId, curpage, 20);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_SHOP_LIST_OF_RANKING, postData, new ShopListParser(),
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null && object instanceof ShopListModule) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SHOP_INFO_SUCCESS;
							mHandler.sendMessage(msg);
							Log.i("lch", "请求成功");
						} else {
							mHandler.sendEmptyMessage(LOAD_SHOP_INFO_FAILE);
							Log.i("lch", "请求失败");
						}
						
					}
			
				});
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				//请求排行版成功
				case LOAD_RANK_SUCCESS :
					Object object = msg.obj;
					mRankingList = (ArrayList<ShopRankingInfo>) object;
					mRankingList.get(0).mIsCheck = true;
					//设置前3个按钮的text
					if (mRankingList != null && mRankingList.size() > 0) {
						mRankingChildList = mRankingList.get(0).mChildList;
						mRankListAdapter.updateDataSource(mRankingChildList);
				
						mBtn1.setText(mRankingChildList.get(0).mName);
						mBtn2.setText(mRankingChildList.get(1).mName);
						mBtn3.setText(mRankingChildList.get(2).mName);
					}
					
					mTitle.setText(mRankingList.get(0).mName + mContext.getResources().getString(R.string.rank));

					mRankId = mRankingList.get(0).mId;
					
					clickBtnRequestShopInfo(0); //重新请求数据
					
					
					
					break;
				
				//请求排行版失败
				case LOAD_RANK_FAILE :
					//TODO：
					break;
				
				//请求商家列表成功
				case LOAD_SHOP_INFO_SUCCESS :
					if (mNextPage == 1) {
						mListView.onRefreshComplete();
						mDataSource.clear();
					}
					
					mCurPage = mNextPage;
					mNextPage = mNextPage + 1;
					
					ShopListModule shopListModule = (ShopListModule) msg.obj;
					ArrayList<ShopModule> shopList = shopListModule.mShopList;
					
					if (shopList != null && shopList.size() > 0) {
						mListView.setPageData(shopListModule.mPageInfo.mPagecount , mCurPage);
						mListView.setLoadMoreFinsh();
						mDataSource.addAll(shopList);
						mMyAdapter.notifyDataSetChanged();
					}
					
					break;
				
				//请求商家列表失败
				case LOAD_SHOP_INFO_FAILE :
					Toast.makeText(mContext, "请求商家列表失败", 0).show();
					break;
					
				default :
					break;
			}
		}

	};

	public void addData() {
		Log.i("lch3", "addData()");
		boolean flag = true;
		for (int i = 0; i < 10; i++) {
			ShopModule shopModule = new ShopModule();
			if (flag) {
				shopModule.defaultpicurl = "http://117.79.80.15/html/uploads/allimg/110615/27_110615133150_3_lit.jpg";
				flag = false;
			} else {
				flag = true;
				shopModule.defaultpicurl = "http://sc.admin5.com/uploads/allimg/110625/0Q45G334-2.png";
			}

			shopModule.score = 10;
			shopModule.name = "商家" + i;
			shopModule.address = "广州天河" + i;
			shopModule.phoneno = "87677660"  + i;
			
			ArrayList<Recommendtags> recommendList = new ArrayList<Recommendtags>();
			for (int j = 0; j < 5; j++) {
				Recommendtags recommendtags = new Recommendtags();
				recommendtags.tag = "猪头炒滑蛋" + i;
				recommendList.add(recommendtags);
			}
			shopModule.recommendtags = recommendList;
			
			
			Commentsituation commentsituation = new Commentsituation(); //评论
			commentsituation.total = 100;
			commentsituation.curuser = "风翼浪影";
			commentsituation.star = 40;
			commentsituation.curcomment = "没吃过这么难吃的东西。我靠！！";
			commentsituation.time = System.currentTimeMillis();
			shopModule.commentsituation = commentsituation;
			
			Signsituation signsituation = new Signsituation();
			signsituation.total = 10;
			signsituation.curuser = "小猪X";
			signsituation.detail = "报道啦~~~~！！";
			signsituation.time = System.currentTimeMillis();
			shopModule.signsituation = signsituation;
			
			mDataSource.add(shopModule);
		}
		Log.i("lch3", "addData()-end");
	}

	
	/**
	 * <br>功能简述:初始化排行榜几个按钮
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	public void initBtn(){
		mTitle = (TextView) findViewById(R.id.title);
		mTitle.setOnClickListener(this);
		
		
		mBtnBack = (ImageView) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		
		mBtn1 = (Button) findViewById(R.id.btn1);
		mBtn1.setOnClickListener(this);
//		mBtn1.setBackgroundResource(R.drawable.rank_title_select);
		mBtn1.setTextColor(Color.parseColor("#ffffffff"));
		
		mBtn2 = (Button) findViewById(R.id.btn2);
		mBtn2.setOnClickListener(this);
		
		mBtn3 = (Button) findViewById(R.id.btn3);
		mBtn3.setOnClickListener(this);
		
		mBtn4 = (Button) findViewById(R.id.btn4);
		mBtn4.setOnClickListener(this);
		
		
		mBtnList = new ArrayList<Button>();
	
		mBtnList.add(mBtn1);
		mBtnList.add(mBtn2);
		mBtnList.add(mBtn3);
		mBtnList.add(mBtn4);
		
	}
	
	
	
	public void initRankListView() {
		mRankListView = (ListView) findViewById(R.id.ranking_listview);
		// 初始化adapter
		mRankListAdapter = new RankListAdapter(mContext);
		mRankListView.setAdapter(mRankListAdapter);

		mRankListAdapter.updateDataSource(mRankingChildList);
		mRankListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				clickBtnRequestShopInfo(arg2);
				mListView.setVisibility(View.VISIBLE);
				mRankListView.setVisibility(View.GONE);
				mBtn4.setClickable(true);
			}
		});
	}
	
	
	
	
	
	
	
	
	public void initListView() {
		mListView = (RefreshListView) findViewById(R.id.shopinfo_listview);
		// 初始化adapter
		mMyAdapter = new MyAdapter(mContext);
		mListView.setAdapter(mMyAdapter);

		mDataSource = new ArrayList<ShopModule>();
		mMyAdapter.updateDataSource(mDataSource);
		mListView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefreshData() {
				mCurPage = 1;
				mNextPage = 1;
				requestShopInfo(mNextPage);
			}

			@Override
			public void loadNextPageData() {
				requestShopInfo(mNextPage);
			}
        });
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (position - 1 >= 0 && mDataSource != null && mDataSource.size() > 0) {
					Intent intent = new Intent(mContext, ShopInfoActivity.class);
					int id = mDataSource.get(position - 1).id;
					intent.putExtra(GoodPlaceContants.KEY_SHOP_ID, id);
					mContext.startActivity(intent);
				}
			}
		});
	
	}

	@Override
	public void onClick(View v) {
		if (v == mTitle) {
			if (mRankingList != null ) {
				showSortPW(findViewById(R.id.title_layout));
			}
		}
		
		if (v == mBtnBack) {
			finish();
		}
		
		if (v == mBtn1) {
			setBtnBg(mBtn1);
			clickBtnRequestShopInfo(0); //重新请求数据
			
		}
		
		if (v == mBtn2) {
			setBtnBg(mBtn2);
			clickBtnRequestShopInfo(1); //重新请求数据
		}
		
		if (v == mBtn3) {
			setBtnBg(mBtn3);
			clickBtnRequestShopInfo(2); //重新请求数据
		}
		
		if (v == mBtn4) {
			setBtnBg(mBtn4);
		}
		
	}
	
	/**
	 * <br>功能简述:重新请求数据
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param position
	 */
	public void clickBtnRequestShopInfo(int position){
		if (mRankingList != null && mRankingList.size() > position) {
			//清除目前的数据
			mDataSource.clear();
			mMyAdapter.notifyDataSetChanged();
			
			//设置请求
			mRankId = mRankingChildList.get(position).mId;
			mNextPage = 1;
			mCurPage = 1;
			requestShopInfo(mCurPage);
		}
		
	}
	
	/**
	 * <br>功能简述:设置按钮背景颜色
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param clickBtn
	 */
	public void setBtnBg(Button clickBtn){
		for (int i = 0; i < mBtnList.size(); i++) {
			Button btn = mBtnList.get(i);
//			if (btn == clickBtn) {
//				btn.setBackgroundResource(R.drawable.rank_title_select);
//				btn.setTextColor(Color.parseColor("#ffffffff"));
//				btn.setClickable(false);
//			}else {
//				btn.setBackgroundResource(R.drawable.rank_title_selector);
//				btn.setTextColor(Color.parseColor("#ff000000"));
//				btn.setClickable(true);
//			}
			
			if (btn == clickBtn) {
				btn.setBackgroundColor(Color.parseColor("#ef4136"));
				btn.setClickable(false);
			}else {
				btn.setBackgroundColor(Color.parseColor("#9f966d"));
				btn.setClickable(true);
			}
			btn.setTextColor(Color.parseColor("#ffffffff"));
		}
		
		//更多按钮
		if (clickBtn == mBtn4) {
			mListView.setVisibility(View.GONE);
			mRankListView.setVisibility(View.VISIBLE);
		}else {
			mListView.setVisibility(View.VISIBLE);
			mRankListView.setVisibility(View.GONE);
		}
	}
	
	
	
	/**
	 * 选择排序方式
	 */
	private void showSortPW(View view){
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopupWindow = inflater.inflate(R.layout.rank_father_list_view, null, false);
		// 关闭popwindow事件
		vPopupWindow.setFocusableInTouchMode(true);
		vPopupWindow.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_BACK) && (pwsort.isShowing())) {
					pwsort.dismiss();// 这里写明模拟menu的PopupWindow退出就行
					return true;
				}
				return false;
			}
		});
		
		ListView listView = (ListView)vPopupWindow.findViewById(R.id.listview);
		mRankFatherListAdapter  = new RankFatherListAdapter(mContext);
		listView.setAdapter(mRankFatherListAdapter);
		mRankFatherListAdapter.updateDataSource(mRankingList);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
			
				pwsort.dismiss();
				
				if (mRankingList != null && position < mRankingList.size()) {
					for (int i = 0; i < mRankingList.size(); i++) {
						mRankingList.get(i).mIsCheck = false;
					}
					mRankingList.get(position).mIsCheck = true;
					mRankFatherListAdapter.updateDataSource(mRankingList);
					
					
					mTitle.setText(mRankingList.get(position).mName + mContext.getResources().getString(R.string.rank));
					
					
					
					mRankingChildList = mRankingList.get(position).mChildList;
					mRankListAdapter.updateDataSource(mRankingChildList);
					
					mBtn1.setText(mRankingChildList.get(0).mName);
					mBtn2.setText(mRankingChildList.get(1).mName);
					mBtn3.setText(mRankingChildList.get(2).mName);
					
					setBtnBg(mBtn1);
					clickBtnRequestShopInfo(0); //重新请求数据
				}
			
			}
		});

		
		if (pwsort == null) {
			pwsort = new PopupWindow(vPopupWindow, mListView.getWidth(),mListView.getHeight(), true);
			pwsort.setBackgroundDrawable(new BitmapDrawable());
			pwsort.setOutsideTouchable(true);
		}
		
	    pwsort.showAsDropDown(view);
//		pwsort.showAtLocation(view, Gravity.BOTTOM, location[0],location[1]);
		pwsort.setFocusable(true);
	}
}
