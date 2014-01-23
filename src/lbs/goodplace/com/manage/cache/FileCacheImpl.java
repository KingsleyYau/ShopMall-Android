/*
 * 文 件 名:  CacheTask.java
 * 版    权:  3G
 * 描    述:  <描述>
 * 修 改 人:  liguoliang
 * 修改时间:  2012-9-25
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package lbs.goodplace.com.manage.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * <br>类描述:通用的缓存任务
 * <br>功能详细描述:默认为使用ZIP压缩,默认使用XOR异或加密
 * 
 * @author  liguoliang
 * @date  [2012-9-25]
 */
public class FileCacheImpl extends BaseCacheImpl {
	private String mFilePath;

	private TimeRecord mTimeRecord;

	private static final int LISTENER_TYPE_START = 1;
	private static final int LISTENER_TYPE_PROGRESS = 2;
	private static final int LISTENER_TYPE_FINISH = 3;
	private static final int LISTENER_TYPE_EXCEPTION = 4;
	private static final int LISTENER_TYPE_WAIT = 5;

	private boolean mIsNotifyProgress = true;

	public FileCacheImpl(String filePath) {
		super();
		setCacheType(CACHE_TYPE_FILE);
		setFilePath(filePath);
		init();
	}

	private void init() {
		// 默认使用gzip压缩
		mCompress = new ZipCompress();

		// 默认使用桌面异或加密
		mEncrypt = new XORCrypt();

		//		mTimeRecord = new TimeRecord();
		//		mTimeRecord.setRecordEnable(false);
	}

