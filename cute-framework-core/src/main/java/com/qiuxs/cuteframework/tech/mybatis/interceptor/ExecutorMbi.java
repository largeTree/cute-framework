package com.qiuxs.cuteframework.tech.mybatis.interceptor;

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.tech.mybatis.interceptor.hook.IMbiHook;
import com.qiuxs.cuteframework.tech.mybatis.interceptor.utils.MbiHookUtils;
import com.qiuxs.cuteframework.tech.mybatis.interceptor.utils.MbiUtils;

@Intercepts({
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }),
		@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class })
})
@Component
public class ExecutorMbi implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		List<IMbiHook> mbiHooks = MbiHookUtils.getMbiHooks();
		for (IMbiHook hook : mbiHooks) {
			hook.beforeExecutor(invocation);
		}

		Object result = null;
		try {
			result = invocation.proceed();
		} catch (Exception e) {
			throw e;
		} finally {
			for (IMbiHook hook : mbiHooks) {
				hook.finallyExecutor(invocation);
			}
		}

		for (int i = mbiHooks.size() - 1; i >= 0; i--) {
			mbiHooks.get(i).afterExecutor(invocation);
		}
		return result;
	}

	@Override
	public Object plugin(Object target) {
		Object realTarget = MbiUtils.getTarget(target);
		if (realTarget instanceof CachingExecutor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
	}

}
