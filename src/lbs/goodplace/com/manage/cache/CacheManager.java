package lbs.goodplace.com.manage.cache;

import java.util.List;
import java.util.Map;

/**
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  liguoliang
 * @date  [2012-9-25]
 */
public class CacheManager implements ICache, IModuleKey {
	private BaseCacheImpl mCacheImpl;

	public CacheManager(BaseCacheImpl impl) {
		setCacheImpl(impl);
	}

	public void setCacheImpl(BaseCacheImpl impl) {
		this.mCacheImpl = impl;
	}

	public BaseCacheImpl getCacheImpl() {
		return mCacheImpl;
	}

	@Override
	public void saveCache(String key, byte[] cacheData) {
		if (mCacheImpl != null) {
			mCacheImpl.saveCache(key, cacheData);
		}
	}

	@Override
	public void saveCacheAsync(String key, byte[] cacheData, ICacheListener listener) {
		if (mCacheImpl != null) {
			mCacheImpl.saveCacheAsync(key, cacheData, listener);
		}
	}

	@Override
	public byte[] loadCache(String key) {
		if (mCacheImpl != null) {
			return mCacheImpl.loadCache(key);
		}
		return null;
	}

	@Override
	public void loadCacheAsync(String key, ICacheListener listener) {
		if (mCacheImpl != null) {
			mCacheImpl.loadCacheAsync(key, listener);
		}
	}

	@Override
	public void clearCache(String key) {
		if (mCacheImpl != null) {
			mCacheImpl.clearCache(key);
		}
	}
	
	@Override
	public void clearCache(List<String> keyList) {
		if (mCacheImpl != null) {
			mCacheImpl.clearCache(keyList);
		}
	}

	@Override
	public boolean isCacheExist(String key) {
		if (mCacheImpl != null) {
			return mCacheImpl.isCacheExist(key);
		}
		return false;
	}

	@Override
	public String buildKey(String module, String extra) {
		if (mCacheImpl != null) {
			return mCacheImpl.buildKey(module, extra);
		}
		return null;
	}

	@Override
	public void saveModuleKey(String module, String extra) {
		if (mCacheImpl != null) {
			mCacheImpl.saveModuleKey(module, extra);
		}
	}

	@Override
	public void saveModuelKey(String module, List<String> extraList) {
		if (mCacheImpl != null) {
			mCacheImpl.saveModuelKey(module, extraList);
		}
	}

	@Override
	public List<String> getModuleKeyList(String module) {
		if (mCacheImpl != null) {
			return mCacheImpl.getModuleKeyList(module);
		}
		return null;
	}

	@Override
	public Map<String, String> getModuleKeyMap(String module) {
		if (mCacheImpl != null) {
			return mCacheImpl.getModuleKeyMap(module);
		}
		return null;
	}

	@Override
	public void clearModuleKeyList(String module) {
		if (mCacheImpl != null) {
			mCacheImpl.clearModuleKeyList(module);
		}
	}

	@Override
	public void clearModuleKey(String module, String key) {
		if (mCacheImpl != null) {
			mCacheImpl.clearModuleKey(module, key);
		}
	}

	@Override
	public void setKeyNeedEncrypt(boolean encrypt) {
		if (mCacheImpl != null) {
			mCacheImpl.setKeyNeedEncrypt(encrypt);			
		}
	}

	@Override
	public boolean getKeyNeedEncrypt() {
		if (mCacheImpl != null) {
			return mCacheImpl.getKeyNeedEncrypt();
		}
		return false;
	}	
}
