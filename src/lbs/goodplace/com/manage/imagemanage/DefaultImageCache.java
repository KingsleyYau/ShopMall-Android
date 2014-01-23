package lbs.goodplace.com.manage.imagemanage;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.text.TextUtils;

/**
 * 图片缓存管理类
 * @author wangzhuobin
 *
 */
public class DefaultImageCache implements IImageCache {

	private ConcurrentHashMap<Integer, SoftReference<Bitmap>> mImageCache = new ConcurrentHashMap<Integer, SoftReference<Bitmap>>();

	public DefaultImageCache() {
	}

	/**
	 * 设置一个键值
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, Bitmap value) {
		if (key == null || value == null || mImageCache == null) {
			return;
		}
		int hashcode = key.hashCode();
		if (mImageCache.containsKey(hashcode) && mImageCache.get(hashcode).get() != null) {
			return;
		} else {
			mImageCache.put(hashcode, new SoftReference<Bitmap>(value));
		}
	}

	/**
	 * 获取值，没有则返回空
	 * 
	 * @param key
	 * @return
	 */
	public Bitmap get(String key) {
		if (key == null || mImageCache == null) {
			return null;
		}
		int hashcode = key.hashCode();
		if (mImageCache.containsKey(hashcode)) {
			/**
			 * 这里判断这个key是不是一个死映射，因为bitmap可能已经被回收，是的话删除该key
			 */
			Bitmap bm = mImageCache.get(hashcode).get();
			if (bm == null) {
				mImageCache.remove(hashcode);
				return null;
			}
			return bm;
		}
		return null;
	}

	public int size() {
		if (mImageCache == null) {
			return -1;
		}
		return mImageCache.size();
	}

	public void clear() {
		if (mImageCache != null) {
			mImageCache.clear();
		}
	}

	@Override
	public void remove(String key) {
		if (mImageCache != null && key != null) {
			int hashcode = key.hashCode();
			if (mImageCache.containsKey(hashcode)) {
				mImageCache.remove(hashcode);
			}
		}
	}

	@Override
	public void recycle(String key) {
		if (TextUtils.isEmpty(key)) {
			return;
		}
		int hashcode = key.hashCode();
		Bitmap bitmap = null;
		SoftReference<Bitmap> softReference = mImageCache.remove(hashcode);
		if (softReference == null) {
			return;
		}
		bitmap = softReference.get();
		if (bitmap == null) {
			return;
		}
		if (!bitmap.isRecycled()) {
			bitmap.recycle();
		}
		bitmap = null;
	}
}
