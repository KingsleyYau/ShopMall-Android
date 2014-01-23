package lbs.goodplace.com.manage.requestmanage;

import lbs.goodplace.com.manage.util.PreferencesManager;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.gau.utils.net.IConnectListener;
import com.gau.utils.net.request.THttpRequest;
import com.gau.utils.net.response.IResponse;

/**
 * 
 * <br>类描述:网络请求管理类。
 * <br>功能详细描述:
 * 
 * @author  licanhui
 * @date  [2013-3-1]
 */
public class RequestManager {

	/**
	 * 
	 * <br>类描述:监听类
	 * <br>功能详细描述:
	 * 
	 * @author  licanhui
	 * @date  [2013-3-5]
	 */
	public interface IDataListener {
		public void loadFinish(boolean success, Object object);
	}
	
	
	
	/**
	 * <br>功能简述:获取网络请求时的缓存数据
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param context
	 * @param iparser 解析类
	 * @param dataListener 监听器
	 * @param cacheKey key值
	 */
	public static void getDataCache(final Context context, final IParser iparser, String cacheKey, final IDataListener dataListener) {
		if (context == null || dataListener == null || TextUtils.isEmpty(cacheKey)) {
			return;
		}
		
		try {
			PreferencesManager preferencesManager = new PreferencesManager(context);
			String cacheString = preferencesManager.getString(cacheKey, "");
			Log.i("lch", "缓存cacheString:" + cacheString);
			if (!TextUtils.isEmpty(cacheString)) {
				//判断是否需要解析
				if (iparser != null) {
					Object object = iparser.parser(new JSONObject(cacheString));
					dataListener.loadFinish(true, object);
				}else {
					dataListener.loadFinish(true, cacheString);
				}
				
			}
		} catch (Exception e) {
			dataListener.loadFinish(false, null);
		}
	}
	
	/**
	 * <br>功能简述:从网络加载数据
	 * <br>功能详细描述:不带缓存
	 * <br>注意:
	 * @param context
	 * @param url
	 * @param postdata
	 * @param iparser
	 * @param dataListener
	 */
	public static void loadDataFromNet(final Context context, final String url,
			byte[] postdata, final IParser iparser,final IDataListener dataListener) {
		loadDataFromNet(context, url, postdata, iparser, false, null, dataListener);
	}
	

	/**
	 * 从网络加载数据
	 * @param context
	 * @param url
	 * @param postdata
	 * @param iparser
	 * @param isNeddCache	是否只读缓存
	 * @param cacheKey
	 * @param dataListener
	 */
	public static void loadDataFromNet(final Context context, final String url,
			byte[] postdata, final IParser iparser,final boolean isNeddCache, 
			final String cacheKey, final IDataListener dataListener) {
		if (url == null  || dataListener == null) {	//|| postdata == null
			Log.i("lch", "请求为空返回");
			return;
		}
		
		//加载缓存数据
		if(isNeddCache){
			getDataCache(context, iparser, cacheKey, dataListener);
			return;
		}
		
		Log.i("lch", "请求地址：" + url);
		
		THttpRequest request = null;
		try {
			request = new THttpRequest(url, postdata, new IConnectListener() {

				@Override
				public void onStart(THttpRequest arg0) {
					
				}

				@Override
				public void onFinish(THttpRequest arg0, IResponse response) {
					if (response != null && response.getResponse() != null && (response.getResponse() instanceof JSONObject)) {
						JSONObject json = (JSONObject) response.getResponse();
						if (json != null && dataListener != null) {
							//判断是否需要解析
							if (iparser != null) {
								Object object = iparser.parser(json);
								dataListener.loadFinish(true, object);
							}else {
								dataListener.loadFinish(true, json);
							}
							
							//保存到缓存xml
							saveLoadNetCache(context, true, cacheKey, json); 
						}else {
							dataListener.loadFinish(false, null);
						}
					}
				}

				@Override
				public void onException(THttpRequest arg0, int arg1) {
					Log.i("lch", "请求 onException：" + arg1);
					if (dataListener != null) {
						dataListener.loadFinish(false, null);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			if (dataListener != null) {
				dataListener.loadFinish(false, null);
			}
		}
		
		if (request != null) {
			//应用是建值对处理了。所以需要加上这个标志
			String POST_CONTENT_TYPE = "application/x-www-form-urlencoded";
			request.addHeader("Content-Type", POST_CONTENT_TYPE);
			
			// 设置线程优先级，读取数据的线程优先级应该最高（比拿取图片的线程优先级高）
			request.setRequestPriority(Thread.MAX_PRIORITY);
			request.setOperator(new AppJsonOperator());
			
			
			AppHttpAdapter httpAdapter = AppHttpAdapter.getInstance(context);
			httpAdapter.addTask(request);
		}
	}
	
	
	/**
	 * <br>功能简述:保存缓存到xml
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param context
	 * @param isNeedCache
	 * @param cacheKey
	 */
	public static void saveLoadNetCache(Context context, boolean isNeedCache, String cacheKey, JSONObject json){
		if (isNeedCache) {
			if (context == null || TextUtils.isEmpty(cacheKey) || json == null) {
				return;
			}
			PreferencesManager preferencesManager = new PreferencesManager(context);
			preferencesManager.putString(cacheKey, json.toString());
			preferencesManager.commit();
		}
	}
}
