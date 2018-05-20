package com.qiuxs.cuteframework.web.syscontroller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiuxs.cuteframework.core.basic.i18n.MessageResourceHolder;
import com.qiuxs.cuteframework.web.WebConstants;
import com.qiuxs.cuteframework.web.controller.BaseController;

@RestController
@RequestMapping(value = WebConstants.SYS_CONTROLLER_PREFIX + "/config", produces = WebConstants.DEFAULT_REQUEST_PRODUCES)
public class ConfigController extends BaseController {

	@Resource
	private MessageResourceHolder messageResourceHolder;

	@GetMapping("/msg/{lang}/{msgKey}")
	public String msgByLang(@PathVariable("lang") String lang, @PathVariable("msgKey") String msgKey) {
		return super.responseVal(messageResourceHolder.getMessage(lang, msgKey));
	}

	@GetMapping("/msg/all")
	public String msgAll() {
		return super.responseRes(messageResourceHolder.getLangMsgs());
	}

}
