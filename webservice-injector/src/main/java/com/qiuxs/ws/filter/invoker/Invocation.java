package com.qiuxs.ws.filter.invoker;

import java.lang.reflect.Method;

public class Invocation {

	private final Object target;
	private final Method method;
	private final Class<?>[] parameterTypes;
	private final Object[] arguments;

	public Invocation(Object target, Method method, Class<?>[] parameterTypes, Object[] arguments) {
		this.target = target;
		this.method = method;
		this.parameterTypes = parameterTypes;
		this.arguments = arguments;
	}

	public Object getTarget() {
		return target;
	}

	public Method getMethod() {
		return method;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public Object[] getArguments() {
		return arguments;
	}

}
