package com.qiuxs.cuteframework.core.basic.i18n;

import java.text.MessageFormat;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class I18nUtils {

	@Resource
	private MessageResourceHolder messageResourceHolder;

	/**
	 * 获取格式化的国际化
	 * @param lang
	 * @param msgKey
	 * @param args
	 * @return
	 */
	public String getMessageByLang(String lang, String msgKey, String... args) {
		String msg = messageResourceHolder.getMessage(lang, msgKey);
		if (msg != null) {
			msg = new MessageFormat(msg).format(args);
		}
		return msg;
	}

}
