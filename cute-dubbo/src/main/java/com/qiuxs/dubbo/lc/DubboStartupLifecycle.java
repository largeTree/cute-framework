package com.qiuxs.dubbo.lc;

import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.qiuxs.cuteframework.core.basic.config.IConfiguration;
import com.qiuxs.cuteframework.core.basic.config.UConfigUtils;
import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
import com.qiuxs.dubbo.DubboContextHolder;

public class DubboStartupLifecycle implements IWebLifecycle {

	@Override
	public int order() {
		return 10;
	}

	@Override
	public void middleInit() {
		IConfiguration domain = UConfigUtils.getDomain("dubbo");
		if (domain != null) {
			ConfigUtils.setProperties(domain.toProperties());
		}
	}
	
	@Override
	public void lastInit() {
		// 暴露服务
		DubboContextHolder.exportServiceConfig();
	}
	
	@Override
	public void lastDestory() {
		ProtocolConfig.destroyAll();
	}
	
}
