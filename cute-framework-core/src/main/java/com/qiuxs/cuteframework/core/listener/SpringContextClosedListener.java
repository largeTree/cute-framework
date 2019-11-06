package com.qiuxs.cuteframework.core.listener;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
import com.qiuxs.cuteframework.core.listener.lc.WebLifecycleContainer;

@Component
public class SpringContextClosedListener implements ApplicationListener<ContextClosedEvent> {

	private static Logger log = LogManager.getLogger(SpringContextClosedListener.class);

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		List<IWebLifecycle> lifecycles = WebLifecycleContainer.getLifecycles();
		for (int i = lifecycles.size() - 1; i >= 0; i--) {
			IWebLifecycle lifecycle = lifecycles.get(i);
			try {
				lifecycle.lastDestory();
			} catch (Exception e) {
				log.error("Lifecycle@" + lifecycle.getClass() + ", destroy Failed ext = " + e.getLocalizedMessage(), e);
			}
		}
	}

}
