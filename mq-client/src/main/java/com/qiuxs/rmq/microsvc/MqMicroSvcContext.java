package com.qiuxs.rmq.microsvc;

import java.util.Map;

import org.apache.rocketmq.common.message.Message;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.UserContext;
import com.qiuxs.cuteframework.tech.microsvc.MicroSvcConstants;
import com.qiuxs.cuteframework.tech.microsvc.MicroSvcContext;

public class MqMicroSvcContext extends MicroSvcContext {

	private Message msg;

	public MqMicroSvcContext(Message msg) {
		this.msg = msg;
	}

	@Override
	public void putMicroContext() {
		super.putMicroContext();
		Map<String, String> extProp = this.msg.getProperties();
		UserLite userLite = UserContext.getUserLiteOpt();
		if (userLite != null) {
			extProp.put(MicroSvcConstants.ATTACH_KEY_USER_LITE, JsonUtils.toJSONString(userLite));
		}
	}

	@Override
	public void cacheContextFromSource() {
		super.cacheContextFromSource();
		Map<String, String> extProp = this.msg.getProperties();
		String strUserLite = extProp.get(MicroSvcConstants.ATTACH_KEY_USER_LITE);
		if (StringUtils.isNotBlank(strUserLite)) {
			UserLite userLite = JsonUtils.parseObject(strUserLite, UserLite.class);
			UserContext.setUserLite(userLite);
		}
	}
	
}
