package com.qiuxs.cuteframework.core.basic.i18n;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class I18nUtils {

	@Resource
	private MessageResourceHolder messageResourceHolder;

	public String getMessageByLang(String lang, String msgKey) {
		String msg = messageResourceHolder.getCn().get(msgKey);
		return msg;
	}

}
