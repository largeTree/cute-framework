package com.qiuxs.ws;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class WsNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("consumer", new WsConsumerBeanDefinitionParser());
	}

}
