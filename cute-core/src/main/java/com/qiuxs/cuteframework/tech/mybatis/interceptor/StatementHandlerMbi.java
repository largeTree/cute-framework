package com.qiuxs.cuteframework.tech.mybatis.interceptor;

import java.sql.Connection;
import java.sql.Statement;
// public abstract java.util.List org.apache.ibatis.executor.statement.StatementHandler.query(java.sql.Statement,org.apache.ibatis.session.ResultHandler) throws java.sql.SQLException
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.tech.mybatis.interceptor.hook.IMbiHook;
import com.qiuxs.cuteframework.tech.mybatis.interceptor.utils.MbiHookUtils;
import com.qiuxs.cuteframework.tech.mybatis.interceptor.utils.MbiUtils;

@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }),
		@Signature(type = StatementHandler.class, method = "query", args = { Statement.class, ResultHandler.class }),
		@Signature(type = StatementHandler.class, method = "update", args = { Statement.class })
})
@Component
public class StatementHandlerMbi implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		List<IMbiHook> mbiHooks = MbiHookUtils.getMbiHooks();
		for (IMbiHook hook : mbiHooks) {
			hook.beforeStatement(invocation);
		}

		Object result = null;
		try {
			result = invocation.proceed();
		} catch (Exception e) {
			throw e;
		} finally {
			for (IMbiHook hook : mbiHooks) {
				hook.finallyStatement(invocation);
			}
		}

		for (int i = mbiHooks.size() - 1; i >= 0; i--) {
			mbiHooks.get(i).afterStatement(invocation);
		}
		return result;
	}

	@Override
	public Object plugin(Object target) {
		Object realTarget = MbiUtils.getTarget(target);
		if (realTarget instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
	}

}
