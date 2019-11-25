package com.qiuxs.cuteframework.web.common.biz.func.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.core.basic.code.provider.CodeOption;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.web.action.IAction;
import com.qiuxs.cuteframework.web.bean.ActionResult;
import com.qiuxs.cuteframework.web.common.biz.func.service.impl.CodeTranslateService;

@Service
public class CodeTranslateAction implements IAction {
	
	@Resource
	private CodeTranslateService codeTranslateService;

	public ActionResult queryCodeOptions(Map<String, String> params) {
		String codeDomain = MapUtils.getStringMust(params, "codeDomain");
		List<CodeOption<?>> codeOptions = this.codeTranslateService.getCodeOptions(codeDomain);
		return new ActionResult(codeOptions);
	}
	
}
