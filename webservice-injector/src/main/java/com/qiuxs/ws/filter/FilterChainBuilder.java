package com.qiuxs.ws.filter;

import java.util.List;

import com.qiuxs.ws.filter.invoker.Invocation;
import com.qiuxs.ws.filter.invoker.Invoker;

public class FilterChainBuilder {
	private List<Filter> filters;

	public FilterChainBuilder(List<Filter> filters) {
		this.filters = filters;
	}

	public Invoker buildFilterChain(Invoker invoker) {
		Invoker last = invoker;
		if (this.filters != null && this.filters.size() > 0) {
			for (final Filter filter : this.filters) {
				final Invoker next = last;
				last = new Invoker() {
					public Object invoke(Invocation invocation) {
						return filter.doFilter(next, invocation);
					}
				};
			}
		}
		return last;
	}

}
