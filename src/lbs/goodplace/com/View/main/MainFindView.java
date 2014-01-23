package lbs.goodplace.com.View.main;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.City.CityListActivity;
import lbs.goodplace.com.View.categories.CategoriesActivity;
import lbs.goodplace.com.View.search.SearchActivity;
import lbs.goodplace.com.View.shops.SearchShopsActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.util.GlobalUtil;
import lbs.goodplace.com.manage.util.MyMothod;
import lbs.goodplace.com.obj.CategoryModule;
import lbs.goodplace.com.obj.MainMenuItem;
import lbs.goodplace.com.obj.NearinfoModule;
import lbs.goodplace.com.obj.RegionModule;
import lbs.goodplace.com.obj.parser.CategoriesParser;
import lbs.goodplace.com.obj.parser.NearInfoParser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class MainFindView extends LinearLayout implements View.OnClickListener{
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;
	//
	private Context mContext;
	private GridView myGVgridview1;
	private Button mButtonCity;		//城市按钮
	private Button mButtonCredittype;	//积分按钮
	private Button mBtnRank; //排行版按钮
	private LinearLayout mLinearLayoutSearch;
	//
	private MainAdapter mMainAdapter;
	private List<MainMenuItem> mMyGVList1 = new ArrayList<MainMenuItem>();
	private List<CategoryModule> mListCategoryParent = new ArrayList<CategoryModule>();
	private String mCategoryParentId = "0";	//行业分类ID
	private NearinfoModule mNearinfo = new NearinfoModule();
	//缓存
	private String CACHE_NAME = "maingetcategories";
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	private NetState mNetState;
	
	public MainFindView(final Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.mainfind_view, this);

		mContext = context;
		
		myGVgridview1 = (GridView) findViewById(R.id.mainfind_gridview1);
		// 添加元素给gridview
		mMainAdapter = new MainAdapter(context, mMyGVList1);
		myGVgridview1.setAdapter(mMainAdapter);
		myGVgridview1.setSelector(new ColorDrawable(Color.TRANSPARENT));
		myGVgridview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MainMenuItem item = (MainMenuItem) arg0.getItemAtPosition(arg2);
				if (item.getId() == 0) {
//					Intent intent = new Intent();
//					intent.setClass(context, EventsListActivity.class);
//					intent.putExtra(EventsListActivity.CATEGORYID_KEY,
//							RequestCategoryID.EVENTS_NEWS_ID);
//					context.startActivity(intent);
					
//					Intent intent = new Intent(mContext,SearchShopsActivity.class);
//					mContext.startActivity(intent);
					
					Intent i = new Intent(mContext, CategoriesActivity.class);
					mContext.startActivity(i);
					
				}else{
					Intent intent = new Intent(mContext,SearchShopsActivity.class);
					intent.putExtra(SearchShopsActivity.KEY_CATEGORY,item.getId()+"");
					intent.putExtra(SearchShopsActivity.KEY_CATEGORY_BUTTONTEXT,mContext.getString(R.string.all) + item.getName());
					intent.putExtra(SearchShopsActivity.KEY_ISSHOW_REGIONS,false);
					mContext.startActivity(intent);
				}
//				else if (item.getId() == 1) {
//				} else if (item.getId() == 2) {
//				} else if (item.getId() == 3) {
//				} else if (item.getId() == 4) {
//				} else if (item.getId() == 5) {
//				}
			}
		});
		
		mLinearLayoutSearch = (LinearLayout)findViewById(R.id.LLayout_search);
		mLinearLayoutSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, SearchActivity.class);
				mContext.startActivity(i);
			}
		});

		mButtonCity = (Button)findViewById(R.id.Btn_city);
		mButtonCity.setOnClickListener(this);
		
		mButtonCredittype = (Button)findViewById(R.id.Btn_credit);
		mButtonCredittype.setOnClickListener(this);
		
		mBtnRank = (Button)findViewById(R.id.Btn_rank);
		mBtnRank.setOnClickListener(this);
		
		mNetState = new NetState(mContext);
		
