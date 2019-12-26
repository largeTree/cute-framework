package com.qiuxs.cuteframework.core.basic.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MethodUtils {

	private static final Logger log = LogManager.getLogger(MethodUtils.class);

	private static final String SETTER_PREFIX = "set";

	private static final String GETTER_PREFIX = "get";

	/**
	 * 获取clazz的methodName(**)方法，返回多个重载方法中的任一个.
	 * <li>范围：public方法；包括类方法和实例方法</li>
	 * <li>模糊：忽略参数</li>
	 *  
	 * @author fengdg  
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	public static Method getPublicMethodWithoutParam(Class<?> clazz, String methodName) {
		Map<String, List<Method>> methodPublicMap = getPublicMethodMap(clazz);
		List<Method> methodList = methodPublicMap.get(methodName);
		if (methodList == null || methodList.size() == 0) {
			return null;
		} else {
			return methodList.get(0);
		}
	}
	
	 /**
     * 获取clazz所有的public方法
	 * <li>范围：public方法；包括类方法和实例方法</li>
     *  
     * @author fengdg  
     * @param clazz
     * @return
     */
    public static Map<String, List<Method>> getPublicMethodMap(final Class<?> clazz) {
    	Map<String, List<Method>> methodMap = new HashMap<String, List<Method>>();
    	Method[] methods = clazz.getMethods();
    	for (Method method : methods) {
    		List<Method> methodList = methodMap.get(method.getName());
    		if (methodList == null) {
    			methodList = new ArrayList<Method>();
    			methodMap.put(method.getName(), methodList);
			}
    		methodList.add(method);
		}
		return methodMap;
	}	
	
    /**
     * 获取clazz的methodName(parameterTypes)方法.
	 * <li>范围：public方法；包括类方法和实例方法</li>
     * <li>精确：完全匹配参数</li>
     *  
     * @author fengdg  
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @return
     */
	public static Method getPublicMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
		try {
			return clazz.getMethod(methodName, parameterTypes);
		} catch (SecurityException | NoSuchMethodException e) {
			log.debug(clazz.getName() + " don't have method " + methodName);
		}
		return null;
	}	
    
    /**
	 * 获取getter方法名的Map
	 * <li>范围：public方法；包括类方法和实例方法</li>
	 * 
	 * @author fengdg  
	 * @param clazz
	 * @return
	 */
	public static Map<String, String> getGetterMethodNameMap(Class<?> clazz) {
		Map<String, String> propGetterMap = new HashMap<String, String>();
		Map<String, Field> fieldMap = FieldUtils.getFieldFlatMap(clazz);
		for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
			String propName = entry.getKey();
			propGetterMap.put(propName, getGetterMethodName(clazz, propName));
		}
		return propGetterMap;
	}
    
	/**
	 * 获取getter方法名
	 * <li>范围：public方法；包括类方法和实例方法</li>
	 * 
	 * 
	 * @author fengdg  
	 * @param propertyName
	 * @return
	 */
	public static String getGetterMethodName(Class<?> clazz, String propertyName ) {
		String getterName = GETTER_PREFIX + StringUtils.capitalize(propertyName);
		Method method = getPublicMethod(clazz, getterName);
		if (method == null) {
			getterName = GETTER_PREFIX + propertyName;
			method = getPublicMethod(clazz, getterName);
		}
		if (method != null) {
			return method.getName();
		}
		return null;
	}
    
	/**
	 * 获取setter方法名
	 * <li>范围：public方法；包括类方法和实例方法</li>
	 * 
	 * @author fengdg  
	 * @param propertyName
	 * @return
	 */
	public static String getSetterMethodName(Class<?> clazz, String propertyName) {
		String setterName = SETTER_PREFIX + StringUtils.capitalize(propertyName);
		Method method = getPublicMethodWithoutParam(clazz, setterName);
		if (method == null) {
			setterName = SETTER_PREFIX + propertyName;
			method = getPublicMethodWithoutParam(clazz, setterName);
		}
		if (method != null) {
			return method.getName();
		}
		return null;
	}

	/**
	 * 获取setter方法名
	 *  
	 * @author fengdg  
	 * @param propertyName
	 * @return
	 */
	public static String getSetterMethodName(Class<?> clazz, String propertyName, Class<?> propType) {
		String setterName = SETTER_PREFIX + StringUtils.capitalize(propertyName);
		try {
			clazz.getMethod(setterName, propType);
			return setterName;
		} catch (NoSuchMethodException | SecurityException e) {
			setterName = SETTER_PREFIX + propertyName;
			try {
				clazz.getMethod(setterName, propType);
				return setterName;
			} catch (NoSuchMethodException | SecurityException e1) {
				log.debug(clazz.getName() + " don't have method " + setterName);
			}
			log.debug(clazz.getName() + " don't have method " + setterName);
		}
		return "";
	}

	/**
	 * 获取方法签名 
	 *  
	 * @author fengdg  
	 * @param method
	 * @return
	 */
	public static String getMethodSign(Method method) {
		String clazzName = method.getClass().getName();
		return clazzName + method.toGenericString();
	}

	/**
	 * 简易方法签名
	 *  
	 * @author fengdg  
	 * @param target
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static String getMethodSignLite(Object target, String methodName, Class<?>[] parameterTypes) {
		return getMethodSignLite(target.getClass(), methodName, parameterTypes);
	}

	/**
	 * 获取定义指定方法的类型
	 * 
	 * 2019年6月15日 下午8:50:05
	 * @auther qiuxs
	 * @param aClass
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static Class<?> getDeclaringType(Class<?> aClass, String methodName, Class<?>[] parameterTypes) {
		Method method = null;
		Class<?> findClass = aClass;
		do {
			Class<?>[] clazzes = findClass.getInterfaces();
			for (Class<?> clazz : clazzes) {
				try {
					method = clazz.getDeclaredMethod(methodName, parameterTypes);
				} catch (NoSuchMethodException e) {
					method = null;
				}
				if (method != null) {
					return clazz;
				}
			}
			findClass = findClass.getSuperclass();
		} while (!findClass.equals(Object.class));
		return aClass;
	}

	/**
	 * 提取带有指定注解的方法列表
	 * @author qiuxs
	 *
	 * @param clz
	 * @param anno
	 * @param includeSuper
	 * @return
	 *
	 * 创建时间：2018年8月6日 下午9:49:14
	 */
	public static List<Method> getDeclaredMethods(Class<?> clz, Class<? extends Annotation> annotationClass, boolean includeSuper) {
		List<Method> declaredMethods = getDeclaredMethods(clz, includeSuper);
		for (Iterator<Method> iter = declaredMethods.iterator(); iter.hasNext();) {
			Method method = iter.next();
			Annotation annotation = method.getAnnotation(annotationClass);
			if (annotation == null) {
				iter.remove();
			}
		}
		return declaredMethods;
	}

	/**
	 * 获取所有定义的方法
	 * @author qiuxs
	 *
	 * @param clz
	 * 		类型
	 * @param includeSuper 
	 * 		是否需要包含父类的方法
	 * @return
	 *
	 * 创建时间：2018年8月6日 下午9:34:59
	 */
	public static List<Method> getDeclaredMethods(Class<?> clz, boolean includeSuper) {
		List<Method> methods = new ArrayList<>();
		Method[] declaredMethods = clz.getDeclaredMethods();
		for (Method m : declaredMethods) {
			methods.add(m);
		}
		Class<?> superclass = clz.getSuperclass();
		// 提取父类方法
		if (includeSuper && !(superclass.equals(Object.class))) {
			methods.addAll(getDeclaredMethods(superclass, includeSuper));
		}
		return methods;
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
	 * 调用Getter方法.
	 */
	public static Object invokeGetter(Object obj, String propertyName) {
		String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(propertyName);
		return invokeMethod(obj, getterMethodName, new Class[] {}, new Object[] {});
	}

	/**
	 * 调用Setter方法, 仅匹配方法名。
	 * 可以忽略存取权限，但不能转类型
	 */
	public static void invokeSetter(Object obj, String propertyName, Object value) {
		String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(propertyName);
		invokeMethodByName(obj, setterMethodName, new Object[] { value });
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符，
	 * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
	 * 只匹配函数名，如果有多个同名函数调用第一个。
	 */
	public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
		Method method = getAccessibleMethodByName(obj, methodName);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用.
	 * 同时匹配方法名+参数类型，
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
	        final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 匹配函数名+参数类型。
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,
	        final Class<?>... parameterTypes) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			try {
				Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
				makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 只匹配函数名。
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			Method[] methods = searchType.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					makeAccessible(method);
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if ((e instanceof IllegalAccessException) || (e instanceof IllegalArgumentException) || (e instanceof NoSuchMethodException)) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}
}
