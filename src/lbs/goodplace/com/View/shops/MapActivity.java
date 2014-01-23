package lbs.goodplace.com.View.shops;

import java.util.ArrayList;

import lbs.goodplace.com.R;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.GPS.CoordinateManage;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.util.AMapUtil;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;
import lbs.goodplace.com.obj.parser.ShopListParser;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

/**
 * AMapV2地图中简单介绍一些Marker的用法.
 */
public class MapActivity extends FragmentActivity implements
		LocationSource,
		AMapLocationListener,
		OnMarkerClickListener, OnInfoWindowClickListener, OnMarkerDragListener,
		OnMapLoadedListener, InfoWindowAdapter {
	
	public static String KEY_SHOP_LIST = "KEY_SHOP_LIST";
	public static String KEY_SHOWBOTTOMBAR = "KEY_SHOWBOTTOMBAR";
	public static String KEY_NEEDPAGEBAR = "KEY_NEEDPAGEBAR";
	public static String KEY_SHOPSUM = "KEY_SHOPSUM";
	public static String KEY_PARAM = "KEY_PARAM";	//请求参数
	private final int LOAD_SHOP_INFO_SUCCESS = 2;
	private final int LOAD_SHOP_INFO_FAILE = 3;
	
	private Context mContext;
	private boolean isInitMove = true;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private ArrayList<ShopModule> mDataSource = new ArrayList<ShopModule>();
	private boolean mIsShowBottomBar = false;
	private int mPageindex = 1;		//当前页数
	private int mPageMaxSum = 14;	//每页总条数
	private int mMaxShopsSum = 0;	//商店最大数目
	private boolean mIsNeedPagebar = true;	//是否需要分页bar
	//请求参数
	private String mParamStr = "";		//参数列表(按以下参数 排列)
	private String mCategoryId = "";	//行业分类子节点ID
	private String mCredittypeId = "";	//积分类型ID
	private String mRegionId = "";		//商圈ID
	private String mKeyword = "";		//搜索关键字
	private String mSorttypeId = "";	//排序类型ID
	private int mDistance = 0;	//选择的距离(默认为0，则搜全部)
	
	//控件
	private Button testUpdateButton = null;
	private EditText indexText = null;
	private Button mButtonMyloca;
	private Button mButtonLeft;
	private Button mButtonRight;
	private TextView mTextViewNum;
	private RelativeLayout mRelativeLayoutBottom;
	private AMap aMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locationoverlay);
		mContext = MapActivity.this;
		
		Bundle extras = getIntent().getExtras();
	      if(extras != null){
	      	if(extras.containsKey(KEY_SHOP_LIST)){
	      		mDataSource = (ArrayList<ShopModule>) extras.getSerializable(KEY_SHOP_LIST);
	  		}
	      	
	      	if(extras.containsKey(KEY_NEEDPAGEBAR)){
	      		mIsNeedPagebar = extras.getBoolean(KEY_NEEDPAGEBAR);
	      	}
	      	
	      	if(extras.containsKey(KEY_SHOPSUM)){
	      		mMaxShopsSum = extras.getInt(KEY_SHOPSUM);
	      	}
	      	
	      	if(extras.containsKey(KEY_PARAM)){
	      		mParamStr = extras.getString(KEY_PARAM);
	      		String[] strparam = mParamStr.split(",");
	      		mCategoryId = strparam[0];
	      		mCredittypeId = strparam[1];
	      		mRegionId = strparam[2];
	      		mKeyword = strparam[3];
	      		mSorttypeId = strparam[4];
	      		mDistance = Integer.valueOf(strparam[5]);
	      	}
	      }
		
        mButtonLeft = (Button)findViewById(R.id.btn_mapleft);
        mButtonLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mPageindex > 0 ){
					mPageindex--;
					requestShopInfo(mPageindex);
				}
			}
		});
        mButtonRight = (Button)findViewById(R.id.btn_mapright);
        mButtonRight.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mPageindex*mPageMaxSum + mDataSource.size() < mMaxShopsSum){
					mPageindex ++;
					requestShopInfo(mPageindex);
				}
			}
		});
        mTextViewNum = (TextView)findViewById(R.id.txt_mapnum);
        if(mDataSource.size()>0){
        	String shopindexstr = getString(R.string.mapshopindex);
        	shopindexstr = String.format(shopindexstr, (mPageindex - 1)*mPageMaxSum + 1 + "" , (mPageindex - 1)*mPageMaxSum + mDataSource.size() + "");
        	mTextViewNum.setText(shopindexstr);
        }else{
        	String shopindexstr = getString(R.string.mapshopindex);
        	shopindexstr = String.format(shopindexstr, "0","0");
        	mTextViewNum.setText(shopindexstr);
        }
        
		testUpdateButton = (Button)findViewById(R.id.button1);
		OnClickListener clickListener = new OnClickListener(){
				public void onClick(View v) {
//					testUpdateClick();
					
				}
	        };
	    testUpdateButton.setOnClickListener(clickListener);
	    
	    
	    mRelativeLayoutBottom = (RelativeLayout)findViewById(R.id.RLayout_bottom);
	    if(!mIsNeedPagebar){
	    	mRelativeLayoutBottom.setVisibility(View.GONE);
	    }  
	    
		init();
		
		if(mHandlerMapLoaded != null)
			mHandlerMapLoaded.sendEmptyMessage(0);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		deactivate();
		super.onDestroy();
	}
	
	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.bmapView)).getMap();
			if (AMapUtil.checkReady(this, aMap)) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
