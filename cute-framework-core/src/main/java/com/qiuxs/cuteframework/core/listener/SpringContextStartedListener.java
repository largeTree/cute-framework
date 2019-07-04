package com.qiuxs.cuteframework.core.listener;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.tech.mybatis.config.MyBatisConfig;
import com.qiuxs.cuteframework.tech.mybatis.utils.MybatisMapperRefresher;

@Component
public class SpringContextStartedListener implements ApplicationListener<ApplicationStartedEvent> {

	private static Logger log = LogManager.getLogger(SpringContextStartedListener.class);

	@Resource
	private MyBatisConfig mybatisConfig;

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		if (this.mybatisConfig.isAutoRefresh()) {
			MybatisMapperRefresher.startRefresher(this.mybatisConfig.getMapperLocations());
		}
		log.info("SpringContextStarted....");
	}

}
