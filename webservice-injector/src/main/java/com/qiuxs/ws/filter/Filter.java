package com.qiuxs.ws.filter;

import com.qiuxs.ws.filter.invoker.Invocation;
import com.qiuxs.ws.filter.invoker.Invoker;

public interface Filter {

	public Object doFilter(Invoker invoker, Invocation invocation);
	
	public int getOrder();

}
