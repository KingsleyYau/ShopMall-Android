/*
 * 文 件 名:  TimeRecord.java
 * 版    权:  3G
 * 描    述:  <描述>
 * 修 改 人:  liguoliang
 * 修改时间:  2012-9-29
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package lbs.goodplace.com.manage.cache;

import android.util.Log;

/**
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  liguoliang
 * @date  [2012-9-29]
 */
public class TimeRecord {
	private boolean mIsRecord = false;

	private long mPrefTime = 0;

	private static final String sTAG = "TimeRecord";

	public void beginRecord() {
		mPrefTime = System.currentTimeMillis();
	}

	public void showTimeInterval(String pref) {
		showTimeInterval(sTAG, pref);
	}

	public void showTimeInterval(String tag, String pref) {
		if (mIsRecord) {
			long t = System.currentTimeMillis();
			Log.i(tag, pref + " timeInterval = " + (t - mPrefTime));
		}
	}

	public void setRecordEnable(boolean enable) {
		mIsRecord = enable;
	}
}
