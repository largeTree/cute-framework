package com.qiuxs.dubbo.lc;

import java.util.Properties;

import org.apache.dubbo.common.utils.ConfigUtils;

import com.qiuxs.cuteframework.core.basic.config.IConfiguration;
import com.qiuxs.cuteframework.core.basic.config.UConfigUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
import com.qiuxs.dubbo.DubboContextHolder;

public class DubboLifecycle implements IWebLifecycle {

	@Override
	public int order() {
		return 10;
	}

	@Override
	public void middleInit() {
		IConfiguration domain = UConfigUtils.getDomain(UConfigUtils.DOMAIN_DUBBO);
		if (domain != null) {
			Properties properties = domain.toProperties();
			if (!properties.containsKey("dubbo.application.name")) {
				properties.put("dubbo.application.name", EnvironmentContext.getAppName());
			}
			ConfigUtils.setProperties(properties);
		}
	}
	
	@Override
	public void lastInit() {
		// 暴露服务
//		DubboContextHolder.exportServiceConfig();
	}
	
	@Override
	public void lastDestory() {
//		ProtocolConfig
		DubboContextHolder.destoryAll();
	}
	
}
