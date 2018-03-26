package com.qiuxs.cuteframework.core.basic.config.uconfig;

/**
 * 实现此接口用于uconfig.xml时，必须实现一个以单个InputStream为参数的构造器
 * @author qiuxs
 *
 */
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
