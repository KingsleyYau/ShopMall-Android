/*
 * 文 件 名:  ZipCompress.java
 * 版    权:  3G
 * 描    述:  <描述>
 * 修 改 人:  liguoliang
 * 修改时间:  2012-9-26
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
 * @date  [2012-9-26]
 */
public class ZipCompress implements ICompress {

	@Override
	public byte[] compress(byte[] src) {
		byte[] dst = null;
		if (src != null) {
			try {
				dst = ZipUtils.gzip(src);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dst;
	}

	@Override
	public byte[] decompress(byte[] src) {
		byte[] dst = null;
		if (src != null) {
			try {
				dst = ZipUtils.ungzip(src);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dst;
	}
}
