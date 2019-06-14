package com.hzecool.frm.monitor.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.hzecool.core.concurrent.thread.MyThread;
import com.hzecool.core.context.ApplicationContextHolder;
import com.hzecool.core.context.BaseContextManager;
import com.hzecool.core.db.ds.lookup.DynamicDataSource;
import com.hzecool.core.log.logger.CommonLogger;
import com.hzecool.frm.monitor.bean.LogInfoBean;

/**
 * 异步写慢请求日志,记录reqsn，具体内容通过reqsn到日志库里查询
 * @author laisf   
 * @version 1.0.0
 */
//@Component
public class LogRequestSlowThread extends MyThread {
	
	private static final String sql = "replace into sc_request_slow(optime,reqsn,requrl,cost,finished) values(?,?,?,?,?)";
	private int cost;
	private int finished;
	private LogInfoBean logInfoBean;
	
	@Override
    public void myWork() {
	    // TODO Auto-generated method stub
	    //super.run();
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			ps.setTimestamp(1, new java.sql.Timestamp(logInfoBean.getStart()));
			ps.setString(2, logInfoBean.getReqsn());
			ps.setString(3, logInfoBean.getRequrl());
			ps.setInt(4, cost);
			ps.setInt(5, finished);
			ps.executeUpdate();
		} catch (Exception e) {
			CommonLogger.logger.error("LogRequestSlowThread run:" + e.getMessage(), e);
		} finally {
			close(ps);
			closeConnection(conn);
		}
    }
//	@Resource
//	private DynamicDataSource dynamicDataSource;
	protected Connection getConnection() throws SQLException {
		DynamicDataSource dynamicDataSource = (DynamicDataSource) ApplicationContextHolder
				.getBean("dynDataSource");
		DataSource currentDataSource = (DataSource) dynamicDataSource
				.getTargetDataSources().get(BaseContextManager.getLogDb());
		Connection conn = currentDataSource.getConnection();
		return conn;
	}
	
	private void close(PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			CommonLogger.logger.error("LogRequestSlowThread close PreparedStatement error:" + e.getMessage());
		}
	}

	private void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				CommonLogger.logger.error("LogRequestSlowThread closeConnection error:"
						+ e.getMessage(), e);
			}
		} else {
			CommonLogger.logger.error("LogRequestSlowThread closeConnection conn==null");
		}
	}
//
//	private void println(String msg) {
//		System.out.println(msg);
//	}

	public static String getSql() {
		return sql;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}




	public LogInfoBean getLogInfoBean() {
		return logInfoBean;
	}


	public void setLogInfoBean(LogInfoBean logInfoBean) {
		this.logInfoBean = logInfoBean;
	}


	public int getFinished() {
		return finished;
	}


	public void setFinished(int finished) {
		this.finished = finished;
	}	
	
	
	
}
