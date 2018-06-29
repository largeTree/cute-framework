package com.qiuxs.cuteframework.tech.task;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.context.UserContext;

public class AbstractAsyncTask<P> {
	private P preparParam;
	private UserLite userLite;

	public AbstractAsyncTask(P preparParam) {
		this.preparParam = preparParam;
		this.userLite = UserContext.getUserLiteOpt();
	}

	protected P getPreparParam() {
		return this.preparParam;
	}

	protected void initUserLite() {
		UserContext.setUserLite(userLite);
	}

}
