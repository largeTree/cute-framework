package com.qiuxs.cuteframework.tech.task;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.context.UserContext;

public abstract class RunnableAsyncTask<P> implements Runnable {

	private P preparParam;
	private UserLite userLite;

	public RunnableAsyncTask(P preparParam) {
		this.preparParam = preparParam;
		this.userLite = UserContext.getUserLiteOpt();
	}

	@Override
	public final void run() {
		UserContext.setUserLite(userLite);
		this.execute(this.preparParam);
	}

	public abstract void execute(P preparParam);

}
