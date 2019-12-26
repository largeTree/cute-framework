package com.qiuxs.cuteframework.core.basic.code.provider;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 直接编码集
 * @author qiuxs
 * 2019年3月28日 下午9:21:17
 * @param <C>
 */
public class DirectCodeHouse<C> implements ICodeTranslator<C> {

	private Map<C, CodeOption<C>> codeHouse = new LinkedHashMap<C, CodeOption<C>>() {
		private static final long serialVersionUID = 244883989680981622L;

		public CodeOption<C> get(Object key) {
			CodeOption<C> codeOption = super.get(key);
			if (codeOption == null) {
				return CodeOption.emptyOption();
			}
			return codeOption;
		}

	};

	/**
	 * 添加一个code
	 * 
	 * 2019年3月28日 下午9:21:26
	 * @auther qiuxs
	 * @param code
	 * @param caption
	 */
	public void addCode(C code, String caption) {
		codeHouse.put(code, new CodeOption<C>(code, caption));
	}

	/**
	 * 2019年3月28日 下午9:22:15
	 * qiuxs
	 * @see com.qiuxs.cuteframework.core.basic.code.provider.ICodeTranslatable#getCaption(java.lang.Object)
	 */
	@Override
	public String getCaption(C code) {
		return codeHouse.get(code).getCaption();
	}

	@Override
	public List<CodeOption<?>> getOptions() {
		return new ArrayList<>(codeHouse.values());
	}

}
