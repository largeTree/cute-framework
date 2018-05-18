package com.qiuxs.cuteframework.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/server", produces = "application/json; charset=UTF-8")
public class ServerStatusController {

	public static final String IGNORE_PATH_PREFIX = "/server/**";

	@ResponseBody
	@RequestMapping("/check")
	public String checkServer() {
		return "success";
	}

}