//		float distance = AMapUtils.calculateLineDistance(marker1, marker2);// 计算两点的距离，单位米
//		float area = AMapUtils.calculateArea(marker3, marker10);// 计算区域面积，单位平方米

		aMap.getUiSettings().setZoomControlsEnabled(false);// 隐藏缩放按钮

		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		
		// 镜头移动到当前坐标
		LatLng latlng = new LatLng(GoodPlaceContants.HXLAT,GoodPlaceContants.HXLNG);
		aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
		
		addMarkersToMap();// 往地图上添加marker
	}

	private void addMarkersToMap() {
		
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));
		myLocationStyle.strokeColor(Color.BLACK);
		myLocationStyle.strokeWidth(2);
		aMap.setMyLocationStyle(myLocationStyle);
		mAMapLocationManager = LocationManagerProxy.getInstance(mContext);
		aMap.setLocationSource(this);
		aMap.setMyLocationEnabled(true);// 设置为true表示系统定位按钮显示并响应点击，false表示隐藏，默认是false
		
		drawMarkers();// 添加10个带有系统默认icon的marker点
	}

	/**
	 * 绘制系统默认的10种marker背景图片
	 */
	public void drawMarkers() {
		LatLng latlng ;
		for(int i = 0 ; i < mDataSource.size();i++){
//			Log.i("zjj", "添加图标:" + mDataSource.get(i).name + ",lat:" + mDataSource.get(i).lat + ",lng:" + mDataSource.get(i).lng);
			
			double templat = CoordinateManage.getLatGG2HX(mDataSource.get(i).lat,mDataSource.get(i).lng);
			double templng = CoordinateManage.getLngGG2HX(mDataSource.get(i).lat,mDataSource.get(i).lng);
			latlng = new LatLng(templat,templng);
			
			aMap.addMarker(new MarkerOptions()
			.position(latlng)
			.title(mDataSource.get(i).name)
//			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
			.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.traffic_pin))));
		}
	}

	/**
	 * 把一个xml布局文件转化成view
	 */
