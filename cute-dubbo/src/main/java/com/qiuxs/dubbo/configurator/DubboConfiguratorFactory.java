package com.qiuxs.dubbo.configurator;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.cluster.Configurator;
import com.alibaba.dubbo.rpc.cluster.ConfiguratorFactory;

public class DubboConfiguratorFactory implements ConfiguratorFactory {

	@Override
	public Configurator getConfigurator(URL url) {
		return new DubboConfigurator(url);
	}

}
