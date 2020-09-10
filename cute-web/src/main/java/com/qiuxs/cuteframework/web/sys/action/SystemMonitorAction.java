package com.qiuxs.cuteframework.web.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.qiuxs.cuteframework.web.action.IAction;
import com.qiuxs.cuteframework.web.bean.ActionResult;
import com.qiuxs.cuteframework.web.bean.ReqParam;
import com.qiuxs.cuteframework.web.sys.model.DataSourceInfo;
import com.qiuxs.cuteframework.web.sys.monitor.SystemMonitor;

@Service
public class SystemMonitorAction implements IAction {

	public ActionResult dsStatus(ReqParam params) {
		Set<Entry<String, List<DataSourceInfo>>> data = SystemMonitor.getDataSourceStatus();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (Entry<String, List<DataSourceInfo>> entry : data) {
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("serverId", entry.getKey());
			mapData.put("dsInfos", entry.getValue());
			list.add(mapData);
		}
		return ActionResult.of(list);
	}
	
}
