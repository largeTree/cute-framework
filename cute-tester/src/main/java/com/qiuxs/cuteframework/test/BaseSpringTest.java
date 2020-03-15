package com.qiuxs.cuteframework.test;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.mock.GConfigClientUtilsMock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:test/applicationContext*.xml"
})
public class BaseSpringTest implements ApplicationContextAware {

	@Before
	public void aaaaaaaaaSetUp() {
		GConfigClientUtilsMock.mock();
	}

	@After
	public void zzzzzzzzzDown() {

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextHolder.setApplicationContext(applicationContext);
	}

}
