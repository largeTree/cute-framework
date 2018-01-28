package com.qiuxs.cuteframework.core.basic.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

	public static Map<String, Object> genMap(Object... keyValues) {
		if (keyValues == null || keyValues.length == 0) {
			return new HashMap<String, Object>();
		}
		if (keyValues.length % 2 != 0) {
			throw new RuntimeException("Key/Value 必须成对出现");
		}
		Map<String, Object> map = new HashMap<String, Object>();

		for (int i = 0; i < keyValues.length - 1; i++) {
			map.put(keyValues[i] == null ? null : String.valueOf(keyValues[i]), keyValues[i + 1]);
		}

		return map;
	}

}
