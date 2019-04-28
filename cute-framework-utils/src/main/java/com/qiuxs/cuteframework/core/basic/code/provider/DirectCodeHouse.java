package com.qiuxs.cuteframework.core.basic.code.provider;

import java.util.HashMap;
import java.util.Map;

/**
 * 直接编码集
 * @author qiuxs
 * 2019年3月28日 下午9:21:17
 * @param <C>
 */
public class DirectCodeHouse<C> implements ICodeTranslatable<C> {
	
	private Map<C, String> codeHouse = new HashMap<>();

	/**
	 * 添加一个code
	 * 
	 * 2019年3月28日 下午9:21:26
	 * @auther qiuxs
	 * @param code
	 * @param caption
	 */
	public void addCode(C code, String caption) {
		codeHouse.put(code, caption);
	}
	
	/**
	 * 2019年3月28日 下午9:22:15
	 * qiuxs
	 * @see com.qiuxs.cuteframework.core.basic.code.provider.ICodeTranslatable#getCaption(java.lang.Object)
	 */
	@Override
	public String getCaption(C code) {
		return codeHouse.get(code);
	}

}
