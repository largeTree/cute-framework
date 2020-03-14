package com.qiuxs.cuteframework.tech.mybatis.lc;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;

import com.qiuxs.cuteframework.core.basic.utils.reflect.FieldUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
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
	
	private static Logger log = LogManager.getLogger(MyBatisLifecycle.class);

	@Override
	public void lastInit() {
		if (EnvironmentContext.getBool("auto-refresh-mybatis-mapper", false)) {
			SqlSessionFactoryBean sqlSessionFactoryBean = ApplicationContextHolder.getBean(SqlSessionFactoryBean.class);
			Resource[] mapperLocations;
			try {
				mapperLocations = (Resource[]) FieldUtils.getFieldValue(sqlSessionFactoryBean, "mapperLocations");
			} catch (ReflectiveOperationException e) {
				log.error("start MyBatisRefresher Failed, getMapperLocations Failed, ext = " + e.getLocalizedMessage(), e);
				return;
			}
			Set<String> paths = new HashSet<String>();
			for (Resource res : mapperLocations) {
				try {
					paths.add(res.getFile().getParent());
				} catch (IOException e) {
					if (log.isDebugEnabled()) {
						log.debug(res.getDescription() + ", getPathFailed, ext = " + e.getLocalizedMessage(), e);
					}
					continue;
				}
			}
			MybatisMapperRefresher.startRefresher(paths);
		}
	}

	@Override
	public int order() {
		return 1;
	}

}
