package com.hzecool.frm.monitor.dubbo.controller;

import java.lang.Thread.State;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hzecool.fdn.utils.StringUtils;
import com.hzecool.fdn.utils.converter.JsonUtils;
import com.hzecool.fdn.utils.net.RequestUtils;
import com.hzecool.tech.dubbo.DubboContextHolder;

@Controller
@RequestMapping({"/devops/dubbo"})
public class DubboDevopsController {


	@RequestMapping(value = "threadList.do", produces="application/json; charset=utf-8")
	@ResponseBody
	public String dubboThreadStack(HttpServletRequest req) throws Exception {
		String stateStr = RequestUtils.getStringValue(req, "state");//"RUNNABLE"
		State state = null;
		if (StringUtils.isNotEmpty(stateStr)) {
			state = State.valueOf(stateStr.toUpperCase());
		}
		
    	JSONObject threadTraceMap = DubboContextHolder.getThreadPoolInfoMap(state, true);
    	return JsonUtils.toJSONString(threadTraceMap);
	}
	
}
