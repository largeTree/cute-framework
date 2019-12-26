package com.qiuxs.cuteframework.core.basic.utils;

import java.io.PrintStream;

/**
 * 控制台日志
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2019年12月21日 下午3:47:50 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class ConsoleLogger {
	
	public static void log(String msg) {
		log(msg, false);
	}

	public static void log(String msg, boolean errorFlag) {
		String threadName = Thread.currentThread().getName();
		StackTraceElement[] stackTraces = Thread.getAllStackTraces().get(Thread.currentThread());
		PrintStream out;
		if (errorFlag) {
			out = System.err;
		} else {
			out = System.out;
		}
		out.println("[" + threadName + "][" + stackTraces[0].getClassName() + "." + stackTraces[0].getMethodName() + ":" + stackTraces[0].getLineNumber() + "] ==>> " + msg);
	}

}
