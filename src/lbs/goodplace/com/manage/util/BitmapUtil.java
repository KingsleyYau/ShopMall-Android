package lbs.goodplace.com.manage.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * bitmap的操作类
 * 
 * @author zhoujun
 * 
 */
public class BitmapUtil {

	/**
	 * 将drawable 专程bitmap
	 * 
	 * @param context
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Context context, Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 压缩图片到指定大小返回
	 * 
	 * @param pathName
	 * @param maxWidth
	 * @param maxHeight
	 * @return null or thumbs bitmap
	 */
	public static Bitmap compressBitmap(String pathName, int maxWidth, int maxHeight) {
		// 获取源图片的大小
		Bitmap bm = null;
		try {
			BitmapFactory.Options opts = createOptions(pathName, maxWidth, maxHeight);
			// 获取缩放后图片
			return BitmapFactory.decodeFile(pathName, opts);

		} catch (Exception e) {
			// showAlert("CreateFile" 0 e.toString() "OK" false);
			Log.e("error", "Compress BitMap failed! " + e.getMessage());
		} catch (OutOfMemoryError ex) {
			ex.printStackTrace();
		}

		return bm;
	}

	/**
	 * 按指定图片大小返回压缩参数
	 * 
	 * @param pathName
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static BitmapFactory.Options createOptions(String pathName, int maxWidth, int maxHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		// 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
		BitmapFactory.decodeFile(pathName, opts);

		int srcWidth = opts.outWidth;
		int srcHeight = opts.outHeight;
		int destWidth = 0;
		int destHeight = 0;
		// 缩放的比例
		double ratio = 0.0;
		// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
		if (srcWidth > srcHeight) {
			ratio = srcWidth / maxWidth;
			destWidth = maxWidth;
			destHeight = (int) (srcHeight / ratio);
		} else {
			ratio = srcHeight / maxHeight;
			destHeight = maxHeight;
			destWidth = (int) (srcWidth / ratio);
		}
		// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
		newOpts.inSampleSize = (int) ratio + 1;
		// inJustDecodeBounds设为false表示把图片读进内存中
		newOpts.inJustDecodeBounds = false;
		// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
		newOpts.outHeight = destHeight;
		newOpts.outWidth = destWidth;
		return newOpts;
	}
}
