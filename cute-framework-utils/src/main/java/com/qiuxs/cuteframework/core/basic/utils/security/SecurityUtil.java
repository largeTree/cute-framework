package com.qiuxs.cuteframework.core.basic.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.Constants;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;

public class SecurityUtil {

	private static Logger log = LogManager.getLogger(SecurityUtil.class);

	public static String sha1Hex(String str) {
		return DigestUtils.sha1Hex(str);
	}

	public static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	public static String encodeSHA(String orginal) {
		// 注意这里参数名必须全部小写，且必须有序
		String signature = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(orginal.getBytes(Constants.UTF_8));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			log.error("加密出错：" + e.getMessage());
		}
		return signature;
	}

	/**
	 * 商陆花原有加密算法 from DlKeyHandler#getEncryptedPass(pass)
	 * 
	 * @author fengdg
	 * @param userPass
	 * @return
	 * @throws Exception
	 */
	public static String getEncryptedPass(String userPass) {
		String signature = userPass;
		try {
			MessageDigest alg = MessageDigest.getInstance("SHA-1");
			alg.update(userPass.getBytes());
			byte[] b = alg.digest();
			signature = Base64.encodeBase64String(b);
		} catch (NoSuchAlgorithmException e) {
			log.error("加密出错：" + e.getMessage());
		}
		return signature;
	}

	/**
	 * 使用SHA256加密
	 * 
	 * @param src
	 * @return
	 */
	public static String encodeSHA256(String src) {
		return encodeSHA(src, "SHA-256");
	}

	/**
	 * 使用密钥进行消息加密
	 * 
	 * @param src
	 * @param key
	 * @return
	 */
	public static String encodeSHA256(String src, String key) {
		String hexBytes = null;
		try {
			byte[] keyBytes = key.getBytes(Constants.UTF_8);
			// 根据给定的字节数组构造一个密钥。
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(src.getBytes(Constants.UTF_8));
			hexBytes = byteToHex(rawHmac);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
			log.error("加密出错：" + e.getMessage());
		}
		return hexBytes;
	}

	private static String encodeSHA(String src, String type) {
		String signature = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance(type);
			crypt.reset();
			crypt.update(src.getBytes(Constants.UTF_8));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			log.error("加密出错：" + e.getMessage());
		}
		return signature;
	}

	/**
	 * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
	 * 
	 * @param encryptText
	 *            被签名的字符串
	 * @param encryptKey
	 *            密钥
	 * @return
	 * 
	 * @throws Exception
	 */
	public static byte[] hmacSHA1EncryptBytes(String encryptText, String encryptKey) {
		try {
			byte[] data = encryptKey.getBytes(Constants.UTF_8);
			// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
			SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
			// 生成一个指定 Mac 算法 的 Mac 对象
			Mac mac = Mac.getInstance("HmacSHA1");
			// 用给定密钥初始化 Mac 对象
			mac.init(secretKey);
			byte[] text = encryptText.getBytes(Constants.UTF_8);
			// 完成 Mac 操作
			return mac.doFinal(text);
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
}
