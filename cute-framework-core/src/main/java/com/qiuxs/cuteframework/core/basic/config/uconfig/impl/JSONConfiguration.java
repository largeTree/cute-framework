package com.qiuxs.cuteframework.core.basic.config.uconfig.impl;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.config.uconfig.IConfiguration;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;

public class JSONConfiguration implements IConfiguration {

	private JSONObject items;

	public JSONConfiguration(String jsonString) throws JSONException {
		this.items = JsonUtils.string2JSONObject(jsonString);
	}

	@Override
	public Object get(String key) {
		return this.items.get(key);
	}

	@Override
	public String getString(String key) {
		return this.items.getString(key);
	}

	@Override
	public String getString(String key, String defaultValue) {
		String val = this.items.getString(key);
		return val == null ? defaultValue : val;
	}

	@Override
	public String getStringMust(String key) {
		return MapUtils.getStringMust(this.items, key);
	}

	@Override
	public Integer getInteger(String key) {
		return this.items.getInteger(key);
	}

	@Override
	public int getIntValue(String key, int defaultValue) {
		Integer val = this.getInteger(key);
		return val == null ? defaultValue : val.intValue();
	}

	@Override
	public Integer getIntegerMust(String key) {
		return MapUtils.getIntegerMust(this.items, key);
	}

	@Override
	public Long getLong(String key) {
		return this.items.getLong(key);
	}

	@Override
	public long getLongValues(String key, long defaultValue) {
		Long val = this.items.getLong(key);
		return val == null ? defaultValue : val.longValue();
	}

	@Override
	public Long getLongMust(String key) {
		return MapUtils.getLongMust(this.items, key);
	}

}
