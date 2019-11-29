package com.hzecool.fdn.utils.security;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.hzecool.fdn.Constant;
import com.hzecool.fdn.utils.generator.GUIDGenerator;
public class MD5Util {
//	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
//		"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
//
//	private static String byteArrayToHexString(byte b[]) {
//		StringBuffer resultSb = new StringBuffer();
//		for (int i = 0; i < b.length; i++)
//			resultSb.append(byteToHexString(b[i]));
//
//		String res = SecurityUtil.byteToHex(b);
//		if (!res.equals(resultSb.toString())) {
//			System.out.println("Not equal; res=" + res + "; resultSb" + resultSb);
//		}
//		return resultSb.toString();
//	}
//
//	private static String byteToHexString(byte b) {
//		int n = b;
//		if (n < 0)
//			n += 256;
//		int d1 = n / 16;
//		int d2 = n % 16;
//		return hexDigits[d1] + hexDigits[d2];
//	}

	/**
	 * md5加密
	 *  
	 * @author fengdg  
	 * @param origin
	 * @param charsetName
	 * @return
	 */
	public static String MD5Encode(String origin, String charsetName) {
		return SecurityUtil.encode(origin, charsetName, SecurityUtil.ALG_MD5);
//		String resultString = null;
//		try {
//			resultString = new String(origin);
//			MessageDigest md = MessageDigest.getInstance("MD5");
//			if (charsetname == null || "".equals(charsetname))
//				resultString = byteArrayToHexString(md.digest(resultString
//						.getBytes()));
//			else
//				resultString = byteArrayToHexString(md.digest(resultString
//						.getBytes(charsetname)));
//		} catch (Exception exception) {
//		}
//		return resultString;
	}

	/**
	 * md5加密
	 *  
	 * @author fengdg  
	 * @param bytes
	 * @return
	 */
	public static String MD5Encode(byte[] bytes) {
		return SecurityUtil.encode(bytes, SecurityUtil.ALG_MD5);
	}
	
	/**
	 * 对文件进行 MD5 加密
	 * 
	 * @param file
	 *            待加密的文件
	 * 
	 * @return 文件加密后的 MD5 值
	 * @throws IOException
	 */
	public static String md5(File file) throws IOException {
		FileInputStream is = new FileInputStream(file);
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			int n = 0;
			byte[] buffer = new byte[1024];
			do {
				n = is.read(buffer);
				if (n > 0) {
					md5.update(buffer, 0, n);
				}
			} while (n != -1);
			is.skip(0);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			is.close();
		}

		byte[] encodedValue = md5.digest();

		int j = encodedValue.length;
		char finalValue[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte encoded = encodedValue[i];
			finalValue[k++] = hexDigits[encoded >> 4 & 0xf];
			finalValue[k++] = hexDigits[encoded & 0xf];
		}

		return new String(finalValue);
	}
	
	@Deprecated
	public static String md5(byte[] bytes) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(bytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MessageDigest不支持MD5Util", e);
		}
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}
	
	
	public static void main(String[] args) {
//		for (int i = 0; i< 1000; i++) {
////			String input = RandomGenerator.getRandomStringByLength(10);
//			GUIDGenerator myGUID = new GUIDGenerator();
//			String input = myGUID.toString();
//			MD5Encode(input, Constant.UTF8);
//		}
//		System.out.println("finish");
		syso
	}
	
}
