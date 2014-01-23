package lbs.goodplace.com.View.main;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.adapter.InfoListAdapter;
import lbs.goodplace.com.View.adapter.InfotypeListAdapter;
import lbs.goodplace.com.View.adapter.MyAdapter;
import lbs.goodplace.com.View.adapter.RankListAdapter;
import lbs.goodplace.com.View.msg.MsgDetailActivity;
import lbs.goodplace.com.View.shops.CategoryListAdapter;
import lbs.goodplace.com.View.shops.SortListAdapter;
import lbs.goodplace.com.View.shops.CategoryListAdapter.CategoryViewHolder;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.controls.MySrcollTabhost;
import lbs.goodplace.com.controls.RefreshListView;
import lbs.goodplace.com.controls.RefreshListView.OnRefreshListener;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.obj.CategoryModule;
import lbs.goodplace.com.obj.Commentsituation;
import lbs.goodplace.com.obj.CredittypeModule;
import lbs.goodplace.com.obj.InfoListModule;
import lbs.goodplace.com.obj.InfoModule;
import lbs.goodplace.com.obj.InfotypeModule;
import lbs.goodplace.com.obj.Recommendtags;
import lbs.goodplace.com.obj.RegionModule;
import lbs.goodplace.com.obj.SessionModule;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;
import lbs.goodplace.com.obj.ShopRankingChildInfo;
import lbs.goodplace.com.obj.Signsituation;
import lbs.goodplace.com.obj.SortModule;
import lbs.goodplace.com.obj.parser.AllinfoParser;
import lbs.goodplace.com.obj.parser.CategoriesParser;
import lbs.goodplace.com.obj.parser.CredittypesParser;
import lbs.goodplace.com.obj.parser.InfotypesParser;
import lbs.goodplace.com.obj.parser.SessionParser;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * 好讯
 * @author Administrator
 *
 */
public class NewsView extends LinearLayout implements View.OnClickListener{
	public static final int LOAD_RANK_SUCCESS = 0; //请求排行版列表成功
	public static final int LOAD_RANK_FAILE = 1; //请求排行版列表失败
	
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;
	
	private int PAGEMAX = 10;	//每页条数
	
	public Context mContext;
	//控件
	private View mView;
	public Button mBtn1;
	public Button mBtn2;
	public Button mBtn3;
	public ListView mRankListView; //排行旁listview
	public RefreshListView mListView;
	private PopupWindow pwnews;
	private PopupWindow pwcategories;
	private ListView mListViewCategoryParent = null;
	private ListView mListViewCategoryNode = null;
	private PopupWindow pwcredittype;
	
	//变量
	public ArrayList<Button> mBtnList; //排行榜4按钮队列
	public ArrayList<String> mRankingList; // 已选择的类型列表	
	public RankListAdapter mRankListAdapter;
	public int mRankId = -1;
	public int mCurPage = 1;
	public int mNextPage = 1;
	public static final int LOAD_SHOP_INFO_SUCCESS = 2;
	public static final int LOAD_SHOP_INFO_FAILE = 3;
	private List<InfotypeModule> mListInfo = new ArrayList<InfotypeModule>();
	private InfotypeListAdapter mSortListAdapter;
	private List<CategoryModule> mListCategoryParent = new ArrayList<CategoryModule>();
	private List<CategoryModule> mListCategoryNode = new ArrayList<CategoryModule>();
	private CategoryListAdapter mAdapterCategoryParent;
	private CategoryListAdapter mAdapterCategoryNote;
	private String mCategoryParentId = "0";	//行业分类ID
	private List<CredittypeModule> mListCredittype = new ArrayList<CredittypeModule>();
	private CredittypeListAdapter mCredittypeListAdapter;
	private String mInfotype = "";	//资讯类型ID
	private String mCredittypeid = "";	//积分类型ID
	private String mCategoryid = "";	//行业ID
	private String mKeyword = "";		//搜索关键字
	private int mDistance = 0;	//选择的距离(默认为0，则搜全部)
	
	public InfoListAdapter mMyAdapter;
	public ArrayList<InfoModule> mDataSource;
	
	//缓存
	private String CACHE_NAME = "NewsView";
	private String CACHE_NAME_CNEWS = "NewsViewNews";
	private String CACHE_NAME_CCATEGORY = "NewsViewCategory";
	private String CACHE_NAME_CCREDITTYPE = "NewsViewCredittype";
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	private NetState mNetState;

