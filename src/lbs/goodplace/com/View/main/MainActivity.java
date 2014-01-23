package lbs.goodplace.com.View.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.contant.ImageUntil;
import lbs.goodplace.com.View.msg.MsgDetailActivity;
import lbs.goodplace.com.View.register.RegistePhoneActivity;
import lbs.goodplace.com.View.shops.SearchShopsActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.controls.MySrcollTabhost;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.GPS.CoordinateManage;
import lbs.goodplace.com.manage.GPS.GPSLocation;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager;
import lbs.goodplace.com.manage.imagemanage.LruImageCache;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.uploadpicmanage.FileUploader;
import lbs.goodplace.com.manage.uploadpicmanage.FormFile;
import lbs.goodplace.com.manage.util.ErrorCodeUtils;
import lbs.goodplace.com.manage.util.GlobalUtil;
import lbs.goodplace.com.manage.util.LoginUtils;
import lbs.goodplace.com.manage.util.MyMothod;
import lbs.goodplace.com.obj.GatewayListModule;
import lbs.goodplace.com.obj.GatewayModule;
import lbs.goodplace.com.obj.LoginModule;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.SessionModule;
import lbs.goodplace.com.obj.parser.GatwayParser;
import lbs.goodplace.com.obj.parser.LoginParser;
import lbs.goodplace.com.obj.parser.ResultParser;
import lbs.goodplace.com.obj.parser.SessionParser;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.drcom.drpalm.objs.DeviceUuidFactory;

public class MainActivity extends ModuleActivity {
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;
	public static String KEY_PUSH2SHOP_ID = "KEY_PUSH2SHOP_ID";
	public static String KEY_PUSH2INFO_ID = "KEY_PUSH2INFO_ID";
	
	private Context mContext;
	private String mUsername;
	private String mPassword;
	private GroupReceiver mGroupReceiver;
	private boolean mIsPushStart2Shopinfo = false;	//是否从PUSH启动到商店详细
	private boolean mIsPushStart2Msginfo = false;	//是否从PUSH启动到资讯详细
	private int mShopId = -1;
	private String mInfoId = "";
	//缓存
	private String CACHE_NAME_USERINFO = "MainActivitygetuserinfo";
	private NetState mNetState;
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	
	//定位
//	private Vibrator mVibrator01 =null;
//	private LocationClient mLocClient;
	private GPSLocation mGPSLocation;

	// 控件
	private MySrcollTabhost srcolltabhost;
	private Button mCityButton;
	private MainFindView mView1;
	private NewsView View2;
	private UserinfoView mView3;
	private MoreView mView4;	
	private Button mButtonLoginout;	//注销按钮
	private Button mButtonRegister;	//登陆按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = MainActivity.this;
		GoodPlaceApp.mIsRuning = true;
		
		Bundle extras = getIntent().getExtras();
        if(extras != null){
        	if(extras.containsKey(KEY_PUSH2SHOP_ID)){
        		mShopId = extras.getInt(KEY_PUSH2SHOP_ID,-1);
        		mIsPushStart2Shopinfo = true;
    		}
        	if(extras.containsKey(KEY_PUSH2INFO_ID)){
        		mInfoId = extras.getString(KEY_PUSH2INFO_ID);
        		mIsPushStart2Msginfo = true;
    		}
        }
		
