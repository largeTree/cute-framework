package com.qiuxs.cuteframework.core.basic.config;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class DefaultPropertiesConfiguration extends AbstractConfiguration {

	public DefaultPropertiesConfiguration(String merge) {
		super(merge);
	}

	public void addProperties(Properties properties) {
		Set<Entry<Object, Object>> entrySet = properties.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			super.put(entry.getKey().toString(), entry.getValue().toString());
		}
	}

}
