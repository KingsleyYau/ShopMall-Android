package lbs.goodplace.com.View.contant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.View.main.Contants;
import lbs.goodplace.com.manage.util.BitmapUtil;
import lbs.goodplace.com.manage.util.FileUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 上传图片的处理类
 * 
 * @author zhoujun
 * 
 */
public class ImageUntil {

	/**
	 * 超过此值的图片需要压缩
	 */
	public static final long IMAGE_COMPRESS_SIZE = 100 * 1024;

	/**
	 * 超过此值的图片 需要缩放后再压缩
	 */
	public static final long IMAGE_SCALE_SIZE = 500 * 1024;

	/**
	 * 图片缩放后的最大宽度
	 */
	public static final int IMAGE_MAX_WIDTH = 1024;

	/**
	 * 图片缩放后的最大高度
	 */
	public static final int IMAGE_MAX_HEIGHT = 720;

	/**
	 * 图片压缩后的质量
	 */
	public static final int IMAGE_COMPRESS_QUALITY = 80;

	/**
	 * 压缩要上传的图片
	 * 
	 * @param filePathList
	 * @return
	 */
	public static ArrayList<String> compressImage(List<String> filePathList) {
		if (filePathList != null && filePathList.size() > 0) {
			int size = filePathList.size();
			ArrayList<String> compressList = new ArrayList<String>(size);
			for (int i = 0; i < size; i++) {
				String filePath = compressImage(filePathList.get(i));
				if (filePath != null) {
					compressList.add(filePath);
				}
			}
			return compressList;
		}
		return null;
	}

	private static String compressImage(String filePath) {
//		Log.i("lch", "compressImage:" + filePath);
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		long length = file.length();
		if (needToCompress(length)) {
			Bitmap bitmap = null;
			if (length > IMAGE_SCALE_SIZE) {
				// 需要压缩比例
				bitmap = BitmapUtil.compressBitmap(filePath, IMAGE_MAX_WIDTH, IMAGE_MAX_HEIGHT);
			} else {
				bitmap = decodeFile(filePath, null);
			}

			if (bitmap != null) {
				String tempPath = Contants.Path.IMAGE_COMPRESS_CACHE_PATH + file.getName();
				compressAndSave(bitmap, tempPath);
				bitmap.recycle();
				return tempPath;
			}
		}
		return filePath;
	}
	
	
	public static void compressImage(String filePath,String savePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return ;
		}
		
		long length = file.length();
		if (needToCompress(length)) {
			Bitmap bitmap = null;
			if (length > IMAGE_SCALE_SIZE) {
				// 需要压缩比例
				bitmap = BitmapUtil.compressBitmap(filePath, IMAGE_MAX_WIDTH, IMAGE_MAX_HEIGHT);
			} else {
				bitmap = decodeFile(filePath, null);
			}

			if (bitmap != null) {
				compressAndSave(bitmap, savePath);
				bitmap.recycle();
			}
		}else {
			Bitmap bitmap = decodeFile(filePath, null);
			compressAndSave(bitmap, savePath);
			bitmap.recycle();
		}
	}
	
	public static void compressImageSamll(String filePath,String savePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return ;
		}
		
//		long length = file.length();
//		if (needToCompress(length)) {
			Bitmap bitmap = null;
//			if (length > IMAGE_SCALE_SIZE) {
//				// 需要压缩比例
				bitmap = BitmapUtil.compressBitmap(filePath, 128, 128);
//			} else {
//				bitmap = decodeFile(filePath, null);
//			}

			if (bitmap != null) {
				compressAndSave(bitmap, savePath);
				bitmap.recycle();
			}
//		}else {
//			Bitmap bitmap = decodeFile(filePath, null);
//			compressAndSave(bitmap, savePath);
//			bitmap.recycle();
//		}
	}
	

	/**
	 * 判断图片是否需要压缩
	 * 
	 * @param length
	 * @return
	 */
	private static boolean needToCompress(long length) {
		if (length > IMAGE_COMPRESS_SIZE) {
			// 压缩图片
			return true;
		}
		return false;
	}

	/**
	 * 按jpeg的格式，压缩图片并保存在本地的临时文件中
	 * 
	 * @param bitmap
	 * @param name
	 */
	public static void compressAndSave(Bitmap bitmap, String name) {
		File file = FileUtil.createNewFile(name, false);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_COMPRESS_QUALITY, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Bitmap decodeFile(String filePath, BitmapFactory.Options options) {
		if (filePath == null || "".equals(filePath)) {
			return null;
		}
		try {
			return BitmapFactory.decodeFile(filePath, options);
		} catch (OutOfMemoryError ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
