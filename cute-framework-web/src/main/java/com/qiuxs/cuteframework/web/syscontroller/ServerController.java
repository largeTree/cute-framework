package com.qiuxs.cuteframework.web.syscontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiuxs.cuteframework.web.WebConstants;

@RestController
@RequestMapping(value = WebConstants.SYS_CONTROLLER_PREFIX + "/server", produces = WebConstants.DEFAULT_REQUEST_PRODUCES)
public class ServerController {

	@GetMapping("/check.do")
	public String checkServer() {
		return "success";
	}

}
