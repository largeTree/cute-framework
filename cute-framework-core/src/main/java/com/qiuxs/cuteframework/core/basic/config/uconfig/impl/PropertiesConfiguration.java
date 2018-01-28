package com.qiuxs.cuteframework.core.basic.config.uconfig.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.qiuxs.cuteframework.core.basic.config.uconfig.IConfiguration;
import com.qiuxs.cuteframework.core.basic.utils.io.FileUtils;

public class PropertiesConfiguration implements IConfiguration {

	private Map<String, String> configItems = new HashMap<String, String>();

	public PropertiesConfiguration(String location) throws IOException {
		InputStream in = FileUtils.readAsInputStream(location);
		this.load(in);
	}

	public PropertiesConfiguration(InputStream in) throws IOException {
		this.load(in);
	}

	public PropertiesConfiguration(Properties props) {
		this.setProperties(props);
	}

	private void load(InputStream in) throws IOException {
		Properties props = new Properties();
		props.load(in);
		this.setProperties(props);
	}

	private void setProperties(Properties props) {
		// Properties的方法多为同步方法 只读的属性没必要加同步 所以转为Map存储
		for (Iterator<Entry<Object, Object>> iter = props.entrySet().iterator(); iter.hasNext();) {
			Entry<Object, Object> entry = iter.next();
			configItems.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		}
	}

	@Override
	public Object get(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getString(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getString(String key, String defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringMust(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Byte getByte(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte getByteValue(String key, byte defaultValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Byte getByte(String key, Byte defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Byte getByteMust(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getInteger(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIntValue(String key, int defaultValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer getInteger(String key, Integer defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getIntegerMust(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getLong(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getLong(String key, Long defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getLongValues(String key, long defaultValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Long getLongMust(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getDouble(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getDouble(String key, Double defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDoubleValue(String key, double defaultValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Double getDoubleMust(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
