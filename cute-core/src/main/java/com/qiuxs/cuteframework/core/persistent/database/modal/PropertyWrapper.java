package com.qiuxs.cuteframework.core.persistent.database.modal;

import com.qiuxs.cuteframework.core.basic.code.provider.ICodeTranslatable;

/**
 * 属性
 * @author qiuxs
 *
 * @param <T>
 */
public class PropertyWrapper<C> {

	/** 字段描述 */
	private BaseField field;

	private ICodeTranslatable<C> codeTranslate;

	public PropertyWrapper(BaseField field, ICodeTranslatable<C> codeTranslate) {
		this.field = field;
		this.codeTranslate = codeTranslate;
	}

	/**
	 * 获取字段描述
	 * @return
	 */
	public BaseField getField() {
		return field;
	}

	/**
	 * 获取翻译器
	 * @return
	 */
	public ICodeTranslatable<C> getCodeTranslate() {
		return this.codeTranslate;
	}

	/**
	 * 判断是否有翻译器
	 * @author qiuxs
	 *
	 * @return
	 *
	 * 创建时间：2018年9月20日 下午11:09:01
	 */
	public boolean hasTranslater() {
		return this.codeTranslate != null;
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
		return null;
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
