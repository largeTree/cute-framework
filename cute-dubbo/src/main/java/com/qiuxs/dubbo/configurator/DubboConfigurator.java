package com.qiuxs.dubbo.configurator;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.cluster.configurator.AbstractConfigurator;

import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.dubbo.DubboConstants;

public class DubboConfigurator extends AbstractConfigurator {

	public DubboConfigurator(URL url) {
		super(url);
	}

	@Override
	protected URL doConfigure(URL currentUrl, URL configUrl) {
		String serverId = EnvironmentContext.getServerId();
		return currentUrl.addParameter(DubboConstants.URL_PARAM_SERVER_ID, serverId);
	}

}
