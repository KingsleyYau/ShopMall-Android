package lbs.goodplace.com.View.fav;

import java.util.ArrayList;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.adapter.MyAdapter;
import lbs.goodplace.com.View.adapter.RankListAdapter;
import lbs.goodplace.com.View.main.ShopInfoActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.controls.RefreshListView;
import lbs.goodplace.com.controls.RefreshListView.OnRefreshListener;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.obj.Commentsituation;
import lbs.goodplace.com.obj.Recommendtags;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;
import lbs.goodplace.com.obj.ShopRankingChildInfo;
import lbs.goodplace.com.obj.Signsituation;
import lbs.goodplace.com.obj.parser.ResultParser;
import lbs.goodplace.com.obj.parser.ShopListParser;
import lbs.goodplace.com.obj.parser.ShopRankingParser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MyFavshopsActivity extends ModuleActivity{
	public Context mContext;
	
	public int mRankId = -1;
	public int mCurPage = 1;
	public int mNextPage = 1;
	public static final int LOAD_SHOP_INFO_SUCCESS = 2;
	public static final int LOAD_SHOP_INFO_FAILE = 3;

	public RefreshListView mListView;
	public MyAdapter mMyAdapter;
	public ArrayList<ShopModule> mDataSource;
	//缓存
	private String CACHE_NAME = "MyFavshopsActivity";
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	private NetState mNetState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.shopslist_view, mLayout_body);
		mContext = this;
		mNetState = new NetState(mContext);
		
		setTitleText(R.string.favshop);
		initListView();
		requestShopInfo(mCurPage);
	}
	
	/**
	 * 请求收藏商店列表
	 * @param curpage
	 */
	public void requestShopInfo(int curpage) {
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = JsonRequestManage.getMyfavshops(curpage, 20);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_MYFAV, postData, new ShopListParser(),mIsNeddCache , CACHE_NAME + curpage,
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
					}else{
						mListView.setPageData(0 , 0);
					}	
					
					break;
				
				//请求商家列表失败
				case LOAD_SHOP_INFO_FAILE :
					Toast.makeText(mContext, "请求收藏商家列表失败", 0).show();
					break;
					
				default :
					break;
			}
		}

	};

	public void initListView() {
		mListView = (RefreshListView) findViewById(R.id.listview);
		// 初始化adapter
		mMyAdapter = new MyAdapter(mContext);
		mMyAdapter.mHandlerDelbtn = mHandlerDelbtn;
		mMyAdapter.isDelShow = true;
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
					int cityid = mDataSource.get(position - 1).cityid;
					intent.putExtra(GoodPlaceContants.KEY_SHOP_ID, id);
					intent.putExtra(GoodPlaceContants.KEY_SHOP_CITYID, cityid);
					mContext.startActivity(intent);
				}
			}
		});
	
	}
	
	/**
	 * 取消收藏按钮消息返回
	 */
	private Handler mHandlerDelbtn = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			requestCancelShopFav(mDataSource.get(msg.arg1).id + "");
			mDataSource.remove(msg.arg1);
			mMyAdapter.notifyDataSetChanged();
			GoodPlaceContants.USERINFO.setFavoshopcount(GoodPlaceContants.USERINFO.getFavoshopcount() - 1);
		}
	};
	
	/**
	 * 取消收藏请求
	 * @param curpage
	 */
	public void requestCancelShopFav(String shopid) {
		if(!mNetState.isNetUsing()){
			ShowToast(R.string.nonet);
			return;
		}
		
		Log.i("zjj", "取消收藏 shopid:"  + shopid);
		byte[] postData = JsonRequestManage.getCancelMyfavshop(shopid);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_CANCELMYFAV, postData, new ResultParser(),
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
//							Message msg = new Message();
//							msg.obj = object;
//							msg.what = LOAD_SHOP_INFO_SUCCESS;
//							mCancelFavHandler.sendMessage(msg);
							Log.i("lch", "取消收藏 请求成功");
						} else {
//							mCancelFavHandler.sendEmptyMessage(LOAD_SHOP_INFO_FAILE);
							Log.i("lch", "取消收藏 请求失败");
						}
						
					}
			
				});
	}

//	private Handler mCancelFavHandler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//				//请求商家列表成功
//				case LOAD_SHOP_INFO_SUCCESS :
////					RequestResultModule rrm = (RequestResultModule)msg.obj;
////					rrm.
//					break;
//				
//				//请求商家列表失败
//				case LOAD_SHOP_INFO_FAILE :
////					Toast.makeText(mContext, "请求收藏商家列表失败", 0).show();
//					break;
//					
//				default :
//					break;
//			}
//		}
//
//	};
}
