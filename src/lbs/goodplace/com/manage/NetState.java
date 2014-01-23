package lbs.goodplace.com.manage;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络状态检测类
 * @author Administrator
 *
 */
public class NetState {
	private Context context;
	public NetState(Context c){
		context = c;
	}
	
	/**
	 * 当前网络是否可用
	 * @param context
	 * @return
	 */
	public boolean isNetUsing(){
		if(isNetworkConnected() && (isWifiConnected()||isMobileConnected())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断是否有网络连接 
	 * @return
	 */
	private boolean isNetworkConnected( ) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * 判断WIFI网络是否可用
	 * @return
	 */
	private boolean isWifiConnected( ) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * 判断MOBILE网络是否可用
	 * @return
	 */
	private boolean isMobileConnected( ) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}
}
