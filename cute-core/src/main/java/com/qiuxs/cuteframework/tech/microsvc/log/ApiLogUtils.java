package com.qiuxs.cuteframework.tech.microsvc.log;

import com.qiuxs.cuteframework.core.context.TLVariableHolder;
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
		return null;
	}

	public static void writeReqLog(ApiLogProp logProp, String topic, String substringBefore, Object body, String typeRequestMq) {
		
	}

}
