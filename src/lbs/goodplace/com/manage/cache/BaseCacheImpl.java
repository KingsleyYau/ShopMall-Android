/*
 * 文 件 名:  AbstractTask.java
 * 版    权:  3G
 * 描    述:  <描述>
 * 修 改 人:  liguoliang
 * 修改时间:  2012-9-25
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package lbs.goodplace.com.manage.cache;


/**
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  liguoliang
 * @date  [2012-9-25]
 */
public abstract class BaseCacheImpl implements ICache, IModuleKey {
	/**
	 * 缓存类型
	 */
	protected int mCacheType = -1;
	
	/**
	 * 压缩接口
	 */
	protected ICompress mCompress;
	
	/**
	 * 加密接口
	 */
	protected IEncrypt mEncrypt;
	
	/**
	 * 缓存事件监听者
	 */
	protected ICacheListener mListener;
	
	/**
	 * Key值是否需要加密,默认为加密
	 */
	protected boolean mIsKeyEncrypt = true;
	
	public static final int CACHE_TYPE_BASE = 100;
	
	public static final int CACHE_TYPE_FILE = 101;
	
	public final static int PROGRESS_SAVE_PARSE_OBJECT_START = 101;
	public final static int PROGRESS_SAVE_PARSE_OBJECT_FINISH = 102;
	public final static int PROGRESS_SAVE_COMPRESS_START = 103;
	public final static int PROGRESS_SAVE_COMPRESS_FINISH = 104;
	public final static int PROGRESS_SAVE_ENCRYPT_START = 105;
	public final static int PROGRESS_SAVE_ENCRYPT_FINISH = 106;
	public final static int PROGRESS_SAVE_CONFIG_START = 107;
	public final static int PROGRESS_SAVE_CONFIG_FINISH = 108;
	public final static int PROGRESS_SAVE_DATA_START = 109;
	public final static int PROGRESS_SAVE_DATAING = 110;
	public final static int PROGRESS_SAVE_DATA_FINISH = 111;
	
	
	public final static int PROGRESS_LOAD_PARSE_OBJECT_START = 201;
	public final static int PROGRESS_LOAD_PARSE_OBJECT_FINISH = 202;
	public final static int PROGRESS_LOAD_DECOMPRESS_START = 203;
	public final static int PROGRESS_LOAD_DECOMPRESS_FINISH = 204;
	public final static int PROGRESS_LOAD_DECRYPT_START = 205;
	public final static int PROGRESS_LOAD_DECRYPT_FINISH = 206;
	public final static int PROGRESS_LOAD_CONFIG_START = 207;
	public final static int PROGRESS_LOAD_CONFIG_FINISH = 208;
	public final static int PROGRESS_LOAD_DATA_START = 209;
	public final static int PROGRESS_LOAD_DATAING = 210;
	public final static int PROGRESS_LOAD_DATA_FINISH = 211;
	
	public int getCacheType() {
		return mCacheType;
	}
	
	protected void setCacheType(int type) {
		this.mCacheType = type;
	}
	
	public boolean getIsCompress() {
		return mCompress == null ? false : true;
	}
	
	public boolean getIsEncrypt() {
		return mEncrypt == null ? false : true;
	}
	
	public void setCompress(ICompress compress) {
		this.mCompress = compress;
	}
	
	public ICompress getCompress() {
		return mCompress;
	}
	
	public void setEncrypt(IEncrypt encrypt) {
		this.mEncrypt = encrypt;
	}
	
	public IEncrypt getEncrypt() {
		return mEncrypt;
	}
	
	public void setCacheListener(ICacheListener listener) {
		this.mListener = listener;
	}
	
	public ICacheListener getCacheListener() {
		return mListener;
	}
}
