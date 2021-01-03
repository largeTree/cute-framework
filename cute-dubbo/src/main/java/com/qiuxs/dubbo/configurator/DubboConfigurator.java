package com.qiuxs.dubbo.configurator;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.cluster.configurator.AbstractConfigurator;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;

public class DubboConfigurator extends AbstractConfigurator {

	public DubboConfigurator(URL url) {
		super(url);
	}

	@Override
	protected URL doConfigure(URL currentUrl, URL configUrl) {
		String serverId = EnvironmentContext.getServerId();
		return currentUrl.addParameter("serverId", serverId);
	}

}
