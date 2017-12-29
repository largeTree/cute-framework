package com.qiuxs.cuteframework.core.basic.bean;

import java.io.Serializable;

import com.qiuxs.cuteframework.core.basic.code.provider.ICodeTranslatable;

/**
 * 属性
 * @author qiuxs
 *
 * @param <T>
 */
public class BaseProperty<F extends BaseField, C extends Serializable, TC extends ICodeTranslatable<C>> {

	/** 字段描述 */
	private F field;

	private TC codeTranslate;

	public BaseProperty(F field, TC codeTranslate) {
		this.field = field;
		this.codeTranslate = codeTranslate;
	}

	/**
	 * 获取字段描述
	 * @return
	 */
	public F getField() {
		return field;
	}

	/**
	 * 获取翻译器
	 * @return
	 */
	public TC getCodeTranslate() {
		return this.codeTranslate;
	}

	/**
	 * 获取翻译
	 * @param object
	 * @return
	 */
	public String getCaption(Object object) {
		if (object == null) {
			return null;
		}
		if (this.codeTranslate != null) {
			return this.codeTranslate.getCaption(getNewValue(object));
		}
		return object.toString();
	}

	@SuppressWarnings("unchecked")
	private C getNewValue(Object code) {
		if (code == null) {
			return null;
		} else {
			return (C) code;
		}
	}
}
