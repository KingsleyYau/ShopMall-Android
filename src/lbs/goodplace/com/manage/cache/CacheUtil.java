/*
 * 文 件 名:  CacheUtil.java
 * 版    权:  3G
 * 描    述:  <描述>
 * 修 改 人:  liguoliang
 * 修改时间:  2012-10-9
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package lbs.goodplace.com.manage.cache;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  liguoliang
 * @date  [2012-10-9]
 */
public class CacheUtil {
	private static final String ENCODE_UTF_8 = "utf-8";

	/** <br>功能简述:字符串转换成二进制数据流
	 * <br>功能详细描述:使用utf-8编码
	 * <br>注意:
	 * @param src
	 * @return
	 */
	public static byte[] stringToByteArray(String src) {
		if (TextUtils.isEmpty(src)) {
			return null;
		}
//		return src.getBytes();
		byte[] ret = null;
		try {
			ret = src.getBytes(ENCODE_UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/** <br>功能简述:二进制数据流转换成字符串
	 * <br>功能详细描述:使用utf-8编码
	 * <br>注意:
	 * @param src
	 * @return
	 */
	public static String byteArrayToString(byte[] src) {
		if (src == null) {
			return null;
		}
//		return new String(src);
		String ret = null;
		try {
			ret = new String(src, ENCODE_UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static byte[] jsonToByteArray(JSONObject json) {
		if (json == null) {
			return null;
		}
		byte[] ret = null;
		ret = stringToByteArray(json.toString());
		return ret;
	}

	public static JSONObject byteArrayToJson(byte[] src) {
		if (src == null) {
			return null;
		}
		JSONObject ret = null;		
		String str = byteArrayToString(src);
		if (str == null) {
			return null;
		}
		try {
			ret = new JSONObject(str);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static byte[] inputStreamToByteArray(InputStream is) {
		if (is == null) {
			return null;
		}
		byte[] ret = null;
		try {
			ret = CacheFileUtils.toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
