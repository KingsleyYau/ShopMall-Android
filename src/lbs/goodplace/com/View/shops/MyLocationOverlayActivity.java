//package lbs.goodplace.com.View.shops;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import lbs.goodplace.com.R;
//import lbs.goodplace.com.View.ModuleActivity;
//import lbs.goodplace.com.View.main.GoodPlaceApp;
//import lbs.goodplace.com.View.main.ShopInfoActivity;
//import lbs.goodplace.com.controls.GoodPlaceContants;
//import lbs.goodplace.com.manage.GPS.CoordinateManage;
//import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
//import lbs.goodplace.com.manage.requestmanage.RequestManager;
//import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
//import lbs.goodplace.com.manage.util.GlobalUtil;
//import lbs.goodplace.com.manage.util.MyMothod;
//import lbs.goodplace.com.obj.ShopListModule;
//import lbs.goodplace.com.obj.ShopModule;
//import lbs.goodplace.com.obj.ShopRankingInfo;
//import lbs.goodplace.com.obj.parser.ShopListParser;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.LinearLayout.LayoutParams;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.BDNotifyListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.mapapi.map.ItemizedOverlay;
//import com.baidu.mapapi.map.LocationData;
//import com.baidu.mapapi.map.MKMapViewListener;
//import com.baidu.mapapi.map.MapController;
//import com.baidu.mapapi.map.MapPoi;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.MyLocationOverlay;
//import com.baidu.mapapi.map.OverlayItem;
//import com.baidu.mapapi.map.PopupClickListener;
//import com.baidu.mapapi.map.PopupOverlay;
//import com.baidu.mapapi.utils.CoordinateConver;
//import com.baidu.platform.comapi.basestruct.GeoPoint;
//public class MyLocationOverlayActivity extends ModuleActivity {
//	public static String KEY_SHOP_LIST = "KEY_SHOP_LIST";
//	public static String KEY_SHOWBOTTOMBAR = "KEY_SHOWBOTTOMBAR";
//	public static String KEY_NEEDPAGEBAR = "KEY_NEEDPAGEBAR";
//	public static String KEY_SHOPSUM = "KEY_SHOPSUM";
//	public static String KEY_PARAM = "KEY_PARAM";	//请求参数
//	private final int LOAD_SHOP_INFO_SUCCESS = 2;
//	private final int LOAD_SHOP_INFO_FAILE = 3;
//	
//	//地图
//	static MapView mMapView = null;
//	private MapController mMapController = null;
//	public MKMapViewListener mMapListener = null;
//	private FrameLayout mMapViewContainer = null;
//	
//	// 定位相关
//	private LocationClient mLocClient;
//	public MyLocationListenner myListener = new MyLocationListenner();
//    public NotifyLister mNotifyer=null;
//    
//    //插图标相关
//    public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
//	
//    //控件
//	Button testUpdateButton = null;
//	private EditText indexText = null;
//	private Button mButtonMyloca;
//	private Button mButtonLeft;
//	private Button mButtonRight;
//	private TextView mTextViewNum;
//	private RelativeLayout mRelativeLayoutBottom;
//	
//	//变量
//	private Context mContext;
//	private MyLocationOverlay myLocationOverlay = null;
//	private int index =0;
//	private LocationData locData = null;
//	private boolean isInitMove = true;
//	private ArrayList<ShopModule> mDataSource;
//	private boolean mIsShowBottomBar = false;
//	private int mPageindex = 1;		//当前页数
//	private int mPageMaxSum = 10;	//每页总条数
//	private int mMaxShopsSum = 0;	//商店最大数目
//	private boolean mIsNeedPagebar = true;	//是否需要分页bar
//	//请求参数
//	private String mParamStr = "";		//参数列表(按以下参数 排列)
//	private String mCategoryId = "";	//行业分类子节点ID
//	private String mCredittypeId = "";	//积分类型ID
//	private String mRegionId = "";		//商圈ID
//	private String mKeyword = "";		//搜索关键字
//	private String mSorttypeId = "";	//排序类型ID
//	private int mDistance = 0;	//选择的距离(默认为0，则搜全部)
//	
//	Handler mHandler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
////            Toast.makeText(MyLocationOverlayActivity.this, "msg:" +msg.what, Toast.LENGTH_SHORT).show();
//        };
//    };
//    
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_locationoverlay);
//        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.activity_locationoverlay, mLayout_body);
//      
//        mContext = MyLocationOverlayActivity.this;
//        
//        Bundle extras = getIntent().getExtras();
//        if(extras != null){
//        	if(extras.containsKey(KEY_SHOP_LIST)){
//        		mDataSource = (ArrayList<ShopModule>) extras.getSerializable(KEY_SHOP_LIST);
//    		}
//        	
//        	if(extras.containsKey(KEY_NEEDPAGEBAR)){
//        		mIsNeedPagebar = extras.getBoolean(KEY_NEEDPAGEBAR);
//        	}
//        	
//        	if(extras.containsKey(KEY_SHOPSUM)){
//        		mMaxShopsSum = extras.getInt(KEY_SHOPSUM);
//        	}
//        	
//        	if(extras.containsKey(KEY_PARAM)){
//        		mParamStr = extras.getString(KEY_PARAM);
//        		String[] strparam = mParamStr.split(",");
//        		mCategoryId = strparam[0];
//        		mCredittypeId = strparam[1];
//        		mRegionId = strparam[2];
//        		mKeyword = strparam[3];
//        		mSorttypeId = strparam[4];
//        		mDistance = Integer.valueOf(strparam[5]);
//        	}
//        }
//        
//        mMapView = (MapView)findViewById(R.id.bmapView);
//        mButtonLeft = (Button)findViewById(R.id.btn_mapleft);
//        mButtonLeft.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(mPageindex > 0 ){
//					mPageindex--;
//					requestShopInfo(mPageindex);
//				}
//			}
//		});
//        mButtonRight = (Button)findViewById(R.id.btn_mapright);
//        mButtonRight.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(mPageindex*mPageMaxSum + mDataSource.size() < mMaxShopsSum){
//					mPageindex ++;
//					requestShopInfo(mPageindex);
//				}
//			}
//		});
//        mTextViewNum = (TextView)findViewById(R.id.txt_mapnum);
//        if(mDataSource.size()>0){
//        	String shopindexstr = getString(R.string.mapshopindex);
//        	shopindexstr = String.format(shopindexstr, (mPageindex - 1)*mPageMaxSum + 1 + "" , (mPageindex - 1)*mPageMaxSum + mDataSource.size() + "");
//        	mTextViewNum.setText(shopindexstr);
//        }else{
//        	String shopindexstr = getString(R.string.mapshopindex);
//        	shopindexstr = String.format(shopindexstr, "0","0");
//        	mTextViewNum.setText(shopindexstr);
//        }
//        
//        mMapController = mMapView.getController();
//        
//        initMapView();
//        
//        mLocClient = new LocationClient( this );
//        mLocClient.registerLocationListener( myListener );
//        
//        //位置提醒相关代码
//        mNotifyer = new NotifyLister();
//        mNotifyer.SetNotifyLocation(42.03249652949337,113.3129895882556,3000,"bd09ll");//4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
//        mLocClient.registerNotify(mNotifyer);
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true);//打开gps
//        option.setCoorType("bd09ll");     //设置坐标类型
//        option.setScanSpan(3000);
//        mLocClient.setLocOption(option);
//        mLocClient.start();
//        mMapView.getController().setZoom(14);
//        mMapView.getController().enableClick(true);
//        
//        mMapView.displayZoomControls(false);
//        mMapListener = new MKMapViewListener() {
//			
//			@Override
//			public void onMapMoveFinish() {
//				// TODO Auto-generated method stub
//			}
//			
//			@Override
//			public void onClickMapPoi(MapPoi mapPoiInfo) {
//				// TODO Auto-generated method stub
//				String title = "";
//				if (mapPoiInfo != null){
//					title = mapPoiInfo.strText;
//					Toast.makeText(MyLocationOverlayActivity.this,title,Toast.LENGTH_SHORT).show();
//				}
//			}
//		};
//		mMapView.regMapViewListener(GoodPlaceApp.getApp().mBMapManager, mMapListener);
//		myLocationOverlay = new MyLocationOverlay(mMapView);
//		locData = new LocationData();
//	    myLocationOverlay.setData(locData);
//		mMapView.getOverlays().add(myLocationOverlay);
//		myLocationOverlay.enableCompass();
//		mMapView.refresh();
//		
//		testUpdateButton = (Button)findViewById(R.id.button1);
//		OnClickListener clickListener = new OnClickListener(){
//				public void onClick(View v) {
//					testUpdateClick();
//				}
//	        };
//	    testUpdateButton.setOnClickListener(clickListener);
//	    
//	    
//	    mRelativeLayoutBottom = (RelativeLayout)findViewById(R.id.RLayout_bottom);
//	    if(!mIsNeedPagebar){
//	    	mRelativeLayoutBottom.setVisibility(View.GONE);
//	    }
//	   
//	    itemizedOverlay();
//	    initTitleBar();
//	    addShopItem();
//    }
//    
//    @Override
//    protected void onPause() {
//        mMapView.onPause();
//        super.onPause();
//    }
//    
//    @Override
//    protected void onResume() {
//        mMapView.onResume();
//        super.onResume();
//    }
//    
//    @Override
//    protected void onDestroy() {
//    	mLocClient.unRegisterLocationListener( myListener );
//        mMapView.destroy();
//        super.onDestroy();
//    }
//    
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//    	super.onSaveInstanceState(outState);
//    	mMapView.onSaveInstanceState(outState);
//    	
//    }
//    
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//    	super.onRestoreInstanceState(savedInstanceState);
//    	mMapView.onRestoreInstanceState(savedInstanceState);
//    }
//    
//    public void testUpdateClick(){
//        mLocClient.requestLocation();
//    }
//    private void initMapView() {
//        mMapView.setLongClickable(true);
//        //mMapController.setMapClickEnable(true);
//        //mMapView.setSatellite(false);
//    }
//   
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
//    
//    /**
//     * 
//     */
//    private void initTitleBar(){
//    	LayoutParams p = new LayoutParams(MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size) , MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size));
//    	
//    	mButtonMyloca = new Button(this);
//    	mButtonMyloca.setLayoutParams(p);
//    	mButtonMyloca.setBackgroundResource(R.drawable.locamap_selector);//location_circle_selector);
//    	mButtonMyloca.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)), mHandler.obtainMessage(1));
//			}
//		});
//		setTitleRightButton(mButtonMyloca);
//		
//		if(mDataSource != null){
//			if(mDataSource.size() == 1){
//				setTitleTxt(mDataSource.get(0).name);
//			}
//		}
//    }
//
//	
//	/**
//     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
//     */
//    public class MyLocationListenner implements BDLocationListener {
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            if (location == null)
//                return ;
//            
//            locData.latitude = location.getLatitude();
//            locData.longitude = location.getLongitude();
//            locData.direction = 2.0f;
//            locData.accuracy = location.getRadius();
//            locData.direction = location.getDerect();
//            Log.d("zjj",String.format("before: lat: %f lon: %f", location.getLatitude(),location.getLongitude()));
//           // GeoPoint p = CoordinateConver.fromGcjToBaidu(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
//          //  Log.d("loctest",String.format("before: lat: %d lon: %d", p.getLatitudeE6(),p.getLongitudeE6()));
//            myLocationOverlay.setData(locData);
//            
////            mMapView.refresh();
//            
//            if(isInitMove)
//            {
//            	mMapView.refresh();
//            	mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)), mHandler.obtainMessage(1));
//            	isInitMove = false;
//            }
//            
//        }
//        
//        public void onReceivePoi(BDLocation poiLocation) {
//            if (poiLocation == null){
//                return ;
//            }
//        }
//    }
//    
//    public class NotifyLister extends BDNotifyListener{
//        public void onNotify(BDLocation mlocation, float distance) {
//        }
//    }
//
//    /**
//     * 商店定位
//     */
//    private void itemizedOverlay(){
//    	mGeoList.clear();
//    	double bdlat;
//    	double bdlng;
//    	
//    	for(int i = 0 ; i < mDataSource.size(); i++){
//    		ShopModule s = mDataSource.get(i);
//    		Log.i("zjj", "地图接收到的商店列表  （接口）坐标   lat:" + s.lat + ",lng:" + s.lng);
////    		bdlat = CoordinateManage.getLatGG2BD(s.lat,s.lng);//CoordinateManage.getLatHX2BD(s.lat,s.lng);//23.13534586028;//CoordinateManage.getLatGG2BD(s.lat,s.lng);
////    		bdlng = CoordinateManage.getLogGG2BD(s.lat,s.lng);//CoordinateManage.getLogHX2BD(s.lat,s.lng);//113.33259987093;//CoordinateManage.getLogGG2BD(s.lat,s.lng);
////    		s.lat = bdlat;
////    		s.lng = bdlng;
////    		Log.i("zjj", "地图接收到的商店列表  （百度）坐标   lat:" + s.lat + ",lng:" + s.lng);
//    		try{
//    			GeoPoint geppoint = new GeoPoint((int)(s.lat* 1e6), (int)(s.lng * 1e6));
//        		GeoPoint bdgeppoint = CoordinateConver.fromWgs84ToBaidu(geppoint);
//        		OverlayItem item= new OverlayItem(bdgeppoint,mDataSource.get(i).name,mDataSource.get(i).id + "") ;
//        		item.setMarker(getResources().getDrawable(R.drawable.traffic_pin));
//        		mGeoList.add(item);
//    		}catch (Exception e) {
//				// TODO: handle exception
//    			// 有可能Caused by: java.lang.NullPointerException
//			}
//    	}
//    }
//    
//    /**
//     * 在地图上加点
//     */
//    private void addShopItem(){
//    	Drawable marker = getResources().getDrawable(R.drawable.traffic_pin);
////	    mMapView.getOverlays().clear();
//	    OverlayTest ov = new OverlayTest(marker, this);
//	    for(OverlayItem item : mGeoList){
//	    	ov.addItem(item);
//	    }
//	    
//	    mMapView.getOverlays().add(ov);
////	    mMapView.getOverlays().add(myLocationOverlay);	//人的坐标
//    }
//    
//	//*****************翻页请求**************
//	private void requestShopInfo(int curpage) {
//		ShowLoadingDialog();
//		byte[] postData = JsonRequestManage.getSearchshopslist(mRegionId, mKeyword, mCategoryId, 
//				mCredittypeId, mDistance + "", "", 
//				"", "", "", 
//				"", mSorttypeId, curpage, mPageMaxSum);
//		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_SEARCHSHOPLIST, postData, new ShopListParser(), false, "123",
//				new IDataListener(){
//
//					@Override
//					public void loadFinish(boolean success, Object object) {
//						if (success && object != null && object instanceof ShopListModule) {
//							// 还在子线程操作，handler是放回主线程处理
//							Message msg = new Message();
//							msg.obj = object;
//							msg.what = LOAD_SHOP_INFO_SUCCESS;
//							mRankHandler.sendMessage(msg);
//							Log.i("lch", "地图--附近商店 请求成功");
//						} else {
//							mRankHandler.sendEmptyMessage(LOAD_SHOP_INFO_FAILE);
//							Log.i("lch", "地图--附近商店 请求失败");
//						}
//						
//					}
//			
//				});
//	}
//	
//	private Handler mRankHandler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//				//请求商家列表成功
//				case LOAD_SHOP_INFO_SUCCESS :
//					ShopListModule shopListModule = (ShopListModule) msg.obj;
//					ArrayList<ShopModule> shopList = shopListModule.mShopList;
//					
//					mMaxShopsSum = shopListModule.mPageInfo.mTotalrecordcount;
//					mPageindex = shopListModule.mPageInfo.mCurpag;
//					
//					if (shopList != null && shopList.size() > 0) {
//						mDataSource.clear();
//						mDataSource.addAll(shopList);
//						//刷新界面
//						mMapView.getOverlays().clear();
//						itemizedOverlay();
//					    addShopItem();
//					    mMapView.refresh();
//					    
//					    String shopindexstr = getString(R.string.mapshopindex);
//			        	shopindexstr = String.format(shopindexstr, mPageindex*mPageMaxSum + 1 + "" , mPageindex*mPageMaxSum + mDataSource.size() + "");
//			        	mTextViewNum.setText(shopindexstr);
//					}
//					
//					break;
//				
//				//请求商家列表失败
//				case LOAD_SHOP_INFO_FAILE :
//					Toast.makeText(mContext, "请求商家列表失败", 0).show();
//					break;
//					
//				default :
//					break;
//			}
//			
//			HideLoadingDialog();
//		}
//
//	};
//    
//}
//
//class OverlayTest extends ItemizedOverlay<OverlayItem> {
//    public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
//	private Context mContext = null;
//    static PopupOverlay pop = null;
//    private int mIndex = 0;
//
//	public OverlayTest(Drawable marker,Context context){
//		super(marker);
//		this.mContext = context;
//        pop = new PopupOverlay( MyLocationOverlayActivity.mMapView,new PopupClickListener() {
//			
//			@Override
//			public void onClickedPopup() {
////				 Log.d("hjtest  ", "clickpop");
////				Intent intent = new Intent(mContext, ShopInfoActivity.class);
////				int id = mGeoList.get(mIndex).;
////				intent.putExtra(GoodPlaceContants.KEY_SHOP_ID, id);
////				mContext.startActivity(intent);
//			}
//		});
//	    populate();
//		
//	}
//	protected boolean onTap(int index){
//		Drawable marker = this.mContext.getResources().getDrawable(R.drawable.pop);  //得到需要标在地图上的资源
//		BitmapDrawable bd = (BitmapDrawable) marker;
//        Bitmap popbitmap = bd.getBitmap();
//        Bitmap popmixbitmap = MyMothod.mixBitmap(popbitmap,mGeoList.get(index).getTitle(),MyMothod.Dp2Px(mContext, 16));
//	    pop.showPopup(popmixbitmap, mGeoList.get(index).getPoint(), 32);
//		// int latspan = this.getLatSpanE6();
//		// int lonspan = this.getLonSpanE6();
////		Toast.makeText(this.mContext, mGeoList.get(index).getTitle(), Toast.LENGTH_SHORT).show();
//	    mIndex = index;
//		super.onTap(index);
//		return false;
//	}
//	public boolean onTap(GeoPoint pt, MapView mapView){
//		if (pop != null){
//			pop.hidePop();
//		}
//		super.onTap(pt,mapView);
//		return false;
//	}
//	
//	@Override
//	protected OverlayItem createItem(int i) {
//		return mGeoList.get(i);
//	}
//	
//	@Override
//	public int size() {
//		return mGeoList.size();
//	}
//	public void addItem(OverlayItem item){
//		mGeoList.add(item);
//		populate();
//	}
//	public void removeItem(int index){
//		mGeoList.remove(index);
//		populate();
//	}
//}

