package com.qiuxs.dubbo.configurator;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.cluster.Configurator;
import org.apache.dubbo.rpc.cluster.ConfiguratorFactory;



public class DubboConfiguratorFactory implements ConfiguratorFactory {

	@Override
	public Configurator getConfigurator(URL url) {
		return new DubboConfigurator(url);
	}

}
