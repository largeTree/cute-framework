package com.qiuxs.cuteframework.core.basic.code;

import com.qiuxs.cuteframework.core.basic.code.annotation.Code;
import com.qiuxs.cuteframework.core.basic.code.annotation.CodeDomain;

/**
 * 直接编码集
 * @author qiuxs
 * 2019年3月27日 下午10:20:14
 */
public class DirectCodeCenter {

	@CodeDomain
	public static final String DOMAIN_GENDER = "domain.Gender";

	@Code(domain = DOMAIN_GENDER, caption = "未知")
	public static final int GENDER_UNKONW = 0;
	@Code(domain = DOMAIN_GENDER, caption = "男")
	public static final int GENDER_MAN = 1;
	@Code(domain = DOMAIN_GENDER, caption = "女")
	public static final int GENDER_WOMEN = 2;
	
	

}
