package com.qiuxs.cuteframework.core.basic.code.provider;

import java.util.List;

public interface IMultipleCodeTranslatable<C> extends ICodeTranslatable<C> {
	
	/**
	 * 获取多个翻译
	 *  
	 * @author qiuxs  
	 * @param codes
	 * @return
	 */
	public List<String> getCaptions(List<C> codes);

}
