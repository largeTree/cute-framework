package com.qiuxs.cuteframework.server;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiuxs.cuteframework.core.basic.i18n.I18nUtils;

@RestController
@RequestMapping(value = "/server", produces = "application/json; charset=UTF-8")
public class ServerController {

	public static final String IGNORE_PATH_PREFIX = "/server/**";
	
	@Resource
	private I18nUtils i18nUtils;

	@GetMapping("/check")
	public String checkServer() {
		return "success";
	}

	@GetMapping("/msg/{lang}/{msgKey}")
	public String checkMsg(@PathVariable("lang") String lang, @PathVariable("msgKey") String msgKey) {
		return i18nUtils.getMessageByLang(lang, msgKey);
	}
}
