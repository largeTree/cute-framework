package com.qiuxs.cuteframework.core.basic.config;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentHolder {

	private static final Map<String, String> ENVIRONMENT = new HashMap<>();

	static {
		ENVIRONMENT.put("seq_type", "db");
	}

	public static String getEnv(String name) {
		return ENVIRONMENT.get(name);
	}

}
