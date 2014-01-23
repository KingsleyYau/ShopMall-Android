package lbs.goodplace.com.View.City;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.City.CityListAdapter.CityViewHolder;
import lbs.goodplace.com.View.City.ProvinceListAdapter.ProvinceViewHolder;
import lbs.goodplace.com.View.main.GoodPlaceApp;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.GPS.GPSLocation;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.util.GlobalUtil;
import lbs.goodplace.com.obj.CityModule;
import lbs.goodplace.com.obj.GatewayListModule;
import lbs.goodplace.com.obj.GatewayModule;
import lbs.goodplace.com.obj.ProvinceModule;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.parser.CityListParser;
import lbs.goodplace.com.obj.parser.CityParser;
import lbs.goodplace.com.obj.parser.GatwayParser;
import lbs.goodplace.com.obj.parser.ShopListParser;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 根据坐标定位城市
 * http://maps.google.com/maps/api/geocode/json?latlng=31.230707,121.472916&language=zh-CN&sensor=true
 * @author Administrator
 *
 */
public class CityListActivity extends ModuleActivity{
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;
	private String TAG = "zjj";
	//
	private Context mContext;
	private List<ProvinceModule> mListProvince = new ArrayList<ProvinceModule>();
//	private List<CityModule> mListProvince = new ArrayList<CityModule>();
	private List<CityModule> mListCity = new ArrayList<CityModule>();
	private List<CityModule> mListSearchResultCity = new ArrayList<CityModule>();
	private ProvinceListAdapter mAdapterProvince;
	private CityListAdapter mAdapterCity;
	private CityListAdapter mAdapterSearchResultCity;
	private int mListViewProvinceChooseIndex = 0;
	
	//定位
//	private LocationManager locationManager;
//	private String provider;
//	private Location location;
//	private Address address;
//	private Vibrator mVibrator01 =null;
//	private LocationClient mLocClient;
	private GPSLocation mGPSLocation;
	//缓存
	private String CACHE_NAME = "getcitylist";
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	private NetState mNetState;
	
