package com.qiuxs.cuteframework.core.basic.utils.dingtalk;

import java.util.Date;

import com.qiuxs.cuteframework.core.basic.utils.DateFormatUtils;
import com.qiuxs.cuteframework.core.basic.utils.dingtalk.DingTalkUtils.HookKey;

/**
 * 
 * 功能描述: 系统通知 <p>  
 * 新增原因: TODO<p>  
 * 新增日期: 2019年11月7日 下午4:36:16 <p>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class NoticeLogger {

	public static void error(String msg, String stackTrace) {
		sendLog("ERROR", msg, stackTrace);
	}

	public static void wran(String msg) {
		sendLog("WRAN", msg, null);
	}

	public static void info(String msg) {
		sendLog("INFO", msg, null);
	}

	private static void sendLog(String prefix, String msg, String stackTrace) {
		String time = DateFormatUtils.formatTime(new Date());
		StringBuilder sb = new StringBuilder();
		sb.append(" >>>>>>>>>>>>>>>>\t").append(prefix).append("\t<<<<<<<<<<<<<<<<< \n")
				.append("时间 : ").append(time).append("\n")
				.append("消息 : ").append(msg);
		if (stackTrace != null) {
			sb.append("\n堆栈: ").append(stackTrace);
		}
		DingTalkUtils.sendTextMsg(HookKey.LOG_NOTICE, sb.toString());
	}

}
