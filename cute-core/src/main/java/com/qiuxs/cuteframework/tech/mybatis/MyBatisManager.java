package com.qiuxs.cuteframework.tech.mybatis;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.persistent.database.lookup.DataSourceContext;

@Component
public class MyBatisManager {

	private static Logger log = LogManager.getLogger(MyBatisManager.class);

	/** Mybatis SqlSession线程缓存 */
	private static final ThreadLocal<Map<String, SqlSession>> threadSqlSession = new MapThreadLocal<String, SqlSession>() {
	};

	/**
	 * 
	 * 功能描述: 存放Map的ThreadLocal
	 * <p>新增原因: 避免每次进行null判断
	 *  
	 * @author qiuxs   
	 * @version 1.0.0
	 * @since 2019-11-03 21:46
	 */
	private static class MapThreadLocal<K, V> extends ThreadLocal<Map<K, V>> {
		@Override
		//set(null)重新get()不会调用该方法
		protected Map<K, V> initialValue() {
			return new HashMap<K, V>();
		}
	}

	private static SqlSessionFactory sqlSessionFactory;

	@Resource
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		MyBatisManager.sqlSessionFactory = sqlSessionFactory;
	}

	/**
	 * 清理mybatis所有缓存
	 */
	public static void clearAllDaoCache() {
		Collection<Cache> caches = getSqlSessionFactory().getConfiguration().getCaches();
		for (Cache cache : caches) {
			cache.clear();
		}
	}

	/**
	 * 获取数据库链接
	 * @return
	 */
	public static Connection getConnection() {
		return getSqlSession().getConnection();
	}

	/**
	 * 获取默认sqlSession
	 * @return
	 */
	public static SqlSession getSqlSession() {
		return getSqlSession(false, true);
	}

	/**
	 * 获取sqlSession
	 * @param newFlag
	 * 	指定获取新的
	 * @param autoCommit
	 * 	是否自动提交事务
	 * @return
	 */
	public static SqlSession getSqlSession(boolean newFlag, boolean autoCommit) {
		if (newFlag) {
			return openSqlSession(autoCommit);
		} else {
			Map<String, SqlSession> mapSqlSession = threadSqlSession.get();
			String dsId = DataSourceContext.getDsId();
			SqlSession sqlSession = mapSqlSession.get(dsId);
			if (sqlSession == null) {
				sqlSession = openSqlSession(autoCommit);
				mapSqlSession.put(dsId, sqlSession);
			}
			return sqlSession;
		}
	}

	/**
	 * 返回的SqlSession在后续的dataSource.getConnection()返回的Connection#autoCommit=true
	 *  
	 * @author qiuxs  
	 * @param autoCommit
	 * @return
	 */
	private static SqlSession openSqlSession(boolean autoCommit) {
		SqlSession s = sqlSessionFactory.openSession(autoCommit);
		return s;
	}

	/**
	 * 获取sqlSessionFactory
	 * @return
	 */
	public static SqlSessionFactory getSqlSessionFactory() {
		if (sqlSessionFactory == null) {
			try {
				sqlSessionFactory = (SqlSessionFactory) ApplicationContextHolder.getBean("sqlSessionFactory");
			} catch (Exception e) {
				log.error("getSqlSessionFactory Failed ext = " + e.getLocalizedMessage(), e);
				return null;
			}
		}
		return sqlSessionFactory;
	}

	public static void closeSession() {

	}

}
