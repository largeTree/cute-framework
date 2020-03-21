package com.qiuxs.cuteframework.core.utils;

@FunctionalInterface
public interface TestFunction<T> {
	public boolean test(T t);
}
