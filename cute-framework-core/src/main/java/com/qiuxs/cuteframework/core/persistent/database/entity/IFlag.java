package com.qiuxs.cuteframework.core.persistent.database.entity;

import com.qiuxs.cuteframework.core.basic.code.annotation.Code;
import com.qiuxs.cuteframework.core.basic.code.annotation.CodeDomain;

/**
 * 记录状态
 * @author qiuxs
 *
 */
public interface IFlag {

	@CodeDomain
	public static final String FLAG_DOMAIN = "flagDomain";
	@Code(domain = FLAG_DOMAIN, caption = "有效")
	public static final Integer VALID = 1;
	@Code(domain = FLAG_DOMAIN, caption = "无效")
	public static final Integer INVALID = 0;
	@Code(domain = FLAG_DOMAIN, caption = "删除")
	public static final Integer DELETED = -1;

	public Integer getFlag();

	public void setFlag();

}
