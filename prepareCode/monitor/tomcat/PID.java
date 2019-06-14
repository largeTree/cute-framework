package com.hzecool.frm.monitor.tomcat;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

final public class PID {

	/**
	 * 获取当前进程id
	 * @return
	 */
	public static String getPID(){
		final RuntimeMXBean rtb = ManagementFactory.getRuntimeMXBean();
		//pid@host
		return rtb.getName().split("@")[0];
	}
}
