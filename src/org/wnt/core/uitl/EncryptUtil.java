
/**   
 * Copyright © 2016 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: EncryptUtil.java 
 * @Prject: USM
 * @Package: org.wnt.core.uitl 
 * @Description: 与前台解密加密的工具类
 * @author: jfQiao   
 * @date: 2016年11月30日 下午5:08:19 
 * @version: V1.0   
 */
package org.wnt.core.uitl;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;


/**
 * @ClassName: EncryptUtil
 * @Description: 与前台解密加密的工具类
 * @author: jfQiao
 * @date: 2016年11月30日 下午5:08:19
 */
public class EncryptUtil {

	private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

	private static String base64Encode(byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}

	private static byte[] base64Decode(String base64Code) throws Exception {
		return new BASE64Decoder().decodeBuffer(base64Code);
	}

	/**
	 * 
	 * @Title: aesEncryptToBytes
	 * @Description:
	 * @param content
	 * @param encryptKey
	 * @return
	 * @return: byte[]
	 */
	public static byte[] aesEncryptToBytes(String content, String encryptKey) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128);
			Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
			return cipher.doFinal(content.getBytes("utf-8"));
		} catch (Exception e) {
			LogUtil.info("", e);
		}
		return null;
	}

	/**
	 * 
	 * @Title: aesEncrypt
	 * @Description:
	 * @param content
	 * @param encryptKey
	 * @return
	 * @return: String
	 */
	public static String aesEncrypt(String content, String encryptKey) {
		return base64Encode(aesEncryptToBytes(content, encryptKey));
	}

	/**
	 * 
	 * @Title: aesDecryptByBytes
	 * @Description:
	 * @param encryptBytes
	 * @param decryptKey
	 * @return
	 * @return: String
	 */
	public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128);
			Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
			byte[] decryptBytes = cipher.doFinal(encryptBytes);
			return new String(decryptBytes);
		} catch (Exception e) {
			LogUtil.info("", e);
		}
		return null;
	}

	/**
	 * 
	 * @Title: aesDecrypt
	 * @Description:
	 * @param encryptStr
	 * @param decryptKey
	 * @return
	 * @return: String
	 */
	public static String aesDecrypt(String encryptStr, String decryptKey) {
		try {
			return aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
		} catch (Exception e) {
			LogUtil.info("", e);
		}
		return null;
	}
	
	public static void main(String[] args){
		String name = "admin";
		String aesName = aesEncrypt(name,"bbb9bfcd5aa395c8");
		System.out.println("加密后数据："+aesName);
		
		
		String decryName = aesDecrypt(aesName,"bbb9bfcd5aa395c8");
		System.out.println("解码后数据："+decryName);
	}

}
