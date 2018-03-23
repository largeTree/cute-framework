package com.qiuxs.cuteframework.core.basic.config.uconfig.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.qiuxs.cuteframework.core.basic.config.uconfig.IConfiguration;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
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
		return configItems.get(key);
	}

	@Override
	public String getString(String key) {
		return MapUtils.getString(configItems, key);
	}

	@Override
	public String getString(String key, String defaultValue) {
		String val = MapUtils.getString(configItems, key);
		return val == null ? defaultValue : val;
	}

	@Override
	public String getStringMust(String key) {
		return MapUtils.getStringMust(configItems, key);
	}

	@Override
	public Integer getInteger(String key) {
		return MapUtils.getInteger(configItems, key);
	}

	@Override
	public int getIntValue(String key, int defaultValue) {
		Integer val = this.getInteger(key);
		return val == null ? defaultValue : val.intValue();
	}

	@Override
	public Integer getIntegerMust(String key) {
		return MapUtils.getIntegerMust(configItems, key);
	}

	@Override
	public Long getLong(String key) {
		return MapUtils.getLong(configItems, key);
	}

	@Override
	public long getLongValues(String key, long defaultValue) {
		Long val = this.getLong(key);
		return val == null ? defaultValue : val.longValue();
	}

	@Override
	public Long getLongMust(String key) {
		return MapUtils.getLongMust(configItems, key);
	}

}
