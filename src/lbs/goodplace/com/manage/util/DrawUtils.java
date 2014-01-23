package lbs.goodplace.com.manage.util;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.WindowManager;

/**
 * 绘制工具类
 * 
 * @author luopeihuan
 * 
 */
public class DrawUtils {
	public static final float STANDARD_DENSITYDPI = 240f; //标准屏幕的densityDpi
	public static float sDensity = 1.0f;
	public static int sWidthPixels;
	public static int sHeightPixels;
	public static float sFontDensity;
	public static int sTouchSlop = 15; // 点击的最大识别距离，超过即认为是移动

	public static int sStatusHeight; // 平板中底边的状态栏高度
	@SuppressWarnings("rawtypes")
	private static Class sDisplayClass = null;
	private static Method sMethodForWidth = null;
	private static Method sMethodForHeight = null;

	
	/**
	 * 对像素进行匹配机型的缩放
	 * 
	 * @param context
	 * @param sourcePx
	 * @return
	 */
	public static int scalePxToMachine(Context context, int sourcePx) {
		int result = sourcePx;
		if (context != null) {
			float scale = 1.0f;
			int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
			scale = densityDpi / STANDARD_DENSITYDPI;
			result = (int) (sourcePx * scale);
		}
		return result;
	}

	
	/**
	 * dip/dp转像素
	 * 
	 * @param dipValue
	 *            dip或 dp大小
	 * @return 像素值
	 */
	public static int dip2px(float dipVlue) {
		return (int) (dipVlue * sDensity + 0.5f);
	}

	/**
	 * 像素转dip/dp
	 * 
	 * @param pxValue
	 *            像素大小
	 * @return dip值
	 */
	public static int px2dip(float pxValue) {
		final float scale = sDensity;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * sp 转 px
	 * 
	 * @param spValue
	 *            sp大小
	 * @return 像素值
	 */
	public static int sp2px(float spValue) {
		final float scale = sDensity;
		return (int) (scale * spValue);
	}

	/**
	 * px转sp
	 * 
	 * @param pxValue
	 *            像素大小
	 * @return sp值
	 */
	public static int px2sp(float pxValue) {
		final float scale = sDensity;
		return (int) (pxValue / scale);
	}

	public static void resetDensity(Context context) {
		if (context != null && null != context.getResources()) {
			DisplayMetrics metrics = context.getResources().getDisplayMetrics();
			sDensity = metrics.density;
			sFontDensity = metrics.scaledDensity;
			sWidthPixels = metrics.widthPixels;
			sHeightPixels = metrics.heightPixels;
			if (DeviceUtils.isTablet(context)) {
				sStatusHeight = getTabletScreenHeight(context) - sHeightPixels;
			}
			final ViewConfiguration configuration = ViewConfiguration.get(context);
			if (null != configuration) {
				sTouchSlop = configuration.getScaledTouchSlop();
			}
			// Log.i("test",">>>>>>>>sDensity = "+sDensity+",sWidthPixels = "+sWidthPixels+",sHeightPixels="+sHeightPixels+",sStatusHeight = "+sStatusHeight+",dpi = "+sDensity*160);
			// Configuration conf = context.getResources().getConfiguration();
			// Log.i("test",
			// ">>conf.screenWidthDp = "+conf.screenWidthDp+",conf.screenHeightDp = "+conf.screenHeightDp);
		}
	}

	@SuppressWarnings("unchecked")
	public static int getTabletScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int width = 0;
		try {
			if (sDisplayClass == null) {
				sDisplayClass = Class.forName("android.view.Display");
			}
			if (sMethodForWidth == null) {
				sMethodForWidth = sDisplayClass.getMethod("getRealWidth");
			}
			width = (Integer) sMethodForWidth.invoke(display);
		} catch (Exception e) {
		}

		// Rect rect= new Rect();
		// ((Activity)
		// context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		// int statusbarHeight = height - rect.bottom;
		if (width <= 0) {
			width = sWidthPixels;
		}

		return width;
	}

	@SuppressWarnings("unchecked")
	public static int getTabletScreenHeight(Context context) {
		int height = 0;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		try {
			if (sDisplayClass == null) {
				sDisplayClass = Class.forName("android.view.Display");
			}
			if (sMethodForHeight == null) {
				sMethodForHeight = sDisplayClass.getMethod("getRealHeight");
			}
			height = (Integer) sMethodForHeight.invoke(display);
		} catch (Exception e) {
		}

		// Rect rect= new Rect();
		// ((Activity)
		// context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		// int statusbarHeight = height - rect.bottom;
		if (height <= 0) {
			height = sHeightPixels;
		}

		return height;
	}

	/**
	 * 获取屏幕物理尺寸
	 * 
	 * @param activity
	 * @return
	 */
	public static double getScreenInches(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		double screenInches = Math.sqrt(x + y);
		Log.v("System.out.print", "Screen inches : " + screenInches);
		return screenInches;
	}

	/**
	 * 获取状态栏的高度
	 * 
	 * @param context
	 *            Activity的context
	 * @return
	 */
//	public static int getStatusBarHeight(Context context) {
//		int statusBarHeight = 0;
//		Rect frame = new Rect();
//		if (context instanceof Activity) {
//			((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//			if (CompatibleUtils.NEEDS_COMPATIBLE) {
//				statusBarHeight = frame.top; // 如果是4.0因为状态栏是在底下，所以不影响，frame.top为0
//			} else {
//				statusBarHeight = sStatusHeight;
//			}
//		}
//		return statusBarHeight;
//	}
}
