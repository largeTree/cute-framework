package com.qiuxs.cuteframework.core.persistent.database.modal;

import static com.qiuxs.cuteframework.core.basic.utils.TypeAdapter.BIGDECIMAL_TYPE_NAME;
import static com.qiuxs.cuteframework.core.basic.utils.TypeAdapter.BYTE_TYPE_NAME_SIMPLE;
import static com.qiuxs.cuteframework.core.basic.utils.TypeAdapter.BYTE_VALUE_TYPE_NAME_SIMPLE;
import static com.qiuxs.cuteframework.core.basic.utils.TypeAdapter.CHARACTER_TYPE_NAME_SIMPLE;
import static com.qiuxs.cuteframework.core.basic.utils.TypeAdapter.CHARCTER_VALUE_TYPE_NAME_SIMPLE;
import static com.qiuxs.cuteframework.core.basic.utils.TypeAdapter.DATE_TYPE_NAME;
import static com.qiuxs.cuteframework.core.basic.utils.TypeAdapter.DOUBLE_TYPE_NAME_SIMPLE;
import static com.qiuxs.cuteframework.core.basic.utils.TypeAdapter.DOUBLE_VALUE_TYPE_NAME_SIMPLE;
import static com.qiuxs.cuteframework.core.basic.utils.TypeAdapter.FLOAT_TYPE_NAME_SIMPLE;
import static com.qiuxs.cuteframework.core.basic.utils.TypeAdapter.FLOAT_VALUE_TYPE_NAME_SIMPLE;
import static com.qiuxs.cuteframework.core.basic.utils.TypeAdapter.INTEGER_TYPE_NAME_SIMPLE;
import static com.qiuxs.cuteframework.core.basic.utils.TypeAdapter.INTEGER_VALUE_TYPE_NAME_SIMPLE;
import static com.qiuxs.cuteframework.core.basic.utils.TypeAdapter.LONG_TYPE_NAME_SIMPLE;
import static com.qiuxs.cuteframework.core.basic.utils.TypeAdapter.LONG_VALUE_TYPE_NAME_SIMPLE;
import static com.qiuxs.cuteframework.core.basic.utils.TypeAdapter.STRING_TYPE_NAME;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * 基础字段配置
 * 
 * @author qiuxs
 *
 */
public class BaseField {

	/** 字段名 */
	private String name;
	/** 字段标签 */
	private String label;
	/** 字段类型 */
	private String type;
	/** 字段类型Class */
	private Class<?> typeClass;
	/** 默认值 */
	private Object defaultValue;

	public BaseField(String name, String label, Class<?> typeClass) {
		this(name, label, typeClass, null);
	}

	public BaseField(String name, String label, Class<?> typeClass, Object defaultValue) {
		this.name = name;
		this.label = label;
		this.typeClass = typeClass;
		this.defaultValue = defaultValue;
		this.type = typeClass.getSimpleName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Class<?> getTypeClass() {
		return typeClass;
	}

	public void setTypeClass(Class<?> typeClass) {
		this.typeClass = typeClass;
	}

	public Object getDefaultValue() {
		if (this.defaultValue != null) {
			return this.defaultValue;
		}
		String simpleTypeName = this.typeClass.getSimpleName();
		if (BYTE_TYPE_NAME_SIMPLE.equals(simpleTypeName) || BYTE_VALUE_TYPE_NAME_SIMPLE.equals(simpleTypeName)) {
			return Byte.valueOf("0");
		} else if (CHARACTER_TYPE_NAME_SIMPLE.equals(simpleTypeName) || CHARCTER_VALUE_TYPE_NAME_SIMPLE.equals(simpleTypeName)) {
			return '\u0000';
		} else if (INTEGER_TYPE_NAME_SIMPLE.equals(simpleTypeName) || INTEGER_VALUE_TYPE_NAME_SIMPLE.equals(simpleTypeName)) {
			return 0;
		} else if (LONG_TYPE_NAME_SIMPLE.equals(simpleTypeName) || LONG_VALUE_TYPE_NAME_SIMPLE.equals(simpleTypeName)) {
			return 0L;
		} else if (FLOAT_TYPE_NAME_SIMPLE.equals(simpleTypeName) || FLOAT_VALUE_TYPE_NAME_SIMPLE.equals(simpleTypeName)) {
			return 0F;
		} else if (DOUBLE_TYPE_NAME_SIMPLE.equals(simpleTypeName) || DOUBLE_VALUE_TYPE_NAME_SIMPLE.equals(simpleTypeName)) {
			return 0D;
		} else if (BIGDECIMAL_TYPE_NAME.equals(simpleTypeName)) {
			return BigDecimal.ZERO;
		} else if (DATE_TYPE_NAME.equals(simpleTypeName)) {
			return Calendar.getInstance().getTime();
		} else if (STRING_TYPE_NAME.equals(simpleTypeName)) {
			return "";
		}
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

}
