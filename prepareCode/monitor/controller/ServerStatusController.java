package com.hzecool.frm.monitor.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.rpc.service.EchoService;
import com.hzecool.core.context.ApplicationContextHolder;
import com.hzecool.core.context.BaseContextManager;
import com.hzecool.core.context.EnvironmentHolder;
import com.hzecool.core.db.ds.SpObserver;
import com.hzecool.core.db.ds.lookup.DynamicDataSource;
import com.hzecool.core.db.mgr.MyBatisManager;
import com.hzecool.core.log.LogFactory;
import com.hzecool.fdn.Constant;
import com.hzecool.fdn.utils.MapUtils;
import com.hzecool.fdn.utils.StringUtils;
import com.hzecool.fdn.utils.converter.JsonUtils;

@Controller
public class ServerStatusController {
	@Resource
	private DataSource dynDataSource;

	@RequestMapping(value = {"/check.do", "/devops/check.do"}, method = RequestMethod.GET, produces = "text/plain; charset=utf-8")
	@ResponseBody
	public String check(HttpServletRequest req, HttpServletResponse rep) {
		String svc = req.getParameter("svc");
		if (svc.equals("checkLine")) {
			StringBuilder retVal = new StringBuilder();
			retVal.append("success,");
			String strdatasize = req.getParameter("datasize");
			int datasize = 0;
			try {
				datasize = Integer.parseInt(strdatasize);
			} catch (Exception e) {
			}
			if (datasize == 0) {
				// 默认返回100kb数据
				datasize = 100;
			}
			// 每个数字占1个字节
			for (int i = 0; i < (1024 * datasize - 8); i++) {
				retVal.append("1");
			}
			return retVal.toString();
		} else if (svc.equals("checkServer")) {
			// 测试缓存服务器是否正常
			try {
				if (!BaseContextManager.cacheIsGood()) {
					return "connect to cache server error!";
				}
			} catch (Exception e) {
				LogFactory.monitorLogger.error(e.getMessage(), e);
				return "connect to cache server error:" + e.getMessage();
			}
			String returnMsg = "success";
			String checkServerExclude = EnvironmentHolder.getEnvParam("check_server_exclude");
			boolean excludeDb = StringUtils.isIn("db", checkServerExclude, ",");
			if (!excludeDb) {
				List<String> list_ds = new ArrayList<String>();
//				if (!EnvironmentHolder.isTest()) {
//					list_ds.add(BaseContextManager.getEntryDb());
//					list_ds.add(BaseContextManager.getSequenceDb());
//				}
				list_ds.addAll(BaseContextManager.getBizDsIdList());
				StringBuilder msgDBInMaintain = new StringBuilder();
				// 测试到每个数据库的数据库连接池是否正常
				for (String dsid : list_ds) {
					try {
						if (DynamicDataSource.getDsidMaintaining().contains(dsid)) {
							msgDBInMaintain.append(dsid).append(",");
						} else {
							SpObserver.putDsFlag(dsid);
							MyBatisManager.getSqlSession().selectOne("queryCountBySql", "select 1");
						}
					} catch (Exception e) {
						LogFactory.monitorLogger.error(e.getMessage(), e);
						return "connect to " + dsid + " error:" + e.getMessage();
					}
				}
				if (msgDBInMaintain.length() > 0) {
					msgDBInMaintain.deleteCharAt(msgDBInMaintain.length() - 1);
					returnMsg += " " + "数据维护中:" + msgDBInMaintain.toString();
				}
			}
			return returnMsg;
		} else if (svc.equals("checkEntryDb")) {
			SpObserver.putDsFlag(BaseContextManager.getEntryDb());
			// DataSource dynDataSource = (DataSource)
			// ApplicationContextUtil.getBean("dynDataSource");
			try {
				dynDataSource.getConnection().close();
			} catch (Exception e) {
				LogFactory.monitorLogger.error(e.getMessage(), e);
				return e.getMessage();
			}
			return "success";
		} else if (svc.equals("checkDubbo")) {
			boolean hasFailure = false;
			StringBuilder result = new StringBuilder();
			String refs = EnvironmentHolder.getEnvParam("monitor_dubbo_refs");
			if (StringUtils.isNotEmpty(refs)) {
				List<String> refList = StringUtils.splitByWhole(refs, Constant.SEPARATOR_COMMA);
				for (String ref : refList) {
					result.append(ref).append("=");
					// 回声测试可用性
					EchoService echoService = (EchoService) ApplicationContextHolder.getBean(ref);
					try {
						result.append(echoService.$echo("success")); 
					} catch (Exception e) {
						hasFailure = true;
						result.append("failure: " + e.getMessage());
					}
					result.append(" ");
				}
			}
			if (hasFailure) {
				result.insert(0, "failure ");
			} else {
				result.insert(0, "success ");
			}
			return result.toString();
		} else if (svc.equals("checkSdn")) {
			return checkSdn(req, rep);
		} else {
			return "unknown";
		}
	}
	

	/** sdn测速服务 */
	private String checkSdn(HttpServletRequest req, HttpServletResponse rep) {
		String keyMac = "mac";
		String mac = req.getParameter(keyMac);
		String keyTsSdnClient = "tsSdnClient"; // 客户端发起请求时间戳
		String tsSdnClient = req.getParameter(keyTsSdnClient);
		String keyTsSdnServer = "tsSdnServer"; // 服务端接收到请求的时间戳
		String tsSdnServer = String.valueOf(System.currentTimeMillis());

		Map<String, Object> map = MapUtils.genMap(keyMac, mac, keyTsSdnClient, tsSdnClient, keyTsSdnServer,
				tsSdnServer);
		return JsonUtils.toJSONString(map);
	}
}
