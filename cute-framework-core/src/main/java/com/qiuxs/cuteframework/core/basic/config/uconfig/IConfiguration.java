package com.qiuxs.cuteframework.core.basic.config.uconfig;

public interface IConfiguration {

	public Object get(String key);

	public String getString(String key);

	public String getString(String key, String defaultValue);

	public String getStringMust(String key);

	public Byte getByte(String key);

	public byte getByteValue(String key, byte defaultValue);

	public Byte getByte(String key, Byte defaultValue);

	public Byte getByteMust(String key);

	public Integer getInteger(String key);

	public int getIntValue(String key, int defaultValue);

	public Integer getInteger(String key, Integer defaultValue);

	public Integer getIntegerMust(String key);

	public Long getLong(String key);

	public Long getLong(String key, Long defaultValue);

	public long getLongValues(String key, long defaultValue);

	public Long getLongMust(String key);

	public Double getDouble(String key);

	public Double getDouble(String key, Double defaultValue);

	public double getDoubleValue(String key, double defaultValue);

	public Double getDoubleMust(String key);

}
