package com.qiuxs.cuteframework.tech.mybatis.interceptor.hook;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.persistent.database.lookup.DataSourceContext;
import com.qiuxs.cuteframework.tech.mybatis.interceptor.utils.MbiUtils;

@Component
public class MbiDsHook implements IMbiHook {

	private static Logger log = LogManager.getLogger(MbiDsHook.class);

	@Override
	public int getOrder() {
		return -1;
	}

	@Override
	public void beforeExecutor(Invocation invocation) {
		if (DataSourceContext.isDsSwitchAuto()) {
			String dsType = null;
			String dsId = null;

			Object[] args = invocation.getArgs();
			MappedStatement ms = (MappedStatement) args[0];
			String nameSpace = MbiUtils.getNameSpace(ms);
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
