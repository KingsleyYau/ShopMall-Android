package lbs.goodplace.com.manage.util;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;

/**
 * 
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  liulixia
 * @date  [2012-9-21]
 */
public class DeviceUtils {
	// 硬件加速
	public static int sLAYER_TYPE_NONE = 0x00000000;
	public static int sLAYER_TYPE_SOFTWARE = 0x00000001;
	public static int sLAYER_TYPE_HARDWARE = 0x00000002;

	private static Method sMAcceleratedMethod = null;

	/**
	 * 设置硬件加速
	 * 
	 * @param view
	 * @param mode
	 *            {@link #LAYER_TYPE_NONE}, {@link #LAYER_TYPE_SOFTWARE} or
	 *            {@link #LAYER_TYPE_HARDWARE}
	 */
	public static void setHardwareAccelerated(View view, int mode) {
		// honeycomb 以上版本才有硬件加速功能
		if (Build.VERSION.SDK_INT >= 11) {
			try {
				if (null == sMAcceleratedMethod) {
					sMAcceleratedMethod = View.class.getMethod("setLayerType", new Class[] {
							Integer.TYPE, Paint.class });
				}
				sMAcceleratedMethod.invoke(view, new Object[] { Integer.valueOf(mode), null });
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 判断是否平板
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context) {
		int layout = context.getResources().getConfiguration().screenLayout;
		return (layout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	/**
	 * 判断是否支持拨打电话的方法 wangzhuobin
	 * 
	 * @param context
	 * @return
	 */
	//	public static boolean isSupportPhoneCall(Context context) {
	//		boolean result = false;
	//		if (context != null) {
	//			TelephonyManager telephonyManager = (TelephonyManager) LauncherApplication.getInstance()
	//					.getSystemService(Context.TELEPHONY_SERVICE);
	//			if (telephonyManager != null
	//					&& telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE) {
	//				result = true;
	//			}
	//		}
	//		return result;
	//	}

	/*
	 * 获取版本号
	 */
	public static String getVersionName(Context context) {
		String version = "unknown";
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (info != null) {
				version = "" + info.versionName;
			}
		} catch (Exception e) {
		}
		return version;
	}

	public static String getVersionCode(Context context) {
		String version = "unknown";
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (info != null) {
				version = "" + info.versionCode;
			}
		} catch (Exception e) {
		}
		return version;
	}
}
