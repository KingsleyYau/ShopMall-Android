package lbs.goodplace.com.manage.util;

import java.io.File;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * 应用推荐模块，工具类
 * 
 * @author zhoujun
 * 
 */
public class RecommAppsUtils {
	public static final float STANDARD_DENSITYDPI = 240f; //标准屏幕的densityDpi
	
	// 应用推荐默认显示系统图标大小
	private static final int APP_ICON_WIDTH = 80;
	private static final int APP_ICON_HEIGHT = 80;

	/**
	 * 得到文件�?包括后缀)
	 * 
	 * @param fileName
	 * @return
	 */
	public static String formatFileName(String fileName) {
		if (fileName == null) {
			return null;
		}
		if (fileName.lastIndexOf(File.separator) >= 0) {
			return fileName.substring(fileName.lastIndexOf(File.separator) + 1);
		}
		return null;
	}

	/**
	 * 得到文件�?不包括后�?
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getSimpleName(String fileName) {
		String str = fileName;
		if (str == null) {
			return null;
		}
		if (str.lastIndexOf(File.separator) >= 0) {
			str = str.substring(str.lastIndexOf(File.separator) + 1);
		}
		if (str.indexOf(".") >= 0) {
			str = str.substring(0, str.lastIndexOf("."));
		}

		return str;
	}

	public static BitmapDrawable loadAppIcon(String iconPath, Context context) {
		try {
			Bitmap bitmap = null;
			Bitmap newBitmap = null;
			if (iconPath != null && !"".equals(iconPath)) {
				bitmap = BitmapFactory.decodeFile(iconPath);
			}

			if (bitmap == null || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
				Log.e("RecommAppsUtils", iconPath + " is not exist");
				return null;
			}
			int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
			float scale = densityDpi / STANDARD_DENSITYDPI;
			newBitmap = zoomBitmap(bitmap, (int) (APP_ICON_WIDTH * scale),
					(int) (APP_ICON_HEIGHT * scale));
			// Log.d("RecommAppsUtils", "densityDpi value : " + densityDpi);
			return new BitmapDrawable(context.getResources(), newBitmap);
		} catch (OutOfMemoryError error) {
			error.printStackTrace();
		}
		return null;
	}

	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w) / width;
		float scaleHeight = ((float) h) / height;
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return newbmp;
	}

	/**
	 * 根据包名判断，是否已经安�?
	 * 
	 * @param context
	 * @param packageName
	 * @param versionName
	 * @return
	 */
	public static boolean isInstalled(Context context, String packageName, String versionName) {
		PackageManager manager = context.getPackageManager();
		PackageInfo info;
		try {
			info = manager.getPackageInfo(packageName, 0);
			if (info != null) {
				if (versionName == null || "".equals(versionName)
						|| versionName.equals(info.versionName)
						|| "Varies with device".equals(info.versionName)) {
					return true;
				}
			}
		} catch (NameNotFoundException e) {
			// e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取设备分辨�?
	 * 
	 * @param context
	 * @return
	 */
	public static String getDisplay(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wMgr.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		return width + "*" + height;
	}

	

	/**
	 * 获取用户运营商代�?
	 * 
	 * @return
	 */
	private static String getCnUser(Context context) {
		String simOperator = "000";
		try {
			if (context != null) {
				// 从系统服务上获取了当前网络的MCC(移动国家�?，进而确定所处的国家和地�?
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
	 * 获取SIM卡所在的国家
	 * 
	 * @author xiedezhi
	 * @param context
	 * @return 当前手机sim卡所在的国家，如果没有sim卡，取本地语�?��表的国家
	 */
	public static String local(Context context) {
		String ret = null;
//		// 根据桌面语言设置请求的语�?���?
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
	 * 获取语言和国家地区的方法 格式: SIM卡方式：cn 系统语言方式：zh-CN
	 * 
	 * @return
	 */
	private static String language(Context context) {

		String ret = null;
		// 根据桌面语言设置请求的语�?���?
		Locale locale = null;
		if (locale == null) {
			locale = Locale.getDefault();
		}
		try {
			TelephonyManager telManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (telManager != null) {
				ret = telManager.getSimCountryIso();
				if (ret != null && !ret.equals("")) {
					ret = String.format("%s_%s", locale.getLanguage().toLowerCase(),
							ret.toLowerCase());
				}
			}
		} catch (Throwable e) {
			// e.printStackTrace();
		}

		if (ret == null || ret.equals("")) {
			ret = String.format("%s_%s", locale.getLanguage().toLowerCase(), locale.getCountry()
					.toLowerCase());
		}
		return null == ret ? "error" : ret;
	}

	/**
	 * 获取当前网络状�?，wifi，GPRS�?G�?G
	 * 
	 * @param context
	 * @return
	 */
	private static String buildNetworkState(Context context) {
		// build Network conditions
		String ret = "";
		try {
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkinfo = manager.getActiveNetworkInfo();
			if (networkinfo.getType() == ConnectivityManager.TYPE_WIFI) {
				ret = "WIFI";
			} else if (networkinfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				int subtype = networkinfo.getSubtype();
				switch (subtype) {
					case TelephonyManager.NETWORK_TYPE_1xRTT :
					case TelephonyManager.NETWORK_TYPE_CDMA :
					case TelephonyManager.NETWORK_TYPE_EDGE :
					case TelephonyManager.NETWORK_TYPE_GPRS :
					case TelephonyManager.NETWORK_TYPE_IDEN :
						// 2G
						ret = "2G" /*+ "(typeid = " + networkinfo.getType() + "  typename = "
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
						ret = "3G/4G" /*+ "(typeid = " + networkinfo.getType() + "  typename = "
										+ networkinfo.getTypeName() + "  subtypeid = "
										+ networkinfo.getSubtype() + "  subtypename = "
										+ networkinfo.getSubtypeName() + ")"*/;
						break;
					case TelephonyManager.NETWORK_TYPE_UNKNOWN :
					default :
						// unknow
						ret = "UNKNOW" /*+ "(typeid = " + networkinfo.getType() + "  typename = "
										+ networkinfo.getTypeName() + "  subtypeid = "
										+ networkinfo.getSubtype() + "  subtypename = "
										+ networkinfo.getSubtypeName() + ")"*/;
						break;
				}
			} else {
				ret = "UNKNOW" /*+ "(typeid = " + networkinfo.getType() + "  typename = "
								+ networkinfo.getTypeName() + ")"*/;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 获取桌面版本�?
	 * 
	 * @author xiedezhi
	 * 
	 * @param context
	 * @return
	 */
	public static String buildVersion(Context context) {
		String ret = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			ret = pi.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 根据手机的分辨率�?dp 的单�?转成�?px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率�?px(像素) 的单�?转成�?dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将bitmap转成drawable
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Context context, Drawable drawable) {

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
}
