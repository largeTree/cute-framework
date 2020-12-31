package com.qiuxs.cuteframework.tech.microsvc;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.UserContext;
import com.qiuxs.cuteframework.tech.microsvc.log.ApiLogConstants;
import com.qiuxs.cuteframework.tech.microsvc.log.ApiLogProp;
import com.qiuxs.cuteframework.tech.microsvc.log.ApiLogUtils;

public abstract class MicroSvcContext {

	private final Map<String, String> attachments;
	private final ApiLogProp logProp;

	protected MicroSvcContext(Map<String, String> attachments) {
		this.attachments = attachments;
		this.logProp = ApiLogUtils.getApiLogProp();
	}

	public void putMicroContext() {
		this.putUserLite();
		this.putApiLog();
	}

	public void cacheContextFromSource() {
		this.cacheUserLite();
		this.cacheApiLog();
	}
	
	private void putApiLog() {
		this.attachments.put(ApiLogConstants.ATTACH_KEY_REQ_PROP, JSONObject.toJSONString(this.logProp));
	}
	
	private void cacheApiLog() {
		String strApiLogProp = MapUtils.getString(this.attachments, ApiLogConstants.ATTACH_KEY_REQ_PROP);
		if (StringUtils.isNotBlank(strApiLogProp)) {
			ApiLogProp apiLogProp = JsonUtils.parseObject(strApiLogProp, ApiLogProp.class);
			ApiLogUtils.initApiLog(apiLogProp);
		}
	}

	private void putUserLite() {
		UserLite userLite = UserContext.getUserLiteOpt();
		if (userLite != null) {
			attachments.put(MicroSvcConstants.ATTACH_KEY_USER_LITE, JsonUtils.toJSONString(userLite));
		}
	}

	private void cacheUserLite() {
		String strUserLite = this.attachments.get(MicroSvcConstants.ATTACH_KEY_USER_LITE);
		if (StringUtils.isNotBlank(strUserLite)) {
			UserLite userLite = JsonUtils.parseObject(strUserLite, UserLite.class);
			UserContext.setUserLite(userLite);
		}
	}

	protected Map<String, String> getAttachments() {
		return this.attachments;
	}

	protected ApiLogProp getLogProp() {
		return this.logProp;
	}

}
