package com.qiuxs.cuteframework.tech.task;

import java.util.Map;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.context.UserContext;
import com.qiuxs.cuteframework.tech.log.LogUtils;

public abstract class AbstractAsyncTask<P> {
	private P preparParam;
	private UserLite userLite;
	private Map<String, String> logContextMap;

	public AbstractAsyncTask(P preparParam) {
		this.preparParam = preparParam;
		this.userLite = UserContext.getUserLiteOpt();
		this.logContextMap = LogUtils.getContextMap();
	}

	protected P getPreparParam() {
		return this.preparParam;
	}

	protected void init() {
		LogUtils.setContextMap(this.logContextMap);
		UserContext.setUserLite(userLite);
	}

}
