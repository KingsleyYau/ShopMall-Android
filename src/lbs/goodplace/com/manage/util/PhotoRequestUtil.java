package lbs.goodplace.com.manage.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

/**
 * 调用系统图库和照相机的工具类
 * 
 * @author zhoujun
 * 
 */
public class PhotoRequestUtil {

	/**
	 * 调用系统相机
	 */
	public static final int TAKE_PHOTO_REQUEST_CODE = 1;

	/**
	 * 调用系统图库
	 */
	public static final int SELECT_PHOTO_REQUEST_CODE = 2;

	/**
	 * 对选择后的图片进行处理
	 */
	public static final int CUT_PHOTO_REQUEST_CODE = 3;

	/**
	 * 相机拍照后的照片存储的路径
	 */
	private static final String PATH_TEST = GoGameEnv.Path.SEND_DATA_TEMP_PATH;

	/**
	 * 获取图片后，对图片进行裁剪
	 * 
	 * @param activity
	 * @param uri
	 */
	public static void cutPhotoRequest(Activity activity, Uri uri, int outputX,
			int outputY) {
		final Intent intent1 = new Intent("com.android.camera.action.CROP");
		intent1.setDataAndType(uri, "image/*");
		intent1.putExtra("crop", "true");
		intent1.putExtra("aspectX", 1);
		intent1.putExtra("aspectY", 1);
		intent1.putExtra("outputX", outputX);
		intent1.putExtra("outputY", outputY);
		intent1.putExtra("return-data", true);
		// 系统的裁剪图片默认对图片进行人脸识别，当识别到有人脸时，会按aspectX和aspectY为1来处理，如果想设置成自定义的裁剪比例，需要设置noFaceDetection为true
		// intent1.putExtra("noFaceDetection", true); //取消人脸识别功能。
		activity.startActivityForResult(intent1, CUT_PHOTO_REQUEST_CODE);
	}

	/**
	 * 调用系统相机
	 * 
	 * @param activity
	 * @return 返回拍照后 图片的存储路径
	 */
	public static String takePhotoRequest(Activity activity) {
		// 调用系统拍照
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File myImageDir = new File(PATH_TEST);

		// 创建图片保存目录
		if (!myImageDir.exists()) {
			myImageDir.mkdirs();
		}
		String filePath = PATH_TEST + System.currentTimeMillis() + ".jpg";
		// 根据时间来命名
		File imagFile = FileUtil.createNewFile(filePath, false);
		Uri mTmpuri = Uri.fromFile(imagFile);
		i.putExtra(MediaStore.EXTRA_OUTPUT, mTmpuri);
		activity.startActivityForResult(i, TAKE_PHOTO_REQUEST_CODE);
		return filePath;
	}

	/**
	 * 调用系统图库
	 * 
	 * @param activity
	 */
	public static void selectPhotoRequest(Activity activity) {
		// 从图库选择图片
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
		innerIntent.setType("image/*"); // 查看类型
										// 详细的类型在com.google.android.mms.ContentType中
		Intent wrapperIntent = Intent.createChooser(innerIntent, null);
		activity.startActivityForResult(wrapperIntent,
				SELECT_PHOTO_REQUEST_CODE);
	}

	/**
	 * 获取系统图库选择图片的路径
	 * 
	 * @param activity
	 * @param uri
	 * @return
	 */
	public static String getSelectPhotoPath(Activity activity, Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		// 好像是android多媒体数据库的封装接口，具体的看Android文档
		Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
		String path = null;
		if (cursor != null) {
			// 是获得用户选择的图片的索引值
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			// 将光标移至开头 ，这个很重要，不小心很容易引起越界
			cursor.moveToFirst();
			// 最后根据索引值获取图片路径
			path = cursor.getString(column_index);
			// 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
			if (Integer.parseInt(Build.VERSION.SDK) < 14) {
				cursor.close();
			}
		}
		return path;
	}

	/**
	 * 获取系统图库，所选择的图片
	 * 
	 * @param resolver
	 * @param uri
	 * @return
	 */
	public static Bitmap getPhoto(ContentResolver resolver, Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

}
