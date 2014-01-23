package lbs.goodplace.com.manage.util;

import java.io.File;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.main.Contants;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager.AsyncImageLoadedCallBack;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

public class DownImageUitl {

	public static void downImage(final Context context, String imgUrl,String iconCachePath,String savePath){
		String fileName = String.valueOf(imgUrl.hashCode());
		final File downFile = new File(iconCachePath + fileName);
		final File saveFile = new File(savePath + fileName + ".jpg");
		
		
		Bitmap bm = AsyncImageManager.getInstance().loadImage(iconCachePath, fileName,
				imgUrl, true, false, null, new AsyncImageLoadedCallBack() {
					@Override
					public void imageLoaded(Bitmap imageBitmap, String imgUrl) {
						boolean success = FileUtil.copyFile(downFile, saveFile, false);
						if (success) {
							Toast.makeText(context, "下载图片成功，保存在：" + saveFile, Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(context, "下载图片失败！", Toast.LENGTH_SHORT).show();

						}
					
					}
				});
		if (bm != null) {
			boolean success = FileUtil.copyFile(downFile, saveFile, false);
			if (success) {
				Toast.makeText(context, "下载图片成功，保存在：" + saveFile, Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(context, "下载图片失败！", Toast.LENGTH_SHORT).show();

			}
		}
	}
	
	/**
	 * 读取图标，然后设到imageview里
	 */
	public static void setIcon(final ImageView imageView, String imgUrl, String imgPath, String imgName,
			boolean setDefaultIcon) {
		AsyncImageManager mImgManager = AsyncImageManager.getInstance();
		imageView.setTag(imgUrl);
		Bitmap bm = mImgManager.loadImage(imgPath, imgName, imgUrl, true, false, null,
				new AsyncImageLoadedCallBack() {
					@Override
					public void imageLoaded(Bitmap imageBitmap, String imgUrl) {
						if (imageView.getTag().equals(imgUrl)) {
							imageView.setImageBitmap(imageBitmap);
						} else {
							imageBitmap = null;
							imgUrl = null;
						}
					}
				});
		if (bm != null) {
			imageView.setImageBitmap(bm);
		} else {
			if (setDefaultIcon) {
				imageView.setImageResource(R.drawable.no_pic_big);
			} else {
				imageView.setImageDrawable(null);
			}
		}
	}
}
