package com.qiuxs.cuteframework.view.pagemodel.config;

import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;

@Component
public class PageModelWebLifecycle implements IWebLifecycle {

	@Override
	public void lastInit() {
		PageModeConfiguration.init();
	}

	@Override
	public int order() {
		return 2;
	}

}
