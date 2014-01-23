package lbs.goodplace.com.manage.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;


/**
 * 
 * <br>类描�?精品用户手机信息获取工具�?
 * <br>功能详细描述:
 * 
 * @author  zhouxuewen
 * @date  [2012-9-12]
 */
public class GoStorePhoneStateUtil {

	public static final int NETTYPE_MOBILE = 0; // 中国移动
	public static final int NETTYPE_UNICOM = 1; // 中国联�?
	public static final int NETTYPE_TELECOM = 2; // 中国电信

	/**
	 * 获取网络类型
	 * @author huyong
	 * @param context
	 * @return 1 for 移动�? for 联�?�? for 电信�?1 for 不能识别
	 */
	public static int getNetWorkType(Context context) {
		int netType = -1;
		// 从系统服务上获取了当前网络的MCC(移动国家�?，进而确定所处的国家和地�?
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String simOperator = manager.getSimOperator();
		if (simOperator != null) {
			if (simOperator.startsWith("46000") || simOperator.startsWith("46002")) {
				//因为移动网络编号46000下的IMSI已经用完�?
				//�?��虚拟了一�?6002编号�?34/159号段使用了此编号 
				//中国移动
				netType = NETTYPE_MOBILE;
			} else if (simOperator.startsWith("46001")) {
				//中国联�?
				netType = NETTYPE_UNICOM;
			} else if (simOperator.startsWith("46003")) {
				//中国电信
				netType = NETTYPE_TELECOM;
			}
		}
		return netType;
	}
	/**
	 * �?��手机WIFI有没有打�?��方法
	 * @param context
	 * @return
	 */
	public static boolean isWifiEnable(Context context) {
		boolean result = false;
		if (context != null) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivityManager != null) {
				NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.isConnected()
						&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					result = true;
				}
			}
		}
		return result;
	}

	//获取本地IP函数
	public static String getLocalIPAddress() {
		try {
			for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
					.getNetworkInterfaces(); mEnumeration.hasMoreElements();) {
				NetworkInterface intf = mEnumeration.nextElement();
				for (Enumeration<InetAddress> enumIPAddr = intf.getInetAddresses(); enumIPAddr
						.hasMoreElements();) {
					InetAddress inetAddress = enumIPAddr.nextElement();
					//如果不是回环地址
					if (!inetAddress.isLoopbackAddress()) {
						//直接返回本地IP地址
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return null;
	}



	/**
	 * 判断SDCard是否可以读写的方�?如果没有SDCard则返回false
	 * 
	 * @return 如果可以读写，则返回true,否则返回false
	 */
	public static boolean isSDCardAccess() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	/**
	 * �?��手机网络是否可用的方�?
	 * @return 可用返回TRUE,否则返回FALSE
	 */
	public static boolean isNetWorkAvailable(Context context) {
		boolean result = false;
		if (context != null) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivityManager != null) {
				NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.isConnected()) {
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * <br>功能�?��:  获取手机号码
	 * <br>功能详细描述:
	 * <br>注意: 不是�?��的手机都可以获取手机号码
	 * @param context
	 * @return
	 */
	public static String getPhoneNumber(Context context) {
		if (context != null) {
			//创建电话管理
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			//获取手机号码
			String phoneNumber = tm.getLine1Number();
			return phoneNumber;
		}
		return "";
	}
}
