package com.qiuxs.cuteframework.mock;

import java.util.HashMap;
import java.util.Map;

import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.gconfig.client.GConfigClientUtils;

import mockit.Mock;
import mockit.MockUp;

public class GConfigClientUtilsMock {

	private static boolean mocked = false;
	private static Map<String, String> gconfigs = new HashMap<String, String>();

	public static void mock() {
		if (mocked) {
			return;
		}
		new MockUp<GConfigClientUtils>(GConfigClientUtils.class) {
			@Mock
			public String getSystemConfig(String code) {
				String val = gconfigs.get(code);
				if (val == null) {
					ExceptionUtils.throwRuntimeException(code + "不存在");
				}
				return val;
			}

			@Mock
			public int getSystemConfigInt(String code, int defVal) {
				String val = getSystemConfig(code);
				if (val == null) {
					return defVal;
				}
				return Integer.parseInt(val);
			}
		};
		mocked = true;
	}

	public static void setGconfig(String code, String val) {
		mock();
		gconfigs.put(code, val);
	}

}
