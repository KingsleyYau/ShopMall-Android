/*
 * 文 件 名:  LauncherCrypt.java
 * 版    权:  3G
 * 描    述:  <描述>
 * 修 改 人:  liguoliang
 * 修改时间:  2012-9-25
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package lbs.goodplace.com.manage.cache;

import java.util.Random;

/**
 * <br>类描述:桌面的加密方法
 * <br>功能详细描述:
 * 
 * @author  liguoliang
 * @date  [2012-9-25]
 */
public class XORCrypt implements IEncrypt {
	private String mRandomKey = null;

	private static final int RANDOM_KEY_LENGTH = 16;

	//	public static final String STATISTICS_DATA_ENCRYPT_KEY = "ABEN201209301830"; // 加密密钥(ABEN的国庆放假时间,哈哈)
	public static final byte[] ENCRYPT_KEY = new byte[] { 65, 66, 69, 78, 50, 48, 49, 50, 48, 57,
			51, 48, 49, 56, 51, 48 };

	/**
	 * 产生随机字符串
	 * */
	public static final String randomString(int length) {
		Random randGen = null;
		char[] numbersAndLetters = null;
		if (length < 1) {
			return null;
		}
		if (randGen == null) {
			randGen = new Random();
			numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
					+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	@Override
	public byte[] encrypt(byte[] src) {
		return CryptTool.xor(src, ENCRYPT_KEY);
	}

	@Override
	public byte[] decrypt(byte[] src) {
		return CryptTool.xor(src, ENCRYPT_KEY);
	}
}
