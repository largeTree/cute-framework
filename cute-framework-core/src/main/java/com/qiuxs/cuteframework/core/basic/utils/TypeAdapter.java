package com.qiuxs.cuteframework.core.basic.utils;

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

	public static Object adapter(Object obj, Class<?> type) {
		if (type == null || obj == null) {
			return obj;
		}
		String simpleTypeName = type.getSimpleName();
		if (BYTE_TYPE_NAME_SIMPLE.equals(simpleTypeName) || BYTE_VALUE_TYPE_NAME_SIMPLE.equals(simpleTypeName)) {
			return Byte.valueOf(obj.toString());
		} else if (CHARACTER_TYPE_NAME_SIMPLE.equals(simpleTypeName) || CHARCTER_VALUE_TYPE_NAME_SIMPLE.equals(simpleTypeName)) {
			return obj.toString().charAt(0);
		} else if (INTEGER_TYPE_NAME_SIMPLE.equals(simpleTypeName) || INTEGER_VALUE_TYPE_NAME_SIMPLE.equals(simpleTypeName)) {
			return Integer.valueOf(obj.toString());
		} else if (LONG_TYPE_NAME_SIMPLE.equals(simpleTypeName) || LONG_VALUE_TYPE_NAME_SIMPLE.equals(simpleTypeName)) {
			return Long.valueOf(obj.toString());
		} else if (FLOAT_TYPE_NAME_SIMPLE.equals(simpleTypeName) || FLOAT_VALUE_TYPE_NAME_SIMPLE.equals(simpleTypeName)) {
			return Float.valueOf(obj.toString());
		} else if (DOUBLE_TYPE_NAME_SIMPLE.equals(simpleTypeName) || DOUBLE_VALUE_TYPE_NAME_SIMPLE.equals(simpleTypeName)) {
			return Double.valueOf(obj.toString());
		}
		return obj;
	}
}