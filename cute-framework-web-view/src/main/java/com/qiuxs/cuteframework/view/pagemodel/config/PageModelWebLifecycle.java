package com.qiuxs.cuteframework.view.pagemodel.config;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.listener.lc.ILifecycle;

@Component
public class PageModelWebLifecycle implements ILifecycle {

	@Override
	public int order() {
		return 11;
	}

	@Override
	public void started(ApplicationStartedEvent event) {
		PageModeConfiguration.init();
	}

}
