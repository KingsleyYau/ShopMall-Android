/*
 * 文 件 名:  CacheConfigConstanst.java
 * 版    权:  3G
 * 描    述:  <描述>
 * 修 改 人:  liguoliang
 * 修改时间:  2012-9-26
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package lbs.goodplace.com.manage.cache;

import java.io.File;

import org.json.JSONObject;

/**
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  liguoliang
 * @date  [2012-9-26]
 */
class CacheConfig {
	public static final int CONFIG_VERSION_INT = 1;

	public static final String VERSION = "version";

	public static final String COMPRESS = "compress";

	/**
	 * 压缩方法名
	 */
	public static final String COMPRESS_NAME = "compress_name";

	/**
	 * 加密类名
	 */
	public static final String ENCRYPT = "encrypt";

	/**
	 * 类名
	 */
	public static final String TASK = "task";

	/**
	 * 是否同步
	 */
	public static final String SYNC = "sync";

	static void createConfigFile(String filePath) throws Exception {
		String pathConfig = getConfigPath(filePath);
		File fileConfig = new File(pathConfig);
		if (!fileConfig.exists()) {
			CacheFileUtils.createNewFile(pathConfig, false);
			JSONObject json = new JSONObject();
			json.put(CacheConfig.VERSION, CacheConfig.CONFIG_VERSION_INT);
			CacheFileUtils.saveByteToSDFile(json.toString().getBytes(), pathConfig);
		}
	}

	/** <br>功能简述:根据路径创建配置文件名
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param filePath
	 * @return
	 */
	private static String getConfigPath(String filePath) {
		File file = new File(filePath);
		return file.getParent() + File.separator + MD5.encode(filePath);
	}

	static void saveMark(String filePath, String key, String mark) throws Exception {
		createConfigFile(filePath);
		String pathConfig = getConfigPath(filePath);
		File fileConfig = new File(pathConfig);
		JSONObject json = null;
		if (fileConfig.exists()) {
			String content = CacheFileUtils.readFileToString(pathConfig);
			if (content != null) {
				json = new JSONObject(content);
			}
		} else {
			CacheFileUtils.createNewFile(pathConfig, false);
			json = new JSONObject();
		}
		if (json != null) {
			json.put(key, mark);
			CacheFileUtils.saveByteToSDFile(json.toString().getBytes(), pathConfig);
		}
	}

	static String getMark(String filePath, String key) throws Exception {
		// 配置文件名为 文件路径 + MD5
		String pathConfig = getConfigPath(filePath);
		File configFile = new File(pathConfig);
		if (!configFile.exists()) {
			throw new Exception("Cache file is not exist");
		}
		String content = CacheFileUtils.readFileToString(pathConfig);
		if (content == null) {
			throw new IllegalArgumentException("Load cache file failed");
		}
		JSONObject json = new JSONObject(content);
		return json.optString(key);
	}

	static void clearMark(String filePath, String key) throws Exception {
		// 配置文件名为 文件路径 + MD5
		String pathConfig = getConfigPath(filePath);
		File configFile = new File(pathConfig);
		if (!configFile.exists()) {
			throw new Exception("Cache file is not exist");
		}
		String content = CacheFileUtils.readFileToString(pathConfig);
		if (content == null) {
			throw new IllegalArgumentException("Load cache file failed");
		}
		JSONObject json = new JSONObject(content);
		json.remove(key);
	}

	static boolean isConfigExist(String filePath) {
		return CacheFileUtils.isFileExist(getConfigPath(filePath));
	}

	static void clearConfigFile(String filePath) {
		String configPath = getConfigPath(filePath);
		File configFile = new File(configPath);
		if (configFile.exists()) {
			configFile.delete();
		}
	}
}
