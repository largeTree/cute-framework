package com.qiuxs.cuteframework.core.basic.code.provider;

import java.io.Serializable;

/***
 * 单个选项
 * @author qiuxs
 *
 */
public class CodeOption<C> implements Serializable {

	private static final long serialVersionUID = 4533987621202560558L;

	private static final CodeOption<?> emptyOption = new CodeOption<>();
	
	/** 编码 */
	private C code;
	/** 翻译 */
	private String caption;

	public CodeOption() {
	}

	public CodeOption(C code, String caption) {
		this.code = code;
		this.caption = caption;
	}

	public C getCode() {
		return code;
	}

	public void setCode(C code) {
		this.code = code;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	@SuppressWarnings("unchecked")
	public static <C> CodeOption<C> emptyOption() {
		return (CodeOption<C>) emptyOption;
	}
	
}
