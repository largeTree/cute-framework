package com.qiuxs.cuteframework.core.basic.code;

import java.util.HashMap;
import java.util.Map;

import com.qiuxs.cuteframework.core.basic.code.annotation.Code;
import com.qiuxs.cuteframework.core.basic.code.annotation.CodeDomain;
import com.qiuxs.cuteframework.core.basic.code.provider.DirectCodeHouse;
import com.qiuxs.cuteframework.core.basic.code.utils.CodeUtils;

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
	
	@CodeDomain
	public static final String DOMAIN_LOG_LEVEL = "domain.LogLevel";
	
	@Code(domain = DOMAIN_LOG_LEVEL, caption = "DEBUG")
	public static final String DEBUG = "debug";
	@Code(domain = DOMAIN_LOG_LEVEL, caption = "INFO")
	public static final String INFO = "info";
	@Code(domain = DOMAIN_LOG_LEVEL, caption = "WRAN")
	public static final String WRAN = "wran";
	@Code(domain = DOMAIN_LOG_LEVEL, caption = "ERROR")
	public static final String ERROR = "error";
	@Code(domain = DOMAIN_LOG_LEVEL, caption = "FATAL")
	public static final String FATAL = "fatal";

	private static Map<String, DirectCodeHouse<?>> codeHouses = new HashMap<>();

	static {
		CodeUtils.genDirectCode(DirectCodeCenter.class);
	}

	/**
	 * 创建一个直接编码集
	 * 
	 * 2019年3月28日 下午9:19:22
	 * @auther qiuxs
	 * @param domain
	 * @return
	 */
	private static <C> DirectCodeHouse<C> createCodeHouse(String domain) {
		DirectCodeHouse<C> codeHouse = new DirectCodeHouse<C>();
		if (codeHouses.containsKey(domain)) {
			throw new RuntimeException("Duplicate codeDomain [" + domain + "]");
		}
		codeHouses.put(domain, codeHouse);
		return codeHouse;
	}

	/**
	 * 获取一个直接编码集
	 * 
	 * 2019年3月28日 下午9:27:28
	 * @auther qiuxs
	 * @param domain
	 * 		编码域
	 * @param createFlag
	 * 		不存在是否自动创建
	 * @return
	 */
	public static <C> DirectCodeHouse<C> getDirectCodeHouse(String domain, boolean createFlag) {
		@SuppressWarnings("unchecked")
		DirectCodeHouse<C> codeHouse = (DirectCodeHouse<C>) codeHouses.get(domain);
		if (createFlag && codeHouse == null) {
			codeHouse = createCodeHouse(domain);
		}
		return codeHouse;
	}

	/**
	 * 获取一个直接编码集
	 * 
	 * 2019年3月28日 下午9:27:28
	 * @auther qiuxs
	 * @param domain
	 * @return
	 */
	public static <C> DirectCodeHouse<C> getDirectCodeHouse(String domain) {
		return getDirectCodeHouse(domain, false);
	}

}
