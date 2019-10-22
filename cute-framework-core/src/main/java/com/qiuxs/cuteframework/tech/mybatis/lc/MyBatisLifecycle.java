package com.qiuxs.cuteframework.tech.mybatis.lc;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.listener.lc.ILifecycle;
import com.qiuxs.cuteframework.tech.mybatis.config.MyBatisConfig;
import com.qiuxs.cuteframework.tech.mybatis.utils.MybatisMapperRefresher;

@Component
public class MyBatisLifecycle implements ILifecycle {

	@Resource
	private MyBatisConfig mybatisConfig;

	@Override
	public int order() {
		return 10;
	}

	@Override
	public void started() {
		if (this.mybatisConfig.isAutoRefresh()) {
			MybatisMapperRefresher.startRefresher(this.mybatisConfig.getMapperLocations());
		}
	}

}
