package com.qiuxs.cuteframework.core.basic.config;

import java.util.Map;

/**
 * 简易配置对象
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2019年12月21日 下午2:46:30 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public interface IConfiguration {

	public Boolean getBoolean(String key);
	
	public boolean getBool(String key, boolean defaultVal);
	
	public String getString(String key);

	public String getString(String key, String defVal);

	public Integer getInteger(String key);

	public int getInt(String key, int defaultVal);

	public Long getLong(String key);

	public long getLongValue(String key, long defaultVal);

	public Map<String, String> toMap();

}
