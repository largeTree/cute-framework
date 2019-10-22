package com.qiuxs.cuteframework.view.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.view.pagemodel.config.PageModeConfiguration;

@Controller
@RequestMapping("/view")
public class ViewController {
	
	private static final String FORM_ACTION_VIEW = "view";

	@RequestMapping
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		modelMap.put("appName", EnvironmentContext.getEnvContext().getAppName());
		return "index";
	}

	@RequestMapping("/datalist")
	public String dataList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		String pageId = request.getParameter("pid");
		modelMap.put("pageModel", PageModeConfiguration.getPage(pageId));
		return "common/dataList";
	}
	
	@RequestMapping("/form")
	public String form(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		String pageId = request.getParameter("pid");
		String formId = request.getParameter("formId");
		String action = request.getParameter("action");
		modelMap.put("pageModel", PageModeConfiguration.getPage(pageId));
		modelMap.put("formId", formId);
		if (FORM_ACTION_VIEW.equals(action)) {
			return "common/viewform";
		}
		return "common/form";
	}

	@RequestMapping("/mydesktop")
	public String desktop(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		return "myDesktop";
	}

}
