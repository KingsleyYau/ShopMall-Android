package lbs.goodplace.com.manage.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 字符加密工具类
 * @author zhaojunjie
 *
 */
public class Encryption {
	public static String toMd5(String str) {
		MessageDigest algorithm = null;
		try {
			algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(str.getBytes("UTF-8"));
		} 
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace(); 
		}
		return toHexString(algorithm.digest(), ""); 
	}
	public static String toMd5(byte[] srcByte) {
		  MessageDigest algorithm = null;
		  try {
			  algorithm = MessageDigest.getInstance("MD5");
			  algorithm.reset();
			  algorithm.update(srcByte);
			  } 
		  catch (NoSuchAlgorithmException e) {			  
			  throw new RuntimeException(e);
        }
        return toHexString(algorithm.digest(), "");  
	}
	public static String toMd5(File file) {
		  MessageDigest algorithm = null;
		  ByteArrayOutputStream out = new ByteArrayOutputStream();			 
		  try {
			FileInputStream input = new FileInputStream(file);
			byte[] srcByte = new byte[4 * 1024];	
			int byteRead;
			while(-1 !=(byteRead= input.read(srcByte))){
				out.write(srcByte, 0, byteRead);
			}						
		  }catch (FileNotFoundException e1) {			  
		  }
		  catch (IOException e) {
		  }
		  try {
			  algorithm = MessageDigest.getInstance("MD5");
			  algorithm.reset();
			  algorithm.update(out.toByteArray());
			  } 
		  catch (NoSuchAlgorithmException e) {			  
			  throw new RuntimeException(e);
		  }
		  
      return toHexString(algorithm.digest(), "");  
	}
	private static String toHexString(byte[] bytes, String separator) {
          StringBuilder hexString = new StringBuilder();
          for (byte b : bytes) {
        	  // 4 example : 00001111 is needed to be fill zero 
        	  if ((b & 0xFF) < 0x10){
        		  hexString.append("0");
        	  }
        	  hexString.append(Integer.toHexString(0xFF & b)).append(separator);
          }
          return hexString.toString();
    }

}
