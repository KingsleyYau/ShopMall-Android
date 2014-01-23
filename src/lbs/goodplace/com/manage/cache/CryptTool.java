package lbs.goodplace.com.manage.cache;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * 加解密工具类
 * 
 * @author yuxiaowei
 * 
 */
public class CryptTool {
	public static byte[] xor(byte[] bytes, byte key) {

		byte[] result = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			result[i] = (byte) ((bytes[i]) ^ key);
		}
		return result;
	}

	/**
	 * 循环异或，一般用于加密
	 * 
	 * @param bytes
	 * @param key
	 * @return
	 */
	public static byte[] xor(byte[] bytes, byte[] key) {
		byte[] result = bytes;
		for (int i = 0; i < key.length; i++) {
			result = xor(result, key[i]);
		}
		return result;
	}

	/**
	 * 加密 ：使用异或加密后采用base64编码返回字符串
	 * 
	 * @param src
	 * @param key
	 * @return
	 */
	public static String encrypt(String src, String key) {
		try {
			byte[] b = xor(src.getBytes("utf-8"), key.getBytes("utf-8"));
			// BASE64Encoder encoder = new BASE64Encoder();
			// return encoder.encode(b);
			return Base64.encodeBytes(b);
			// return new String(b,"utf-8");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解密 ：使用异或解密后采用utf8编码返回字符串
	 * 
	 * @param src
	 * @param key
	 * @return
	 */
	public static String decrypt(String src, String key) {
		try {
			// BASE64Decoder decoder = new BASE64Decoder();
			// byte[] b=xor(decoder.decodeBuffer(src), key.getBytes("utf-8"));
			byte[] b = xor(Base64.decode(src), key.getBytes("utf-8"));
			return new String(b, "utf-8");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 产生MD5加密串
	 * 
	 * @author huyong
	 * @param plainText
	 * @param charset
	 * @return
	 */
	private static String to32BitString(String plainText, String charset) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			if (charset != null) {
				md.update(plainText.getBytes(charset));
			} else {
				md.update(plainText.getBytes());
			}
			byte[] byteArray = md.digest();
			StringBuffer md5Buf = new StringBuffer();
			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
					md5Buf.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
				} else {
					md5Buf.append(Integer.toHexString(0xFF & byteArray[i]));
				}
			}
			return md5Buf.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 产生32位md5加密字符串
	 * 
	 * @param s
	 *            待加密的字符串
	 * @return md5加密处理后的字符串
	 */
	public final static String mD5generator(String s) {
		// String charset =System.getProperties()
		return to32BitString(s, null);
	}

	public static String decryptFileToString(InputStream in, String key) {
		StringBuffer buf = new StringBuffer();
		BufferedReader breader = null;
		try {
			breader = new BufferedReader(new InputStreamReader(in, Charset.forName("utf-8")));
			while (breader.ready()) {
				buf.append((char) breader.read());
			}
			breader.close();
			return decrypt(buf.toString(), key);
		} catch (Exception e) {
			e.printStackTrace();
		}// catch
		return buf.toString();
	}
}
