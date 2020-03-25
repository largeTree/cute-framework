package com.qiuxs.mq.test;

import org.junit.Test;

import com.qiuxs.cuteframework.core.basic.utils.generator.RandomGenerator;
import com.qiuxs.rmq.ProducerUtils;

public class ProducerUtilsTest {

	@Test
	public void initTest() {
		ProducerUtils.init();
	}

	@Test
	public void testSend() {
		ProducerUtils.init();
		ProducerUtils.send("testTopic", "testTag", RandomGenerator.getRandomStr(), RandomGenerator.getRandomStr());
	}

}
