package com.hzecool.fdn.utils.security;

/**
 * 功能描述: <br>
 * 新增日期: 2018/9/5<br>
 *
 * @author laisf
 * @version 1.0.0
 */

import org.apache.commons.net.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;

public class AESUtils {

    public static final String KEY_ALGORITHM = "AES";
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    public static final Charset UTF8 = Charset.forName("UTF-8");

    /***
     * 编码后进行base64编码，便于字符传输
     * @param data
     * @param key
     * @return
     */
    public static String encryptBase64(String data, String key) {
        byte[] byteRet = encrypt(data.getBytes(UTF8), key.getBytes(UTF8));
        return new String(Base64.encodeBase64(byteRet), UTF8);
    }

    public static byte[] encrypt(byte[] data, String key) {
        return encrypt(data, key.getBytes(UTF8));
    }

    public static byte[] encrypt(byte[] data, byte[] key) {
        try {
            Key k = genSecretKey(key);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, k);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("decrypt error", e);
        }
    }

    /**
     * 解码
     * @param data 已做base64编码的字符串
     * @param key
     * @return
     */
    public static String decryptBase64(String data, String key) {
        byte[] dataAfterDecodeBase64 = Base64.decodeBase64(data.getBytes(UTF8));
        byte[] byteRet = decrypt(dataAfterDecodeBase64, key.getBytes(UTF8));
        return new String(byteRet, UTF8);
    }

    public static byte[] decrypt(String data, byte[] key) {
        return decrypt(data.getBytes(UTF8), key);
    }

    public static byte[] decrypt(byte[] data, String key) {
        return decrypt(data, key.getBytes(UTF8));
    }

    public static byte[] decrypt(byte[] data, byte[] key) {
        try {
            Key k = genSecretKey(key);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, k);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("decrypt error", e);
        }
    }

    /**
     * AES only supports key sizes of 16, 24 or 32 bytes
     */
    public static Key genSecretKey(byte[] key) {
        if (key.length == 16 || key.length == 24 || key.length == 32) {
            return new SecretKeySpec(key, KEY_ALGORITHM);
        }
        throw new IllegalArgumentException("AES only supports key sizes of 16, 24 or 32 bytes");
    }

    /**
     * 是否合法密钥
     * @param key
     * @return
     */
    public static boolean isValidKey(String key){
        if(key == null)return false;
        byte[] keyBytes = key.getBytes(UTF8);
        if (keyBytes.length == 16 || keyBytes.length == 24 || keyBytes.length == 32) {
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args) {
        String key = "000000000000000a";
        long start = System.currentTimeMillis();
        String s = encryptBase64("123456788888", key);
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(s);
        start = System.currentTimeMillis();
        String t = decryptBase64(s, key);
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(t);

    }
}
