package com.qiuxs.cuteframework.tech.mybatis.interceptor.hook;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.apache.ibatis.plugin.Invocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.JDBC4PreparedStatement;
import com.qiuxs.cuteframework.core.basic.utils.reflect.FieldUtils;
import com.qiuxs.cuteframework.core.persistent.database.lookup.DataSourceContext;

/**
 * sql监控hook
 * 
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年5月23日 下午12:46:31 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
@Component
public class MbiSqlMonitorHook implements IMbiHook {
	
	private static Logger log = LogManager.getLogger(MbiSqlMonitorHook.class);

	@Override
	public int getOrder() {
		return 10;
	}

	@Override
	public void beforeStatement(Invocation invocation) {
		Object[] args = invocation.getArgs();
		Object arg0 = args[0];
		if (!DataSourceContext.monitorSql()) {
			return;
		}
		if (arg0 instanceof Statement) {
			Field field = null;
			Statement stmt = (Statement) arg0;
			Statement lastPs = null;
			do {
				field = FieldUtils.getAccessibleField(stmt, "_stmt");
				if (field != null) {
					try {
						stmt = (Statement) field.get(stmt);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						log.error("get _stmt failed, ext = " + e.getLocalizedMessage(), e);
					}
				} else {
					lastPs = (Statement) stmt;
				}
			} while (field != null);
			if (lastPs != null && lastPs instanceof PreparedStatement) {
				if (lastPs instanceof JDBC4PreparedStatement) {
					try {
						JDBC4PreparedStatement jps = (JDBC4PreparedStatement) lastPs;
						String finalSql = jps.asSql(true).replaceAll("[\\t\\n\\r]", " ").replaceAll("\\s+", " ");
						log.info("exec sql = " + finalSql);
					} catch (Exception e) {
						log.error("asSql(true) failed, ext = " + e.getLocalizedMessage(), e);
					}
				}
			}
		}
	}
	
}
