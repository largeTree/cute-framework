package com.qiuxs.cuteframework.core.basic.i18n;

import java.text.MessageFormat;

public class I18nUtils {

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
