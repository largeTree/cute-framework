package com.qiuxs.cuteframework.core.basic.utils.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.utils.DateFormatUtils;
import com.qiuxs.cuteframework.core.basic.utils.ListUtils;

public class FieldUtils {
	
	private static final Logger log = LogManager.getLogger(FieldUtils.class);
	
	/**
     * 获取clazz所有字段，平铺形式.
	 * <li>范围：含所有的存取级别；包括类字段和实例字段</li>
     * <li>平铺：子类字段覆盖同名父类字段</li>
     *  
     * @author fengdg  
     * @param clazz
     * @return
     */
    public static Map<String, Field> getFieldFlatMap(final Class<?> clazz) {
    	Map<String, Field> fieldMap = new HashMap<String, Field>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			Field[] fields = superClass.getDeclaredFields();
			for (Field field : fields) {
				//子类优先
				if (!fieldMap.containsKey(field.getName())) {
					fieldMap.put(field.getName(), field);
				}
			}
		}
		return fieldMap;
	}
	
	/**
	 * 获取List字段的泛型类型，未设置泛型的返回Null
	 * @author qiuxs
	 *
	 * @param field
	 * @return
	 *
	 * 创建时间：2018年8月11日 下午5:04:29
	 */
	public static Class<?> getListFieldParameterizedType(Field field) {
		Type genericType = field.getGenericType();
		if (genericType == null) {
			return null;
		}
		if (genericType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type type = parameterizedType.getActualTypeArguments()[0];
			return (Class<?>) type;
		}
		return null;
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
	 * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(fieldName, "fieldName can't be blank");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {// NOSONAR
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
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
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			log.error("不可能抛出的异常:{}", e);
		}
	}
	
	/**
	 * 设置属性值.
	 * 可以转类型，但只能用public方法
	 *  
	 * @author fengdg  
	 * @param bean
	 * @param name
	 * @param value
	 */
	public static void setProperty(Object bean, String name, Object value) {
		if (bean == null || value == null) {
			return;
		}
		Field field = getField(bean.getClass(), name);
		if (field == null) {
			return;
		}
		//Date类型直接设置属性
		if (!field.getType().isAssignableFrom(value.getClass()) && field.getType().equals(Date.class)) {
			Date date = DateFormatUtils.parseDate(value.toString());
			setFieldValue(bean, name, date);
			return;
		}
		try {
			BeanUtils.setProperty(bean, name, value);
		} catch (IllegalAccessException | InvocationTargetException e) {
			log.error("ex:" + e.getMessage());
		}
	}

	/**
	 * 根据字段名返回Field，不存在返回null.
	 * <li>范围：含所有的存取级别；包括类字段和实例字段</li>
	 *  
	 * @author fengdg  
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field getField(Class<?> clazz, String fieldName) {
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				return field;
			} catch (NoSuchFieldException | SecurityException e) {
				log.error("ext = " + e.getLocalizedMessage(), e);
			}
		}
		return null;
	}

	
	/**
	 * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

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
}
