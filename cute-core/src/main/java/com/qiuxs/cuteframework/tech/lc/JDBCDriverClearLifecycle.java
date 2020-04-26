package com.qiuxs.cuteframework.tech.lc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import com.qiuxs.cuteframework.core.listener.lc.IWebLifecycle;

public class JDBCDriverClearLifecycle implements IWebLifecycle {
	
	@Override
	public int order() {
		return -100;
	}

	@Override
	public void lastDestory() {
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
	}

}
