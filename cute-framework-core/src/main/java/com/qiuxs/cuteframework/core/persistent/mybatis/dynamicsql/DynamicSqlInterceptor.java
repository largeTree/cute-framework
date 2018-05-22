package com.qiuxs.cuteframework.core.persistent.mybatis.dynamicsql;

import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;

@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }), @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class })
})
@Component
public class DynamicSqlInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement) args[0];
		SqlSource sqlSource = ms.getSqlSource();
		BoundSql boundSql = sqlSource.getBoundSql(args[1]);
		String sql = boundSql.getSql();
		if (DynamicSqlProvider.DYNAMIC_SQL.equals(sql) && sqlSource instanceof ProviderSqlSource) {
			ProviderSqlSource pss = (ProviderSqlSource) sqlSource;
			MetaObject metaPss = SystemMetaObject.forObject(pss);
			Class<?> providerType = (Class<?>) metaPss.getValue("providerType");
			Object provider = ApplicationContextHolder.getBean(providerType);
			String providerMethodName = StringUtils.substringAfterLast(ms.getId(), ".");
			Method method = providerType.getMethod(providerMethodName, MappedStatement.class);
			String generatedSql = (String) method.invoke(provider, ms);
			MetaObject metaMs = SystemMetaObject.forObject(ms);
			metaMs.setValue("sqlSource", new DynamicSqlSource(ms.getConfiguration(), new TextSqlNode(generatedSql)));
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		// 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub

	}

}