	public NewsView(final Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.news_view, this);

		mContext = context;
		mNetState = new NetState(mContext);
		
		initBtn(); //初始化排行榜几个按钮
		
//		initRankListView(); //初始化排行版榜列表
		initListView();
//		requestRankingList();
//		loadData(mNextPage);
	}

	/**
	 * 刷新列表（当列表为空时）
	 */
	public void ReflashList(){
		if(mDataSource.size() == 0){
			requestRankingList();
		}
	}
	
	/**
	 * 清空数据重新刷新
	 */
	public void ReflashAllList(){
		mDataSource.clear();
		mCurPage = 1;
		mNextPage = 1;
		requestRankingList();
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
		
		byte[] postData = JsonRequestManage.getInfolist(mInfotype,
				mKeyword,
				mCategoryid,
				mCredittypeid,
				mDistance + "",
				"",
				mNextPage,PAGEMAX);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_ALLINFO, postData, new AllinfoParser(),mIsNeddCache, CACHE_NAME+mNextPage,
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

	/**
	 * <br>功能简述:初始化排行榜几个按钮
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	public void initBtn(){
		mBtn1 = (Button) findViewById(R.id.btn1);
		mBtn1.setOnClickListener(this);
//		mBtn1.setBackgroundResource(R.drawable.rank_title_select);
//		mBtn1.setTextColor(Color.parseColor("#ffffffff"));
		
		mBtn2 = (Button) findViewById(R.id.btn2);
		mBtn2.setOnClickListener(this);
		
		mBtn3 = (Button) findViewById(R.id.btn3);
		mBtn3.setOnClickListener(this);
		
		mBtnList = new ArrayList<Button>();
	
		mBtnList.add(mBtn1);
		mBtnList.add(mBtn2);
		mBtnList.add(mBtn3);
		
	}
	
	
	public void initListView() {
		mListView = (RefreshListView) findViewById(R.id.shopinfo_listview);
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
				requestRankingList();
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

	@Override
	public void onClick(View v) {
		
		if (v == mBtn1) {
//			setBtnBg(mBtn1);
//			clickBtnRequestShopInfo(1); //重新请求数据
			showNewsPW();
			getInfotype();
		}
		
		if (v == mBtn2) {
//			setBtnBg(mBtn2);
//			clickBtnRequestShopInfo(2); //重新请求数据
			showCategoryPW();
			getCategorytype();
		}
		
		if (v == mBtn3) {
//			setBtnBg(mBtn3);
//			clickBtnRequestShopInfo(3); //重新请求数据
			showCredittypePW();
			getCredittypes();
		}
		
	}

	//---------------------弹出框-------------------------------
	
	/**
	 * 资讯类
	 */
	private void showNewsPW(){
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopupWindow = inflater.inflate(R.layout.popupwindow_sort_view, null, false);
		// 关闭popwindow事件
		vPopupWindow.setFocusableInTouchMode(true);
		vPopupWindow.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_BACK)
						&& (pwnews.isShowing())) {
					pwnews.dismiss();// 这里写明模拟menu的PopupWindow退出就行
					return true;
				}
				return false;
			}
		});
		
		ListView mListViewSort = (ListView)vPopupWindow.findViewById(R.id.sort_listview);
		
		mSortListAdapter  = new InfotypeListAdapter(mContext, mListInfo);
		mSortListAdapter.mIsFoucesChangeSelector = true;
		
		mListViewSort.setAdapter(mSortListAdapter);
		mListViewSort.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
