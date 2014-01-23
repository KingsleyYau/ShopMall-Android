/*
 * 文 件 名:  IEncrypt.java
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
 * <br>类描述:加密解密接口
 * <br>功能详细描述:
 * 
 * @author  liguoliang
 * @date  [2012-9-25]
 */
public interface IEncrypt {
	byte[] encrypt(byte[] src);
	
	byte[] decrypt(byte[] src);
}
