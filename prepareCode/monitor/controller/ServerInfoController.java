package com.hzecool.frm.monitor.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hzecool.core.context.BaseContextManager;

/**
 * 
 * 功能描述: 服务器运行时信息<p>  
 * 新增原因: TODO<p>  
 * 新增日期: 2019年5月16日 上午9:30:52 <p>  
 *  
 * @author fengdg   
 * @version 1.0.0
 */
@Controller
@RequestMapping({"/devops/server"})
public class ServerInfoController {
	@Resource
	private DataSource dynDataSource;

	@RequestMapping(value = {"/print.do"}, method = RequestMethod.GET, produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String print(HttpServletRequest req, HttpServletResponse rep) {
		StringBuilder info = new StringBuilder();
		info.append("webIndex:").append(BaseContextManager.getWebIndex());
		info.append("webCount:").append(BaseContextManager.getWebCount());
		return info.toString();
	}
	
}
