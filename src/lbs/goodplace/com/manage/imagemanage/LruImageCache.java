package lbs.goodplace.com.manage.imagemanage;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.text.TextUtils;
/**
 * 
 * <br>类描述:LRU图片缓存器
 * <br>功能详细描述:
 * 
 * @author  wangzhuobin
 * @date  [2012-11-30]
 */
public class LruImageCache implements IImageCache {
	/**
	 * 强引用图片缓存的大小，5M
	 */
	private int mMaxMemorySize = 5 * 1024 * 1024;

	/**
	 * 强引用缓存，线程安全
	 * 当缓存超过限定大小时，该缓存会把最久没有使用的图片从缓存中移除，直到小于限制值为止
	 */
	private LruCache<String, Bitmap> mLruCache = null;

	/**
	 * 弱引用缓存
	 */
	private ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();

	public LruImageCache(int maxMemorySize) {
		if (maxMemorySize > 0) {
			mMaxMemorySize = maxMemorySize;
		}
		mLruCache = new LruCache<String, Bitmap>(mMaxMemorySize) {

			@Override
			protected void entryRemoved(boolean evicted, String key, Bitmap oldValue,
					Bitmap newValue) {
				//如果超过了大小，就把从强引用移除的图片加入到弱引用中
				if (evicted) {
					mSoftCache.put(key, new SoftReference<Bitmap>(oldValue));
				}
			}

			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}

		};
	}

	/**
	 * 设置一个键值
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, Bitmap value) {
		if (key == null || value == null) {
			return;
		}
		if (mLruCache != null) {
			mLruCache.put(key, value);
		}
	}

	/**
	 * 获取值，没有则返回空
	 * 
	 * @param key
	 * @return
	 */
	public Bitmap get(String key) {
		if (key == null) {
			return null;
		}
		//先从强引用缓存中取，如果取不到的话，再从弱引用缓存里面取
		Bitmap bitmap = mLruCache.get(key);
		if (bitmap == null) {
			SoftReference<Bitmap> softReference = mSoftCache.get(key);
			if (softReference != null) {
				bitmap = softReference.get();
			}
		}
		return bitmap;
	}

	public void clear() {
		mLruCache.evictAll();
		mSoftCache.clear();
	}

	@Override
	public void remove(String key) {
		if (key == null) {
			return;
		}
		mLruCache.remove(key);
		mSoftCache.remove(key);
	}

	public int getMaxMemorySize() {
		return mMaxMemorySize;
	}

	@Override
	public void recycle(String key) {
		if (TextUtils.isEmpty(key)) {
			return;
		}
		Bitmap bitmap = mLruCache.remove(key);
		if (bitmap == null) {
			SoftReference<Bitmap> softReference = mSoftCache.remove(key);
			if (softReference == null) {
				return;
			}
			bitmap = softReference.get();
		}
		if (bitmap == null) {
			return;
		}
		if (!bitmap.isRecycled()) {
			bitmap.recycle();
		}
		bitmap = null;
	}
}
