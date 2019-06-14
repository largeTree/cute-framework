package com.hzecool.fdn.utils.generator;

import java.util.Random;


public class RandomGenerator {
	
	/**
	 * 随机生成两位数
	 *  
	 * @author fengdg  
	 * @return
	 */
	public static long randomTwoDigit() {
		return Math.round(10 + 89 * Math.random());
	} 
	
	public static String getRandomStr() {
		return getRandomStr(6);
	}
	
	/**
	 * 随机数字
	 *  
	 * @author fengdg  
	 * @param len
	 * @return
	 */
	public static String getRandomStr(int len) {
		if (len <= 0) {
			throw new IllegalArgumentException();
		}
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for(int i=0;i<len;i++){
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}
	
	public static final String ALPHA_NUMERIC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
    /**
     * 获取一定长度的随机字符串：数字+字母
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(ALPHA_NUMERIC.length());
            sb.append(ALPHA_NUMERIC.charAt(number));
        }
        return sb.toString();
    }
    
	/**
	 * 生成6位随机代码，获取时间戳后6位
	 * @return
	 */
	public static String getRandCode(){
		String code = String.valueOf(System.currentTimeMillis());
		code = code.substring(code.length() - 6);
		return code;
	}
	
	/**
	 * 生成[0,max)之间的随机数
	 * -含0；不含max
	 *  
	 * @author fengdg  
	 * @param max
	 * @return
	 */
    public static int getRandomInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }
    
	public static void main(String [] args) {
		for(int i = 0; i < 1000; i++)
		System.out.println(getRandomInt(10));

//		System.out.println(getRandomStr(10));
	}
}
