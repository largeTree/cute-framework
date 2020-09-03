package com.qiuxs.cuteframework.core.persistent.database.modal;

import java.util.ArrayList;
import java.util.List;

import com.qiuxs.cuteframework.core.basic.code.provider.ICodeTranslatable;
import com.qiuxs.cuteframework.core.basic.code.provider.IMultipleCodeTranslatable;
import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.core.basic.utils.TypeAdapter;
import com.qiuxs.cuteframework.core.persistent.util.CodeTranslateUtils;

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
	private boolean mutiple;

	public PropertyWrapper(BaseField field, ICodeTranslatable<C> codeTranslate) {
		this(field, codeTranslate, false);
	}
	
	public PropertyWrapper(BaseField field, ICodeTranslatable<C> codeTranslate, boolean mutiple) {
		this.field = field;
		this.codeTranslate = codeTranslate;
		this.mutiple = mutiple;
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
	 * @param code
	 * @return
	 */
	public String getCaption(Object code) {
		if (code == null) {
			return null;
		}
		if (this.codeTranslate != null) {
			if (this.mutiple) {
				IMultipleCodeTranslatable<C> mcodeTranslatable = (IMultipleCodeTranslatable<C>) this.codeTranslate;
				
				Class<?> codeType = CodeTranslateUtils.getCodeType(this.codeTranslate);
				
				String[] codeArray = ((String)code).split(",");
				List<C> codes = new ArrayList<>(codeArray.length);
				for (String codeStr : codeArray) {
					codes.add(this.getNewValue(codeStr, codeType));
				}
				
				return ListUtils.listToString(mcodeTranslatable.getCaptions(codes));
			} else {
				return this.codeTranslate.getCaption(getNewValue(code, null));
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private C getNewValue(Object code, Class<?> paramType) {
		if (code == null) {
			return null;
		} else {
			if (paramType != null) {
				return (C) TypeAdapter.adapter(code, paramType);
			} else {
				return (C) code;
			}
		}
	}
}
