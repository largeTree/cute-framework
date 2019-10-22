package com.qiuxs.cuteframework.func.helper;

import org.junit.Test;

import com.qiuxs.cuteframework.common.biz.func.helper.FuncInitHelper;
import com.qiuxs.cuteframework.test.BaseTestCase;

public class FuncInitHelperTester extends BaseTestCase {

	@Test
	public void testInit() {
		FuncInitHelper helper = new FuncInitHelper();
		helper.init();
	}
	
}
