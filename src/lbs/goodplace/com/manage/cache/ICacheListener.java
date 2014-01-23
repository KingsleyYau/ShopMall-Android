package lbs.goodplace.com.manage.cache;


/**
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  liguoliang
 * @date  [2012-9-25]
 */
public interface ICacheListener {

	/** <br>功能简述:
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param task
	 * @param arg1	保留参数
	 * @param arg2	保留参数
	 */
	void onWait(ICache cache, Object arg1, Object arg2);

	/** <br>功能简述:
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param task
	 * @param arg1
	 * @param arg2
	 */
	void onStart(ICache cache, Object arg1, Object arg2);

	/** <br>功能简述:
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param task
	 * @param arg1	保留参数
	 * @param arg2	保留参数
	 */
	void onProgress(ICache cache, Object arg1, Object arg2);

	/** <br>功能简述:
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param task
	 * @param arg1	保留参数
	 * @param arg2	保留参数
	 */
	void onFinish(ICache cache, byte[] data, Object arg1, Object arg2);

	/** <br>功能简述:
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param task
	 * @param exception
	 * @param arg1	保留参数
	 * @param arg2	保留参数
	 */
	void onException(ICache cache, Exception exception, Object arg1, Object arg2);
}