		// 初始化图片管理器
		final int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// 使用系统分配的内存的1/8做图片强引用的的缓存
		final int cacheSize = 1024 * 1024 * memClass / 8; // 使用
		AsyncImageManager.buildInstance(new LruImageCache(cacheSize));

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.main, mLayout_body);

		View but1 = initView(R.drawable.bottom_btn_selector, R.drawable.foot_btn1, R.string.bottom_btn_good_find, "0");
		View but2 = initView(R.drawable.bottom_btn_selector, R.drawable.foot_btn2, R.string.bottom_btn_good_msg, "0");
		View but3 = initView(R.drawable.bottom_btn_selector, R.drawable.foot_btn3, R.string.bottom_btn_me_has, "0");
		View but4 = initView(R.drawable.bottom_btn_selector, R.drawable.foot_btn4, R.string.bottom_btn_more, "0");

		mView1 = new MainFindView(mContext);
		View2 = new NewsView(mContext);
		mView3 = new UserinfoView(mContext);
		mView4 = new MoreView(mContext);

		srcolltabhost = (MySrcollTabhost) findViewById(R.id.mySrcollTabhost);
		srcolltabhost.addView(mView1, but1);
		srcolltabhost.addView(View2, but2);
		srcolltabhost.addView(mView3, but3);
		srcolltabhost.addView(mView4, but4);

		srcolltabhost.AddOnSelectTabListener(0,new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳转
				srcolltabhost.SetCurrentTab(0);
				//事件
				SetSearchBarVisibility(true);
				mButtonLoginout.setVisibility(View.GONE);
				mButtonRegister.setVisibility(View.GONE);
			}
		});
		srcolltabhost.AddOnSelectTabListener(1,new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳转
				srcolltabhost.SetCurrentTab(1);
				//事件
				SetSearchBarVisibility(false);
				setTitleTxt(getResources().getString(R.string.bottom_btn_good_msg));
				View2.ReflashList();
				mButtonLoginout.setVisibility(View.GONE);
				mButtonRegister.setVisibility(View.GONE);
			}
		});
		srcolltabhost.AddOnSelectTabListener(2,new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ShowLogin();
			}
		});
		srcolltabhost.AddOnSelectTabListener(3,new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳转
				srcolltabhost.SetCurrentTab(03);
				//事件
				SetSearchBarVisibility(false);
				setTitleTxt(getResources().getString(R.string.bottom_btn_more));
				mButtonLoginout.setVisibility(View.GONE);
				mButtonRegister.setVisibility(View.GONE);
			}
		});
		
		initTitilebar();
		SetSearchBarVisibility(true);
		
		initReceiver();
		getDevictID();
		
		ShowLoadingDialog();
		getLocation();
		
		mNetState = new NetState(mContext);
		if(!mNetState.isNetUsing()){
			//5. 取第一分页的数据
			mView1.getCategorytype();
			//6.取上次登录的信息
			getUserinfo();
			//
			HideLoadingDialog();
		}
		
		
//		getWG();
		
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		//删除收藏收,刷新数字
		mView3.ReflasUI();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mGroupReceiver);
		GoodPlaceApp.mIsRuning = false;
		super.onDestroy();
	}

	public View initView(int backgroundId, int imageId, int nameId, String newNum) {
		View view = View.inflate(this, R.layout.tabview, null);
		view.setBackgroundResource(backgroundId);

		ImageView iconImageView = (ImageView) view.findViewById(R.id.tabview_imageView);
		iconImageView.setBackgroundResource(imageId);

		TextView nameTextView = (TextView) view.findViewById(R.id.tabview_textView);
		nameTextView.setText(nameId);

		TextView newNumTextView = (TextView) view.findViewById(R.id.tabview_number_txtv);
		if(newNum.equals("0") || newNum.equals("")){
			newNumTextView.setVisibility(View.GONE);
		}else{
			newNumTextView.setText(newNum);
		}
		
		return view;
	}

	private void initTitilebar(){
//		mCityButton = new Button(this);
//		mCityButton.setText("city");
//		mCityButton.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent i = new Intent();
//				i.setClass(MainActivity.this, CityListActivity.class);
//				startActivity(i);
//			}
//		});
//		setTitleLeftButton(mCityButton);
		
		LayoutParams p = new LayoutParams(MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size) , MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size));
		
		mButtonLoginout = new Button(this);
		mButtonLoginout.setLayoutParams(p);
		mButtonLoginout.setBackgroundResource(R.drawable.button_logout_selector);
		mButtonLoginout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginUtils.Logout(mContext, mLogouthandler);
			}
		});
		setTitleRightButton(mButtonLoginout);
		
		
		mButtonRegister = new Button(this);
