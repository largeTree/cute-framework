package com.qiuxs.cuteframework.core.basic.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;

public abstract class AbstractConfiguration implements IConfiguration {
	
	protected static Logger log = LogManager.getLogger(AbstractConfiguration.class);

	/** 合并方式：默认为替换 */
	protected String merge = UConfigUtils.MERGE_TYPE_REPLACE;
	private Map<String, String> values = new HashMap<>();

	public AbstractConfiguration(String merge) {
		this.merge = merge;
	}

	@Override
	public String getString(String key) {
		return this.values.get(key);
	}
	
	@Override
	public String getStringMust(String key) {
		return MapUtils.getStringMust(this.values, key);
	}
	
	@Override
	public String getString(String key, String defVal) {
		String val = MapUtils.getString(this.values, key);
		return val == null ? defVal : val;
	}

	@Override
	public Integer getInteger(String key) {
		return MapUtils.getInteger(this.values, key);
	}

	@Override
	public int getInt(String key, int defaultVal) {
		Integer val = this.getInteger(key);
		if (val == null) {
			val = defaultVal;
		}
		return val;
	}

	@Override
	public Long getLong(String key) {
		return MapUtils.getLong(this.values, key);
	}

	@Override
	public long getLongValue(String key, long defaultVal) {
		return MapUtils.getLongValue(this.values, key, defaultVal);
	}
	
	@Override
	public boolean getBool(String key, boolean defaultVal) {
		return MapUtils.getBooleanValue(this.values, key, defaultVal);
	}
	
	@Override
	public Boolean getBoolean(String key) {
		return MapUtils.getBoolean(this.values, key);
	}

	@Override
	public Map<String, String> toMap() {
		return new HashMap<String, String>(this.values);
	}

	protected void clear() {
		this.values.clear();
	}

	protected void put(String key, String val) {
		this.values.put(key, val);
	}

	protected void putAll(Map<? extends String, ? extends String> map) {
		this.values.putAll(map);
	}

	protected boolean containsKey(String key) {
		return this.values.containsKey(key);
	}
	
	@Override
	public String toString() {
		return "[merge = " + this.merge + ", values = " + this.values.toString() + "]";
	}

	public String handlePath(String path) {
		// 替换路径中的系统属性
		path = StringUtils.replaceSystemProp(path);
		path = path.replace("\\", "/");
		path = path.replace("file://", "");
		return path;
	}

}
