package com.qiuxs.ws;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.qiuxs.ws.config.WsConsumer;

public class WsConsumerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser  {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return WsConsumer.class;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		System.out.println(builder.getBeanDefinition());
	}

	@Override
	protected boolean shouldGenerateId() {
		return true;
	}
}
