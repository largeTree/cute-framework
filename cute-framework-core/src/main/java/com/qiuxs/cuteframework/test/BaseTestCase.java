package com.qiuxs.cuteframework.test;

import org.junit.After;
import org.junit.Before;

public class BaseTestCase {

	private static final String LABLE = " ------------------------ ";

	private long startTime;

	@Before
	public final void before() {
		this.startTime = System.currentTimeMillis();
		System.out.println(LABLE + "Start TestCase " + this.getClass().getName() + "@" + this.hashCode() + LABLE);
		this.setUp();
	}

	protected void setUp() {
		// do something prepare
	}

	@After
	public final void after() {
		long endTime = System.currentTimeMillis();
		System.out.println(LABLE + "TestCase Finished CostMs : " + (endTime - this.startTime) + LABLE);
		this.finished();
	}

	protected void finished() {
		// do something after
	}
}
