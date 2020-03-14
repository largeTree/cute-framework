package com.qiuxs.cuteframework.mc;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.qiuxs.cuteframework.core.basic.utils.generator.RandomGenerator;
import com.qiuxs.cuteframework.core.persistent.redis.RedisConfiguration;
import com.qiuxs.cuteframework.tech.mc.McFactory;

public class McFactoryTester {

	
	@Before
	public void setUp() {
		new RedisConfiguration().initDefaultPoll();
	}
	
	@Test
	public void testRedisMap() {
		McFactory.init();
		Map<String, String> map = McFactory.getFactory().createMap(String.class, String.class, 10, "test_string_map");
		String val = RandomGenerator.getRandomStringByLength(10);
		map.put("key", val);

		McFactory.getFactory().clearMcName();

		Map<String, String> map2 = McFactory.getFactory().createMap(String.class, String.class, 10, "test_string_map");
		String val2 = map2.get("key");
		Assert.assertEquals("Assert redis Val", val, val2);
	}
	
}
