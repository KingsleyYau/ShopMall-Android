package lbs.goodplace.com.View.main;

import java.util.ArrayList;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.adapter.ShopSignAdapter;
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
import lbs.goodplace.com.obj.ShopSignInfo;
import lbs.goodplace.com.obj.ShopSignListInfo;
import lbs.goodplace.com.obj.UserInfo;
import lbs.goodplace.com.obj.parser.ShopSignParser;
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
import android.widget.Toast;

/**
 * 签到留言墙
 * @author shazhuzhu
 *
 */
public class ShopSignWallActivity extends ModuleActivity implements OnClickListener{
	public static String KEY_MYSIGN = "KEY_MYSIGN";
	
	public Context mContext;
	public ImageView mBtnBack;
	public Button mBtnAddSign;

	public TextView mShopName;
	public RefreshListView mListView;
	public ShopSignAdapter mAdapter;
	ArrayList<ShopSignInfo> mDataSource = new ArrayList<ShopSignInfo>();
	
	public int mShopId = -1;
	public int mCurPage = 1;
	public int mNextPage = 1;
	private boolean mIsMysign = false;	//是否查看我的留言
	
	//缓存
	private String CACHE_NAME = "ShopSignWallActivity";
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	private NetState mNetState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.shop_sign_view);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.shop_sign_view, mLayout_body);
		
		mContext = this;
		mNetState = new NetState(mContext);
		
//		mBtnBack = (ImageView) findViewById(R.id.btnBack);
//		mBtnBack.setOnClickListener(this);
//		
//		mBtnAddSign = (ImageView) findViewById(R.id.btn_add_sign);
//		mBtnAddSign.setOnClickListener(this);
		
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
		mAdapter = new ShopSignAdapter(mContext);
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
//				ShopLeaveMessageInfo info = mDataSource.get(position - 1);
//				info.mIsShowAllBody = !info.mIsShowAllBody;
//				mAdapter.notifyDataSetChanged();
				//是我的留言时，点击要进入商铺详细
				if(mIsMysign){
					ShopSignInfo info = mDataSource.get(position - 1);
					
					Intent i = new Intent(mContext, ShopInfoActivity.class);
					i.putExtra(GoodPlaceContants.KEY_SHOP_ID, info.mShopid);
					mContext.startActivity(i);
				}
			}
		});
	
	}
	
	private void initTitle(){
		if(mIsMysign){
			setTitleText(R.string.mysignup);
		}else{
			setTitleText(R.string.signup);
			
			LayoutParams p = new LayoutParams(MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size) , MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size));
			
			mBtnAddSign = new Button(this);
			mBtnAddSign.setLayoutParams(p);
			mBtnAddSign.setBackgroundResource(R.drawable.upload_camera_selector);
			mBtnAddSign.setOnClickListener(new View.OnClickListener() {
				
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
						Intent intent = new Intent(mContext, LeaveMessageActivity.class);
						intent.putExtra(GoodPlaceContants.KEY_SHOP_ID, mShopId);
						startActivity(intent);
					}
				}
			});
			setTitleRightButton(mBtnAddSign);
		}
		
	}
	
	@Override
	public void onClick(View v) {
		if (v == mBtnBack) {
			finish();
		}
		
//		else if (v == mBtnAddSign) {
//			//先判断是否登录了
//			if (GoodPlaceContants.USERINFO == null) {
//				Intent intent = new Intent(this, LoginActivity.class);
//				startActivity(intent);
//			}else {
//				Intent intent = new Intent(this, LeaveMessageActivity.class);
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
			postData = JsonRequestManage.getMySignWallPostData(curpage);
			url = GoodPlaceContants.URL_MY_SIGN_WALL;
		}else{
			postData = JsonRequestManage.getShopSignWallPostData(mShopId, curpage);
			url = GoodPlaceContants.URL_SHOP_SIGN_WALL;
		}
		
		RequestManager.loadDataFromNet(mContext, url, postData, new ShopSignParser(), mIsNeddCache, CACHE_NAME + mShopId + curpage,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null && object instanceof ShopSignListInfo) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = Contants.LOAD_SUCCESS;
							mHandler.sendMessage(msg);
						} else {
							mHandler.sendEmptyMessage(Contants.LOAD_FALSE);
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
					
					ShopSignListInfo shopSignListInfo = (ShopSignListInfo) msg.obj;
					ArrayList<ShopSignInfo> shopSignInfoList = shopSignListInfo.mShopSignInfoList;
					if (shopSignInfoList != null && shopSignInfoList.size() > 0) {
						mListView.setPageData(shopSignListInfo.mPageInfo.mPagecount , mCurPage);
						mListView.setLoadMoreFinsh();
						mDataSource.addAll(shopSignInfoList);
						mAdapter.notifyDataSetChanged();
					}else{
						mListView.setPageData(0 , 0);
					}					
					break;
					
				case Contants.LOAD_FALSE :
					Toast.makeText(mContext, "请求失败", 0).show();
					break;
					
				default :
					Log.i("lch", "fales");
					break;
			}
		}

	};

	public void addData() {
		for (int i = 0; i < 10; i++) {
			ShopSignInfo info = new ShopSignInfo();
			info.mScore = 40;
			info.mSignbody = "朋友过生日选择来此庆祝，好好感受一下";
			
			UserInfo userInfo = new UserInfo();
			userInfo.mUsernickname = "小猪" + i ;
			info.mUserInfo = userInfo;
			info.mSigntime = System.currentTimeMillis();
			mDataSource.add(info);
		}
	}

	

}
