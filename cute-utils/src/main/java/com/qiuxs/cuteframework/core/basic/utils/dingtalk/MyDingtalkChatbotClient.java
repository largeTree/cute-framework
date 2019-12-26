package com.qiuxs.cuteframework.core.basic.utils.dingtalk;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dingtalk.chatbot.DingtalkChatbotClient;
import com.dingtalk.chatbot.SendResult;
import com.dingtalk.chatbot.message.Message;

/**
 * 钉钉客户端工具
 * @author qiuxs
 * 2019年4月28日 下午9:49:38
 */
public class MyDingtalkChatbotClient {

	private static Logger log = LogManager.getLogger(MyDingtalkChatbotClient.class);

	private static final String API_URL = "https://oapi.dingtalk.com/robot/send?access_token={0}";

	private String token;
	private DingtalkChatbotClient client = new DingtalkChatbotClient();

	/**
	 * 构造客户端实例
	 * @param token
	 * 		机器人的token
	 * 2019年4月28日 下午9:49:56
	 * @author qiuxs
	 */
	public MyDingtalkChatbotClient(String token) {
		this.token = token;
	}

	/**
	 * 发送消息
	 * 
	 * 2019年4月28日 下午9:49:51
	 * @auther qiuxs
	 * @param message
	 * @return
	 */
	public SendResult send(Message message) {
		try {
			return this.client.send(MessageFormat.format(API_URL, this.token), message);
		} catch (IOException e) {
			log.error("send error ext = " + e.getLocalizedMessage(), e);
		}
		return null;
	}

}
