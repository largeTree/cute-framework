package com.hzecool.frm.monitor.tomcat.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.hzecool.frm.monitor.tomcat.ThreadInformations;
import com.hzecool.frm.monitor.tomcat.TomcatCurrentThreadBusyHolder;
import com.hzecool.frm.monitor.tomcat.TomcatInformations;

@Controller
@RequestMapping({"/tomcat", "/devops/tomcat"})
public class TomcatInfoController {

	
	/**
	 * 重新加载配置域
	 *  
	 * @author laisf  
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/threadBusy.do",produces = "text/html;charset=utf-8")  
	@ResponseBody
	public String threadBusy() throws Exception {
		long start = System.currentTimeMillis();
		List<TomcatInformations> listThreadPool = 
				TomcatInformations.buildTomcatInformationsList();
		StringBuilder sb = new StringBuilder();
		for(TomcatInformations infors : listThreadPool){
			sb.append("currentThreadsBusy=" + infors.getCurrentThreadsBusy());
			sb.append(",currentThreadCount=" + infors.getCurrentThreadCount());
		}
		
		 sb.append(",holderThreadsBusyCount=" + TomcatCurrentThreadBusyHolder.getCurrentThreadBusy());
		 sb.append(",cost=" + (System.currentTimeMillis() - start));
		return sb.toString();
	}
	

	@RequestMapping(value = "/threadList.do",produces = "text/html;charset=utf-8")  
	@ResponseBody
	public String threadList() throws Exception {
		StringBuilder sb = new StringBuilder();
		List<ThreadInformations> listThread= ThreadInformations.buildThreadInformationsList();
		for(ThreadInformations infors : listThread) {
			append(sb, infors.getId());
			append(sb, infors.getGlobalThreadId());
			append(sb, infors.getName());
			append(sb, infors.isDaemon());
			append(sb, infors.isDeadlocked());
			append(sb, infors.getState());
			append(sb, infors.getCpuTimeMillis());
			sb.append("\n");
		}
		return sb.toString();
	}

	private static void  append(StringBuilder sb, Object val){
		sb.append(val).append(",");
	}


	@RequestMapping(value = "/threadListJson.do",produces = "text/html;charset=utf-8")
	@ResponseBody
	public String threadListJson() throws Exception {
		List<ThreadInformations> listThread= ThreadInformations.buildThreadInformationsList();
		return JSONArray.toJSONString(listThread);
	}

	@RequestMapping(value = "/threadListTable.do",produces = "text/html;charset=utf-8")  
	@ResponseBody
	public String threadListTable(){
		StringBuilder sb = new StringBuilder().append("<html><body><table border='1'>");
		sb.append("<tr>");
		addTd(sb, "线程id");
		addTd(sb, "globalid");
		addTd(sb, "名称");
		addTd(sb, "守护线程");
		addTd(sb, "死锁");
		addTd(sb, "状态");
		addTd(sb, "CPU(ms)");
		//addTd(sb, "执行方法");
	    addTd(sb, "堆栈");
		sb.append("</tr>");
		List<ThreadInformations> listThread= ThreadInformations.buildThreadInformationsList();
		for(ThreadInformations infors : listThread){
			sb.append("<tr>");
			addTd(sb, infors.getId());
			addTd(sb, infors.getGlobalThreadId());
			addTd(sb, infors.getName());
			addTd(sb, infors.isDaemon());
			addTd(sb, infors.isDeadlocked());
			addTd(sb, infors.getState());
			addTd(sb, infors.getCpuTimeMillis());
			//addTd(sb, infors.getExecutedMethod());
			addTd(sb, infors.getStackTraceText());
			sb.append("</tr>");
		}
		sb.append("</table></body></html>");
		return sb.toString();
	}
	
	private static void  addTd(StringBuilder sb, Object val){
		sb.append("<td>").append(val).append("</td>");
	}

}
