package lbs.goodplace.com.manage.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Environment;

/**
 * PreferencesManager 公共类
 * 
 */
public final class PreferencesManager {
	private SharedPreferences mPreferences;
	private Editor mEditor;

	public SharedPreferences getPreferences() {
		return mPreferences;
	}
	/**
	 * 默认构造函数
	 * @name IPreferencesIds.DESK_SHAREPREFERENCES_FILE
	 * @mode MODE_PRIVATE
	 */
	public PreferencesManager(Context context) {
		setPreferences(context, Context.MODE_PRIVATE, IPreferencesIds.GAME_SHAREPREFERENCES_FILE);
	}

	/**
	 * 自定义构造函数
	 */
	public PreferencesManager(Context context, String name, int mode) {
		if (context != null) {
			mPreferences = context.getSharedPreferences(name, mode);
			mEditor = mPreferences.edit();

		}
	}

	public void setPreferences(Context context, int mode, String name) {
		if (name == null) {
			name = IPreferencesIds.GAME_SHAREPREFERENCES_FILE;
		}
		// preferences = activity.getPreferences(mode);
		// else
		mPreferences = context.getSharedPreferences(name, mode);
		mEditor = mPreferences.edit();
	}

	/**
	 * 清除数据
	 */
	public void clear() {
		if (mEditor != null) {
			mEditor.clear().commit();
		} else if (mPreferences != null) {
			mEditor = mPreferences.edit();
			mEditor.clear().commit();
		}
	}

	public Editor edit() {
		if (mEditor != null) {
			return mEditor;
		} else if (mPreferences != null) {
			mPreferences.edit();
		}
		return null;
	}

	public void remove(String key) {
		mPreferences.edit().remove(key).commit();
	}

	public Map<String, ?> getAll() {
		return mPreferences.getAll();
	}

	public boolean contains(String key) {
		return mPreferences.contains(key);
	}

	public boolean getBoolean(String key, boolean defValue) {
		if (mPreferences != null) {
		return mPreferences.getBoolean(key, defValue);
		}
		return defValue;
	}

	public float getFloat(String key, float defValue) {
		if (mPreferences != null) {
			return mPreferences.getFloat(key, defValue);
		}
		return defValue;
	}

	public int getInt(String key, int defValue) {
		if (mPreferences != null) {
			return mPreferences.getInt(key, defValue);
		}
		return defValue;
	}

	public long getLong(String key, long defValue) {
		if (mPreferences != null) {
			return mPreferences.getLong(key, defValue);
		}
		return defValue;
	}
	public String getString(String key, String defValue) {
		if (mPreferences != null) {
			return mPreferences.getString(key, defValue);
		}
		return defValue;
	}

	public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
		mPreferences.registerOnSharedPreferenceChangeListener(listener);
	}

	public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
		mPreferences.unregisterOnSharedPreferenceChangeListener(listener);
	}

	public void putBoolean(String key, boolean b) {
		if (mEditor != null) {
			mEditor.putBoolean(key, b);
		}
	}

	public void putInt(String key, int i) {
		if (mEditor != null) {
			mEditor.putInt(key, i);
		}
	}

	public void putFloat(String key, float f) {
		if (mEditor != null) {
			mEditor.putFloat(key, f);
		}
	}

	public void putLong(String key, long l) {
		if (mEditor != null) {
			mEditor.putLong(key, l);
		}
	}

	public void putString(String key, String s) {
		if (mEditor != null) {
			mEditor.putString(key, s);
		}
	}

	public boolean commit() {
		boolean bRet = false;
		if (mEditor != null) {
			bRet = mEditor.commit();
		}
		return bRet;
	}

	/**
	 * <br>功能简述:备份sharePreference到指定路径
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param context 
	 * @param preferencesName
	 * @param path
	 */
	public static void backUpPreference(Context context, String preferencesName, String path) {
		File sharedPreferenceFile = new File(Environment.getDataDirectory() + "/data/"
				+ context.getPackageName() + "/shared_prefs/" + preferencesName);
		File tmpSPFile = new File(path);
		File backupSPFiles = new File(path + "/" + preferencesName);

		// note:此处为避免因SharedPreferences文件的拷贝出错，而影响到数据库文件的备份与提示工作
		try {
			tmpSPFile.mkdirs();
			// 拷贝SharedPreferences文件
			backupSPFiles.createNewFile();
			copyOutPutFile(sharedPreferenceFile, backupSPFiles, 0); // 不需要加密
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void importSharedPreferences(Context context, String preferencesName) {
		File backupSPFile = new File(GoGameEnv.Path.SDCARD
				+ GoGameEnv.Path.PREFERENCESFILE_PATH + "/" + preferencesName);
		if (backupSPFile == null || !backupSPFile.exists()) {
			// 首先查找新版preferences目录下是否存在，若未找到，则再查找老版DB目录下是否存在SharedPreferences文件
			backupSPFile = new File(GoGameEnv.Path.SDCARD + GoGameEnv.Path.DBFILE_PATH + "/"
					+ preferencesName);
		}
		if (backupSPFile != null && backupSPFile.exists()) {
			File tmpSPFile = new File(Environment.getDataDirectory() + "/data/"
					+ context.getPackageName() + "/shared_prefs/" + preferencesName);
			if (tmpSPFile.exists()) {
				tmpSPFile.delete();
			}
			try {
				tmpSPFile.createNewFile();
				copyInputFile(backupSPFile, tmpSPFile, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void clearPreferences(Context context) {
		if (context != null) {
			for (int i = 0; i < IPreferencesIds.NEED_CLEAR_PREFERENCES.length; i++) {
				String name = IPreferencesIds.NEED_CLEAR_PREFERENCES[i];
				context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().commit();
			}
		}
	}
	
	/**
	 * 
	 * @param src：源文件
	 * @param dst：目标文件
	 * @param encryptbyte：加密字节长度，不需加密，则传入0
	 * @throws IOException
	 */
	public static void copyOutPutFile(File src, File dst, int encryptbyte) throws IOException {
		FileInputStream srcStream = new FileInputStream(src);
		FileOutputStream dstStream = new FileOutputStream(dst);
		FileChannel inChannel = srcStream.getChannel();
		FileChannel outChannel = dstStream.getChannel();
		if (encryptbyte < 0) {
			encryptbyte = 0;
		}
		try {
			inChannel.transferTo(inChannel.size() - encryptbyte, inChannel.size(), outChannel);
			outChannel.transferFrom(inChannel, outChannel.size(), inChannel.size());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			srcStream.close();
			dstStream.close();
		}
	}
	
	/**
	 * 
	 * @param src：源文件
	 * @param dst：目标文件
	 * @param encryptbyte：加密字节数，若不需要加密，直接传入0
	 * @throws IOException
	 */
	public static void copyInputFile(File src, File dst, int encryptbyte) throws IOException {
		FileInputStream srcStream = new FileInputStream(src);
		FileOutputStream dstStream = new FileOutputStream(dst);
		FileChannel inChannel = srcStream.getChannel();
		FileChannel outChannel = dstStream.getChannel();

		if (encryptbyte < 0) {
			encryptbyte = 0;
		}
		try {
			inChannel.transferTo(encryptbyte, inChannel.size(), outChannel);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			srcStream.close();
			dstStream.close();
		}
	}

}
