package lbs.goodplace.com.manage.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 
 * <br>类描述:用户手机信息获取工具类
 * <br>功能详细描述:
 * 
 */
public class PhoneStateUtil {

	public static final int NETTYPE_MOBILE = 0; // 中国移动
	public static final int NETTYPE_UNICOM = 1; // 中国联通
	public static final int NETTYPE_TELECOM = 2; // 中国电信

	public static boolean isCnUser(Context context) {
		boolean result = false;

		if (context != null) {
			// 从系统服务上获取了当前网络的MCC(移动国家号)，进而确定所处的国家和地区
			TelephonyManager manager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			//SIM卡状态
			boolean simCardUnable = manager.getSimState() != TelephonyManager.SIM_STATE_READY;
			String simOperator = manager.getSimOperator();

			if (simCardUnable || TextUtils.isEmpty(simOperator)) {
				//如果没有SIM卡的话simOperator为null，然后获取本地信息进行判断处理
				// 获取当前国家或地区，如果当前手机设置为简体中文-中国，则使用此方法返回CN
				String curCountry = Locale.getDefault().getCountry();
				if (curCountry != null && curCountry.contains("CN")) {
					//如果获取的国家信息是CN，则返回TRUE
					result = true;
				} else {
					//如果获取不到国家信息，或者国家信息不是CN
					result = false;
				}
			} else if (simOperator.startsWith("460")) {
				//如果有SIM卡，并且获取到simOperator信息。
				/**
				 * 中国大陆的前5位是(46000)
				 * 中国移动：46000、46002
				 * 中国联通：46001
				 * 中国电信：46003
				 */
				result = true;
			}
		}

		return result;
	}

	/**
	 * 采用随机数的形式模拟imei号，避免申请权限。
	 * @param context
	 * @return
	 */
	public static String getVirtualIMEI(Context context) {
		/**
		 * 
		 * <br>类描述:内部类
		 * <br>功能详细描述:
		 * 
		 * @date  [2012-9-12]
		 */
		class Statistics {
			private final static String RANDOM_DEVICE_ID = "random_device_id"; // 存入sharedPreference中的key
			private final static String DEFAULT_RANDOM_DEVICE_ID = "0000000000000000"; // 默认随机IMEI
			private final static String SHAREDPREFERENCES_RANDOM_DEVICE_ID = "randomdeviceid"; // sharedPreference文件名
			/**
			 * @param context
			 * @return
			 */
			public String getVirtualIMEI(Context context) {
				//如果已保存IMEI或者存在旧的虚拟IMEI号则直接使用。
				String imei = getDeviceIdFromSharedpreference(context);
				// 获取手机的IMEI，并保存下来
				if (context != null && null != imei && imei.equals(DEFAULT_RANDOM_DEVICE_ID)) {
					try {
						TelephonyManager telephonyManager = (TelephonyManager) context
								.getSystemService(Context.TELEPHONY_SERVICE);
						imei = telephonyManager.getDeviceId();
						saveDeviceIdToSharedpreference(context, imei);
					} catch (Exception e) {

					}
				}
				return imei;
			}
			
			private void saveDeviceIdToSharedpreference(Context context, String deviceId) {
				PreferencesManager sharedPreferences = new PreferencesManager(context,
						SHAREDPREFERENCES_RANDOM_DEVICE_ID, Context.MODE_PRIVATE);
				sharedPreferences.putString(RANDOM_DEVICE_ID, deviceId);
				sharedPreferences.commit();
			}

			/**
			 * 从Sharedpreference获取上次保存的lIMEI
			 * @return
			 */
			private String getDeviceIdFromSharedpreference(Context context) {
				PreferencesManager sharedPreferences = new PreferencesManager(context,
						SHAREDPREFERENCES_RANDOM_DEVICE_ID, Context.MODE_PRIVATE);
				return sharedPreferences.getString(RANDOM_DEVICE_ID, DEFAULT_RANDOM_DEVICE_ID);
			}
		}

		return new Statistics().getVirtualIMEI(context);
	}
	
	/**
	 * 获取网络类型
	 * @param context
	 * @return 1 for 移动，2 for 联通，3 for 电信，-1 for 不能识别
	 */
	public static int getNetWorkType(Context context) {
		int netType = -1;
		// 从系统服务上获取了当前网络的MCC(移动国家号)，进而确定所处的国家和地区
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String simOperator = manager.getSimOperator();
		if (simOperator != null) {
			if (simOperator.startsWith("46000") || simOperator.startsWith("46002")) {
				//因为移动网络编号46000下的IMSI已经用完，
				//所以虚拟了一个46002编号，134/159号段使用了此编号 
				//中国移动
				netType = NETTYPE_MOBILE;
			} else if (simOperator.startsWith("46001")) {
				//中国联通
				netType = NETTYPE_UNICOM;
			} else if (simOperator.startsWith("46003")) {
				//中国电信
				netType = NETTYPE_TELECOM;
			}
		}
		return netType;
	}
	
