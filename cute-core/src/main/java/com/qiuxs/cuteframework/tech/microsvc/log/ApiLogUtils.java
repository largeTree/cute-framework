package com.qiuxs.cuteframework.tech.microsvc.log;

import java.util.Map;

import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.context.TLVariableHolder;
import com.qiuxs.cuteframework.tech.log.LogConstant;
import com.qiuxs.cuteframework.tech.log.LogUtils;

public class ApiLogUtils {

	public static ApiLogProp initApiLog(String srvName, String fromApp, String toApp, Long globalId, String clientIp, String requestId) {
		ApiLogProp apiLogProp = new ApiLogProp();
		apiLogProp.setSvcName(srvName);
		apiLogProp.setFromApp(fromApp);
		apiLogProp.setToApp(toApp);
		apiLogProp.setGlobalId(globalId);
		apiLogProp.setClientIp(clientIp);
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
		ApiLogProp apiLogProp = TLVariableHolder.getVariable(ApiLogConstants.TL_APILOG);
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

}
