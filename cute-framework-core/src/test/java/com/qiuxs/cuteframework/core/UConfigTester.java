package com.qiuxs.cuteframework.core;

import org.junit.Test;

import com.qiuxs.cuteframework.core.basic.config.uconfig.IConfiguration;
import com.qiuxs.cuteframework.core.basic.config.uconfig.UConfigUtils;

public class UConfigTester {

	@Test
	public void testUConfig() {
		IConfiguration db = UConfigUtils.getDomain("db");
		if (db != null) {
			System.out.println(db.getString("driver"));
		}
	}

}
