/**
 * 
 */
package lbs.goodplace.com.manage.requestmanage;

import java.util.Map;

import android.content.Context;

import com.gau.utils.net.HttpAdapter;
import com.gau.utils.net.request.THttpRequest;

/**
 * AppHttpAdapter：应用游戏中心中使用的网络调度器,为单实例对象
 * 
 * 默认并发量为2
 * 
 */
public class AppHttpAdapter {
//	private Context mContext;
	private HttpAdapter mHttpAdapter = null;

	/**
	 * 是否可以使用长连接,开关由服务器下发控制
	 */
	private boolean mIsAliveEnable = false;

	private volatile static AppHttpAdapter sSelf = null;

	private AppHttpAdapter(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("context can not be null");
		}
//		mContext = context;
		mHttpAdapter = new HttpAdapter(context);
		mHttpAdapter.setMaxConnectThreadNum(2);
		initAppHeartUrl();
	}

	private void initAppHeartUrl() {
		if (mHttpAdapter != null) {
//			putCommonHeartUrl(NetUtil.getHost(GameNetConstant.GAME_CENTER_URL_CHINA),
//					GameNetConstant.HEART_GAME_CHINA);
//			putCommonHeartUrl(NetUtil.getHost(GameNetConstant.GAME_CENTER_URL_OTHERS),
//					GameNetConstant.HEART_GAME_OTHERS);
//			putCommonHeartUrl(NetUtil.getHost(AppsNetConstant.APP_CENTER_URL_CHINA),
//					AppsNetConstant.HEART_APPCENTER_CHINA);
//			putCommonHeartUrl(NetUtil.getHost(AppsNetConstant.APP_CENTER_URL_OTHERS),
//					AppsNetConstant.HEART_APPCENTER_OTHERS);
		}
	}

	/**
	 * 销毁资源
	 */
	public static void destory() {
		if (sSelf != null) {
			sSelf.recycle();
			sSelf = null;
		}
	}

	private void recycle() {
		mHttpAdapter = null;
	}

	public static AppHttpAdapter getInstance(Context context) {
		if (sSelf == null) {
			synchronized (AppHttpAdapter.class) {
				if (sSelf == null) {
					sSelf = new AppHttpAdapter(context);
				}
			}
		}
		return sSelf;
	}

	public HttpAdapter getHttpAdapter() {
		return mHttpAdapter;
	}

	/**
	 * 添加异步网络请求
	 * 
	 * @param request
	 */
	public void addTask(THttpRequest request) {
		if (mHttpAdapter != null) {
			mHttpAdapter.addTask(request);
		}
	}

	/**
	 * 添加网络请求
	 * 
	 * @param request
	 * @param isAsync
	 *            是否异步
	 */
	public void addTask(THttpRequest request, boolean isAsync) {
		request.setIsAsync(isAsync);
		//		addTask(request);
		// 设置为默认使用长连接，测试长连接的可用性
		addTask(true, request);
	}

	/**
	 * 添加网络请求 注意如果使用长连接,则设置同步连接无效,由于长连接需要使用心跳包,使用同步会导致进程卡死
	 * 
	 * @param isAlive
	 *            是否使用长连接
	 * @param request
	 */
	public void addTask(boolean isAlive, THttpRequest request) {
//		// 判断该渠道长连接是否允许被使用
//		boolean channelAliveEnable = ChannelConfig.getInstance(mContext).isKeepAliveEnable();
		if (mIsAliveEnable) {
			request.setIsKeepAlive(true);
		} else {
			request.setIsKeepAlive(false);
		}
		addTask(request);
	}

	/**
	 * 设置是否长连接可用
	 * 
	 * @param enable
	 */
	public void setAliveEnable(boolean enable) {
		mIsAliveEnable = enable;
	}

	/**
	 * 取消网络请求
	 * 
	 * @param request
	 */
	public void cancelTask(THttpRequest request) {
		if (mHttpAdapter != null) {
			mHttpAdapter.cancelTask(request);
		}
	}

	public void cleanup() {
		if (mHttpAdapter != null) {
			mHttpAdapter.cleanup();
			mHttpAdapter = null;
		}
	}

	/**
	 * 设置网络请求最大并发数,如果不设置，为5
	 * 
	 * @param num
	 */
	public void setMaxConnectThreadNum(int num) {
		if (mHttpAdapter != null) {
			mHttpAdapter.setMaxConnectThreadNum(num);
		}
	}

	/**
	 * 添加心跳包地址映射表,如果已经存在,则替换旧值
	 * 
	 * @param host
	 * @param heartUrl
	 * @return 被替换的心跳包地址值
	 */
	public String putCommonHeartUrl(String host, String heartUrl) {
		if (mHttpAdapter != null) {
			return mHttpAdapter.putCommonHeartUrl(host, heartUrl);
		}
		return null;
	}

	/**
	 * 移除心跳包地址键值对
	 * 
	 * @param host
	 * @return 被删除的心跳包地址值
	 */
	public String removeHeartUrl(String host) {
		if (mHttpAdapter != null) {
			return mHttpAdapter.removeHeartUrl(host);
		}
		return null;
	}

	public String getHeartUrl(String host) {
		if (mHttpAdapter != null) {
			return mHttpAdapter.getHeartUrl(host);
		}
		return null;
	}

	/**
	 * 获取全部心跳包地址映射表
	 * 
	 * @return
	 */
	public Map<String, String> getAllCommonHeartUrl() {
		if (mHttpAdapter != null) {
			return mHttpAdapter.getAllCommonHeartUrl();
		}
		return null;
	}
}
