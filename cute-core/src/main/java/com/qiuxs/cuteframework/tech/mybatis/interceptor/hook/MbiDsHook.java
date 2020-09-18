package com.qiuxs.cuteframework.tech.mybatis.interceptor.hook;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.persistent.database.lookup.DataSourceContext;
import com.qiuxs.cuteframework.tech.mybatis.interceptor.utils.MbiUtils;

@Component
public class MbiDsHook implements IMbiHook {

	private static Logger log = LoggerFactory.getLogger(MbiDsHook.class);

	@Override
	public int getOrder() {
		return -1;
	}

	@Override
	public void beforeExecutor(Invocation invocation) {
		boolean dsSwitchAuto = DataSourceContext.isDsSwitchAuto();
		Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement) args[0];
		String nameSpace = MbiUtils.getNameSpace(ms);
		if (log.isDebugEnabled()) {
			log.debug("nameSpace = {}, dsSwitchAuto = {}", nameSpace, dsSwitchAuto);
		}
		if (dsSwitchAuto) {
			String dsType = null;
			String dsId = null;

			dsType = DataSourceContext.getDsType(nameSpace);
			if (dsType != null) {
				dsId = DataSourceContext.getDsIdByNameSpace(nameSpace);
				if (log.isDebugEnabled()) {
					log.debug("ms.id = " + ms.getId() + ", dsType = " + dsType + ", dsId = " + dsId);
				}
				if (dsId != null) {
					DataSourceContext.setUpDs(dsId);
				}
			}
		}

	}

}
