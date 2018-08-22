package com.qiuxs.cuteframework.core.basic.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号相关工具类
 * @author zhangyz
 *
 */
public class MobileUtils {

	/**
	 * 校验手机号格式
	 *  
	 * @author fengdg  
	 * @param mobile
	 */
	public static void checkMobile(String mobile) {
		if (!MobileUtils.isMobileNO(mobile)) {
			// 不符合要求的手机号码
			ExceptionUtils.throwLogicalException("mobile_format_illegal");
		}
	}

	private static Pattern p = Pattern.compile("^1[3-9]\\d{9}$");

	/**
	 * 正则表达式，判断电话号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 从文本中查找符合规范的手机号，多个返回数组
	 *  
	 * @author laisf  
	 * @param text
	 * @return
	 */
	public static List<String> findMobileNo(String text) {
		Pattern p = Pattern.compile("((13)|(15)|(17)|(18))\\d{9}");
		Matcher m = p.matcher(text);
		List<String> listResult = new ArrayList<String>();
		while (m.find()) {
			listResult.add(m.group(0));
		}
		return listResult;
	}

	/***
	 * 隐藏手机号中间4位
	 *  
	 * @author laisf  
	 * @param text
	 * @return
	 */
	public static String maskMobileNo(String phone) {
		if (StringUtils.isNotBlank(phone)) {
			int len = phone.length();
			int beginIdx = 3;
			int endIdx = 7;
			if (len > beginIdx) {
				if (len > endIdx)
					len = endIdx;
				phone = new StringBuilder(phone).replace(beginIdx, len, "****").toString();
			}
		}
		return phone;
	}

}
