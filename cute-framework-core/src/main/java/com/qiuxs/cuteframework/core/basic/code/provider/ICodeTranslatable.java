package com.qiuxs.cuteframework.core.basic.code.provider;

/**
 * 简单翻译接口
 * @author qiuxs
 *
 * @param <C>
 */
public interface ICodeTranslatable<C> {

	/**
	 * 获取翻译
	 * @param code
	 * @return
	 */
	public String getCaption(C code);

}