//		getCategorytype();
		initData();
	}

	/**
	 * 初始化 按钮数据
	 */
	private void initData() {
		mMyGVList1.clear();
		
		for(int i = 0 ; i < mListCategoryParent.size();i++){
			MainMenuItem mi = new MainMenuItem();
			mi.setId(Integer.valueOf( mListCategoryParent.get(i).getId()));
			mi.setCount(0);
			mi.setName(mListCategoryParent.get(i).getName());
//			mi.setPicResId(R.drawable.main_more);
			mi.setmIconUrl(mListCategoryParent.get(i).getFaviconurl());
			mi.setBottomNum1(mListCategoryParent.get(i).getHyfshopcount());
			mi.setBottomNum2(mListCategoryParent.get(i).getDsfshopcount());
			
			mMyGVList1.add(mi);
		}
		
		
		MainMenuItem mi1 = new MainMenuItem();
		mi1.setId(0);
		mi1.setCount(0);
		mi1.setName(mContext.getResources().getString(R.string.more));
		mi1.isShowBottom = false;
		mi1.setPicResId(R.drawable.main_more);

//		MainMenuItem mi2 = new MainMenuItem();
//		mi2.setId(1);
//		mi2.setCount(0);
//		mi2.setName(getResources().getString(R.string.activity));
//		mi2.setPicResId(R.drawable.icon_class_activitys_selector);

//		MainMenuItem mi3 = new MainMenuItem();
//		mi3.setId(2);
//		mi3.setCount(0);
//		mi3.setName(getResources().getString(R.string.comment));
//		mi3.setPicResId(R.drawable.icon_class_comment_selector);

//		MainMenuItem mi4 = new MainMenuItem();
//		mi4.setId(3);
//		mi4.setCount(0);
//		mi4.setName(getResources().getString(R.string.album));
//		mi4.setPicResId(R.drawable.icon_class_album_selector);

//		MainMenuItem mi5 = new MainMenuItem();
//		mi5.setId(4);
//		mi5.setCount(0);
//		mi5.setName(getResources().getString(R.string.courseware));
//		mi5.setPicResId(R.drawable.icon_class_courseware_selector);

//		MainMenuItem mi6 = new MainMenuItem();
//		mi6.setId(5);
//		mi6.setCount(0);
//		mi6.setName(getResources().getString(R.string.face2face));
//		mi6.setPicResId(R.drawable.icon_class_face2face_selector);

		mMyGVList1.add(mi1);
//		mMyGVList1.add(mi1);
//		mMyGVList1.add(mi1);
//		mMyGVList1.add(mi1);
//		mMyGVList1.add(mi1);
//		mMyGVList1.add(mi1);
//		mMyGVList1.add(mi1);
//		mMyGVList1.add(mi1);
//		mMyGVList1.add(mi1);
//		mMyGVList1.add(mi1);
//		mMyGVList1.add(mi1);
//		mMyGVList1.add(mi1);
//		mMyGVList1.add(mi1);
//		mMyGVList1.add(mi1);
//		mMyGVList1.add(mi2);
//		mMyGVList1.add(mi3);
//		mMyGVList1.add(mi4);
//		mMyGVList1.add(mi5);
//		mMyGVList1.add(mi6);
		
//		myGVgridview1.postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				mHandler.sendEmptyMessage(0);
//			}
//		}, 800);
		
		mMainAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		if (v == mButtonCredittype) {
			Intent intent = new Intent(mContext, CredittypeActivity.class);
			mContext.startActivity(intent);
		}
		
		if (v == mBtnRank) {
			Intent intent = new Intent(mContext, RankingView.class);
			mContext.startActivity(intent);
		}
		
		if(v == mButtonCity){
			Intent intent = new Intent(mContext, CityListActivity.class);
			mContext.startActivity(intent);
		}
	}
	
	/**
	 * 设置城市按钮的内容
	 * @param text
	 */
	public void SetCityBtnText(String text){
		mButtonCity.setText(text);
	}
	
	/**
	 * 
	 */
//	private Handler mHandler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			super.handleMessage(msg);
//			int h = 50;
//			if(mMyGVList1.size() > 0 && mMyGVList1.size() < 4){
//				h = MyMothod.Dp2Px(mContext, 88);
//			}else if(mMyGVList1.size() > 3 && mMyGVList1.size() < 7){
//				h = MyMothod.Dp2Px(mContext, 88*2);
//			}else if(mMyGVList1.size() > 6 ){
//				h = MyMothod.Dp2Px(mContext, 88*3);
//			}
//			
//			LayoutParams p = new LayoutParams(LayoutParams.FILL_PARENT , h);
//			myGVgridview1.setLayoutParams(p);
//		}
//	};
	
	
	/**
	 * 取行业分类数据
	 */
	public void getCategorytype(){
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = JsonRequestManage.getNearshopcoutinfoPostData();
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_NEARSHOPCOUNTINFO, postData, new NearInfoParser(), mIsNeddCache, CACHE_NAME,
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
							
//							//添加全部
//							CategoryModule cmall = new CategoryModule();
//							cmall.setId("");
//							cmall.setName(mContext.getString(R.string.allchannel));
//							mListCategoryParent.add(cmall);
							
							//
							mNearinfo = (NearinfoModule)msg.obj;
							mListCategoryParent.addAll(mNearinfo.mCategoriesList);
							
							if(mListCategoryParent != null){
								initData();
							}
						}else if(mCategoryParentId.equals("")){
							break;
						}
					}
					break;
					
				default :
					Log.i("zjj", "getCategorytype 请求失败");
					break;
			}
		}
	};
	
}
