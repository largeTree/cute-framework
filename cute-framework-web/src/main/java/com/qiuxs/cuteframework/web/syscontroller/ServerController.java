package com.qiuxs.cuteframework.web.syscontroller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiuxs.cuteframework.core.persistent.database.lookup.DynamicDataSource;
import com.qiuxs.cuteframework.web.WebConstants;

@RestController
@RequestMapping(value = WebConstants.DEVOPS_CONTROLLER_PREFIX + "/server", produces = WebConstants.DEFAULT_REQUEST_PRODUCES)
public class ServerController {

	@Resource
	private DynamicDataSource dynamicDataSource;

	@GetMapping(value = "/check.do", produces = "text/plain")
	public String checkServer() {
		if (dynamicDataSource == null) {
			return "failed dataBase is null";
		}
		try {
			dynamicDataSource.getConnection().close();
		} catch (Exception e) {
			return "failed ext = " + e.getLocalizedMessage();
		}
		return "success";
	}

}
