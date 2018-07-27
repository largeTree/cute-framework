package com.qiuxs.cuteframework.core.basic.i18n;

import java.text.MessageFormat;

import com.qiuxs.cuteframework.core.basic.Constants;

public class I18nUtils {

	/**
	 * 获取默认语言提示消息
	 * @author qiuxs
	 *
	 * @param msgKey
	 * @param args
	 * @return
	 *
	 * 创建时间：2018年7月27日 下午10:23:09
	 */
	public static String getDefaultLangMsg(String msgKey,String ...args) {
		return getMessageByLang(Constants.DEFAULT_LANG, msgKey, args);
	}
	
	/**
	 * 获取格式化的国际化
	 * 
	 * @param lang
	 * @param msgKey
	 * @param args
	 * @return
	 */
	public static String getMessageByLang(String lang, String msgKey, String... args) {
		String msg = MessageResourceHolder.getMessage(lang, msgKey);
		if (msg != null) {
			msg = new MessageFormat(msg).format(args);
		} else {
			msg = msgKey;
		}
		return msg;
	}

}
