package com.qiuxs.mq.test;

import org.junit.Test;

import com.qiuxs.cuteframework.test.BaseSpringTest;
import com.qiuxs.rmq.ConsumerUtils;

public class ConsumerUtilsTest extends BaseSpringTest {

	@Test
	public void initTest() {
		ConsumerUtils.init();
		
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
