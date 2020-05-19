package com.qiuxs.cuteframework.tech.lc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;
import com.qiuxs.cuteframework.core.persistent.database.lookup.DynamicDataSource;

public class JDBCDriverClearLifecycle implements IWebLifecycle {
	
	@Override
	public int order() {
		return -100;
	}

	@Override
	public void lastDestory() {
		DynamicDataSource dynamicDataSource = ApplicationContextHolder.getBean(DynamicDataSource.class);
		if (dynamicDataSource != null) {
			Map<Object, DataSource> dataSources = dynamicDataSource.getTargetDataSources();
			for (Iterator<Object> iter = dataSources.keySet().iterator(); iter.hasNext();) {
				try {
					String key = (String) iter.next();
					BasicDataSource dataSource = (BasicDataSource) dataSources.get(key);
					dataSource.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				System.out.println("deregisterDriver : " + String.valueOf(driver));
			} catch (SQLException e) {
				System.err.println("deregisterDriver failed, ext = " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		
		try {
			// MySQL driver leaves around a thread. This static method cleans it up.
			AbandonedConnectionCleanupThread.checkedShutdown();
			System.out.println("shutdown AbandonedConnection Cleanup Thread");
		} catch (Exception e) {
			System.err.println("shutdown the AbandonedConnectionCleanupThread failed, ext = " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

}
