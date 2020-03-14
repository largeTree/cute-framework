package com.qiuxs.cuteframework.core.basic.utils.reflect;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * 反射工具集单元测试
 * 
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年3月1日 下午9:43:46 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class ReflectUtilsTest {
	
	@Test
	public void testGetSuperClassGenricType() {
		SubDemoClass sdc = new SubDemoClass();
		Class<?> resClass = ReflectUtils.getSuperClassGenricType(sdc.getClass(), 0);
		Assert.assertEquals("Assert resClass : ", Integer.class, resClass);
	}

	public static class DemoClass<T> {
		
	}
	
	public static class SubDemoClass extends DemoClass<Integer> {
		
	}
	
}
