package com.qiuxs.cuteframework.web.common.biz.func.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.basic.code.DirectCodeCenter;
import com.qiuxs.cuteframework.core.basic.code.provider.CodeOption;
import com.qiuxs.cuteframework.core.basic.code.provider.DirectCodeHouse;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.ICodeOptionServiceProvider;

@Service
public class CodeTranslateService {

	/**
	 * 获取直接编码集
	 *  
	 * @author qiuxs  
	 * @param codeDomain
	 * @return
	 */
	public List<CodeOption<?>> getCodeOptions(String codeDomain) {
		DirectCodeHouse<?> directCodeHouse = DirectCodeCenter.getDirectCodeHouse(codeDomain);
		List<CodeOption<?>> options;
		if (directCodeHouse != null) {
			options = directCodeHouse.getOptions();
		} else {
			options = Collections.emptyList();
		}
		return options;
	}

	/**
	 * 服务编码集
	 *  
	 * @author qiuxs  
	 * @param codeDomain
	 * @param searchToken
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CodeOption<?>> queryServiceCodes(String codeDomain, String searchToken) {
		String[] split = codeDomain.split("-");
		String svcName = split[0] + "Service";
		ICodeOptionServiceProvider<?> codeOptionServiceProvider = ApplicationContextHolder.getBean(svcName, ICodeOptionServiceProvider.class);
		List<?> searchOptions = codeOptionServiceProvider.searchOptions(searchToken);
		return (List<CodeOption<?>>) searchOptions;
	}

}
