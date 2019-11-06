package com.qiuxs.cuteframework.tech.mybatis.lc;

import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
import com.qiuxs.cuteframework.tech.mybatis.config.MyBatisConfig;
import com.qiuxs.cuteframework.tech.mybatis.utils.MybatisMapperRefresher;

/**
 * MyBatis初始化
 * 功能描述: <p>  
 * 新增原因: TODO<p>  
 * 新增日期: 2019年11月6日 下午5:27:46 <p>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class MyBatisLifecycle implements IWebLifecycle {

	@Override
	public void lastInit() {
		MyBatisConfig mybatisConfig = ApplicationContextHolder.getBean(MyBatisConfig.class);
		if (mybatisConfig != null && mybatisConfig.isAutoRefresh()) {
			MybatisMapperRefresher.startRefresher(mybatisConfig.getMapperLocations());
		}
	}

	@Override
	public int order() {
		return 1;
	}

}
