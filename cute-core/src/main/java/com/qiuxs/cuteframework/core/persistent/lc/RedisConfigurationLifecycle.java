package com.qiuxs.cuteframework.core.persistent.lc;

import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
import com.qiuxs.cuteframework.core.persistent.redis.RedisConfiguration;

public class RedisConfigurationLifecycle implements IWebLifecycle {

	@Override
	public int order() {
		return 0;
	}
	
	@Override
	public void lastDestory() {
		RedisConfiguration.shutdown();
	}

}
