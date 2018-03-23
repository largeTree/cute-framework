package com.qiuxs.cuteframework.core.basic.config.uconfig;

public interface IConfiguration {

	public Object get(String key);

	public String getString(String key);

	public String getString(String key, String defaultValue);

	public String getStringMust(String key);

	public Integer getInteger(String key);

	public int getIntValue(String key, int defaultValue);
	
	public Integer getIntegerMust(String key);

	public Long getLong(String key);

	public long getLongValues(String key, long defaultValue);

	public Long getLongMust(String key);

}
