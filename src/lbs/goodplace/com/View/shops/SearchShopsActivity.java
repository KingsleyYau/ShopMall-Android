package lbs.goodplace.com.View.shops;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.adapter.MyAdapter;
import lbs.goodplace.com.View.adapter.RegionsListAdapter;
import lbs.goodplace.com.View.main.CredittypeListAdapter;
import lbs.goodplace.com.View.main.GoodPlaceApp;
import lbs.goodplace.com.View.main.ShopInfoActivity;
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
import lbs.goodplace.com.obj.RegionModule;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;
import lbs.goodplace.com.obj.SortModule;
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
 * 由积分/其它信息查询商店
 * （2.3.2.3）
 * @author Administrator
 *
 */
public class SearchShopsActivity extends ModuleActivity{
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;
//	private int MAXPAGE = 20;
	public static String KEY_CREDITTYPE = "KEY_CREDITTYPE";
	public static String KEY_REGION = "KEY_REGION";
	public static String KEY_CATEGORY = "KEY_CATEGORY";
	public static String KEY_KEYWORD = "KEY_KEYWORD";
	public static String KEY_ISSHOW_REGIONS = "KEY_ISSHOW_REGIONS";
	public static String KEY_ISSHOW_DISTANCE = "KEY_ISSHOW_DISTANCE";
	public static String KEY_REGION_BUTTONTEXT = "KEY_REGION_BUTTONTEXT";
	public static String KEY_CATEGORY_BUTTONTEXT = "KEY_CATEGORY_BUTTONTEXT";
	
	//
	private Context mContext;
	private int mCurPage = 1;
	private int mNextPage = 1;
	private int mShopSum = 0;
	private int mPageMaxSum = 14;	//每页总条数
	private MyAdapter mMyAdapter;
	private ArrayList<ShopModule> mDataSource;
	private int mDistance = 0;	//选择的距离(默认为0，则搜全部)
	private CategoryModule mCategoryModule ;	//选择的分类
	private RegionModule mRegionModule;	//商圈
	private CredittypeModule mCredittypeModule ;	//选择的积分分类
	private SortModule mSortModule;	//选择的排序方式
	private List<SortModule> mListSort = new ArrayList<SortModule>();
	private String mSorttypeId = "";	//排序类型ID
	private SortListAdapter mSortListAdapter;
	
	private List<CategoryModule> mListCategoryParent = new ArrayList<CategoryModule>();
	private List<CategoryModule> mListCategoryNode = new ArrayList<CategoryModule>();
	private CategoryListAdapter mAdapterCategoryParent;
	private CategoryListAdapter mAdapterCategoryNote;
	private String mCategoryParentId = "0";	//行业分类父节点ID
	private String mCategoryId = "";	//行业分类子节点ID
	private List<CredittypeModule> mListCredittype = new ArrayList<CredittypeModule>();
	private CredittypeListAdapter mCredittypeListAdapter;
	private String mCredittypeId = "";	//积分类型ID
	
	private List<RegionModule> mListRegionParent = new ArrayList<RegionModule>();
	private List<RegionModule> mListRegionNode = new ArrayList<RegionModule>();
	private RegionsListAdapter mAdapterRegionParent;
	private RegionsListAdapter mAdapterRegionNote;
	private String mRegionParentId = "0";	//行业分类ID
	private String mRegionId = "";		//商圈ID
	private String mKeyword = "";		//搜索关键字
	
	private boolean mIsShowRegions = true;
	private boolean mIsShowDistance = true;
	
	private String mButtonRegionText = "";
	private String mButtonCategoryText = "";
	//定位
//	private Vibrator mVibrator01 =null;
//	private LocationClient mLocClient;
	private GPSLocation mGPSLocation;
	//缓存
	private String CACHE_NAME = "searchshopslist";
	private String CACHE_NAME_REGIONS = "searchshopsRegtions";
	private String CACHE_NAME_CATEGORY = "searchshopsCategory";
	private String CACHE_NAME_CREDITTYPE = "searchshopsCredittype";
	private String CACHE_NAME_SORT = "searchshopsSort";
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	private NetState mNetState;
	
