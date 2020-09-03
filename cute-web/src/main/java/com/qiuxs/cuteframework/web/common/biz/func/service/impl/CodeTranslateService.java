package com.qiuxs.cuteframework.web.common.biz.func.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.basic.code.DirectCodeCenter;
import com.qiuxs.cuteframework.core.basic.code.provider.CodeOption;
import com.qiuxs.cuteframework.core.basic.code.provider.DirectCodeHouse;
import com.qiuxs.cuteframework.core.basic.utils.ListUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.basic.utils.TypeAdapter;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.persistent.database.service.ifc.ICodeOptionServiceProvider;
import com.qiuxs.cuteframework.core.persistent.util.CodeTranslateUtils;

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
	public List<CodeOption<?>> queryServiceCodes(String codeDomain, String searchToken, String code, boolean multiple) {
		String[] split = codeDomain.split("-");
		String svcName = split[0] + "Service";
		@SuppressWarnings("rawtypes")
		ICodeOptionServiceProvider codeOptionServiceProvider = ApplicationContextHolder.getBean(svcName, ICodeOptionServiceProvider.class);
		if (StringUtils.isNotBlank(code)) {
			Class<?> codeType = CodeTranslateUtils.getCodeType(codeOptionServiceProvider);
			if (multiple) {
				String[] codeArr = code.split(",");
				List<Object> codes = new ArrayList<>();
				for (String singleCode : codeArr) {
					codes.add(TypeAdapter.adapter(singleCode, codeType));
				}
				List<String> captions = codeOptionServiceProvider.getCaptions(codes);
				List<CodeOption<?>> codeOptions = new ArrayList<CodeOption<?>>(captions.size());
				for (int i = 0; i < codeArr.length; i++) {
					CodeOption<?> codeOption = new CodeOption<Object>(codes.get(i), captions.get(i));
					codeOptions.add(codeOption);
				}
				return codeOptions;
			} else {
				Object newCode = TypeAdapter.adapter(code, codeType);
				String caption = codeOptionServiceProvider.getCaption(newCode);
				return ListUtils.genList(new CodeOption<Object>(newCode, caption));
			}
		} else {
			List<?> searchOptions = codeOptionServiceProvider.searchOptions(searchToken);
			return (List<CodeOption<?>>) searchOptions;
		}
	}

}
