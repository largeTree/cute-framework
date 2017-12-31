package com.qiuxs.cuteframework.core.basic.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtils {

	public static List<Method> getMethods(Class<?> clz, String mthName, boolean includeSupperClass) {
		Method[] methods = clz.getDeclaredMethods();
		List<Method> retList = new ArrayList<>();
		retList.addAll(filterMethods(methods, mthName));
		if (includeSupperClass) {
			Class<?> superClz = clz.getSuperclass();
			while (!superClz.equals(Object.class)) {
				methods = superClz.getDeclaredMethods();
				retList.addAll(filterMethods(methods, mthName));
				superClz = superClz.getSuperclass();
			}
		}
		return retList;
	}

	private static List<Method> filterMethods(Method[] mths, String name) {
		List<Method> retList = new ArrayList<>();
		for (Method mth : mths) {
			if (mth.getName().equals(name)) {
				retList.add(mth);
			}
		}
		return retList;
	}
}
