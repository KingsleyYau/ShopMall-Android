package lbs.goodplace.com.manage.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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
import java.util.zip.GZIPInputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

/**
 * 文件管理类
 * 
 * @author zhoujun
 * 
 */
public class FileUtil {
	public final static String SDCARD = Environment.getExternalStorageDirectory().getPath();
	public static final String ICON_CACHE_PATH = SDCARD + "/goodplaceImgs/iamgeCache1/";

	/**
	 * sd卡是否可读写
	 * 
	 * @return
	 */
	public static boolean isSDCardAvaiable() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * 保存位图到sd卡目录下
	 * 
	 * @param bitmap
	 *            ：位图资源
	 * @param filePathName
	 *            ：待保存的文件完整路径名
	 * @param iconFormat
	 *            ：图片格式
	 * @return true for 保存成功，false for 保存失败。
	 */
	public static boolean saveBitmapToSDFile(final Bitmap bitmap,
			final String filePathName, CompressFormat iconFormat) {
		boolean result = false;
		try {
			createNewFile(filePathName, false);
			OutputStream outputStream = new FileOutputStream(filePathName);
			result = bitmap.compress(iconFormat, 100, outputStream);
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 
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
			} else {
				// 不存在，则删除带png后缀名的文件
				File prePngFile = new File(path + ".png");
				if (prePngFile != null && prePngFile.exists()) {
					prePngFile.delete();
				}
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

	public static int copy(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[1024 * 4];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static byte[] ungzip(byte[] bs) throws Exception {
		GZIPInputStream gzin = null;
		ByteArrayInputStream bin = null;
		try {
			bin = new ByteArrayInputStream(bs);
			gzin = new GZIPInputStream(bin);
			return toByteArray(gzin);
		} catch (Exception e) {
			throw e;
		} finally {
			if (bin != null) {
				bin.close();
			}
		}
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	public static String unzipData(InputStream inStream) {
		try {
			byte[] old_bytes = toByteArray(inStream);
			byte[] new_bytes = ungzip(old_bytes);
			return new String(new_bytes, "utf-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 将字符串转成UTF字节
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] toUtfByte(String str) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(output);
		byte[] postData = null;
		try {
			out.writeUTF(str);
			postData = output.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return postData;
	}

	public static String getFileSuffix(String fileName) {
		if (fileName != null && !"".equals(fileName)) {
			int index = fileName.indexOf(".");
			return index == -1 ? fileName : fileName.substring(index);
		}
		return fileName;
	}
	
	
	/**  
     *   
     * @param fromFile 被复制的文件  
     * @param toFile 复制的目录文件  
     * @param rewrite 是否重新创建文件  
     *   
     * <p>文件的复制操作方法  
     */  
    public static boolean copyFile(File fromFile, File toFile,Boolean rewrite ){  
          
        if(!fromFile.exists() || !fromFile.isFile() || !fromFile.canRead()){  
            return false;  
        }  
        
        if(!toFile.getParentFile().exists()){  
            toFile.getParentFile().mkdirs();  
        }  
        if(toFile.exists() && rewrite){  
            toFile.delete();  
        }  
          
          
        try {  
            FileInputStream fosfrom = new FileInputStream(fromFile);  
            FileOutputStream fosto = new FileOutputStream(toFile);  
              
            byte[] bt = new byte[1024];  
            int c;  
            while((c=fosfrom.read(bt)) > 0){  
                fosto.write(bt,0,c);  
            }  
            //关闭输入、输出流  
            fosfrom.close();  
            fosto.close();  
            return true;  
              
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return false;  
    }  
}