	//
	private View mView;
	private Button mButtonMap;
	private RefreshListView mListView;
	private PopupWindow pwdistance;
	private PopupWindow pwregions;
	private PopupWindow pwcategories;
	private PopupWindow pwcredittype;
	private PopupWindow pwsort;
	private ListView mListViewCategoryParent = null;
	private ListView mListViewCategoryNode = null;
	private ListView mListViewRegionParent = null;
	private ListView mListViewRegionNode = null;
	private Button mButtonRegions;
	private Button mButtonDistance;
	private Button mButtonRank;
	private Button mButtonCredittype;
	private Button mButtonSort;
	private TextView mTextViewLocation;
	private int mPopuWindowHegith = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.searchshops_list_view, mLayout_body);
		mContext = SearchShopsActivity.this;
		mGPSLocation = new GPSLocation(mContext);
		
		//取参数
        Bundle budle = getIntent().getExtras();
		if(budle != null ){
			if(budle.get(KEY_CREDITTYPE) != null){
				this.mCredittypeId = (String)budle.get(KEY_CREDITTYPE);
			}
			
			if(budle.get(KEY_REGION) != null){
				this.mRegionId = (String)budle.get(KEY_REGION);
			}
			
			if(budle.get(KEY_CATEGORY) != null){
				this.mCategoryId = (String)budle.get(KEY_CATEGORY);
			}
			
			if(budle.get(KEY_ISSHOW_DISTANCE) != null){
				this.mIsShowDistance = budle.getBoolean(KEY_ISSHOW_DISTANCE);
			}
			
			if(budle.get(KEY_ISSHOW_REGIONS) != null){
				this.mIsShowRegions = budle.getBoolean(KEY_ISSHOW_REGIONS);
			}
			
			if(budle.get(KEY_KEYWORD) != null){
				this.mKeyword = budle.getString(KEY_KEYWORD);
			}
			
			if(budle.get(KEY_REGION_BUTTONTEXT) != null){
				this.mButtonRegionText = budle.getString(KEY_REGION_BUTTONTEXT);
			}
			
			if(budle.get(KEY_CATEGORY_BUTTONTEXT) != null){
				this.mButtonCategoryText = budle.getString(KEY_CATEGORY_BUTTONTEXT);
			}
			
		}
		
		mTextViewLocation = (TextView)findViewById(R.id.searchshops_locatext);
		mNetState = new NetState(mContext);
		
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		loadCondition();
		initTitleBar();
		initListView();
		initButtons();
//		getCache();
		loadData(mCurPage);
		getLocation();
		
		//把它赋为空，否则搜索条件一直存在
		mKeyword = "";
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		mLocClient.stop();
		mGPSLocation.deactivate();
		super.onDestroy();
	}
	
	/**
	 * 取缓存
	 */
