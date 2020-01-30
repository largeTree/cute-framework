package com.qiuxs.cuteframework.tech.mc;

import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
import com.qiuxs.cuteframework.tech.mc.McFactory.McServerType;

/**
 * 
 * 功能描述: 初始化缓存类型<br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年1月30日 下午12:12:25 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class McWebLifecycle implements IWebLifecycle {

	@Override
	public int order() {
		return 1;
	}
	
	@Override
	public void lastInit() {
		McServerType serverType = EnvironmentContext.getMcServerType();
		McFactory.init(serverType);
	}

}
