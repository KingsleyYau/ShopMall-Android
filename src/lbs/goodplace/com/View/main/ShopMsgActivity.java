package lbs.goodplace.com.View.main;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.adapter.InfoListAdapter;
import lbs.goodplace.com.View.adapter.MyAdapter;
import lbs.goodplace.com.View.adapter.RegionsListAdapter;
import lbs.goodplace.com.View.main.CredittypeListAdapter;
import lbs.goodplace.com.View.main.GoodPlaceApp;
import lbs.goodplace.com.View.main.ShopInfoActivity;
import lbs.goodplace.com.View.msg.MsgDetailActivity;
import lbs.goodplace.com.View.shops.CategoryListAdapter.CategoryViewHolder;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.controls.RefreshListView;
import lbs.goodplace.com.controls.RefreshListView.OnRefreshListener;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.GPS.GPSLocation;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.util.GlobalUtil;
import lbs.goodplace.com.manage.util.MyMothod;
import lbs.goodplace.com.obj.CategoryModule;
import lbs.goodplace.com.obj.CredittypeModule;
import lbs.goodplace.com.obj.InfoListModule;
import lbs.goodplace.com.obj.InfoModule;
import lbs.goodplace.com.obj.RegionModule;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;
import lbs.goodplace.com.obj.SortModule;
import lbs.goodplace.com.obj.parser.AllinfoParser;
import lbs.goodplace.com.obj.parser.CategoriesParser;
import lbs.goodplace.com.obj.parser.CredittypesParser;
import lbs.goodplace.com.obj.parser.RegionsParser;
import lbs.goodplace.com.obj.parser.ShopListParser;
import lbs.goodplace.com.obj.parser.SorttypesParser;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 商店资讯列表
 * （2.3.2.10）
 * @author Administrator
 *
 */
public class ShopMsgActivity extends ModuleActivity{
	public static String KEY_SHOPID = "KEY_SHOPID";
	public static String KEY_SHOPMSGSUM = "KEY_SHOPMSGSUM";
	
	public static final int LOAD_RANK_SUCCESS = 0; //请求排行版列表成功
	public static final int LOAD_RANK_FAILE = 1; //请求排行版列表失败
	
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;
	
	public static final int LOAD_SHOP_INFO_SUCCESS = 2;
	public static final int LOAD_SHOP_INFO_FAILE = 3;
	
	private int mCurPage = 1;
	private int mNextPage = 1;
	private int PAGEMAX = 10;	//每页条数
	
	public Context mContext;
	
	//控件
	private View mView;
	public RefreshListView mListView;
	
	//变量
	private String mShopid = "";
	private String mShopMsgSum = "";
	private InfoListAdapter mMyAdapter;
	private ArrayList<InfoModule> mDataSource;
	
	//缓存
	private String CACHE_NAME = "shopMsg";
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	private NetState mNetState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.shopmsg_list_view, mLayout_body);
		mContext = ShopMsgActivity.this;
		mNetState = new NetState(mContext);
		
		//取参数
        Bundle budle = getIntent().getExtras();
		if(budle != null ){
			if(budle.get(KEY_SHOPID) != null){
				this.mShopid = (String)budle.get(KEY_SHOPID);
			}
			if(budle.get(KEY_SHOPMSGSUM) != null){
				this.mShopMsgSum = (String)budle.get(KEY_SHOPMSGSUM);
			}
		}
		
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		initListView();
		requestRankingList();
		
		String strshopmsg = mContext.getString(R.string.shopmsg);
		strshopmsg = String.format(strshopmsg, mShopMsgSum);
		setTitleTxt(strshopmsg);
	}
	
	/**
	 * <br>功能简述:请求资讯列表
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	private void requestRankingList(){
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = JsonRequestManage.getInfolist("",
				"",
				"",
				"",
				"",
				mShopid,
				mCurPage,PAGEMAX);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_ALLINFO, postData, new AllinfoParser(),mIsNeddCache, CACHE_NAME,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
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
					
					InfoListModule infolist = (InfoListModule) msg.obj;
					
					if (infolist.mInfoModuleList != null && infolist.mInfoModuleList.size() > 0) {
						mListView.setPageData(infolist.mPageInfo.mPagecount , mCurPage);
						mDataSource.addAll(infolist.mInfoModuleList);
					}
					
					mListView.setLoadMoreFinsh();
					mMyAdapter.notifyDataSetChanged();
					
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
	
	private void initListView() {
		mListView = (RefreshListView) findViewById(R.id.shopmsg_listview);
		// 初始化adapter
		mMyAdapter = new InfoListAdapter(mContext);
		mListView.setAdapter(mMyAdapter);

		mDataSource = new ArrayList<InfoModule>();
		mMyAdapter.updateDataSource(mDataSource);
		mListView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefreshData() {
				mCurPage = 1;
				mNextPage = 1;
//				requestShopInfo(mNextPage);
				requestRankingList();
			}

			@Override
			public void loadNextPageData() {
//				requestShopInfo(mNextPage);
			}
        });
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (position - 1 >= 0 && mDataSource != null && mDataSource.size() > 0) {
					Intent intent = new Intent(mContext, MsgDetailActivity.class);
					intent.putExtra(MsgDetailActivity.KEY_INFOID, mDataSource.get(position- 1).getId());			//-1是因为有下拉的头
					mContext.startActivity(intent);
				}
			}
		});
	
	}
	
}