//	private void getCache(){
//		RequestManager.getDataCache(mContext, new ShopListParser(), "searchshopslist", new IDataListener(){
//
//			@Override
//			public void loadFinish(boolean success, Object object) {
//				// TODO Auto-generated method stub
//				if (success && object != null) {
//					// 还在子线程操作，handler是放回主线程处理
//					Message msg = new Message();
//					msg.obj = object;
//					msg.what = LOAD_SUCCESS;
//					mHandler.sendMessageDelayed(msg, 500);
//					Log.i("lch", "取缓存成功");
//				} else {
//					mHandler.sendEmptyMessage(LOAD_SUCCESS);
//					Log.i("lch", "取缓存失败");
//				}
//			}
//			
//		});
//	}
	
	/**
	 * 保存用户的搜索条件
	 */
	private void saveCondition(){
		SharedPreferences userInfo = getSharedPreferences(GlobalUtil.SHAREDPERFERENCES_NAME_SEARCHCONDITION, 0);  
        userInfo.edit().putInt(GlobalUtil.SHAREDPERFERENCES_KEY_SEARCHDISTANCE, mDistance).commit();  
	}
	
	/**
	 * 读取用户的搜索条件
	 */
	private void loadCondition(){
		SharedPreferences userInfo = getSharedPreferences(GlobalUtil.SHAREDPERFERENCES_NAME_SEARCHCONDITION, 0);  
		mDistance = userInfo.getInt(GlobalUtil.SHAREDPERFERENCES_KEY_SEARCHDISTANCE, 0);  
	}
	
	/**
	 * 请求列表数据
	 */
	private void loadData(final int curpage) {

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				byte[] postData = JsonRequestManage.getSearchshopslist(mRegionId, mKeyword, mCategoryId, 
						mCredittypeId, mDistance + "", "", 
						"", "", "", 
						"", mSorttypeId, curpage, mPageMaxSum);
				
				//请求接口
				RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_SEARCHSHOPLIST, postData, new ShopListParser(), mIsNeddCache , CACHE_NAME + curpage,
						new IDataListener(){

							@Override
							public void loadFinish(boolean success, Object object) {
								if (success && object != null) {
									// 还在子线程操作，handler是放回主线程处理
									Message msg = new Message();
									msg.obj = object;
									msg.what = LOAD_SUCCESS;
									mHandler.sendMessageDelayed(msg, 500);
									Log.i("lch", "请求成功");
								} else {
									mHandler.sendEmptyMessage(LOAD_SUCCESS);
									Log.i("lch", "请求失败");
								}
							}
					
						});
			}
		}).start();
		
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case LOAD_SUCCESS :
					if (mNextPage == 1) {
						mDataSource.clear();
					}
					
					if(msg.obj != null){
						mListView.onRefreshComplete();
						
						ShopListModule slm = (ShopListModule)msg.obj;
						mDataSource.addAll(slm.mShopList);
						mCurPage = slm.mPageInfo.mCurpag;
						mNextPage = mCurPage + 1;
						mShopSum = slm.mPageInfo.mTotalrecordcount;
						mListView.setPageData(slm.mPageInfo.mPagecount, mCurPage);
						Log.i("lch", "page:" + mCurPage + "");
						
						String titlestr = mContext.getString(R.string.shoplist);
						titlestr = String.format(titlestr, mShopSum + "");
						setTitleTxt(titlestr);
					}
					
					mListView.setLoadMoreFinsh();
					mMyAdapter.notifyDataSetChanged();
					
					
					break;
					
				case LOAD_FAILE :
					Toast.makeText(mContext, "请求失败", 0).show();
					break;
					
				default :
					Log.i("lch", "fales");
					break;
			}
		}

	};
	
	/**
	 * 初始化Titlebar
	 */
	private void initTitleBar(){
		setTitleText(R.string.nearshop);
		
		LayoutParams p = new LayoutParams(MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size) , MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size));
		
		mButtonMap = new Button(this);
		mButtonMap.setLayoutParams(p);
		mButtonMap.setBackgroundResource(R.drawable.locamap_selector);
		mButtonMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.gc();
				
				ShowLoadingDialog();
				
				Intent i = new Intent();
				i.setClass(SearchShopsActivity.this, MapActivity.class);
				if(mDataSource.size() > mPageMaxSum){
					ArrayList<ShopModule> tmepDataSource = new ArrayList<ShopModule>();
					tmepDataSource.addAll(mDataSource.subList(0, mPageMaxSum));
					i.putExtra(MapActivity.KEY_SHOP_LIST, tmepDataSource);
				}else{
					i.putExtra(MapActivity.KEY_SHOP_LIST, mDataSource);
				}
				i.putExtra(MapActivity.KEY_SHOPSUM, mShopSum);
				i.putExtra(MapActivity.KEY_PARAM, 
						mCategoryId+","+
						mCredittypeId+","+
						mRegionId+","+
						mKeyword+","+
						mSorttypeId+","+
						mDistance
						);
				startActivity(i);
				
				MapActivity.setMapLoadedHandler(mHandlerHideLoading);
			}
		});
		setTitleRightButton(mButtonMap);
	}
	
	Handler mHandlerHideLoading = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			HideLoadingDialog();
		}
	};
	
	/**
	 * 初始化列表
	 */
	public void initListView() {
		mListView = (RefreshListView) findViewById(R.id.searchshops_listview);
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
				if(mNetState.isNetUsing()){
					mIsNeddCache = false;
				}else{
					mIsNeddCache = true;
				}
				loadData(mCurPage);
			}

			@Override
			public void loadNextPageData() {
				if(mNetState.isNetUsing()){
					mIsNeddCache = false;
				}else{
					mIsNeddCache = true;
				}
				loadData(mNextPage);
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
	
	/**
	 * 顶部5个按钮
	 */
	private void initButtons(){
		//
		mButtonRegions = (Button)findViewById(R.id.Btn_allregions);
		mButtonRegions.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showRegionsPW();
				getRegionstype();
			}
		});
		if(!mButtonRegionText.equals(""))
			mButtonRegions.setText(mButtonRegionText);
		
		if(mIsShowRegions)
			mButtonRegions.setVisibility(View.VISIBLE);
		else
			mButtonRegions.setVisibility(View.GONE);
		
		//
		mButtonDistance = (Button)findViewById(R.id.Btn_distance);
		mButtonDistance.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showChooseDistancePW();
			}
		});
		
		if(mIsShowDistance){
			mButtonDistance.setVisibility(View.VISIBLE);
			if(mDistance == 0){
				mButtonDistance.setText(getResources().getString(R.string.wholecity));
			}else{
				if(mDistance > 5000){
					mButtonDistance.setText(getString(R.string.wholecity));
				}else{
					mButtonDistance.setText(mDistance + "m");
				}
				
			}
		}
		else
			mButtonDistance.setVisibility(View.GONE);
		
		//
		mButtonRank = (Button)findViewById(R.id.Btn_rank);
		mButtonRank.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showCategoryPW();
				getCategorytype();
			}
		});
		if(!mButtonCategoryText.equals(""))
			mButtonRank.setText(mButtonCategoryText);
		
		//
		mButtonCredittype = (Button)findViewById(R.id.Btn_credit);
		mButtonCredittype.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showCredittypePW();
				getCredittypes();
			}
		});
		
		//
		mButtonSort = (Button)findViewById(R.id.Btn_Sort);
		mButtonSort.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSortPW();
				getSorttypes();
			}
		});
	}
	
	/**
	 * 刷新界面
	 */
	private void RefalshUI(){
		saveCondition();
		
		if(mDistance == 0){
			mButtonDistance.setText(getResources().getString(R.string.wholecity));
		}else{
			if(mDistance > 5000){
				mButtonDistance.setText(getString(R.string.wholecity));
			}else{
				mButtonDistance.setText(mDistance + "m");
			}
			
		}
		
		if(mCategoryModule!=null){
			mButtonRank.setText(mCategoryModule.getName());
			mCategoryId = mCategoryModule.getId();
		}
			
		if(mCredittypeModule!=null){
			mButtonCredittype.setText(mCredittypeModule.getName());
			mCredittypeId = mCredittypeModule.getId();
		}

		if(mSortModule!=null){
			mButtonSort.setText(mSortModule.getName());
			mSorttypeId = mSortModule.getId();
		}
		
		if(mRegionModule!=null){
			mButtonRegions.setText(mRegionModule.getName());
			mRegionId = mRegionModule.getId();
		}
		
		//取数据
		mCurPage = 1;
		mNextPage = 1;
		loadData(mCurPage);
	}
	
	//-----------------弹出框------------------------
	
	/**
	 * 选择距离
	 */
	private void showChooseDistancePW(){
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopupWindow = inflater.inflate(R.layout.popupwindow_distance_view, null, false);
		// 关闭popwindow事件
		vPopupWindow.setFocusableInTouchMode(true);
		vPopupWindow.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_BACK)
						&& (pwdistance.isShowing())) {
					pwdistance.dismiss();// 这里写明模拟menu的PopupWindow退出就行
					return true;
				}
				return false;
			}
		});
		
		RadioButton rb500 = (RadioButton)vPopupWindow.findViewById(R.id.RadioButton_500);
		rb500.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDistance = 500;
				RefalshUI();
				pwdistance.dismiss();
			}
		});
		
		RadioButton rb1k = (RadioButton)vPopupWindow.findViewById(R.id.RadioButton_1k);
		rb1k.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDistance = 1000;
				RefalshUI();
				pwdistance.dismiss();
			}
		});
		
		RadioButton rb2k = (RadioButton)vPopupWindow.findViewById(R.id.RadioButton_2k);
		rb2k.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDistance = 2000;
				RefalshUI();
				pwdistance.dismiss();
			}
		});
		
		RadioButton rb5k = (RadioButton)vPopupWindow.findViewById(R.id.RadioButton_5k);
		rb5k.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDistance = 5000;
				RefalshUI();
				pwdistance.dismiss();
			}
		});
		
		RadioButton rbwhole = (RadioButton)vPopupWindow.findViewById(R.id.RadioButton_wholecity);
		rbwhole.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDistance = 10000;
				RefalshUI();
				pwdistance.dismiss();
			}
		});

		if (pwdistance == null) {
			//弹出窗的高度
			mPopuWindowHegith = mListView.getHeight() + mTextViewLocation.getHeight();
			
			pwdistance = new PopupWindow(vPopupWindow, mListView.getWidth(),mPopuWindowHegith, true);
			pwdistance.setBackgroundDrawable(new BitmapDrawable());
			pwdistance.setOutsideTouchable(true);
		}
		pwdistance.showAtLocation(mView, Gravity.BOTTOM, 0,0);
		pwdistance.setFocusable(true);
	}
	
	/**
	 * 商圈分类
	 */
	private void showRegionsPW(){
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopupWindow = inflater.inflate(R.layout.popupwindow_rank_view, null, false);
		// 关闭popwindow事件
		vPopupWindow.setFocusableInTouchMode(true);
		vPopupWindow.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_BACK)
						&& (pwregions.isShowing())) {
					pwregions.dismiss();// 这里写明模拟menu的PopupWindow退出就行
					return true;
				}
				return false;
			}
		});
		
		
		if(mListViewRegionParent == null){
			mListViewRegionParent = (ListView)vPopupWindow.findViewById(R.id.listview_Province);
			mAdapterRegionParent  = new RegionsListAdapter(mContext, mListRegionParent);
			mListViewRegionParent.setAdapter(mAdapterRegionParent);
			mAdapterRegionParent.mIsFoucesChangeSelector = true;
		}
		
		if(mListViewRegionNode == null){
			mListViewRegionNode = (ListView)vPopupWindow.findViewById(R.id.listview_City);
			mAdapterRegionNote  = new RegionsListAdapter(mContext, mListRegionNode);
			mListViewRegionNode.setAdapter(mAdapterRegionNote);
		}
		
		mListViewRegionParent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				mAdapterRegionParent.mFoucesIndex = arg2;
				mAdapterRegionParent.notifyDataSetInvalidated();
				
				mRegionParentId = mListRegionParent.get(arg2).getId();
				
				if(mRegionParentId.equals("")){
					mListRegionNode.clear();
					mAdapterRegionNote.notifyDataSetChanged();	//一定要这句
					
					mRegionModule = mListRegionParent.get(arg2);
					RefalshUI();
					pwregions.dismiss();
				}else{
					getRegionstype();
				}
				
			}
		});
		mListViewRegionNode.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mRegionModule = mListRegionNode.get(arg2);
				RefalshUI();
				pwregions.dismiss();
			}
		});
		
		if (pwregions == null) {
			//弹出窗的高度
			mPopuWindowHegith = mListView.getHeight() + mTextViewLocation.getHeight();
			
			pwregions = new PopupWindow(vPopupWindow, mListView.getWidth(),mPopuWindowHegith, true);
			pwregions.setBackgroundDrawable(new BitmapDrawable());
			pwregions.setOutsideTouchable(true);
		}
		
		pwregions.showAtLocation(mView, Gravity.BOTTOM, 0,0);
		pwregions.setFocusable(true);
	}
	
	/**
	 * 取商圈分类数据
	 */
	private void getRegionstype(){
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = JsonRequestManage.getRegionsListPostData(mRegionParentId);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_REGIONS, postData, new RegionsParser(), mIsNeddCache, CACHE_NAME_REGIONS,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mRegionsHandler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
						} else {
							mRegionsHandler.sendEmptyMessage(LOAD_SUCCESS);
						}
					}
			
				});
	}
	
	private Handler mRegionsHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
				case LOAD_SUCCESS :
					if(msg.obj!=null){
						if(mRegionParentId.equals("0")){
							mListRegionParent.clear();
							
							//添加全部
							RegionModule rmall = new RegionModule();
							rmall.setId("");
							rmall.setName(mContext.getString(R.string.allregions));
							mListRegionParent.add(rmall);
							
							//添加返回数据
							mListRegionParent.addAll((List<RegionModule>)msg.obj);
							
							if(mListRegionParent != null){
								Log.i("zjj", "getCategorytype 请求成功1");
								mAdapterRegionParent.notifyDataSetChanged();
								
								mRegionParentId = mListRegionParent.get(0).getId();
								
//								getRegionstype();
							}
						}else{
							mListRegionNode.clear();
							
							if(!mRegionParentId.equals("")){
								//将父类添加到第一项中
								for(int i = 0 ; i < mListRegionParent.size(); i++){
									if(mListRegionParent.get(i).getId().equals(mRegionParentId)){
										RegionModule rm = new RegionModule(); 
										rm.setId(mListRegionParent.get(i).getId());
										rm.setLat(mListRegionParent.get(i).getLat());
										rm.setLng(mListRegionParent.get(i).getLng());
										rm.setName(mContext.getResources().getString(R.string.all) + mListRegionParent.get(i).getName());
										rm.setParentid(mListRegionParent.get(i).getId());
										mListRegionNode.add(rm);
									}
								}
								
								if(msg.obj != null){
									mListRegionNode.addAll((List<RegionModule>)msg.obj);
								}
								
								if(mListRegionNode != null){
									Log.i("zjj", "getCategorytype 请求成功2");
									mAdapterRegionNote.notifyDataSetChanged();
								}
							}
							
						}
					}
					break;
					
				default :
					Log.i("zjj", "getCategorytype 请求失败");
					break;
			}
		}
	};
	
	/**
	 * 行业分类
	 */
	private void showCategoryPW(){
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopupWindow = inflater.inflate(R.layout.popupwindow_rank_view, null, false);
		// 关闭popwindow事件
		vPopupWindow.setFocusableInTouchMode(true);
		vPopupWindow.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_BACK)
						&& (pwcategories.isShowing())) {
					pwcategories.dismiss();// 这里写明模拟menu的PopupWindow退出就行
					return true;
				}
				return false;
			}
		});
		
		
		if(mListViewCategoryParent == null){
			mListViewCategoryParent = (ListView)vPopupWindow.findViewById(R.id.listview_Province);
			mAdapterCategoryParent  = new CategoryListAdapter(mContext, mListCategoryParent);
			mListViewCategoryParent.setAdapter(mAdapterCategoryParent);
			mAdapterCategoryParent.mIsFoucesChangeSelector = true;
		}
		
		if(mListViewCategoryNode == null){
			mListViewCategoryNode = (ListView)vPopupWindow.findViewById(R.id.listview_City);
			mAdapterCategoryNote  = new CategoryListAdapter(mContext, mListCategoryNode);
			mListViewCategoryNode.setAdapter(mAdapterCategoryNote);
		}
		
		
		
		mListViewCategoryParent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				mAdapterCategoryParent.mFoucesIndex = arg2;
				mAdapterCategoryParent.notifyDataSetInvalidated();
				
				mCategoryParentId = mListCategoryParent.get(arg2).getId();
				
				if(mCategoryParentId.equals("")){
					mListCategoryNode.clear();
					mAdapterCategoryNote.notifyDataSetChanged();	//一定要这句
					
					mCategoryModule = mListCategoryParent.get(arg2);
					RefalshUI();
					pwcategories.dismiss();
				}else{
					getCategorytype();
				}
			}
		});
		mListViewCategoryNode.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mCategoryModule = mListCategoryNode.get(arg2);
				RefalshUI();
				pwcategories.dismiss();
			}
		});
		
		if (pwcategories == null) {
			//弹出窗的高度
			mPopuWindowHegith = mListView.getHeight() + mTextViewLocation.getHeight();
			
			pwcategories = new PopupWindow(vPopupWindow, mListView.getWidth(),mPopuWindowHegith, true);
			pwcategories.setBackgroundDrawable(new BitmapDrawable());
			pwcategories.setOutsideTouchable(true);
		}
		
		pwcategories.showAtLocation(mView, Gravity.BOTTOM, 0,0);
		pwcategories.setFocusable(true);
	}
	
	/**
	 * 取行业分类数据
	 */
	private void getCategorytype(){
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = JsonRequestManage.getCategoriesListPostData(mCategoryParentId);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_CATEGORIES, postData, new CategoriesParser(), mIsNeddCache, CACHE_NAME_CATEGORY,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mCategoriesHandler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
						} else {
							mCategoriesHandler.sendEmptyMessage(LOAD_SUCCESS);
						}
					}
			
				});
	}
	
	private Handler mCategoriesHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
				case LOAD_SUCCESS :
					if(msg.obj!=null){
						if(mCategoryParentId.equals("0")){
							mListCategoryParent.clear();
							
							//添加全部
							CategoryModule cmall = new CategoryModule();
							cmall.setId("");
							cmall.setName(mContext.getString(R.string.allchannel));
							mListCategoryParent.add(cmall);
							
							//
							mListCategoryParent.addAll((List<CategoryModule>)msg.obj);
							
							if(mListCategoryParent != null){
								Log.i("zjj", "getCategorytype 请求成功1");
								mAdapterCategoryParent.notifyDataSetChanged();
								
								mCategoryParentId = mListCategoryParent.get(0).getId();
								getCategorytype();
							}
						}else{
							mListCategoryNode.clear();
							
							//将父类添加到第一项中
							for(int i = 0 ; i < mListCategoryParent.size(); i++){
								if(mListCategoryParent.get(i).getId().equals(mCategoryParentId)){
									CategoryModule allcm = new CategoryModule(); 
									allcm.setCarrankList(mListCategoryParent.get(i).getCarrankList());
									allcm.setFaviconurl(mListCategoryParent.get(i).getFaviconurl());
									allcm.setId(mListCategoryParent.get(i).getId());
									allcm.setParentid(mListCategoryParent.get(i).getId());
									allcm.setRegionList(mListCategoryParent.get(i).getRegionList());
									allcm.setName(mContext.getResources().getString(R.string.all) + mListCategoryParent.get(i).getName());
									mListCategoryNode.add(allcm);
								}
							}
							
							mListCategoryNode.addAll((List<CategoryModule>)msg.obj);
							
							if(mListCategoryNode != null){
								Log.i("zjj", "getCategorytype 请求成功2");
								mAdapterCategoryNote.notifyDataSetChanged();
							}
						}
					}
					break;
					
				default :
					Log.i("zjj", "getCategorytype 请求失败");
					break;
			}
		}
	};
	
	/**
	 * 积分分类
	 */
	private void showCredittypePW(){
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopupWindow = inflater.inflate(R.layout.popupwindow_credittype_view, null, false);
		// 关闭popwindow事件
		vPopupWindow.setFocusableInTouchMode(false);
		vPopupWindow.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_BACK)
						&& (pwcredittype.isShowing())) {
					pwcredittype.dismiss();// 这里写明模拟menu的PopupWindow退出就行
					return true;
				}
				return false;
			}
		});
		
		ListView mListViewCategoryParent = (ListView)vPopupWindow.findViewById(R.id.credittype_listview);
		
		mCredittypeListAdapter = new CredittypeListAdapter(mContext, mListCredittype);
		
		mListViewCategoryParent.setAdapter(mCredittypeListAdapter);
		mListViewCategoryParent.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				mCredittypeModule  = mListCredittype.get(arg2);
				RefalshUI();
