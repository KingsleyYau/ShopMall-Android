package lbs.goodplace.com.manage.GPS;

import lbs.goodplace.com.View.main.GoodPlaceApp;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;



/**
 * 高德定位
 * @author zhaojunjie
 *
 */
public class GPSLocation implements LocationSource, AMapLocationListener {
	private Context mContext;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
//	public Handler mHandlerLocachange;
	
	public GPSLocation(Context context){
		mContext = context;
		mAMapLocationManager = LocationManagerProxy.getInstance(context);
	}
	
	@Override
	public void onLocationChanged(Location location) {
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
	public void onLocationChanged(AMapLocation location) {
		// TODO Auto-generated method stub
		if (mListener != null) {
			mListener.onLocationChanged(location);
			Log.i("zjj", "定位类中:lat:" + location.getLatitude() + ",lng:" + location.getLongitude() );
			
//			if(mHandlerLocachange != null){
////				Location bdl = new AMapLocation("")
////				bdl.setLatitude(lat);
////				bdl.setLongitude(lng);
//				
//				Message msg = new Message();
//				msg.arg1 = GoodPlaceApp.MyLOCATION_CHANGE;
//				msg.obj = location;
//				mHandlerLocachange.sendMessage(msg);
//			}
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(mContext);
		}
		/*
		 * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true
		 */
		// Location API定位采用GPS和网络混合定位方式，时间最短是5000毫秒
		mAMapLocationManager.setGpsEnable(true);
		mAMapLocationManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 5000, 10, this);

	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}
	
//	public static Location getLocatonByGPS(final Context context){
//		LocationManager locationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
//        Location location = getLocationProvider(locationManager);
//        Log.i("TAG", "provider---location:" + location);
//        if(location==null){
//        	location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            Log.i("TAG", "GPS_PROVIDER---location:" + location);
//        }         
//        if(location==null){
//	        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//	        Log.i("TAG", "NETWORK_PROVIDER---location:" + location);
//        }
//		return location;
//	}
//	
//    
//	public static Location getLocationProvider(LocationManager lm){
//		Location reLocation =null;
//		try {
//			//设置服务商的信息
//			Criteria criteria=new Criteria();
//			criteria.setAccuracy(Criteria.ACCURACY_FINE);
//			criteria.setAltitudeRequired(false);
//			criteria.setBearingRequired(false);
//			criteria.setCostAllowed(true);
//			criteria.setPowerRequirement(Criteria.POWER_LOW);
//			Log.i("cndtacom status","begin");
//			//取得效果最好的criteria
//			String provider=lm.getBestProvider(criteria,true);
//			Log.i("provider", provider);
//			reLocation=lm.getLastKnownLocation(provider);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return reLocation;
//	}
	
	
	//07-23
//	private Context mContext;
//	private LocationManager locationManager;
//	private String provider;
//	private Location location;
//	public Handler mHandlerLocachange;
//	
//	public GPSLocation(Context context){
//		mContext = context;
//	}
//	
//	public void getLocation(){
//		// 获取 LocationManager 服务
//		locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
//		// 获取 Location Provider
//		getProvider();
//		// 如果未设置位置源，打开 GPS 设置界面
//		openGPS();
//		
//		if(provider == null){
//			return;
//		}
//		// 获取位置
//		location = locationManager.getLastKnownLocation(provider);
//		// 显示位置信息到文字标签
//		updateWithNewLocation(location);
//		// 注册监听器 locationListener ，第 2 、 3 个参数可以控制接收 gps 消息的频度以节省电力。第 2 个参数为毫秒，
//		// 表示调用 listener 的周期，第 3 个参数为米 , 表示位置移动指定距离后就调用 listener
////		locationManager.requestLocationUpdates(provider, 2000, 10,locationListener);
//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10,locationListener);
//	}
//	
//	/**
//	 * 解除定位
//	 */
//	public void cancelLocation(){
//		locationManager.removeUpdates(locationListener);
//	}
//
//	// 判断是否开启 GPS ，若未开启，打开 GPS 设置界面
//	private void openGPS() {
//		if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
//				|| locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
//		) {
////			Toast.makeText(mContext, " 位置源已设置！ ", Toast.LENGTH_SHORT).show();
//			Log.i("zjj", "位置源已设置！");
//			return;
//		}
//	
//		Toast.makeText(mContext, " 位置源未设置！ ", Toast.LENGTH_SHORT).show();
//		// 转至 GPS 设置界面
//		Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
//		((Activity)mContext).startActivityForResult(intent, 0);
//	}
//
//	// 获取 Location Provider
//	private void getProvider() {
//		// 构建位置查询条件
//		Criteria criteria = new Criteria();
//		// 查询精度：高
//		criteria.setAccuracy(Criteria.ACCURACY_FINE);
//		// 是否查询海拨：否
//		criteria.setAltitudeRequired(false);
//		// 是否查询方位角 : 否
//		criteria.setBearingRequired(false);
//		// 是否允许付费：是
//		criteria.setCostAllowed(true);
//		// 电量要求：低
//		criteria.setPowerRequirement(Criteria.POWER_LOW);
//		// 返回最合适的符合条件的 provider ，第 2 个参数为 true 说明 , 如果只有一个 provider 是有效的 , 则返回当前
//		// provider
//		provider = locationManager.getBestProvider(criteria, true);
//	}
//	
//	// Gps 消息监听器
//	private final LocationListener locationListener = new LocationListener() {
//		// 位置发生改变后调用
//		public void onLocationChanged(Location location) {
//			Log.i("zjj", "onLocationChanged:" + location.getLatitude());
//			updateWithNewLocation(location);
//		}
//	
//		// provider 被用户关闭后调用
//		public void onProviderDisabled(String provider) {
//			Log.i("zjj", "onProviderDisabled");
//			updateWithNewLocation(null);
//		}
//	
//		// provider 被用户开启后调用
//		public void onProviderEnabled(String provider) {
//		}
//		// provider 状态变化时调用
//		public void onStatusChanged(String provider, int status,Bundle extras) {
//		}
//	
//	};
//	
//	// Gps 监听器调用，处理位置信息
//	private void updateWithNewLocation(Location location) {
//		String latLongString;
//		if (location != null) {
//			double lat = location.getLatitude();
//			double lng = location.getLongitude();
//			latLongString = " 纬度 :" + lat + ", 经度 :" + lng;
//			
//			cancelLocation();
//			
//			if(mHandlerLocachange != null){
//				BDLocation bdl = new BDLocation();
//				bdl.setLatitude(lat);
//				bdl.setLongitude(lng);
//				
//				Message msg = new Message();
//				msg.arg1 = GoodPlaceApp.MyLOCATION_CHANGE;
//				msg.obj = bdl;
//				mHandlerLocachange.sendMessage(msg);
//			}
//			
//		} else {
//			latLongString = " 获取地理信息中 ";
//		}
//		
//		Log.i("zjj", "GPS定义信息:" + latLongString);
//		
//	//	if(getAddressbyGeoPoint(location) != null){
//	//		mTextViewLoca.setText(latLongString + "," + 
//	//				getAddressbyGeoPoint(location).get(0).getLocality());
//	//	}else{
//	//		mTextViewLoca.setText(latLongString + "," + "城市定位不了");
//	//	}
//		
//	
//	}
//	
//	// 获取地址信息
//	private List<Address> getAddressbyGeoPoint(Location location) {
//		List<Address> result = null;
//		// 先将 Location 转换为 GeoPoint
//		// GeoPoint gp =getGeoByLocation(location);
//		try {
//			if (location != null) {
//				// 获取 Geocoder ，通过 Geocoder 就可以拿到地址信息
//				Geocoder gc = new Geocoder(mContext, Locale.getDefault());
//				result = gc.getFromLocation(location.getLatitude(),
//						location.getLongitude(), 1);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	
//	}
	
}
