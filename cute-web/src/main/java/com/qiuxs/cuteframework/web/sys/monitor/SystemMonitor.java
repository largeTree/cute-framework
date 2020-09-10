package com.qiuxs.cuteframework.web.sys.monitor;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.tech.mc.McFactory;
import com.qiuxs.cuteframework.web.sys.model.DataSourceInfo;

public class SystemMonitor {

	private static class Holder {
		private static Map<String, List<DataSourceInfo>> dataSourceStatusMap = McFactory.getFactory().createMap("data_source_status_map");
	}

	public static void setDataSourceStatus(List<DataSourceInfo> dsInfos) {
		Holder.dataSourceStatusMap.put(EnvironmentContext.getServerId(), dsInfos);
	}

	public static Set<Entry<String, List<DataSourceInfo>>> getDataSourceStatus() {
		return Holder.dataSourceStatusMap.entrySet();
	}

}
