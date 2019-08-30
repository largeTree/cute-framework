package com.qiuxs.ws.config.spring;

import org.springframework.beans.factory.FactoryBean;

import com.qiuxs.ws.bytecode.SoapWebServiceClassGenerator;
import com.qiuxs.ws.config.WsConsumer;

/**
 * 
 * @author qiuxs
 *
 * @param <T>
 */
public class WSReferenceBean<T> implements FactoryBean<T> {
	
	
	private WsConsumer wsConsumer;
	private Class<T> referenceClass;
	private T ref;

	public WSReferenceBean(Class<T> referenceClass) {
		this.referenceClass = referenceClass;
	}

	public synchronized T get() {
		if (this.ref == null) {
			this.init();
		}
		return this.ref;
	}

	private void init() {
		SoapWebServiceClassGenerator<T> cg = new SoapWebServiceClassGenerator<>(this.referenceClass);
		cg.setFilterChainBuilder(this.wsConsumer.getFilterChainBuilder());
		this.ref = cg.getWsRef();
	}

	@Override
	public T getObject() {
		return this.get();
	}

	@Override
	public Class<?> getObjectType() {
		return this.referenceClass;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	public void setWsConsumer(WsConsumer wsConsumer) {
		this.wsConsumer = wsConsumer;
	}

}
