package com.qiuxs.cuteframework.common.biz.func.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.basic.code.DirectCodeCenter;
import com.qiuxs.cuteframework.core.basic.code.provider.CodeOption;
import com.qiuxs.cuteframework.core.basic.code.provider.DirectCodeHouse;

@Service
public class CodeTranslateService {

	public List<CodeOption<?>> getCodeOptions(String codeDomain) {
		DirectCodeHouse<?> directCodeHouse = DirectCodeCenter.getDirectCodeHouse(codeDomain);
		List<CodeOption<?>> options;
		if (directCodeHouse != null) {
			options = directCodeHouse.getOptions();
		} else {
			options = new ArrayList<>();
		}
		return options;
	}
	
}
