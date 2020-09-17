package com.qiuxs.rmq.log.utils;

import java.util.Date;

import org.apache.rocketmq.common.message.MessageExt;

import com.qiuxs.cuteframework.core.basic.bean.UserLite;
import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.JsonUtils;
import com.qiuxs.cuteframework.core.context.ApplicationContextHolder;
import com.qiuxs.cuteframework.core.context.EnvironmentContext;
import com.qiuxs.cuteframework.core.context.UserContext;
import com.qiuxs.cuteframework.tech.microsvc.log.ApiLogUtils;
import com.qiuxs.rmq.log.entity.MqFailed;
import com.qiuxs.rmq.log.service.IMqFailedService;

/**
 * 记录消费失败
 * 功能描述: <br/>  
 * 新增原因: TODO<br/>  
 * 新增日期: 2020年9月16日 下午3:01:50 <br/>  
 *  
 * @author qiuxs   
 * @version 1.0.0
 */
public class MqLogUtils {
	
	private static IMqFailedService mqFailedService;
	
	/**
	 * 保存消费失败数据
	 *  
	 * @author qiuxs  
	 * @param msg
	 * @param body
	 * @param e
	 */
	public static void saveMqFailed(MessageExt msg, Object body, Throwable e) {
		MqFailed mqFailed = getMqFailedService().getByUk(msg.getMsgId());
		if (mqFailed == null) {
			mqFailed = new MqFailed();
		}
		mqFailed.setBizBody(JsonUtils.toJSONString(body));
		mqFailed.setBizKey(msg.getKeys());
		mqFailed.setBornTime(new Date(msg.getBornTimestamp()));
		mqFailed.setExtProp(JsonUtils.toJSONString(msg.getProperties()));
		mqFailed.setGlobalId(ApiLogUtils.getGlobalId());
		mqFailed.setMsgId(msg.getMsgId());
		mqFailed.setReconsumeTimes(msg.getReconsumeTimes());
		mqFailed.setServerId(EnvironmentContext.getServerId());
		mqFailed.setStacktrace(ExceptionUtils.getStackTrace(e));
		mqFailed.setTags(msg.getTags());
		mqFailed.setTopic(msg.getTopic());
		UserLite userLite = UserContext.getUserLiteOpt();
		if (userLite != null) {
			mqFailed.setUnitId(userLite.getUnitId());
			mqFailed.setUserId(userLite.getUserId());
		}
		getMqFailedService().save(mqFailed);
	}
	
	private static IMqFailedService getMqFailedService() {
		if (mqFailedService == null) {
			mqFailedService = ApplicationContextHolder.getBean(IMqFailedService.class);
		}
		return mqFailedService;
	}

	public static void deleteMqFailed(MessageExt msg) {
		getMqFailedService().deleteByUk(msg.getMsgId());
	}
	
}
