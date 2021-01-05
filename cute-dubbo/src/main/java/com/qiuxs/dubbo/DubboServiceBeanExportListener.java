package com.qiuxs.dubbo;

import org.apache.dubbo.config.spring.context.event.ServiceBeanExportedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DubboServiceBeanExportListener implements ApplicationListener<ServiceBeanExportedEvent> {

	@Override
	public void onApplicationEvent(ServiceBeanExportedEvent event) {
		DubboContextHolder.addServiceBean(event.getServiceBean());
	}

}
