package com.hzecool.frm.monitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hzecool.core.context.EnvironmentHolder;


@Controller
public class VersionController {

	@RequestMapping(value = {"/version.do", "/devops/version.do"}, method = RequestMethod.GET)
	@ResponseBody
	public String getVersion(){
		return EnvironmentHolder.getEnvParam("version");
	}
}
