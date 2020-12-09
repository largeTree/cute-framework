package com.qiuxs.cuteframework.web.sys.timertask;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.utils.TimeUtils;
import com.qiuxs.cuteframework.core.persistent.database.lookup.DynamicDataSource;
import com.qiuxs.cuteframework.core.timertask.MyTimerTask;
import com.qiuxs.cuteframework.web.sys.model.DataSourceInfo;
import com.qiuxs.cuteframework.web.sys.monitor.SystemMonitor;

@Component
public class SystemMonitorTimerTask implements MyTimerTask {

	@Resource
	private DataSource dataSource;

	@Override
	public void run(String arg) {
		List<DataSourceInfo> dsInfos = new ArrayList<>();
		if (this.dataSource instanceof DynamicDataSource) {
			Map<Object, DataSource> dataSources = ((DynamicDataSource) this.dataSource).getTargetDataSources();
			for (Iterator<Map.Entry<Object, DataSource>> iter = dataSources.entrySet().iterator(); iter.hasNext();) {
				Entry<Object, DataSource> entry = iter.next();

				BasicDataSource ds = (BasicDataSource) entry.getValue();
				int maxIdle = ds.getMaxIdle();
				int maxActive = ds.getMaxTotal();
				long maxWait = ds.getMaxWaitMillis();
				Boolean defaultAutoCommit = ds.getDefaultAutoCommit();
				int numActive = ds.getNumActive();
				int numIdle = ds.getNumIdle();
				String driverClassName = ds.getDriverClassName();
				String validationQuery = ds.getValidationQuery();

				DataSourceInfo dsInfo = new DataSourceInfo();
				dsInfo.setDsId(String.valueOf(entry.getKey()));
				dsInfo.setMaxIdle(maxIdle);
				dsInfo.setMaxActive(maxActive);
				dsInfo.setMaxWait(maxWait);
				dsInfo.setDefaultAutoCommit(defaultAutoCommit);
				dsInfo.setNumActive(numActive);
				dsInfo.setNumIdle(numIdle);
				dsInfo.setDriverClassName(driverClassName);
				dsInfo.setValidationQuery(validationQuery);
				dsInfo.setClosed(ds.isClosed());
				dsInfos.add(dsInfo);
			}
		}
		SystemMonitor.setDataSourceStatus(dsInfos);
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Date getFirstTime() {
		return new Date(System.currentTimeMillis() + TimeUtils.MINUTE);
	}

	@Override
	public long getPeriod() {
		return TimeUtils.MINUTE;
	}

}