	//控件
	private ListView mListViewProvince;
	private ListView mListViewCity;
	private ListView mListViewSearchResultCity;
	private TextView mTextViewLoca;
	private EditText mEditTextSearch;
	private Button mButtonSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.citylist_view, mLayout_body);
		mContext = CityListActivity.this;
		
		mTextViewLoca = (TextView)findViewById(R.id.textView_loca);
		mListViewProvince = (ListView)findViewById(R.id.listview_Province);
		mListViewCity = (ListView)findViewById(R.id.listview_City);
		mListViewSearchResultCity = (ListView)findViewById(R.id.listview_searchresult);
		
		mNetState = new NetState(mContext);
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		initData();
		
		mAdapterProvince = new ProvinceListAdapter(mContext, mListProvince);
		mAdapterCity = new CityListAdapter(mContext, mListCity);
		mAdapterCity.mIsinitfirstindexbg = false;
		mAdapterSearchResultCity = new CityListAdapter(mContext, mListSearchResultCity);
		mAdapterSearchResultCity.mIsinitfirstindexbg = false;
		
		mListViewProvince.setAdapter(mAdapterProvince);
		mListViewCity.setAdapter(mAdapterCity);
		mListViewSearchResultCity.setAdapter(mAdapterSearchResultCity);
		
		mListViewSearchResultCity.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(!mNetState.isNetUsing()){
					ShowToast(R.string.nonet);
					return;
				}
				loadData(mListSearchResultCity.get(arg2).getId()+"");
			}
		});
		
		mListViewCity.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(!mNetState.isNetUsing()){
					ShowToast(R.string.nonet);
					return;
				}
				loadData(mListCity.get(arg2).getId()+"");
			}
		});
		
		mListViewProvince.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				mListCity.clear();
				mListCity.addAll(mListProvince.get(arg2).getCityList());
				mAdapterCity.notifyDataSetChanged();
				
				Log.i(TAG, "mListCity size" + mListCity.size());
				
				ProvinceViewHolder vh = (ProvinceViewHolder)arg0.getChildAt(mListViewProvinceChooseIndex).getTag();
				vh.mLinearLayoutbg.setBackgroundResource(R.drawable.list_parent_selector);
				
				ProvinceViewHolder vh2 = (ProvinceViewHolder)arg0.getChildAt(arg2).getTag();
				vh2.mLinearLayoutbg.setBackgroundResource(R.drawable.list_parent_click_selector);
				
				mListViewProvinceChooseIndex = arg2;
			}
		});
		
		
		mButtonSearch = (Button)findViewById(R.id.button_searchcitybtn);
		mButtonSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListSearchResultCity.clear();
				
				for(int i = 0 ; i < mListProvince.size(); i ++){
					for(int j = 0 ; j < mListProvince.get(i).getCityList().size(); j++){
						if(mListProvince.get(i).getCityList().get(j).getName().indexOf(mEditTextSearch.getText().toString()) > -1){
							mListSearchResultCity.add(mListProvince.get(i).getCityList().get(j));
						}
					}
				}
				
				mListViewProvince.setVisibility(View.GONE);
				mListViewCity.setVisibility(View.GONE);
				mListViewSearchResultCity.setVisibility(View.VISIBLE);
				
				mAdapterSearchResultCity.notifyDataSetChanged();
			}
		});
		
		mEditTextSearch = (EditText)findViewById(R.id.editText_searchcity);
		mEditTextSearch.addTextChangedListener(new TextWatcher() {             
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) { 
		    } 
		      
		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count, int after) { 
		    } 
		      
		    @Override
		    public void afterTextChanged(Editable s) { 
		    	if(mEditTextSearch.getText().toString().length() > 0){
					mButtonSearch.setVisibility(View.VISIBLE);
				}else{
					mButtonSearch.setVisibility(View.GONE);
					
					mListViewProvince.setVisibility(View.VISIBLE);
					mListViewCity.setVisibility(View.VISIBLE);
					mListViewSearchResultCity.setVisibility(View.GONE);
				}
		    } 
		}); 
		
		
		mGPSLocation = new GPSLocation(mContext);
		setTitleText(R.string.citylist);
		getLocation();
	}
	
	@Override
	protected void onDestroy(){
//		locationManager.removeUpdates(locationListener);
//		mLocClient.stop();
//		mGPSLocation.deactivate();
		super.onDestroy();
	}
	
	/**
	 * 取城市列表
	 */
	private void initData(){
		//请求接口
		Log.i("zjj", "GoodPlaceContants.URL_CITY_LIST:" + GoodPlaceContants.URL_CITY_LIST);
		
		byte[] postData = JsonRequestManage.getCityList();
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_CITY_LIST, postData, new CityListParser(), mIsNeddCache, CACHE_NAME,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mHandler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
							Log.i("zjj", "请求成功");
						} else {
							mHandler.sendEmptyMessage(LOAD_SUCCESS);
							Log.i("zjj", "请求失败");
						}
						
					}
			
				});
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case LOAD_SUCCESS :
					if(msg.obj!=null){
						mListProvince.clear();
						mListProvince.addAll((List<ProvinceModule>)msg.obj);
						Log.i("zjj", "citylist success" + mListProvince.get(0).getName());
						
						mListCity.clear();
						mListCity.addAll(mListProvince.get(0).getCityList());
						
						mAdapterProvince.notifyDataSetChanged();
						mAdapterCity.notifyDataSetChanged();
					}
					
					break;
					
				default :
					Log.i("zjj", "fales");
					break;
			}
		}

	};
	
	/**
	 * 
	 */
