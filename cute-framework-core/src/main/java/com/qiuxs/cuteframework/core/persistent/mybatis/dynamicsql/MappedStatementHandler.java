package com.qiuxs.cuteframework.core.persistent.mybatis.dynamicsql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;

/**
 * MyBatisMappedStatement缓存
 * @author qiuxs
 *
 */
@Component
public class MappedStatementHandler {

	private static Logger log = LogManager.getLogger(MappedStatementHandler.class);

	private Map<String, MappedStatement> mappedStatementCache = new ConcurrentHashMap<>();
	private Map<String, Class<?>> mappedTypeCache = new ConcurrentHashMap<>();

	@Resource
	private SqlSessionFactory sqlSessionFactory;

	@PostConstruct
	private void init() {
		log.info("init MappedStatementHandler start...");
		Collection<MappedStatement> mss = this.sqlSessionFactory.getConfiguration().getMappedStatements();
		mss.forEach(ms -> {
			try {
				this.put(ms);
			} catch (SecurityException | ReflectiveOperationException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 添加一个statement
	 * @param ms
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private void put(MappedStatement ms) throws NoSuchMethodException, SecurityException, ReflectiveOperationException {
		String msid = ms.getId();
		mappedStatementCache.put(msid, ms);
		SqlSource sqlSource = ms.getSqlSource();
		if (sqlSource instanceof ProviderSqlSource) {
			log.info("init msid[" + msid + "]");
			// 获取设置的sqlSource对象
			ProviderSqlSource pss = (ProviderSqlSource) sqlSource;
			// 包装一下方便获取对象属性
			MetaObject metaPss = SystemMetaObject.forObject(pss);
			// 获取定义的providerType
			Class<?> providerType = (Class<?>) metaPss.getValue("providerType");
			if (providerType.isAssignableFrom(DynamicSqlProvider.class)) {
				// 获取provider实例
				Object provider = ApplicationContextHolder.getBean(providerType);
				// 获取方法名
				String providerMethodName = StringUtils.substringAfterLast(ms.getId(), ".");
				// 获取方法实例
				Method method = providerType.getMethod(providerMethodName, Class.class);
				// 获取当前实体类类型
				Class<?> pojoClass = getPojoClass(msid);
				// 缓存一下
				mappedTypeCache.put(msid, pojoClass);
				// 生成sql
				String generatedSql = (String) method.invoke(provider, pojoClass);
				// 替换sqlSource
				MetaObject metaMs = SystemMetaObject.forObject(ms);
				metaMs.setValue("sqlSource", new DynamicSqlSource(ms.getConfiguration(), new TextSqlNode(generatedSql)));
			}
		}
	}

	private Class<?> getPojoClass(String msid) throws ClassNotFoundException {
		String daoClassName = StringUtils.substringBeforeLast(msid, ".");
		Class<?> daoClass = Class.forName(daoClassName);
		Type[] type = daoClass.getGenericInterfaces();
		ParameterizedType pt = (ParameterizedType) type[0];
		Type[] genericTypes = pt.getActualTypeArguments();
		return (Class<?>) genericTypes[1];
	}

	/**
	 * 获取statement关联的实体类
	 * @param ms
	 * @return
	 */
	public Class<?> getPojoClass(MappedStatement ms) {
		return null;
	}

}