//		mButtonRegister.setLayoutParams(p);
		mButtonRegister.setText(R.string.register);
		mButtonRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, RegistePhoneActivity.class);
				startActivity(i);
			}
		});
		setTitleRightButton(mButtonRegister);
		
		SetSearchListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, SearchShopsActivity.class);
				i.putExtra(SearchShopsActivity.KEY_KEYWORD, getSearchKeyword());
				i.putExtra(SearchShopsActivity.KEY_ISSHOW_REGIONS, false);
				startActivity(i);
			}
		});
		
		SetBackBtnOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//提示框退出
				showLogoutMessage(getResources().getString(R.string.exit_sure));
			}
		});
		
		hideBackButton();
	}
	
	/**
	 * 取设备唯一标识
	 */
	private void getDevictID(){
		DeviceUuidFactory duf = new DeviceUuidFactory(mContext);
		
//		Log.d("zjj", "*indetify:" + duf.getDeviceMac() + ",*packagename:" + this.getPackageName());
		
		GoodPlaceContants.DEVICETOKEN = duf.getTokenid(duf.getDeviceMac(), this.getPackageName());
		
		Log.i("zjj", "唯一标识:" + GoodPlaceContants.DEVICETOKEN);
	}
	
	/**
	 * 1.定位
	 */
	private void getLocation(){
		//百度定位
//		mLocClient = ((GoodPlaceApp)getApplication()).mLocationClient;
//		mVibrator01 =(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
//		((GoodPlaceApp)getApplication()).mVibrator01 = mVibrator01;
//		((GoodPlaceApp)getApplication()).mHandlerLoca = mHandlerLocachange;
//		setLocationOption();
//		mLocClient.start();
		
//		Location l = GPSLocation.getLocatonByGPS(mContext);
//		Log.i("zjj", "1.定位  lat:" + l.getLatitude() + ",lng:" + l.getLongitude());
		
		//Google定位
//		GPSLocation gpsl = new GPSLocation(mContext);
//		gpsl.getLocation();
//		gpsl.mHandlerLocachange = mHandlerLocachange;
		
		mGPSLocation = new GPSLocation(mContext);
		mGPSLocation.activate(new OnLocationChangedListener() {
			
			@Override
			public void onLocationChanged(Location l) {
				// TODO Auto-generated method stub
//				AMapLocation aml = (AMapLocation)l;
				GoodPlaceContants.HXLAT = l.getLatitude();
				GoodPlaceContants.HXLNG = l.getLongitude();
				
				GoodPlaceContants.LAT = CoordinateManage.getLatHX2GG(l.getLatitude()) ;
				GoodPlaceContants.LNG = CoordinateManage.getLngHX2GG(l.getLongitude()) ;
				
				//在家测试
//				GoodPlaceContants.LAT = 23.14299d;
//				GoodPlaceContants.LNG = 113.3267d;
				
				mGPSLocation.deactivate();
				Log.i("zjj", "getLocation 定位成功 lat:" + GoodPlaceContants.LAT + ",lng:" + GoodPlaceContants.LNG);
				
				getWG();
			}
		});
	}
	
	/**
	 * 
	 */
//	private Handler mHandlerLocachange = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			if(msg.arg1 == GoodPlaceApp.MyLOCATION_CHANGE){
//				//百度定位
//				BDLocation l = ((GoodPlaceApp)getApplication()).getLocation();
////				GoodPlaceContants.LAT = CoordinateManage.getLatBD2HX(l.getLatitude(),l.getLongitude());
////				GoodPlaceContants.LNG = CoordinateManage.getLogBD2HX(l.getLatitude(),l.getLongitude());
//				
//				GoodPlaceContants.LAT = CoordinateManage.getLatBD2GG(l.getLatitude());
//				GoodPlaceContants.LNG = CoordinateManage.getLngBD2GG(l.getLongitude());
//				
////				GoodPlaceContants.LAT =  l.getLatitude();
////				GoodPlaceContants.LNG = l.getLongitude();
//				
//				mLocClient.stop();
//				//*******************
//				
//				//Google定位
////				BDLocation l = (BDLocation)msg.obj;
////				GoodPlaceContants.LAT = l.getLatitude();
////				GoodPlaceContants.LNG = l.getLongitude();
//				//*******************
//				
//				Log.i("zjj", "getLocation 请求成功 lat:" + GoodPlaceContants.LAT + ",lng:" + GoodPlaceContants.LNG);
//				getWG();
//			}
//		}
//	};
	
	//设置相关参数
