package com.qiuxs.cuteframework.core.utils.notice;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.config.IConfiguration;
import com.qiuxs.cuteframework.core.basic.config.UConfigUtils;
import com.qiuxs.cuteframework.core.basic.utils.DateFormatUtils;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.core.basic.utils.dingtalk.DingTalkUtils;
import com.qiuxs.cuteframework.core.context.UserContext;
import com.qiuxs.cuteframework.tech.microsvc.log.ApiLogUtils;

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
	
	private static Logger log = LoggerFactory.getLogger(NoticeLogger.class);

	public static void error(String msg, Throwable e) {
		error(msg, ExceptionUtils.getStackTrace(e));
	}
	
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
		IConfiguration env = UConfigUtils.getDomain("env");
		sb.append(" >>>>>>>>>>>>>>>>\t").append(prefix).append("\t<<<<<<<<<<<<<<<<< \n")
				.append("时间 : ").append(time).append("\n")
        		.append("globalId：").append(ApiLogUtils.getGlobalId()).append("\n");
				if (env != null) {
					sb.append("AppName：").append(env.getString("app-name")).append("\n")
					.append("ServerId：").append(env.getString("server-id")).append("\n");
				}
				UserLite userLite = UserContext.getUserLiteOpt();
				if (userLite != null) {
					sb.append("操作用户：").append(userLite.getName()).append("\n");
				}
				sb.append("消息 : ").append(msg);
		if (stackTrace != null) {
			sb.append("\n堆栈: ").append(stackTrace);
		}
		
		String noticeLogToken = env.getString("dd_notice_log_token");
		if (StringUtils.isNotBlank(noticeLogToken)) {
			DingTalkUtils.sendTextMsg(noticeLogToken, sb.toString());
		} else {
			log.warn("没有配置钉钉日志通知，将不发送钉钉通知...");
			switch (prefix) {
			case "ERROR":
				log.error(sb.toString());
				break;
			case "WARN":
				log.warn(sb.toString());
				break;
			default:
				log.info(sb.toString());
				break;
			}
		}
	}

}
