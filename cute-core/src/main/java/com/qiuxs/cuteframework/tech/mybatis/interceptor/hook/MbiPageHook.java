package com.qiuxs.cuteframework.tech.mybatis.interceptor.hook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.qiuxs.cuteframework.core.FrmLogger;
import com.qiuxs.cuteframework.core.persistent.database.dao.page.PageInfo;
import com.qiuxs.cuteframework.tech.mybatis.interceptor.utils.MbiUtils;

@Component
public class MbiPageHook implements IMbiHook {

	private static Logger log = LogManager.getLogger(MbiDsHook.class);
	
	public static final String DB_COUNT = "__count";

	@Override
	public int getOrder() {
		return 10;
	}

	@Override
	public void beforeStatement(Invocation invocation) {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaStatementHandler = MbiUtils.getMetaObject(statementHandler);
		// 获取分页
		RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
		if (rowBounds != null && rowBounds instanceof PageInfo) {
			String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
			
			PageInfo pageInfo = (PageInfo) rowBounds;
			
			int limit = pageInfo.getPageSize();
			int offset = (pageInfo.getPageNo() - 1) * limit;
			StringBuffer newSql = new StringBuffer(sql);
			// sql不带分页参数时 自动拼接分页参数
			if (!sql.contains("offset") && !sql.contains("limit")) {
				newSql.append(" limit ").append(limit).append(" offset ").append(offset);
			}
			metaStatementHandler.setValue("delegate.boundSql.sql", newSql.toString());
			// 采用物理分页后就不需要mybatis自己的内存分页了，重置下面两个参数
			metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
			metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);

			// 自动合计总数
			this.autoTotal(sql, (PageInfo) rowBounds, invocation);
		}
	}

	/**
	 * 自动合计
	 * 
	 * @author qiuxs
	 *
	 * @param sql
	 * @param pageInfo
	 * @param metaStatementHandler
	 *
	 * 创建时间：2018年8月17日 下午10:39:14
	 */
	private void autoTotal(String sql, PageInfo pageInfo, Invocation invocation) {
		Object connObj = invocation.getArgs()[0];
		if (!(connObj instanceof Connection)) {
			return;
		}
		PreparedStatement stat = null;
		ResultSet rs = null;
		String countSql = null;
		try {
			countSql = getCountSql(sql);
			Connection conn = (Connection) invocation.getArgs()[0];
			StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
			if (log.isDebugEnabled()) {
				log.debug("auto total sql::" + countSql);
			}
			stat = conn.prepareStatement(countSql);
			statementHandler.parameterize(stat);
			rs = stat.executeQuery();
			if (rs.next()) {
				pageInfo.setTotal(rs.getInt(DB_COUNT));
			}
		} catch (Exception e) {
			FrmLogger.log.info("自动合计总数SQL = " + countSql);
			FrmLogger.log.warn("自动合计总数异常，请自行实现合计总数方法 ext = " + e.getLocalizedMessage(), e);
		} finally {
			close(stat, rs);
		}
	}

	/**
	 * 获取合计数量sql
	 * 
	 * @author qiuxs
	 *
	 * @param sql
	 * @return
	 *
	 * 创建时间：2018年8月17日 下午10:52:13
	 */
	private String getCountSql(String sql) {
		sql = sql.replaceAll("[\\t\\n\\r]", " ").toLowerCase();
		String fromSql = sql.substring(sql.indexOf("from"), sql.length());

		if (fromSql.indexOf(" order ") != -1) {
			fromSql = fromSql.substring(0, fromSql.indexOf(" order "));
		}

		StringBuilder countSql = new StringBuilder();
		countSql.append("select count(1) as ").append(DB_COUNT).append(" ")
				.append(fromSql);
		return countSql.toString();
	}

	private void close(PreparedStatement stat, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stat != null) {
				stat.close();
			}
		} catch (SQLException e) {
			FrmLogger.log.error("close stat or rs error ext=" + e.getLocalizedMessage(), e);
		}
	}

}