//	private void setLocationOption(){/*
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(false); // 打开gps
//		option.setCoorType("bd90ll"); //("wgs84");// // 设置坐标类型
//		option.setServiceName("com.baidu.location.service_v2.9");
//		option.setPoiExtraInfo(false);
//		option.setAddrType("all");
//
//		option.setScanSpan(3000);
//
//		option.setPriority(LocationClientOption.GpsFirst); // 不设置，默认是gps优先
//
//		option.setPoiNumber(3);
//		option.disableCache(true);
//		mLocClient.setLocOption(option);
//	}*/
	
	/**
	 * 2.取网关
	 */
	private void getWG(){
		byte[] postData = JsonRequestManage.getWG();
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_APPWG, postData, new GatwayParser(), false, "getwg",
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mHandler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
							Log.i("zjj", "getWG 请求成功");
						} else {
							mHandler.sendEmptyMessage(LOAD_SUCCESS);
							Log.i("zjj", "getWG 请求失败");
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
						GatewayListModule gatewayListModule = (GatewayListModule)msg.obj;
						GatewayModule gwModule = gatewayListModule.gatewayList.get(0);
						
						GoodPlaceContants.setWGIP(gwModule.getIp() + ":" + gwModule.getPort(), gatewayListModule.mLbsappid);
						
						getSession();
						Log.i("zjj", "SHOP_RANKING_TYEP URL:" + GoodPlaceContants.URL_SHOP_RANKING_TYEP);
					}
					break;
					
				default :
					Log.i("zjj", "fales");
					break;
			}
		}

	};
	
	/**
	 * 3.签到
	 */
	private void getSession(){
		byte[] postData = JsonRequestManage.getSession();
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_SEESION, postData, new SessionParser(),// true, "getsession",
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mSessionHandler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
						} else {
							mSessionHandler.sendEmptyMessage(LOAD_SUCCESS);
						}
					}
			
				});
	}
	
	private Handler mSessionHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			HideLoadingDialog();
			
			switch (msg.what) {
				case LOAD_SUCCESS :
					if(msg.obj!=null){
						SessionModule sm = (SessionModule)msg.obj;
						
						GoodPlaceContants.SESSION_KEY = sm.getSessionkey();
						GoodPlaceContants.CITY_ID = sm.getCity().getId() + "";
						GoodPlaceContants.SESSEIONMOUDLE = sm;
						
						mView1.SetCityBtnText(sm.getCity().getName());
						
						AutoLogin();
						Log.i("zjj", "getSession 请求成功" + sm.getSessionkey());
					}
					break;
					
				default :
					Log.i("zjj", "getSession 请求失败");
					break;
			}
		}

	};
	
	/**
	 * 取用户信息
	 */
	private void getUserinfo(){
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = JsonRequestManage.getUserinfo();
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_USERINFO, postData, new LoginParser(), mIsNeddCache, CACHE_NAME_USERINFO,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mUserinfoHandler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
						} else {
							mUserinfoHandler.sendEmptyMessage(LOAD_SUCCESS);
						}
					}
			
				});
	}
	
	private Handler mUserinfoHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			HideLoadingDialog();
			
			switch (msg.what) {
				case LOAD_SUCCESS :
					if(msg.obj!=null){
						LoginModule loginmodule = (LoginModule)msg.obj;
						if(loginmodule.mRequestResult.getResult()){
							GoodPlaceContants.USERINFO = loginmodule.mUserInfo;
							
							mView3.ReflasUI();
							Log.i("zjj", "getUserinfo 请求成功");
						}else{
							Toast.makeText(mContext, ErrorCodeUtils.getErrorStr(mContext, loginmodule.mRequestResult.getErrorcode()), Toast.LENGTH_LONG).show();
						}
					}
					break;
					
				default :
					Log.i("zjj", "getUserinfo 请求失败");
					break;
			}
		}

	};
	
	/**
	 * 4.自动登录
	 */
	private void AutoLogin(){
		SharedPreferences userInfo = getSharedPreferences(GlobalUtil.SHAREDPERFERENCES_NAME_LOGIN, 0);  
		mUsername = userInfo.getString(GlobalUtil.SHAREDPERFERENCES_KEY_USERNAME, "");  
        mPassword = userInfo.getString(GlobalUtil.SHAREDPERFERENCES_KEY_PW, "");  
        
        if(!mUsername.equals("") && !mPassword.equals("")){
        	LoginUtils.Login(mContext,
            		mUsername,
            		mPassword,
    				null
    				);
        }else{
        	HideLoadingDialog();
        	
			//5. 取第一分页的数据
			mView1.getCategorytype();
			
			//6. 从PUSH进入详细窗体
			if(mIsPushStart2Shopinfo){
				ShowLogin();
			}else if(mIsPushStart2Msginfo){
				ShowLogin();
			}
        }
	}
	
	/**
	 * 打开商店详细；
	 */
	private void ShowShopinfo(){
		Log.i("zjj", "---*-----打开商店详细--------------");
		
		Intent i = new Intent(mContext, ShopInfoActivity.class);
		i.putExtra(GoodPlaceContants.KEY_SHOP_ID, this.mShopId);
		mContext.startActivity(i);
	}
	
	/**
	 * 打开资讯详细；
	 */
	private void ShowMsgDetail(){
		Log.i("zjj", "---*-----打开资讯详细--------------");
		
		Intent i = new Intent(mContext, MsgDetailActivity.class);
		i.putExtra(MsgDetailActivity.KEY_INFOID, this.mInfoId);
		mContext.startActivity(i);
	}
	
	/**
	 * 打开登录页面
	 */
	private void ShowLogin(){
		//跳转
		srcolltabhost.SetCurrentTab(2);
		//事件
		SetSearchBarVisibility(false);
		setTitleTxt(getResources().getString(R.string.bottom_btn_me_has));
		if(GoodPlaceContants.USERINFO != null){
			mButtonLoginout.setVisibility(View.VISIBLE);
			mButtonRegister.setVisibility(View.GONE);
			getUserinfo();
		}else {
			mButtonLoginout.setVisibility(View.GONE);
			mButtonRegister.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 注销事件
	 */
	private Handler mLogouthandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			mView3.ClearUI();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			// 拍照
			if (requestCode == UserinfoView.REQUEST_CODE_CAMERA) {
				ImageUntil.compressImageSamll(Contants.Path.CAMERA_SAVE_USER_PATH,
						Contants.Path.COMPRESS_SAVE_USER_PATH);
				mView3.SetUserImg();
				UploadUserimg();
			}// 相冊
			else if (requestCode == UserinfoView.REQUEST_CODE_ALBUM) {
				String datastr = data.getData().toString();
				if (datastr.startsWith("content:")) {
					try {
						Uri originalUri = data.getData();// 得到图片的URI
						String[] imgs = { MediaStore.Images.Media.DATA };// 将图片URI转换成存储路径
						Cursor cursor = this.managedQuery(originalUri, imgs, null,null, null);
						int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						String filePath = cursor.getString(index);
						try
			            {
							// 4.0以上的版本会自动关闭 (3.0.1--11;  4.0--14; 4.0.3--15)
							if (Integer.parseInt(Build.VERSION.SDK) < 11) {
			                    cursor.close();
			                }
			            }catch(Exception e){}
						ImageUntil.compressImage(filePath,Contants.Path.COMPRESS_SAVE_USER_PATH);
						mView3.SetUserImg();
						UploadUserimg();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (datastr.startsWith("file:")) {
					String filePath = datastr.substring(datastr.indexOf("file://")+7);
					ImageUntil.compressImage(filePath, Contants.Path.COMPRESS_SAVE_PATH);
					mView3.SetUserImg();
					UploadUserimg();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
     * initialize receiver
     */
    private void initReceiver(){
    	mGroupReceiver = new GroupReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalUtil.BROADCAST_KEY_LOGINSUCCESS);
        filter.addAction(GlobalUtil.BROADCAST_KEY_LOGOUTSUCCESS);
        filter.addAction(GlobalUtil.BROADCAST_KEY_CHANGECITYSUCCESS);
        filter.addAction(GlobalUtil.BROADCAST_KEY_CHANGECITYNOTLOCA);
        mContext.registerReceiver(mGroupReceiver,filter);
    }
    
    /**
     * ***************
     * 广播接收
     * ***************
     */
    public class GroupReceiver extends BroadcastReceiver{
    	@Override
    	public void onReceive(Context context, Intent intent){
    		try{
        		String stringAction = intent.getAction();
//        		Bundle extras = intent.getExtras();
        		if(stringAction.equals(GlobalUtil.BROADCAST_KEY_LOGINSUCCESS)){
        			//5. 取第一分页的数据
        			mView1.getCategorytype();
        			mView3.ReflasUI();
        			//6. 从PUSH进入详细窗体
        			if(mIsPushStart2Shopinfo){
        				ShowShopinfo();
        			}else if(mIsPushStart2Msginfo){
        				ShowMsgDetail();
        			}
        			
    				mButtonLoginout.setVisibility(View.VISIBLE);
    				mButtonRegister.setVisibility(View.GONE);
        			
        		}else if(stringAction.equals(GlobalUtil.BROADCAST_KEY_LOGOUTSUCCESS)){
        			mButtonLoginout.setVisibility(View.GONE);
    				mButtonRegister.setVisibility(View.VISIBLE);
        		}else if(stringAction.equals(GlobalUtil.BROADCAST_KEY_CHANGECITYSUCCESS)){
        			mView1.SetCityBtnText(GoodPlaceContants.SESSEIONMOUDLE.getCity().getName());
        			
        			getLocation();
        			mView1.getCategorytype();
        			View2.ReflashAllList();
        		}else if(stringAction.equals(GlobalUtil.BROADCAST_KEY_CHANGECITYNOTLOCA)){
        			mView1.SetCityBtnText(GoodPlaceContants.SESSEIONMOUDLE.getCity().getName());
        			
        			String msg = getString(R.string.notlocacity);
        			msg = String.format(msg, GoodPlaceContants.SESSEIONMOUDLE.getCity().getName(),GoodPlaceContants.LOCACITYNAME);
        			showChangeCityMessage(msg);   
        			
        			mView1.getCategorytype();
        			View2.ReflashAllList();
        		}
        			
        	}
    		catch (Exception e) {
				// TODO: handle exception
    				e.printStackTrace();
			}
        }
	}
	
	/**
	 * 上传用户头像
	 */
	private void UploadUserimg(){		
		//读取文件
		String imagePathString = Contants.Path.COMPRESS_SAVE_USER_PATH;
		File file = new File(imagePathString);
		
		List formFiles = new ArrayList<FormFile>();	//<--存放要上传的文件(可多个)
		
		FileInputStream fileStream = null;
		try {
			fileStream = new FileInputStream(file);
		} 
		catch (FileNotFoundException ex) {
			//MessageHelper.Alert(EvidenceActivity.this,"读取图片文件失败，请重试！");
		}
		FormFile formFile = new FormFile(fileStream,file.getName(),"ufile","image/pjpeg");
		formFiles.add(formFile);
		
		//请求参数
		Map<String, Object> postData = JsonRequestManage.getUploadmemberimg(
				GoodPlaceContants.USERINFO.getId()
				);
		
		//上传
		FileUploader fu = new FileUploader();
		fu.UploadPic(GoodPlaceContants.URL_UPLOADUSERIMG,
				formFiles,
				postData,
				mContext,
				new ResultParser(),
				new IDataListener(){
					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
//												Message msg = new Message();
//												msg.obj = object;
//												msg.what = LOAD_SUCCESS;
//												mSessionHandler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
							RequestResultModule rrm = (RequestResultModule)object;
							Log.i("zjj", "上传头像结果:" + rrm.getResult());
							
							//重新登录下载新头像
							System.gc();
							AutoLogin();
						} else {
//												mSessionHandler.sendEmptyMessage(LOAD_SUCCESS);
							Log.i("zjj", "上传头像结果:null");
						}
					}
			
				});
	}
	
	//---------------------注销-----------------
	/**
	 * 退出提示框(系统)
	 * @param pMsg
	 */
	private void showLogoutMessage(String pMsg) {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage(pMsg);
		builder.setPositiveButton(R.string.OK,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});

		builder.setNegativeButton(R.string.Cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();
	}
	
	//--------------------切换城市-----------------
	/**
	 * 切换城市但非所在城市 
	 */
	private void showChangeCityMessage(String pMsg) {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage(pMsg);
		builder.setPositiveButton(R.string.OK,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

//		builder.setNegativeButton(R.string.Cancel,
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				});

		builder.create().show();
	}
}
