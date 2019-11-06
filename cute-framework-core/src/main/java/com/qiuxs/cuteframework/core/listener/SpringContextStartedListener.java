package com.qiuxs.cuteframework.core.listener;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
import com.qiuxs.cuteframework.core.listener.lc.WebLifecycleContainer;

@Component
public class SpringContextStartedListener implements ApplicationListener<ApplicationStartedEvent> {

	private static Logger log = LogManager.getLogger(SpringContextStartedListener.class);

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		List<IWebLifecycle> lifecycles = WebLifecycleContainer.getLifecycles();
		for (IWebLifecycle lifecycle : lifecycles) {
			try {
				lifecycle.lastInit();
			} catch (Exception e) {
				log.error("Lifecycle@" + lifecycle.getClass() + " started Faild ext = " + e.getLocalizedMessage(), e);
			}
		}

		log.info("SpringContextStarted....");
	}

}
