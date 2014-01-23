package lbs.goodplace.com.View.main;

import java.util.ArrayList;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.adapter.ShopCommentAdapter;
import lbs.goodplace.com.View.login.LoginActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.controls.RefreshListView;
import lbs.goodplace.com.controls.RefreshListView.OnRefreshListener;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.util.GlobalUtil;
import lbs.goodplace.com.manage.util.MyMothod;
import lbs.goodplace.com.obj.ShopCommentListInfo;
import lbs.goodplace.com.obj.ShopCommontInfo;
import lbs.goodplace.com.obj.UserInfo;
import lbs.goodplace.com.obj.parser.ShopCommentParser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 商家评论
 * @author shazhuzhu
 *
 */
public class ShopCommontActivity extends ModuleActivity implements OnClickListener{
	public static String KEY_MYSIGN = "KEY_MYSIGN";
	
	public Context mContext;
	public ImageView mBtnBack;
	public Button mBtnAddComment;
	public TextView mShopName;
	public RefreshListView mListView;
	public ShopCommentAdapter mAdapter;
	ArrayList<ShopCommontInfo> mDataSource = new ArrayList<ShopCommontInfo>();
	
	public int mShopId = -1;
	public int mCurPage = 1;
	public int mNextPage = 1;
	private boolean mIsMysign = false;	//是否查看我的留言
	
	//缓存
	private String CACHE_NAME = "ShopCommontActivity";
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	private NetState mNetState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.shop_commont_view);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.shop_commont_view, mLayout_body);
		
		mContext = this;
		mNetState = new NetState(mContext);
		
//		mBtnBack = (ImageView) findViewById(R.id.btnBack);
//		mBtnBack.setOnClickListener(this);
//		mBtnAddComment = (ImageView) findViewById(R.id.btn_add_commnet);
//		mBtnAddComment.setOnClickListener(this);
		
		
//		mShopName = (TextView) findViewById(R.id.shopName);
		
		initListView();
		
		//取参数
        Bundle budle = getIntent().getExtras();
		if(budle != null ){
			if(budle.get(GoodPlaceContants.KEY_SHOP_ID) != null){
				this.mShopId = budle.getInt(GoodPlaceContants.KEY_SHOP_ID);
			}
			
			if(budle.get(KEY_MYSIGN) != null){
				this.mIsMysign = budle.getBoolean(KEY_MYSIGN);
			}
		}
		
		if (mShopId == -1 && !mIsMysign) {
			finish();
			return;
		}
		
		initTitle();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		mCurPage = 1;
		mNextPage = 1;
		loadData(mCurPage);
	}
	
	
	public void initListView() {
		mListView = (RefreshListView) findViewById(R.id.listview);
		// 初始化adapter
		mAdapter = new ShopCommentAdapter(mContext);
		mListView.setAdapter(mAdapter);
		mAdapter.updateDataSource(mDataSource);
		
		mListView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefreshData() {
				mCurPage = 1;
				mNextPage = 1;
				loadData(mNextPage);
			}

			@Override
			public void loadNextPageData() {
				loadData(mNextPage);
			}
        });
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				ShopCommontInfo shopCommontInfo = mDataSource.get(position - 1);
				shopCommontInfo.mIsShowAllBody = !shopCommontInfo.mIsShowAllBody;
				mAdapter.notifyDataSetChanged();
				
				//是我的评论时，点击要进入商铺详细
				if(mIsMysign){
					Intent i = new Intent(mContext, ShopInfoActivity.class);
					i.putExtra(GoodPlaceContants.KEY_SHOP_ID, shopCommontInfo.mShopid);
					mContext.startActivity(i);
				}
			}
		});
	
	}
	
	private void initTitle(){
		if(mIsMysign){
			setTitleText(R.string.mycommont);
		}else{
			setTitleText(R.string.commont);
			
			LayoutParams p = new LayoutParams(MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size) , MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size));
			
			mBtnAddComment = new Button(this);
			mBtnAddComment.setLayoutParams(p);
			mBtnAddComment.setBackgroundResource(R.drawable.button_add_comment_selector);
			mBtnAddComment.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(!mNetState.isNetUsing()){
						ShowToast(R.string.nonet);
						return;
					}
					
					//先判断是否登录了
					if (GoodPlaceContants.USERINFO == null) {
						Intent intent = new Intent(mContext, LoginActivity.class);
						startActivity(intent);
					}else {
						Intent intent = new Intent(mContext, CommontAddActivity.class);
						intent.putExtra(GoodPlaceContants.KEY_SHOP_ID, mShopId);
						startActivity(intent);
					}
				}
			});
			setTitleRightButton(mBtnAddComment);
		}
		
	}
	
	@Override
	public void onClick(View v) {
		if (v == mBtnBack) {
			finish();
		}
		
//		else if (v == mBtnAddComment) {
//			//先判断是否登录了
//			if (GoodPlaceContants.USERINFO == null) {
//				Intent intent = new Intent(this, LoginActivity.class);
//				startActivity(intent);
//			}else {
//				Intent intent = new Intent(this, CommontAddActivity.class);
//				intent.putExtra(GoodPlaceContants.KEY_SHOP_ID, mShopId);
//				startActivity(intent);
//			}
//		}
		
	}
	
	public void loadData(int curpage) {
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = null;
		String url = "";
		if(mIsMysign){
			postData = JsonRequestManage.getMyCommentPostData(curpage);
			url = GoodPlaceContants.URL_MY_COMMENT;
		}else{
			postData = JsonRequestManage.getShopCommentPostData(mShopId, curpage);
			url = GoodPlaceContants.URL_SHOP_COMMENT;
		}
		
		RequestManager.loadDataFromNet(mContext, url, postData, new ShopCommentParser(), mIsNeddCache, CACHE_NAME + mShopId + curpage,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null && object instanceof ShopCommentListInfo) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = Contants.LOAD_SUCCESS;
							mHandler.sendMessage(msg);
							Log.i("lch", "请求商家点评成功");
						} else {
							mHandler.sendEmptyMessage(Contants.LOAD_FALSE);
							Log.i("lch", "请求商家点评失败");
						}
					}
				});
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Contants.LOAD_SUCCESS :
					if (mNextPage == 1) {
						mListView.onRefreshComplete();
						mDataSource.clear();
					}
					
					mCurPage = mNextPage;
					mNextPage = mNextPage + 1;
					
					ShopCommentListInfo commentListInfo = (ShopCommentListInfo) msg.obj;
					ArrayList<ShopCommontInfo> commontList = commentListInfo.mShoCommontList;
					if (commontList != null && commontList.size() > 0) {
						mListView.setPageData(commentListInfo.mPageInfo.mPagecount , mCurPage);
						mListView.setLoadMoreFinsh();
						mDataSource.addAll(commontList);
						mAdapter.notifyDataSetChanged();
					}else{
						mListView.setPageData(0 , 0);
					}
					break;
					
				case Contants.LOAD_FALSE :
