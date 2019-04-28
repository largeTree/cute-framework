package com.qiuxs.cuteframework.core.basic.utils;

import java.math.BigDecimal;

/**
 * 类型适配器
 * @author qiuxs
 *
 */
public class TypeAdapter {

	public static final String BYTE_TYPE_NAME_SIMPLE = "Byte";
	public static final String BYTE_VALUE_TYPE_NAME_SIMPLE = "byte";
	public static final String CHARACTER_TYPE_NAME_SIMPLE = "Character";
	public static final String CHARCTER_VALUE_TYPE_NAME_SIMPLE = "char";
	public static final String INTEGER_TYPE_NAME_SIMPLE = "Integer";
	public static final String INTEGER_VALUE_TYPE_NAME_SIMPLE = "int";
	public static final String LONG_TYPE_NAME_SIMPLE = "Long";
	public static final String LONG_VALUE_TYPE_NAME_SIMPLE = "long";
	public static final String FLOAT_TYPE_NAME_SIMPLE = "Float";
	public static final String FLOAT_VALUE_TYPE_NAME_SIMPLE = "float";
	public static final String DOUBLE_TYPE_NAME_SIMPLE = "Double";
	public static final String DOUBLE_VALUE_TYPE_NAME_SIMPLE = "double";
	public static final String BIGDECIMAL_TYPE_NAME = "BigDecimal";
	public static final String DATE_TYPE_NAME = "Date";
	public static final String STRING_TYPE_NAME = "String";

	/**
	 * 类型适配
	 * @param obj
	 * @param type
	 * @return
	 */
	public static Object adapter(Object obj, Class<?> type) {
		if (type == null || obj == null) {
			return obj;
		}
		if (type.isAssignableFrom(Byte.class) || type.isAssignableFrom(byte.class)) {
			return Byte.valueOf(obj.toString());
		} else if (type.isAssignableFrom(Character.class) || type.isAssignableFrom(char.class)) {
			return obj.toString().charAt(0);
		} else if (type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class)) {
			return Integer.valueOf(obj.toString());
		} else if (type.isAssignableFrom(Long.class) || type.isAssignableFrom(long.class)) {
			return Long.valueOf(obj.toString());
		} else if (type.isAssignableFrom(Float.class) || type.isAssignableFrom(float.class)) {
			return Float.valueOf(obj.toString());
		} else if (type.isAssignableFrom(Double.class) || type.isAssignableFrom(double.class)) {
			return Double.valueOf(obj.toString());
		} else if (type.isAssignableFrom(BigDecimal.class)) {
			return BigDecimal.valueOf(Double.valueOf(obj.toString()));
		} else if (type.isAssignableFrom(String.class)) {
			return obj.toString();
		}
		return obj;
	}

	/**
	 * 转换为Integer
	 * @param obj
	 * @return
	 */
	public static Integer toInteger(Object obj) throws NumberFormatException {
		return obj == null ? null : Integer.valueOf(obj.toString());
	}

	/**
	 * 转换为Long
	 * @param obj
	 * @return
	 * @throws NumberFormatException
	 */
	public static Long toLong(Object obj) throws NumberFormatException {
		return obj == null ? null : Long.parseLong(obj.toString());
	}

	/**
	 * 转换为Boolean
	 * @author qiuxs
	 *
	 * @param obj
	 * @return
	 *
	 * 创建时间：2018年9月20日 下午11:02:25
	 */
	public static Boolean toBoolean(Object obj) {
		return obj == null ? null : Boolean.valueOf(obj.toString());
	}

}
