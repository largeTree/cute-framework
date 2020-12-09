package com.qiuxs.cuteframework.web.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.utils.DateFormatUtils;
import com.qiuxs.cuteframework.core.timertask.MyTimerTask;
import com.qiuxs.cuteframework.core.timertask.TimerManager;
import com.qiuxs.cuteframework.web.action.IAction;
import com.qiuxs.cuteframework.web.bean.ActionResult;
import com.qiuxs.cuteframework.web.bean.ReqParam;
import com.qiuxs.cuteframework.web.sys.model.DataSourceInfo;
import com.qiuxs.cuteframework.web.sys.monitor.SystemMonitor;

@Service
public class SystemMonitorAction implements IAction {

	/**
	 * 数据源状态
	 *  
	 * @author qiuxs  
	 * @param params
	 * @return
	 */
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
	
	/**
	 * 定时任务列表
	 *  
	 * @author qiuxs  
	 * @param params
	 * @return
	 */
	public ActionResult timerTaskList(ReqParam params) {
		List<MyTimerTask> allTimers = TimerManager.getAllTimers();
		JSONArray list = new JSONArray(allTimers.size());
		for (MyTimerTask task : allTimers) {
			JSONObject item = new JSONObject();
			item.put("name", task.getName());
			item.put("firstTime", DateFormatUtils.formatTime(task.getFirstTime()));
			item.put("period", task.getPeriod());
			list.add(item);
		}
		return ActionResult.of(list.size(), list);
	}
	
}