//					Toast.makeText(mContext, "请求失败", 0).show();
//					if (mNextPage == 1) {
//						mListView.onRefreshComplete();
//						mDataSource.clear();
//					}
//					
//					mCurPage = mNextPage;
//					mNextPage = mNextPage + 1;
//
//					mListView.setPageData(3, mCurPage);
//					mListView.setLoadMoreFinsh();
//					addData();
//					mAdapter.notifyDataSetChanged();
				
					break;
					
				default :
					Log.i("lch", "fales");
					break;
			}
		}

	};

	public void addData() {
//		Log.i("lch", "addData");
		
		for (int i = 0; i < 10; i++) {
			ShopCommontInfo info = new ShopCommontInfo();
			info.mScore = 40;
			info.mReviewbody = "朋友过生日选择来此。（主要是前天晚上在三楼的俏湘妃吃过，同一个老板，办了张贵宾卡，期待品尝）\n\n顾名思义，巴喜阳光，以BBQ为主打的烤肉，自助。\n\n未进店里，服务生确实夹道欢迎，主要原因是要先买单（多少有些不爽）。90元一位，打折后人均80，不含酒水。（提供瓶装可乐和雪碧）\n可能因为\nBBQ的缘故，时尚的音乐声音有点吵，影响聊天。\n\n总体评价，还不错，逛街累了、饿了的时候，稍事休息，可以填填肚子。\n对了，还不能刷卡.";
			
			UserInfo userInfo = new UserInfo();
			userInfo.mUsernickname = "小猪" + i ;
			info.mUserInfo = userInfo;
			info.mPosttime = System.currentTimeMillis();
			mDataSource.add(info);
		}
	}

	

}
