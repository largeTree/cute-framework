package com.qiuxs.cuteframework.web.common.biz.func.constants;

import com.qiuxs.cuteframework.core.basic.code.annotation.Code;
import com.qiuxs.cuteframework.core.basic.code.annotation.CodeDomain;

public class FuncConstants {

	@CodeDomain
	public static final String FUNC_TYPE_DOMAIN = "funcType.domain";
	@Code(domain = FUNC_TYPE_DOMAIN, caption = "顶级菜单")
	public static final int FUNC_TYPE_TOPMENU = 1;
	@Code(domain = FUNC_TYPE_DOMAIN, caption = "页面集合")
	public static final int FUNC_TYPE_PAGE_SET = 2;
	@Code(domain = FUNC_TYPE_DOMAIN, caption = "页面")
	public static final int FUNC_TYPE_PAGE = 3;
	@Code(domain = FUNC_TYPE_DOMAIN, caption = "页面级按钮")
	public static final int FUNC_TYPE_PAGE_BTN = 4;
	@Code(domain = FUNC_TYPE_DOMAIN, caption = "行级按钮")
	public static final int FUNC_TYPE_LINE_BTN = 5;

	@CodeDomain
	public static final String FUNC_TERM_TYPE_DOMAIN = "funcTermType.domain";
	@Code(domain = FUNC_TERM_TYPE_DOMAIN, caption = "Web")
	public static final long FUNC_TERM_TYPE_WEB = 0;

}