//				mBtn3.setText(mListCredittype.get(arg2).getName());
//				mCredittypeid = mListCredittype.get(arg2).getId();
				pwcredittype.dismiss();
			}
		});

		if (pwcredittype == null) {
			//弹出窗的高度
			mPopuWindowHegith = mListView.getHeight() + mTextViewLocation.getHeight();
			
			pwcredittype = new PopupWindow(vPopupWindow, mListView.getWidth(),mPopuWindowHegith, true);
			pwcredittype.setBackgroundDrawable(new BitmapDrawable());
			pwcredittype.setOutsideTouchable(true);
		}
		
		pwcredittype.showAtLocation(mView, Gravity.BOTTOM, 0,0);
		pwcredittype.setFocusable(true);
	}
	
	/**
	 * 取积分分类数据
	 */
	private void getCredittypes(){
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = JsonRequestManage.getAllcredittypesList();
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_CREDITTYPES_LIST, postData, new CredittypesParser(), mIsNeddCache, CACHE_NAME_CREDITTYPE,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mCredittypesHandler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
							
						} else {
							mCredittypesHandler.sendEmptyMessage(LOAD_SUCCESS);
						}
						
					}
			
				});
	}
	
	private Handler mCredittypesHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case LOAD_SUCCESS :
					if(msg.obj!=null){
						mListCredittype.clear();
						mListCredittype.addAll((List<CredittypeModule>)msg.obj);
						mCredittypeListAdapter.notifyDataSetChanged();
						
						Log.i("zjj", "getCredittypes 请求成功");
					}
					break;
					
				default :
					Log.i("zjj", "getCredittypes 请求失败");
					break;
			}
		}
	};
	
	/**
	 * 选择排序方式
	 */
	private void showSortPW(){
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopupWindow = inflater.inflate(R.layout.popupwindow_sort_view, null, false);
		// 关闭popwindow事件
		vPopupWindow.setFocusableInTouchMode(true);
		vPopupWindow.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_BACK)
						&& (pwsort.isShowing())) {
					pwsort.dismiss();// 这里写明模拟menu的PopupWindow退出就行
					return true;
				}
				return false;
			}
		});
		
		ListView mListViewSort = (ListView)vPopupWindow.findViewById(R.id.sort_listview);
		
		mSortListAdapter  = new SortListAdapter(mContext, mListSort);
		mSortListAdapter.mIsFoucesChangeSelector = true;
		
		mListViewSort.setAdapter(mSortListAdapter);
		mListViewSort.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				mSortModule  = mListSort.get(arg2);
				mSortListAdapter.mFoucesIndex = arg2;
				mSortListAdapter.notifyDataSetInvalidated();
				RefalshUI();
				pwsort.dismiss();
			}
		});

		
		if (pwsort == null) {
			//弹出窗的高度
			mPopuWindowHegith = mListView.getHeight() + mTextViewLocation.getHeight();
			
			pwsort = new PopupWindow(vPopupWindow, mListView.getWidth(),mPopuWindowHegith, true);
			pwsort.setBackgroundDrawable(new BitmapDrawable());
			pwsort.setOutsideTouchable(true);
		}
		pwsort.showAtLocation(mView, Gravity.BOTTOM, 0,0);
		pwsort.setFocusable(true);
	}
	
	/**
	 * 取排序方式数据
	 */
	private void getSorttypes(){
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = JsonRequestManage.getSortList();
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_SHOPSORTTYPES, postData, new SorttypesParser(), mIsNeddCache, CACHE_NAME_SORT,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mSorttypesHandler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
							
						} else {
							mSorttypesHandler.sendEmptyMessage(LOAD_SUCCESS);
						}
						
					}
			
				});
	}
	
	private Handler mSorttypesHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case LOAD_SUCCESS :
					if(msg.obj!=null){
						mListSort.clear();
						mListSort.addAll((List<SortModule>)msg.obj);
						mSortListAdapter.notifyDataSetChanged();
						
						Log.i("zjj", "getSorttypes 请求成功");
					}
					break;
					
				default :
					Log.i("zjj", "getSorttypes 请求失败");
					break;
			}
		}
	};
	
	//****************************************************
	
	/**
	 * 定位
	 */
	private void getLocation(){
//		mLocClient = ((GoodPlaceApp)getApplication()).mLocationClient;
//		mVibrator01 =(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
//		((GoodPlaceApp)getApplication()).mVibrator01 = mVibrator01;
//		((GoodPlaceApp)getApplication()).mHandlerLoca = mHandlerLocachange;
//		setLocationOption();
//		mLocClient.start();
//		
		mGPSLocation.activate(new OnLocationChangedListener() {
			
			@Override
			public void onLocationChanged(Location l) {
				// TODO Auto-generated method stub
				GoodPlaceContants.HXLAT = l.getLatitude();
				GoodPlaceContants.HXLNG = l.getLongitude();
				
				AMapLocation aml = (AMapLocation)l;
				
				String desc = "";
				Bundle locBundle = aml.getExtras();
				if (locBundle != null) {
					desc = locBundle.getString("desc");
				}
				
				mGPSLocation.deactivate();
				
				mTextViewLocation.setText(desc);
			}
		});
	}
//	
//	//设置相关参数
//	private void setLocationOption(){
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(false);				//打开gps
//		option.setCoorType("bd90ll");		//设置坐标类型
//		option.setServiceName("com.baidu.location.service_v2.9");
//		option.setPoiExtraInfo(false);	
//		option.setAddrType("all");	
//		option.setScanSpan(3000);
//		option.setPriority(LocationClientOption.GpsFirst);        //不设置，默认是gps优先
//		option.setPoiNumber(10);
//		option.disableCache(true);		
//		mLocClient.setLocOption(option);
//	}
//
//	/**
//	 * 
//	 */
//	private Handler mHandlerLocachange = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			if(msg.arg1 == GoodPlaceApp.MyLOCATION_CHANGE){
//				BDLocation l = ((GoodPlaceApp)getApplication()).getLocation();
//				mTextViewLocation.setText(l.getStreet());
//			}
//		}
//	};
}
