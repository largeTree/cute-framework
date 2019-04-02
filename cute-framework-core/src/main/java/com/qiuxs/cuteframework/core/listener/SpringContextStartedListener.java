package com.qiuxs.cuteframework.core.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.tech.mybatis.utils.MybatisMapperRefresher;

@Component
@ConfigurationProperties(prefix = MybatisProperties.MYBATIS_PREFIX)
public class SpringContextStartedListener implements ApplicationListener<ApplicationStartedEvent> {

	private static Logger log = LogManager.getLogger(SpringContextStartedListener.class);

	private boolean autoRefresh;

	private String[] mapperLocations;

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		if (this.autoRefresh) {
			MybatisMapperRefresher.startRefresher(this.mapperLocations);
		}
		log.info("SpringContextStarted....");
	}

	public void setMapperLocations(String[] mapperLocations) {
		this.mapperLocations = mapperLocations;
	}

	public boolean isAutoRefresh() {
		return autoRefresh;
	}

	public void setAutoRefresh(boolean autoRefresh) {
		this.autoRefresh = autoRefresh;
	}

}