	public void setFilePath(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			throw new IllegalArgumentException("filePath can not be null");
		}
		if (!filePath.endsWith(File.separator)) {
			// 如果文件路径不包括后缀则自动添加后缀
			filePath += File.separator;
		}
		this.mFilePath = filePath;
	}

	public String getFilePath() {
		return mFilePath;
	}

	public void setNotifyProgressEnable(boolean enable) {
		mIsNotifyProgress = enable;
	}

	/** <br>功能简述:同步保存
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	private void saveSync(String key, byte[] cacheData) throws Exception {
		if (!CacheFileUtils.isSDCardExist()) {
			throw new RuntimeException("SDCard is not exist");
		}
		notifyListener(this, LISTENER_TYPE_START, null, null, null);

		byte[] src = cacheData;
		/////////////////////////////////
		// 判断是否需要压缩
		if (mCompress != null) {
			notifyListener(this, LISTENER_TYPE_PROGRESS, PROGRESS_SAVE_COMPRESS_START, src, null);
			src = mCompress.compress(src);
			if (src == null) {
				throw new RuntimeException("Compress cache object  failed");
			}
			notifyListener(this, LISTENER_TYPE_PROGRESS, PROGRESS_SAVE_COMPRESS_FINISH, src, null);
		}

		/////////////////////////////////
		// 是否需要加密
		if (mEncrypt != null) {
			notifyListener(this, LISTENER_TYPE_PROGRESS, PROGRESS_SAVE_ENCRYPT_START, src, null);
			src = mEncrypt.encrypt(src);
			if (src == null) {
				throw new RuntimeException("Encrypt cache object  failed");
			}
			notifyListener(this, LISTENER_TYPE_PROGRESS, PROGRESS_SAVE_ENCRYPT_FINISH, src, null);
		}

		// 保存文件
		try {
			notifyListener(this, LISTENER_TYPE_PROGRESS, PROGRESS_SAVE_DATA_START, src, null);
			CacheFileUtils.saveByteToSDFile(src, mFilePath + key); //将filePath跟key值统一起来作为文件名 
			notifyListener(this, LISTENER_TYPE_PROGRESS, PROGRESS_SAVE_DATA_FINISH, src, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Save data  failed");
		}

		notifyListener(this, LISTENER_TYPE_FINISH, src, null, null);
	}

	private void handleException(Exception e) {
		notifyListener(this, LISTENER_TYPE_EXCEPTION, e, null, null);
	}

	/** <br>功能简述:同步加载缓存
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	private byte[] loadSync(String key) throws Exception {
		if (!CacheFileUtils.isSDCardExist()) {
			throw new RuntimeException("SDCard is not exist");
		}
		byte[] data = null;
		try {
			notifyListener(this, LISTENER_TYPE_START, null, null, null);
			notifyListener(this, LISTENER_TYPE_PROGRESS, PROGRESS_LOAD_DATA_START, null, null);
			data = CacheFileUtils.readFileToByte(mFilePath + key); //将filePath跟key值统一起来作为文件名
			notifyListener(this, LISTENER_TYPE_PROGRESS, PROGRESS_LOAD_DATA_FINISH, data, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (data == null) {
			throw new RuntimeException("Read cache Failed");
		}

		////////////////////////////////		
		// 解密数据
		if (mEncrypt != null) {
			notifyListener(this, LISTENER_TYPE_PROGRESS, PROGRESS_LOAD_DECRYPT_START, data, null);
			data = mEncrypt.decrypt(data);
			if (data == null) {
				throw new RuntimeException("Decrypt cache falied");
			}
			notifyListener(this, LISTENER_TYPE_PROGRESS, PROGRESS_LOAD_DECRYPT_FINISH, data, null);
		}

		//////////////////////////////////
		// 解压数据
		if (mCompress != null) {
			notifyListener(this, LISTENER_TYPE_PROGRESS, PROGRESS_LOAD_DECOMPRESS_START, data, null);
			data = mCompress.decompress(data);
			if (data == null) {
				throw new RuntimeException("Decompress cache faile");
			}
			notifyListener(this, LISTENER_TYPE_PROGRESS, PROGRESS_LOAD_DECOMPRESS_FINISH, data,
					null);
		}

		//		// 数据parse
		//		notifyListener(listener, this, LISTENER_TYPE_PROGRESS, PROGRESS_LOAD_PARSE_OBJECT_START,
		//				data, null);
		//		ICacheResponse response = parse.parseLoadCache(data);
		//		notifyListener(listener, this, LISTENER_TYPE_PROGRESS, PROGRESS_LOAD_PARSE_OBJECT_FINISH,
		//				response, null);

		notifyListener(this, LISTENER_TYPE_FINISH, data, null, null);
		return data;
	}

	private void notifyListener(BaseCacheImpl task, int type, Object arg0, Object arg1, Object arg2) {
		ICacheListener listener = getCacheListener();
		if (listener == null) {
			return;
		}
		switch (type) {
			case LISTENER_TYPE_START :
				listener.onStart(task, arg0, arg1);
				break;
			case LISTENER_TYPE_PROGRESS :
				if (mIsNotifyProgress) {
					listener.onProgress(task, arg0, arg1);
				}
				break;
			case LISTENER_TYPE_FINISH :
				listener.onFinish(task, (byte[]) arg0, arg1, arg2);
				break;
			case LISTENER_TYPE_EXCEPTION :
				listener.onException(task, (Exception) arg0, arg1, arg2);
				break;
			case LISTENER_TYPE_WAIT :
				listener.onWait(task, arg0, arg1);
				break;
			default :
				break;
		}
	}

	@Override
	public boolean isCacheExist(String key) {
		return CacheFileUtils.isFileExist(mFilePath + key);
	}

	@Override
	public void saveCache(String key, byte[] cacheData) {
		try {
			saveSync(key, cacheData);
		} catch (Exception e) {
			e.printStackTrace();
			handleException(e);
		}
	}

	@Override
	public void saveCacheAsync(final String key, final byte[] cacheData, ICacheListener listener) {
		setCacheListener(listener);
		new Thread(new Runnable() {

			@Override
			public void run() {
				saveCache(key, cacheData);
			}
		}, "saveCacheThread").start();
	}

	@Override
	public byte[] loadCache(String key) {
		byte[] ret = null;
		try {
			ret = loadSync(key);
		} catch (Exception e) {
			e.printStackTrace();
			// 加载缓存失败的话清除旧的缓存,防止出现一直重复加载旧的数据
			clearCache(key);
			handleException(e);
		}
		return ret;
	}

	@Override
	public void loadCacheAsync(final String key, ICacheListener listener) {
		setCacheListener(listener);
		new Thread(new Runnable() {

			@Override
			public void run() {
				loadCache(key);
			}
		}, "loadCacheThread").start();
	}

	@Override
	public void clearCache(String key) {
		CacheFileUtils.deleteFile(mFilePath + key);
	}

	@Override
	public void clearCache(List<String> keyList) {
		if (keyList == null) {
			return;
		}
		for (String key : keyList) {
			clearCache(key);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getModuleKeyList(String module) {
		String moduleFileName = null;		
		if (mIsKeyEncrypt) {
			// 模块名的MD5码作为保存key值的文件名
			moduleFileName = mFilePath + MD5.encode(module);
		} else {
			moduleFileName = mFilePath + module;
		}
		if (!CacheFileUtils.isFileExist(moduleFileName)) {
			// 文件不存在
			return null;
		}
		String data = CacheFileUtils.readFileToString(moduleFileName);
		if (data == null) {
			return null;
		}
		JSONObject json = null;
		try {
			json = new JSONObject(data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (json == null || json.length() < 1) {
			return null;
		}

		Iterator<String> iterator = json.keys();
		if (iterator == null) {
			return null;
		}
		List<String> keyList = new ArrayList<String>();
		while (iterator.hasNext()) {
			String extra = iterator.next();
			if (extra == null) {
				continue;
			}
			String key = json.optString(extra, null);
			if (key != null) {
				keyList.add(key);
			}
		}
		return keyList;
	}

	@Override
	public void clearModuleKeyList(String module) {
		String moduleFileName = null;		
		if (mIsKeyEncrypt) {
			// 模块名的MD5码作为保存key值的文件名
			moduleFileName = mFilePath + MD5.encode(module);
		} else {
			moduleFileName = mFilePath + module;
		}
		if (CacheFileUtils.isFileExist(moduleFileName)) {
			CacheFileUtils.deleteFile(moduleFileName);
		}
	}

	@Override
	public void clearModuleKey(String module, String extra) {
		String moduleFileName = null;		
		if (mIsKeyEncrypt) {
			// 模块名的MD5码作为保存key值的文件名
			moduleFileName = mFilePath + MD5.encode(module);
		} else {
			moduleFileName = mFilePath + module;
		}
		if (!CacheFileUtils.isFileExist(moduleFileName)) {
			// 文件不存在
			return;
		}
		String data = CacheFileUtils.readFileToString(moduleFileName);
		if (data == null) {
			return;
		}
		JSONObject json = null;
		try {
			json = new JSONObject(data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (json == null || json.length() < 1) {
			return;
		}
		json.remove(extra);
		if (json.length() < 1) {
			CacheFileUtils.deleteFile(moduleFileName);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getModuleKeyMap(String module) {		
		String moduleFileName = null;		
		if (mIsKeyEncrypt) {
			// 模块名的MD5码作为保存key值的文件名
			moduleFileName = mFilePath + MD5.encode(module);
		} else {
			moduleFileName = mFilePath + module;
		}
		if (!CacheFileUtils.isFileExist(moduleFileName)) {
			// 文件不存在
			return null;
		}
		String data = CacheFileUtils.readFileToString(moduleFileName);
		if (data == null) {
			return null;
		}
		JSONObject json = null;
		try {
			json = new JSONObject(data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (json == null || json.length() < 1) {
			return null;
		}

		Iterator<String> iterator = json.keys();
		if (iterator == null) {
			return null;
		}
		Map<String, String> keyMap = new ConcurrentHashMap<String, String>();
		while (iterator.hasNext()) {
			String extra = iterator.next();
			if (extra == null) {
				continue;
			}
			String key = json.optString(extra, null);
			if (key != null) {
				keyMap.put(extra, key);
			}
		}
		return keyMap;
	}

	public String buildKey(String module, String extra) {
		if (module == null || extra == null) {
			return null;
		}
		String key = null;		
		if (mIsKeyEncrypt) {
			key = MD5.encode(module + "_" + extra);
		} else {
			key = module + "_" + extra;
		}
		return key;
	}

	@Override
	public void saveModuleKey(String module, String extra) {
		// 模块名 + 额外数据 的 MD5码作为key值
		String key = buildKey(module, extra);

		// 模块名的MD5码作为保存key值的文件名
		String moduleFileName = null;		
		if (mIsKeyEncrypt) {
			moduleFileName = mFilePath + MD5.encode(module);
		} else {
			moduleFileName = mFilePath + module;
		}

		// 先判断缓存文件是否存在
		JSONObject json = null;
		if (CacheFileUtils.isFileExist(moduleFileName)) {
			String data = CacheFileUtils.readFileToString(moduleFileName);
			if (data != null) {
				try {
					json = new JSONObject(data);
				} catch (JSONException e) {
					e.printStackTrace();
					CacheFileUtils.deleteFile(moduleFileName);
				}
			}
		}
		if (json == null) {
			json = new JSONObject();
		}

		// 通过extra作为索引值缓存key值
		try {
			json.put(extra, key);
			CacheFileUtils.saveByteToSDFile(json.toString().getBytes(), moduleFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveModuelKey(String module, List<String> extraList) {
		if (module == null || extraList == null || extraList.isEmpty()) {
			return;
		}
		String moduleFileName = null;		
		if (mIsKeyEncrypt) {
			moduleFileName = mFilePath + MD5.encode(module);
		} else {
			moduleFileName = mFilePath + module;
		}

		// 先判断缓存文件是否存在
		JSONObject json = null;
		if (CacheFileUtils.isFileExist(moduleFileName)) {
			String data = CacheFileUtils.readFileToString(moduleFileName);
			if (data != null) {
				try {
					json = new JSONObject(data);
				} catch (JSONException e) {
					e.printStackTrace();
					CacheFileUtils.deleteFile(moduleFileName);
				}
			}
		}
		if (json == null) {
			json = new JSONObject();
		}
		try {
			for (String extra : extraList) {
				String key = buildKey(module, extra);
				json.put(extra, key);
			}
			CacheFileUtils.saveByteToSDFile(json.toString().getBytes(), moduleFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setKeyNeedEncrypt(boolean encrypt) {
		mIsKeyEncrypt = encrypt;
	}

	@Override
	public boolean getKeyNeedEncrypt() {
		return mIsKeyEncrypt;
	}
}