//	private Handler mHandlerLocachange = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			if(msg.arg1 == GoodPlaceApp.MyLOCATION_CHANGE){
////				BDLocation l = ((GoodPlaceApp)getApplication()).getLocation();
////				mTextViewLoca.setText("latitude :"
////						+ l.getLatitude()
////						+ ",lontitude :"
////						+ l.getLongitude()
////						+ "," 
////						+ l.getAddrStr());	//getCity()
//				AMapLocation l = (AMapLocation)msg.obj;
//				mTextViewLoca.setText(l.getCity());
//				
//				GoodPlaceContants.LOCACITYNAME = l.getCity();
//			}
//		}
//	};
	
	/**
	 * 切换城市
	 */
	private void loadData(String cityid) {
		ShowLoadingDialog();
		byte[] postData = JsonRequestManage.getChangeCity(cityid);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_CHANGECITY, postData, new CityParser(), false, "123",
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mChangecityHandler.sendMessageDelayed(msg, 500);
							Log.i("lch", "请求成功");
						} else {
							mChangecityHandler.sendEmptyMessage(LOAD_SUCCESS);
							Log.i("lch", "请求失败");
						}
						
					}
			
				});
	}

	private Handler mChangecityHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case LOAD_SUCCESS :
					HideLoadingDialog();
					
					if(msg.obj != null){
						CityModule cm = (CityModule)msg.obj;
						GoodPlaceContants.SESSEIONMOUDLE.setCity(cm);
						GoodPlaceContants.CITY_ID = cm.getId()+"";
						
						Log.i("zjj", "切换城市 城市坐标 lat:" + cm.getLat() + ",lng:" + cm.getLng() + ".GoodPlaceContants.LOCACITYNAME:" + GoodPlaceContants.LOCACITYNAME);
						
						if(GoodPlaceContants.LOCACITYNAME.indexOf(cm.getName()) > -1){	//选择的城市是当前城市
							Intent intent = new Intent(GlobalUtil.BROADCAST_KEY_CHANGECITYSUCCESS);
							mContext.sendBroadcast(intent);
							
//							GoodPlaceContants.ISYOURCITY = true;
						}else{	//选择的城市不是当前城市
							Intent intent = new Intent(GlobalUtil.BROADCAST_KEY_CHANGECITYNOTLOCA);
							mContext.sendBroadcast(intent);
							
							//设置当前坐标为切换后的城市坐标
							GoodPlaceContants.LAT = cm.getLat();
							GoodPlaceContants.LNG = cm.getLng();
							
//							GoodPlaceContants.ISYOURCITY = false;
						}
					}
					finish();
					break;
					
				case LOAD_FAILE :
					HideLoadingDialog();
					Toast.makeText(mContext, "切换城市失败", 0).show();
					break;
					
				default :
					Log.i("lch", "fales");
					break;
			}
		}

	};
	
	private void getLocation(){
//		mLocClient = ((GoodPlaceApp)getApplication()).mLocationClient;
//		mVibrator01 =(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
//		((GoodPlaceApp)getApplication()).mVibrator01 = mVibrator01;
//		((GoodPlaceApp)getApplication()).mHandlerLoca = mHandlerLocachange;
//		setLocationOption();
//		mLocClient.start();
		
		mGPSLocation.activate(new OnLocationChangedListener() {
			
			@Override
			public void onLocationChanged(Location l) {
				// TODO Auto-generated method stub
				AMapLocation aml = (AMapLocation)l;
				mTextViewLoca.setText(aml.getCity());
				
				GoodPlaceContants.LOCACITYNAME = aml.getCity();
				
				mGPSLocation.deactivate();
			}
		});
		
	}
	
	//设置相关参数
//	private void setLocationOption(){
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(false);				//打开gps
//		option.setCoorType("bd90ll");		//设置坐标类型
//		option.setServiceName("com.baidu.location.service_v2.9");
//		option.setPoiExtraInfo(false);	
//		option.setAddrType("all");	
////		if(null!=mSpanEdit.getText().toString())
////		{
////			boolean b = isNumeric(mSpanEdit.getText().toString());
////			 if(b)
////			{
////				option.setScanSpan(Integer.parseInt(mSpanEdit.getText().toString()));	//设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
////			}
////		}
//		option.setScanSpan(3000);
//		
////		if(mPriorityCheck.isChecked())
////		{
////			option.setPriority(LocationClientOption.NetWorkFirst);      //设置网络优先
////		}
////		else
////		{
//			option.setPriority(LocationClientOption.GpsFirst);        //不设置，默认是gps优先
////		}
//
//		option.setPoiNumber(10);
//		option.disableCache(true);		
//		mLocClient.setLocOption(option);
//	}
}



