package lbs.goodplace.com.View.main;

import lbs.goodplace.com.manage.CrashHandler;
import android.app.Application;
import android.content.Context;
import android.os.RemoteException;

import com.drcom.drpalm.Tool.PushManager;
import com.drcom.drpalm.Tool.service.ConnectPushCallback;


public class GoodPlaceApp extends Application {

	private static GoodPlaceApp sApp = null;
	private static Context sContext = null;
	public static boolean mIsRuning = false;	//是否从ManiActivity进入启动应用的
	
	public static int MyLOCATION_CHANGE = 1;
	
	public static long GET_REGISTRCODE_TIME = 0;	//注册取验证码时间
	
	public static String GCRASHFILEPATH = "/sdcard/GoodPlaceError/";	//Crash路径
	
	//定位
//	public LocationClient mLocationClient = null;
//	public MyLocationListenner myListener = new MyLocationListenner();
//	public NotifyLister mNotifyer=null;
//	public Vibrator mVibrator01;
//	public static String TAG = "zjj";
//	private BDLocation mLocation;
//	public Handler mHandlerLoca;
//	//地图
//	public boolean m_bKeyRight = true;
//	public BMapManager mBMapManager = null;
//	//地图:"请输入您的key";
//    public static final String strKey = "554808CC32D57EBB950CB2C5855A829D56265AFF";
    
	
	@Override
	public void onCreate() {
//		mLocationClient = new LocationClient( this );
//		mLocationClient.registerLocationListener( myListener );
		
		super.onCreate();
		initStaticApp(this);
		initStaticContext(getApplicationContext());
		
//		initEngineManager(this);
		
//		Log.d(TAG, "... Application onCreate... pid=" + Process.myPid());
		
		
	    //push相关
		PushManager.init(this,new ConnectPushCallback() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				try {
					PushManager.Register("zhf123", "WZgNkq2sgJ1001qQUWJoNYUtYo6AvinL");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onError(String err) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// 注册crashHandler
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(GoodPlaceApp.getContext());
	}

	private static void initStaticApp(GoodPlaceApp app) {
		sApp = app;
	}

	private static void initStaticContext(Context context) {
		sContext = context;
	}
	
	public static GoodPlaceApp getApp() {
		return sApp;
	}
	
	public static Context getContext() {
		return sContext;
	}
	
//	/**
//	 * 取定位数据
//	 */
//	public BDLocation getLocation(){
//		return mLocation;
//	}
//	
//	/**
//	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
//	 */
//	public class MyLocationListenner implements BDLocationListener {
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			if (location == null)
//				return ;
//			
//			mLocation = location;
//			
//			if(mHandlerLoca != null){
//				Message msg = new Message();
//				msg.arg1 = MyLOCATION_CHANGE;
//				mHandlerLoca.sendMessage(msg);
//			}
//			
//		}
//		
//		public void onReceivePoi(BDLocation poiLocation) {
//			if (poiLocation == null){
//				return ; 
//			}
//			
//			mLocation = poiLocation;
//		}
//	}
//	
//	public class NotifyLister extends BDNotifyListener{
//		public void onNotify(BDLocation mlocation, float distance){
//			mVibrator01.vibrate(1000);
//		}
//	}
//	
//	
//	@Override
//	//建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
//	public void onTerminate() {
//		// TODO Auto-generated method stub
//	    if (mBMapManager != null) {
//            mBMapManager.destroy();
//            mBMapManager = null;
//        }
//	    PushManager.unbindService();
//		super.onTerminate();
//	}
//	
//	public void initEngineManager(Context context) {
//        if (mBMapManager == null) {
//            mBMapManager = new BMapManager(context);
//        }
//
//        if (!mBMapManager.init(strKey,new MyGeneralListener())) {
//            Toast.makeText(GoodPlaceApp.getApp().getApplicationContext(), 
//                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
//        }
//	}
//	
//	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
//    static class MyGeneralListener implements MKGeneralListener {
//        
//        @Override
//        public void onGetNetworkState(int iError) {
//            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
//                Toast.makeText(GoodPlaceApp.getApp().getApplicationContext(), "您的网络出错啦！",
//                    Toast.LENGTH_LONG).show();
//            }
//            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
//                Toast.makeText(GoodPlaceApp.getApp().getApplicationContext(), "输入正确的检索条件！",
//                        Toast.LENGTH_LONG).show();
//            }
//            // ...
//        }
//
//        @Override
//        public void onGetPermissionState(int iError) {
//            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
//                //授权Key错误：
//                Toast.makeText(GoodPlaceApp.getApp().getApplicationContext(), 
//                        "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
//                GoodPlaceApp.getApp().m_bKeyRight = false;
//            }
//        }
//    }
}
