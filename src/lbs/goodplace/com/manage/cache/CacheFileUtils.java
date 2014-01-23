/*
 * 文 件 名:  CacheFileUtils.java
 * 版    权:  3G
 * 描    述:  <描述>
 * 修 改 人:  liguoliang
 * 修改时间:  2012-9-26
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package lbs.goodplace.com.manage.cache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import android.os.Environment;
import android.text.TextUtils;

/**
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  liguoliang
 * @date  [2012-9-26]
 */
public class CacheFileUtils {
	/**
	 * 是否存在SDCard
	 * 
	 * @author chenguanyu
	 * @return
	 */
	public static boolean isSDCardExist() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getSDPath() {
		return Environment.getExternalStorageDirectory().toString();
	}

	public static String readFileToString(String filePath) {
		if (filePath == null || "".equals(filePath)) {
			return null;
		}
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			return readToString(inputStream, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return null;
	}

	public static byte[] readFileToByte(String filePath) throws Exception {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		InputStream inputStream = null;
		inputStream = new FileInputStream(file);
		byte[] data = toByteArray(inputStream);
		return data;
	}

	public static void saveByteToSDFile(final byte[] byteData, final String filePathName)
			throws Exception {
		File newFile = createNewFile(filePathName, false);
		FileOutputStream fileOutputStream = new FileOutputStream(newFile);
		fileOutputStream.write(byteData);
		fileOutputStream.flush();
		fileOutputStream.close();
	}

	/**
	 * 
	 * @author huyong
	 * @param path
	 *            ：文件路径
	 * @param append
	 *            ：若存在是否插入原文件
	 * @return
	 */
	public static File createNewFile(String path, boolean append) {
		File newFile = new File(path);
		if (!append) {
			if (newFile.exists()) {
				newFile.delete();
			}
		}
		if (!newFile.exists()) {
			try {
				File parent = newFile.getParentFile();
				if (parent != null && !parent.exists()) {
					parent.mkdirs();
				}
				newFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return newFile;
	}

	/**
	 * 指定路径文件是否存在
	 * 
	 * @author huyong
	 * @param filePath
	 * @return
	 */
	public static boolean isFileExist(String filePath) {
		boolean result = false;
		if (!TextUtils.isEmpty(filePath)) {
			try {
				File file = new File(filePath);
				result = file.exists();
				file = null;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	public static boolean deleteFile(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return false;
		}
		File file = new File(filePath);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * 
	 * @param inputStream
	 * @param encoding
	 * @return
	 */
	public static String readToString(InputStream inputStream, String encoding) {

		InputStreamReader in = null;
		try {
			StringWriter sw = new StringWriter();
			in = new InputStreamReader(inputStream, encoding);
			copy(in, sw);
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return null;
	}

	private static int copy(Reader input, Writer output) throws IOException {
		char[] buffer = new char[1024 * 4];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static int copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[1024 * 4];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}
}
