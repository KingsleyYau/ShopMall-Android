package lbs.goodplace.com.manage.imagemanage;

import android.graphics.Bitmap;
/**
 * 
 * <br>类描述:图片缓存的接口
 * <br>功能详细描述:
 * 
 * @author  wangzhuobin
 * @date  [2012-11-30]
 */
public interface IImageCache {
	public void set(String key, Bitmap value);
	public Bitmap get(String key);
	public void remove(String key);
	public void recycle(String key);
	public void clear();
}