	/**
	 * 检测手机WIFI有没有打开的方法
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
	 * 检测手机网络是否可用的方法
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
	 * <br>功能简述:  获取手机号码
	 * <br>功能详细描述:
	 * <br>注意: 不是所有的手机都可以获取手机号码
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
	
	/**
	 * 获取用户运营商代码
	 * 
	 * @return
	 */
	public static String getCnUser(Context context) {
		String simOperator = "000";
		try {
			if (context != null) {
				// 从系统服务上获取了当前网络的MCC(移动国家号)，进而确定所处的国家和地区
				TelephonyManager manager = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				simOperator = manager.getSimOperator();
			}
		} catch (Throwable e) {
			// TODO: handle exception
		}

		return simOperator;
	}
	
	/**
	 * 获取语言 格式: SIM卡方式：cn 系统语言方式
	 * 
	 * @return
	 */
	public static String language(Context context) {

		String ret = null;
		//TODO 我们现在拿不到桌面语言设置，如何处理？？？？？？？？？？？？？？？？？？？？？？
//		// 根据桌面语言设置请求的语言信息
//		DeskResourcesConfiguration dc = DeskResourcesConfiguration.getInstance();
		Locale locale = null;
//		if (dc != null) {
//			locale = dc.getmLocale();
//		}
		if (locale == null) {
			locale = Locale.getDefault();
		}
		try {
			TelephonyManager telManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (telManager != null) {
				ret = telManager.getSimCountryIso();
				if (ret != null && !ret.equals("")) {
//					ret = String.format("%s_%s", locale.getLanguage().toLowerCase(),
//							ret.toLowerCase());
					ret = ret.toLowerCase();
				}
				
			}
		} catch (Throwable e) {
			// e.printStackTrace();
		}

		if (ret == null || ret.equals("")) {
//			ret = String.format("%s_%s", locale.getLanguage().toLowerCase(), locale.getCountry()
//					.toLowerCase());
			ret = ret.toLowerCase();
		}
		return null == ret ? "error" : ret;
	}
	
	/**
	 * 获取SIM卡所在的国家
	 * 
	 * @param context
	 * @return 当前手机sim卡所在的国家，如果没有sim卡，取本地语言代表的国家
	 */
	public static String local(Context context) {
		String ret = null;
//		// 根据桌面语言设置请求的语言信息
//		DeskResourcesConfiguration dc = DeskResourcesConfiguration.getInstance();
		Locale locale = null;
//		if (dc != null) {
//			locale = dc.getmLocale();
//		}
		if (locale == null) {
			locale = Locale.getDefault();
		}
		try {
			TelephonyManager telManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (telManager != null) {
				ret = telManager.getSimCountryIso();
			}
		} catch (Throwable e) {
			// e.printStackTrace();
		}

		if (ret == null || ret.equals("")) {
			ret = locale.getCountry().toLowerCase();
		}
		return null == ret ? "error" : ret;
	}
	
	
	
	/**
	 * 获取当前网络状态，wifi，GPRS，3G，4G
	 * 
	 * @param context
	 * @return
	 */
	public static int buildNetworkState(Context context) {
		// build Network conditions
//		0：未知
//		1:wifi
//		2:gprs
//		3:3G网络
//		4:4G网络
//		如果无法准确识别，非wifi的网络均可识别为gprs网络
		
		int ret = 0;    //默认是未知
		try {
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkinfo = manager.getActiveNetworkInfo();
			if (networkinfo == null) {
				return 0;	
			}
			if (networkinfo.getType() == ConnectivityManager.TYPE_WIFI) {
				ret = 1;
			} else if (networkinfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				int subtype = networkinfo.getSubtype();
				switch (subtype) {
					case TelephonyManager.NETWORK_TYPE_1xRTT :
					case TelephonyManager.NETWORK_TYPE_CDMA :
					case TelephonyManager.NETWORK_TYPE_EDGE :
					case TelephonyManager.NETWORK_TYPE_GPRS :
					case TelephonyManager.NETWORK_TYPE_IDEN :
						// 2G
						ret = 2 /*+ "(typeid = " + networkinfo.getType() + "  typename = "
									+ networkinfo.getTypeName() + "  subtypeid = "
									+ networkinfo.getSubtype() + "  subtypename = "
									+ networkinfo.getSubtypeName() + ")"*/;
						break;
					case TelephonyManager.NETWORK_TYPE_EVDO_0 :
					case TelephonyManager.NETWORK_TYPE_EVDO_A :
					case TelephonyManager.NETWORK_TYPE_HSDPA :
					case TelephonyManager.NETWORK_TYPE_HSPA :
					case TelephonyManager.NETWORK_TYPE_HSUPA :
					case TelephonyManager.NETWORK_TYPE_UMTS :
						// 3G,4G
						ret = 3 /*+ "(typeid = " + networkinfo.getType() + "  typename = "
										+ networkinfo.getTypeName() + "  subtypeid = "
										+ networkinfo.getSubtype() + "  subtypename = "
										+ networkinfo.getSubtypeName() + ")"*/;
						break;
					case TelephonyManager.NETWORK_TYPE_UNKNOWN :
					default :
						// unknow
						ret = 2 /*+ "(typeid = " + networkinfo.getType() + "  typename = "
										+ networkinfo.getTypeName() + "  subtypeid = "
										+ networkinfo.getSubtype() + "  subtypename = "
										+ networkinfo.getSubtypeName() + ")"*/;
						break;
				}
			} else {
				ret = 2 /*+ "(typeid = " + networkinfo.getType() + "  typename = "
								+ networkinfo.getTypeName() + ")"*/;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
