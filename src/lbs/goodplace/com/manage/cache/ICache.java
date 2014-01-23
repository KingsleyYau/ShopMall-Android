package lbs.goodplace.com.manage.cache;

import java.util.List;

/**
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  liguoliang
 * @date  [2012-9-25]
 */
public interface ICache {

	void saveCache(String key, byte[] cacheData);

	void saveCacheAsync(String key, byte[] cacheData, ICacheListener listener);

	byte[] loadCache(String key);

	void loadCacheAsync(String key, ICacheListener listener);

	void clearCache(String key);

	void clearCache(List<String> keyList);

	boolean isCacheExist(String key);
}
