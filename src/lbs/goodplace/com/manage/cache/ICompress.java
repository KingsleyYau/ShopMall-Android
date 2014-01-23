package lbs.goodplace.com.manage.cache;

/**
 * <br>类描述:压缩接口
 * <br>功能详细描述:
 * 
 * @author  liguoliang
 * @date  [2012-9-25]
 */
public interface ICompress {
	/** <br>功能简述:压缩
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param src	原数据
	 * @return
	 */
	byte[] compress(byte[] src);
	
	/** <br>功能简述:解压
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param src
	 * @return
	 */
	byte[] decompress(byte[] src);
}
