package com.qiuxs.cuteframework.view.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qiuxs.cuteframework.core.persistent.database.lookup.DynamicDataSource;
import com.qiuxs.cuteframework.view.datamodel.DataSourceInfo;
import com.qiuxs.cuteframework.web.WebConstants;

@Controller
@RequestMapping(value = WebConstants.SYS_CONTROLLER_PREFIX)
public class ServiceStatusController {

	@Resource
	private DynamicDataSource dynamicDataSource;

	@RequestMapping("/status")
	public String dsStatus(ModelMap model) {
		Map<Object, DataSource> dataSources = dynamicDataSource.getTargetDataSources();
		List<DataSourceInfo> dsInfos = new ArrayList<>();
		for (Iterator<Map.Entry<Object, DataSource>> iter = dataSources.entrySet().iterator(); iter.hasNext();) {
			Entry<Object, DataSource> entry = iter.next();
			
			BasicDataSource ds = (BasicDataSource) entry.getValue();
			int maxIdle = ds.getMaxIdle();
			int maxTotal = ds.getMaxTotal();
			long maxWaitMillis = ds.getMaxWaitMillis();
			Boolean defaultAutoCommit = ds.getDefaultAutoCommit();
			int numActive = ds.getNumActive();
			int numIdle = ds.getNumIdle();
			String driverClassName = ds.getDriverClassName();
			String validationQuery = ds.getValidationQuery();
			
			DataSourceInfo dsInfo = new DataSourceInfo();
			dsInfo.setDsId(String.valueOf(entry.getKey()));
			dsInfo.setMaxIdle(maxIdle);
			dsInfo.setMaxTotal(maxTotal);
			dsInfo.setMaxWaitMillis(maxWaitMillis);
			dsInfo.setDefaultAutoCommit(defaultAutoCommit);
			dsInfo.setNumActive(numActive);
			dsInfo.setNumIdle(numIdle);
			dsInfo.setDriverClassName(driverClassName);
			dsInfo.setValidationQuery(validationQuery);
			dsInfos.add(dsInfo);
		}
		model.put("dsInfos", dsInfos);
		return "sys/status";
	}

}
