package com.qiuxs.cuteframework.tech.microsvc.log;

import java.util.Map;

import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.context.TLVariableHolder;
import com.qiuxs.cuteframework.core.persistent.util.IDGenerateUtil;
import com.qiuxs.cuteframework.tech.log.LogConstant;
import com.qiuxs.cuteframework.tech.log.LogUtils;

public class ApiLogUtils {

	public static ApiLogProp initApiLog(String srvName, String fromApp, String toApp, Long globalId, String clientIp, String requestId) {
		ApiLogProp apiLogProp = new ApiLogProp();
		apiLogProp.setSvcName(srvName);
		apiLogProp.setFromApp(fromApp);
		if (StringUtils.isBlank(toApp)) {
			toApp = EnvironmentContext.getAppName();
		}
		apiLogProp.setToApp(toApp);
		if (globalId == null) {
			globalId = genGlobalId();
		}
		apiLogProp.setGlobalId(globalId);
		apiLogProp.setClientIp(clientIp);
		apiLogProp.setRequestId(requestId);
		initApiLog(apiLogProp);
		return apiLogProp;
	}

	public static ApiLogProp initApiLog(ApiLogProp apiLogProp) {
		TLVariableHolder.setVariable(ApiLogConstants.TL_APILOG, apiLogProp);
		TLVariableHolder.setVariable(ApiLogConstants.TL_APILOG_GLOBAL_ID, apiLogProp.getGlobalId());
		LogUtils.putMDC(apiLogProp);
		return apiLogProp;
	}

	public static ApiLogProp genSendToInvokedLogProp() {
		ApiLogProp apiLogProp = getApiLogProp();
		if (apiLogProp == null) {
			apiLogProp = new ApiLogProp();
			Map<String, String> mdcMap = LogUtils.getContextMap();
			Long globalId = MapUtils.getLong(mdcMap, LogConstant.COLUMN_GLOBALID);
			apiLogProp.setGlobalId(globalId);
		}
		ApiLogProp sendToInvoke = apiLogProp.cloneTo();
		sendToInvoke.setFromApp(EnvironmentContext.getAppName());
		sendToInvoke.setRequestId(apiLogProp.getRequestId() + "." + ApiLogConstants.TYPE_REQUEST_MQ);
		return apiLogProp;
	}

	public static void writeReqLog(ApiLogProp logProp, String svcName, String svcMethod, Object data, String type) {

	}

	public static void writeResLog(ApiLogProp logProp, String answerId, Object resLogData, Throwable ex, String type) {

	}

	public static String genRandomAnswerId() {
		return null;
	}

	public static ApiLogProp getApiLogProp() {
		return TLVariableHolder.getVariable(ApiLogConstants.TL_APILOG);
	}

	/**
	 * 获取globalId，当前线程不存在的自动生成一个
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static Long getGlobalId() {
		// 先取线程变量
		Long globalId = TLVariableHolder.getVariable(ApiLogConstants.TL_APILOG_GLOBAL_ID);
		if (globalId == null) {
			// 再取ApiLogProp
			ApiLogProp apiLogProp = getApiLogProp();
			if (apiLogProp == null || apiLogProp.getGlobalId() == null) {
				// 自动生成
				globalId = genGlobalId();
				if (apiLogProp != null) {
					apiLogProp.setGlobalId(globalId);
				}
				TLVariableHolder.setVariable(ApiLogConstants.TL_APILOG_GLOBAL_ID, globalId);
			} else {
				globalId = apiLogProp.getGlobalId();
			}

		}
		return globalId;
	}

	public static Long genGlobalId() {
		return IDGenerateUtil.getNextLongId(LogConstant.GLOBAL_ID_SEQ);
	}

}
