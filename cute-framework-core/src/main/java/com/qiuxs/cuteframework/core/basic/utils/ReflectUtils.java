package com.qiuxs.cuteframework.core.basic.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 反射相关工具
 * @author qiuxs
 * 
 * 创建时间 ： 2018年7月26日 下午10:21:35
 *
 */
public class ReflectUtils {

	/**
	 * 获取所有字段定义，包含父类，并排除重复字段
	 * 
	 * @param clz
	 * @return
	 */
	public static List<Field> getDeclaredFieldsNoDup(Class<?> clz) {
		List<Field> declaredFields = getDeclaredFields(clz);
		Map<String, Field> map = ListUtils.listToMap(declaredFields, "name");
		return new ArrayList<>(map.values());
	}

	/**
	 * 获取所有字段包含父类
	 * 
	 * @param clz
	 * @return
	 */
	public static List<Field> getDeclaredFields(Class<?> clz) {
		List<Field> fields = new ArrayList<>();
		Field[] declaredFields = clz.getDeclaredFields();
		for (Field f : declaredFields) {
			fields.add(f);
		}
		Class<?> superclass = clz.getSuperclass();
		if (superclass.getSimpleName().equals(Object.class.getSimpleName())) {
			return fields;
		}
		// 获取父类字段
		fields.addAll(getDeclaredFields(superclass));
		return fields;
	}

	/**
	 * 获取类中所有定义的方法对象
	 * @author qiuxs
	 *
	 * @param clz
	 * @param mthName
	 * @param includeSupperClass
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:21:46
	 */
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

	/**
	 * 过滤方法列表
	 * @author qiuxs
	 *
	 * @param mths
	 * @param name
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:22:06
	 */
	private static List<Method> filterMethods(Method[] mths, String name) {
		List<Method> retList = new ArrayList<>();
		for (Method mth : mths) {
			if (mth.getName().equals(name)) {
				retList.add(mth);
			}
		}
		return retList;
	}

	/**
	 * 获取无权限判断的字段对象
	 * @author qiuxs
	 *
	 * @param clz
	 * @param fieldName
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:22:14
	 */
	public static Field getAccessibleField(Class<?> clz, String fieldName) {
		try {
			Field declaredField = clz.getDeclaredField(fieldName);
			declaredField.setAccessible(true);
			return declaredField;
		} catch (NoSuchFieldException e) {
			// 不存在字段，向父类寻找
			Class<?> superclass = clz.getSuperclass();
			// 父类是Object时直接返回
			if (superclass.getSimpleName().equals(Object.class.getSimpleName())) {
				return null;
			}
			return getAccessibleField(superclass, fieldName);
		}
	}

	/**
	 * 获取字段值
	 * 当前类不存在则寻找父类
	 * @param bean
	 * @param name
	 * @return
	 * @throws ReflectiveOperationException
	 */
	public static Object getFieldValue(Object bean, String name) throws ReflectiveOperationException {
		Field accessibleField = getAccessibleField(bean.getClass(), name);
		return accessibleField.get(bean);
	}

	/**
	 * 设置字段值
	 * 当前类不存在时则寻找父类
	 * @param bean
	 * @param name
	 * @param defaultValue
	 * @throws ReflectiveOperationException
	 */
	public static void setFieldValue(Object bean, String name, Object defaultValue) throws ReflectiveOperationException {
		Field accessibleField = getAccessibleField(bean.getClass(), name);
		accessibleField.set(bean, defaultValue);
	}

	/**
	 * 判断类型是否为基础类型包装类
	 * @author qiuxs
	 *
	 * @param type
	 * @return
	 *
	 * 创建时间：2018年7月25日 下午9:34:12
	 */
	public static boolean isPrimitivePackagingClass(Class<?> type) {
		if (type.isAssignableFrom(Boolean.class) 
				|| type.isAssignableFrom(Byte.class) 
				|| type.isAssignableFrom(Character.class) 
				|| type.isAssignableFrom(Double.class) 
				|| type.isAssignableFrom(Float.class) 
				|| type.isAssignableFrom(Integer.class) 
				|| type.isAssignableFrom(Long.class) 
				|| type.isAssignableFrom(Short.class)) {
			return true;
		}
		return false;
	}
}
