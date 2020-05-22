package com.qiuxs.cuteframework.web.sys.controller;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiuxs.cuteframework.web.WebConstants;

@RestController
@RequestMapping(value = WebConstants.DEVOPS_CONTROLLER_PREFIX + "/server", produces = WebConstants.DEFAULT_REQUEST_PRODUCES)
public class ServerController {

	@Resource
	private DataSource dataSource;

	@GetMapping(value = "/check.do", produces = "text/plain")
	public String checkServer() {
		if (dataSource == null) {
			return "failed dataBase is null";
		}
		try {
			dataSource.getConnection().close();
		} catch (Exception e) {
			return "failed ext = " + e.getLocalizedMessage();
		}
		return "success";
	}

}