//private void getLocation(){
//	// 获取 LocationManager 服务
//	locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//	// 获取 Location Provider
//	getProvider();
//	// 如果未设置位置源，打开 GPS 设置界面
//	openGPS();
//	
//	if(provider == null){
//		return;
//	}
//	// 获取位置
//	location = locationManager.getLastKnownLocation(provider);
//	// 显示位置信息到文字标签
//	updateWithNewLocation(location);
//	// 注册监听器 locationListener ，第 2 、 3 个参数可以控制接收 gps 消息的频度以节省电力。第 2 个参数为毫秒，
//	// 表示调用 listener 的周期，第 3 个参数为米 , 表示位置移动指定距离后就调用 listener
//	locationManager.requestLocationUpdates(provider, 2000, 10,locationListener);
//}
//
//// 判断是否开启 GPS ，若未开启，打开 GPS 设置界面
//private void openGPS() {
//	if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
//			|| locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
//	) {
//		Toast.makeText(this, " 位置源已设置！ ", Toast.LENGTH_SHORT).show();
//		return;
//	}
//
//	Toast.makeText(this, " 位置源未设置！ ", Toast.LENGTH_SHORT).show();
//	// 转至 GPS 设置界面
//	Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
//	startActivityForResult(intent, 0);
//}
//
//// 获取 Location Provider
//private void getProvider() {
//	// 构建位置查询条件
//	Criteria criteria = new Criteria();
//	// 查询精度：高
//	criteria.setAccuracy(Criteria.ACCURACY_FINE);
//	// 是否查询海拨：否
//	criteria.setAltitudeRequired(false);
//	// 是否查询方位角 : 否
//	criteria.setBearingRequired(false);
//	// 是否允许付费：是
//	criteria.setCostAllowed(true);
//	// 电量要求：低
//	criteria.setPowerRequirement(Criteria.POWER_LOW);
//	// 返回最合适的符合条件的 provider ，第 2 个参数为 true 说明 , 如果只有一个 provider 是有效的 , 则返回当前
//	// provider
//	provider = locationManager.getBestProvider(criteria, true);
//}
//
//// Gps 消息监听器
//private final LocationListener locationListener = new LocationListener() {
//	// 位置发生改变后调用
//	public void onLocationChanged(Location location) {
//		Log.i(TAG, "onLocationChanged:" + location.getLatitude());
//		updateWithNewLocation(location);
//	}
//
//	// provider 被用户关闭后调用
//	public void onProviderDisabled(String provider) {
//		Log.i(TAG, "onProviderDisabled");
//		updateWithNewLocation(null);
//	}
//
//	// provider 被用户开启后调用
//	public void onProviderEnabled(String provider) {
//	}
//	// provider 状态变化时调用
//	public void onStatusChanged(String provider, int status,Bundle extras) {
//	}
//
//};
//
//// Gps 监听器调用，处理位置信息
//private void updateWithNewLocation(Location location) {
//	String latLongString;
//	if (location != null) {
//		double lat = location.getLatitude();
//		double lng = location.getLongitude();
//		latLongString = " 纬度 :" + lat + ", 经度 :" + lng;
//	} else {
//		latLongString = " 获取地理信息中 ";
//	}
//	
//	if(getAddressbyGeoPoint(location) != null){
//		mTextViewLoca.setText(latLongString + "," + 
//				getAddressbyGeoPoint(location).get(0).getLocality());
//	}else{
//		mTextViewLoca.setText(latLongString + "," + "城市定位不了");
//	}
//	
//
//}
//
//// 获取地址信息
//private List<Address> getAddressbyGeoPoint(Location location) {
//	List<Address> result = null;
//	// 先将 Location 转换为 GeoPoint
//	// GeoPoint gp =getGeoByLocation(location);
//	try {
//		if (location != null) {
//			// 获取 Geocoder ，通过 Geocoder 就可以拿到地址信息
//			Geocoder gc = new Geocoder(this, Locale.getDefault());
//			result = gc.getFromLocation(location.getLatitude(),
//					location.getLongitude(), 1);
//		}
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
//	return result;
//
//}

















