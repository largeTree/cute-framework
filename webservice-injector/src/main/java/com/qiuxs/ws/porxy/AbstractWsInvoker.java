package com.qiuxs.ws.porxy;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.ws.WsInvokeException;
import com.qiuxs.ws.filter.FilterChainBuilder;
import com.qiuxs.ws.filter.invoker.Invocation;
import com.qiuxs.ws.filter.invoker.Invoker;

public abstract class AbstractWsInvoker {

	private static Logger logger = LogManager.getLogger(AbstractWsInvoker.class);

	private FilterChainBuilder filterChainBuilder;

	protected AbstractWsInvoker(FilterChainBuilder filterChainBuilder) {
		this.filterChainBuilder = filterChainBuilder;
	}

	protected Invoker buildChain() {
		Invoker invoke = new Invoker() {
			@Override
			public Object invoke(Invocation invocation) {
				long start = System.currentTimeMillis();
				String svcName = invocation.getTarget().getClass().getInterfaces()[0].getName() + "@" + invocation.getMethod().getName();
				if (logger.isDebugEnabled()) {
					logger.debug("Invkoe WebService " + svcName + ", arguments = "
							+ Arrays.toString(invocation.getArguments()));
				}
				try {
					return invocation.getMethod().invoke(invocation.getTarget(), invocation.getArguments());
				} catch (ReflectiveOperationException e) {
					throw new WsInvokeException("Failed invoke WebService : " + invocation.getTarget().getClass().getName() + "@" + invocation.getMethod().getName(), e);
				} finally {
					if (logger.isDebugEnabled()) {
						logger.debug("Invoke WebService " + svcName + " finished CostMs = " + (System.currentTimeMillis() - start));
					}
				}
			}
		};
		if (this.filterChainBuilder != null) {
			return this.filterChainBuilder.buildFilterChain(invoke);
		} else {
			return invoke;
		}
	}

	public void setFilterChainBuilder(FilterChainBuilder filterChainBuilder) {
		this.filterChainBuilder = filterChainBuilder;
	}

}