//				mSortModule  = mListSort.get(arg2);
//				RefalshUI();
				
				mSortListAdapter.mFoucesIndex  = arg2;
				mSortListAdapter.notifyDataSetInvalidated();
				
				mBtn1.setText(mListInfo.get(arg2).getName());
				mInfotype = mListInfo.get(arg2).getId();
				mCurPage = 1;
				mNextPage = 1;
				requestRankingList();
				pwnews.dismiss();
			}
		});

		
		if (pwnews == null) {
			pwnews = new PopupWindow(vPopupWindow, mView.getWidth(),mView.getHeight() - mBtn1.getHeight(), true);
			//点击PopupWindow 外部区域消失
			//两句位置不能颠倒，不然无效
			pwnews.setBackgroundDrawable(new BitmapDrawable());
			pwnews.setOutsideTouchable(true);
		}
		
		int[] location = new int[2];  
		mBtn1.getLocationOnScreen(location);
		int y = location[1]; 
		
		pwnews.showAtLocation(mView, Gravity.TOP, 0,y + mBtn1.getHeight());
		pwnews.setFocusable(true);
	}
	
	/**
	 * 取资讯类型数据
	 */
	private void getInfotype(){
		if(mListInfo.size()==0){
			if(mNetState.isNetUsing()){
				mIsNeddCache = false;
			}else{
				mIsNeddCache = true;
			}
			
			byte[] postData = JsonRequestManage.getInfotypeListPostData();
			RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_INFOTYPE, postData, new InfotypesParser(), mIsNeddCache, CACHE_NAME_CNEWS,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mInfotypeHandler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
						} else {
							mInfotypeHandler.sendEmptyMessage(LOAD_SUCCESS);
						}
					}
			
				});
		}
	}
	
	private Handler mInfotypeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
				case LOAD_SUCCESS :
					if(msg.obj!=null){
						mListInfo.clear();
						
						//添加全部
						InfotypeModule imall = new InfotypeModule();
						imall.setId("");
						imall.setName(mContext.getString(R.string.allnewmsg));
						mListInfo.add(imall);
						
						//
						mListInfo.addAll((List<InfotypeModule>)msg.obj);
						if(mListInfo != null){
							Log.i("zjj", "getInfotype 请求成功" + mListInfo.get(0).getName());
							mSortListAdapter.notifyDataSetChanged();
						}
					}
					break;
					
				default :
					Log.i("zjj", "getInfotype 请求失败");
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
					
					mBtn2.setText(mListCategoryParent.get(arg2).getName());
					mCategoryid = mListCategoryParent.get(arg2).getId();
					mCurPage = 1;
					mNextPage = 1;
					requestRankingList();
					
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
//				mCategoryModule = mListCategoryNode.get(arg2);
//				RefalshUI();
				mBtn2.setText(mListCategoryNode.get(arg2).getName());
				mCategoryid = mListCategoryNode.get(arg2).getId();
				pwcategories.dismiss();
				mCurPage = 1;
				mNextPage = 1;
				requestRankingList();
			}
		});
		
		if (pwcategories == null) {
			pwcategories = new PopupWindow(vPopupWindow, mView.getWidth(),mView.getHeight() - mBtn1.getHeight(), true);
			pwcategories.setBackgroundDrawable(new BitmapDrawable());
			pwcategories.setOutsideTouchable(true);
		}
		
		int[] location = new int[2];  
		mBtn1.getLocationOnScreen(location);
		int y = location[1]; 
		
		pwcategories.showAtLocation(mView, Gravity.TOP, 0,y + mBtn1.getHeight());
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
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_CATEGORIES, postData, new CategoriesParser(), mIsNeddCache, CACHE_NAME_CCATEGORY + mCategoryParentId,
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
								Log.i("zjj", "getCategorytype 请求成功1:" + mListCategoryParent.get(0).getName());
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
								Log.i("zjj", "getCategorytype 请求成功2:" + mListCategoryNode.get(0).getName());
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
//				mCredittypeModule  = mListCredittype.get(arg2);
//				RefalshUI();
				mBtn3.setText(mListCredittype.get(arg2).getName());
				mCredittypeid = mListCredittype.get(arg2).getId();
				pwcredittype.dismiss();
				mCurPage = 1;
				mNextPage = 1;
				requestRankingList();
			}
		});

		if (pwcredittype == null) {
			pwcredittype = new PopupWindow(vPopupWindow, mView.getWidth(),mView.getHeight() - mBtn1.getHeight(), true);
			pwcredittype.setBackgroundDrawable(new BitmapDrawable());
			pwcredittype.setOutsideTouchable(true);
		}
		
		int[] location = new int[2];  
		mBtn1.getLocationOnScreen(location);
		int y = location[1]; 
		
		pwcredittype.showAtLocation(mView, Gravity.TOP, 0,y + mBtn1.getHeight());
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
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_CREDITTYPES_LIST, postData, new CredittypesParser(), mIsNeddCache, CACHE_NAME_CCREDITTYPE,
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
}