//	public View getView(String title, String text) {
//		View view = getLayoutInflater().inflate(R.layout.marker, null);
//		TextView text_title = (TextView) view.findViewById(R.id.marker_title);
//		TextView text_text = (TextView) view.findViewById(R.id.marker_text);
//		text_title.setText(title);
//		text_text.setText(text);
//		return view;
//	}

	/**
	 * 把一个view转化成bitmap对象
	 */
	public static Bitmap getViewBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

	/**
	 * 清空地图上所有已经标注的marker
	 */
	public void onClearMap(View view) {
		if (AMapUtil.checkReady(this, aMap)) {
			aMap.clear();
		}
	}

	/**
	 * 重新标注所有的marker
	 */
	public void onResetMap(View view) {
		if (AMapUtil.checkReady(this, aMap)) {
			aMap.clear();
			addMarkersToMap();
		}
	}

	/**
	 * 对marker标注点点击响应事件
	 */
	@Override
	public boolean onMarkerClick(final Marker marker) {
		return false;

	}

	@Override
	public void onInfoWindowClick(Marker marker) {
//		ToastUtil.show(this, "你点击了Info  Window");
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {

	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
	}

	@Override
	public void onMapLoaded() {
		
	}

	@Override
	public View getInfoContents(Marker marker) {
			return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
			return null;
	}

	@Override
	public void onLocationChanged(Location aLocation) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		// TODO Auto-generated method stub
		if (mListener != null) {
			mListener.onLocationChanged(aLocation);

			if (isInitMove) {
				// 镜头移动到当前坐标
				if(mDataSource.size() > 0){	//先移到列表第一间商铺位置
					double templat = CoordinateManage.getLatGG2HX(mDataSource.get(0).lat,mDataSource.get(0).lng);
					double templng = CoordinateManage.getLngGG2HX(mDataSource.get(0).lat,mDataSource.get(0).lng);
					LatLng latlng = new LatLng(templat,templng);
					aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
					isInitMove = false;
				}else{
					LatLng latlng = new LatLng(aLocation.getLatitude(),aLocation.getLongitude());
					aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
					isInitMove = false;
				}
				
			}
		}
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
		}
		/*
		 * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true
		 */
		// Location API定位采用GPS和网络混合定位方式，时间最短是5000毫秒
		mAMapLocationManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 5000, 10, this);
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	//*****************翻页请求**************
	private void requestShopInfo(int curpage) {
//		ShowLoadingDialog();
		byte[] postData = JsonRequestManage.getSearchshopslist(mRegionId, mKeyword, mCategoryId, 
				mCredittypeId, mDistance + "", "", 
				"", "", "", 
				"", mSorttypeId, curpage, mPageMaxSum);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_SEARCHSHOPLIST, postData, new ShopListParser(), false, "123",
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null && object instanceof ShopListModule) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SHOP_INFO_SUCCESS;
							mRankHandler.sendMessage(msg);
							Log.i("zjj", "地图--附近商店 请求成功");
						} else {
							mRankHandler.sendEmptyMessage(LOAD_SHOP_INFO_FAILE);
							Log.i("zjj", "地图--附近商店 请求失败");
						}
						
					}
			
				});
	}
	
	private Handler mRankHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				//请求商家列表成功
				case LOAD_SHOP_INFO_SUCCESS :
					ShopListModule shopListModule = (ShopListModule) msg.obj;
					ArrayList<ShopModule> shopList = shopListModule.mShopList;
					
					mMaxShopsSum = shopListModule.mPageInfo.mTotalrecordcount;
					mPageindex = shopListModule.mPageInfo.mCurpag;
					
					if (shopList != null && shopList.size() > 0) {
						mDataSource.clear();
						mDataSource.addAll(shopList);
						
						//刷新界面
						if (AMapUtil.checkReady(mContext, aMap)) {
							aMap.clear();
							addMarkersToMap();// 往地图上添加marker
						}
					    
					    String shopindexstr = getString(R.string.mapshopindex);
			        	shopindexstr = String.format(shopindexstr, mPageindex*mPageMaxSum + 1 + "" , mPageindex*mPageMaxSum + mDataSource.size() + "");
			        	mTextViewNum.setText(shopindexstr);
					}
					
					break;
				
				//请求商家列表失败
				case LOAD_SHOP_INFO_FAILE :
					Toast.makeText(mContext, "请求商家列表失败", 0).show();
					break;
					
				default :
					break;
			}
			
//			HideLoadingDialog();
		}

	};
    
	static Handler mHandlerMapLoaded;
	public static void setMapLoadedHandler(Handler h){
//		Message m = new Message();
//		h.sendMessage(msg)
		mHandlerMapLoaded = h;
	}

}
