package com.hzecool.fdn.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.hzecool.fdn.Constant;
import com.hzecool.fdn.FdnCommonLogger;
import com.hzecool.fdn.utils.converter.ByteUtils;
import com.hzecool.fdn.utils.generator.RandomGenerator;

public class SecurityUtil {
	public static final String ALG_MD5 = "MD5";
	public static final String ALG_SHA_1 = "SHA-1";
	
	/**
	 * 用algorithm加密缺省编码的origin成字符串
	 *  
	 * @author fengdg  
	 * @param origin
	 * @param algorithm
	 * @return
	 */
	public static String encode(String origin, String algorithm) {
		return encode(origin, null, algorithm);
	}
	
	/**
	 * 用algorithm加密charsetname编码的origin成字符串
	 *  
	 * @author fengdg  
	 * @param origin
	 * @param charsetName
	 * @param algorithm
	 * @return
	 */
	public static String encode(String origin, String charsetName, String algorithm) {
		byte[] digest = encodeToBytes(origin, charsetName, algorithm);
		return byteToHex(digest);
	}
	
	/**
	 * 用algorithm加密bytes
	 *  
	 * @author fengdg  
	 * @param bytes
	 * @param algorithm
	 * @return
	 */
	public static String encode(byte[] bytes, String algorithm) {
		byte[] digest =  encodeToBytes(bytes, algorithm);
		return byteToHex(digest);
	}

	/**
	 * 用algorithm加密缺省编码的origin成byte数组
	 *  
	 * @author fengdg  
	 * @param origin
	 * @param algorithm
	 * @return
	 */
	public static byte[] encodeToBytes(String origin, String algorithm) {
		return encodeToBytes(origin, null, algorithm);
	}
	
	/**
	 * 用algorithm加密charsetname编码的origin成byte数组
	 *  
	 * @author fengdg  
	 * @param origin	待加密字符串
	 * @param charsetName	字符集
	 * @param algorithm	加密算法
	 * @return
	 */
	public static byte[] encodeToBytes(String origin, String charsetName, String algorithm) {
		try {
			byte[] bytes = StringUtils.isEmpty(charsetName) ? origin.getBytes() : origin.getBytes(charsetName);
			return encodeToBytes(bytes, algorithm);
		} catch (UnsupportedEncodingException e) {
			FdnCommonLogger.log.error("加密出错："+ e.getMessage());
		}
		return null;
	}
	
	/**
	 * 用algorithm加密bytes
	 *  
	 * @author fengdg  
	 * @param bytes
	 * @param algorithm
	 * @return
	 */
	public static byte[] encodeToBytes(byte[] bytes, String algorithm) {
		try {
			MessageDigest crypt = MessageDigest.getInstance(algorithm);
			crypt.reset();
			return crypt.digest(bytes);
		} catch (NoSuchAlgorithmException e) {
			FdnCommonLogger.log.error("加密出错："+ e.getMessage());
		}
		return null;
	}
	
	public static String byteToHex(final byte[] hash) {
		return ByteUtils.bytesToHexStr(hash);
	}

	public static String encodeSHA(String orginal) {
		return encode(orginal, Constant.UTF8, ALG_SHA_1);
//		// 注意这里参数名必须全部小写，且必须有序
//		String signature = "";
//		try
//		{
//			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
//			crypt.reset();
//			crypt.update(orginal.getBytes("UTF-8"));
//			signature = byteToHex(crypt.digest());
//		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//			FdnCommonLogger.log.error("加密出错："+ e.getMessage());
//		}
//		return signature;
	}
	
	/**
	 * 商陆花原有加密算法
	 * from DlKeyHandler#getEncryptedPass(pass)
	 *  
	 * @author fengdg  
	 * @param userPass
	 * @return
	 * @throws Exception
	 */
	public static String getEncryptedPass(String userPass) {
		byte[] b =  encodeToBytes(userPass, ALG_SHA_1);
		return Base64.encodeBase64String(b);
//		String signature = userPass;
//		try {
//			MessageDigest alg = MessageDigest.getInstance("SHA-1");
//			alg.update(userPass.getBytes());
//			byte[] b = alg.digest();
//			signature = Base64.encodeBase64String(b);
//		} catch (NoSuchAlgorithmException e) {
//			FdnCommonLogger.log.error("加密出错："+ e.getMessage());
//		}
//		return signature;
	}
	
	public static void main(String[] args) throws Exception {
//		System.err.println(encodeSHA("000000"));
		String pass = RandomGenerator.getRandomStringByLength(10);
		System.err.println("pass:" + pass);
		System.err.println("encryptedPass:" + getEncryptedPass(pass));
	}
}
