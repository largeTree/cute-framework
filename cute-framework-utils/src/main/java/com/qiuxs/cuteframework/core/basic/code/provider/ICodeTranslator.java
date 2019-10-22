package com.qiuxs.cuteframework.core.basic.code.provider;

import java.util.List;

/**
 * 翻译器
 * @author qiuxs
 *
 * @param <C>
 */
public interface ICodeTranslator<C> extends ICodeTranslatable<C> {
	
	/***
	 * 获取支持的翻译集合
	 * @return
	 */
	public List<CodeOption<?>> getOptions();
	
}
