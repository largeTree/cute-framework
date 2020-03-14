package com.qiuxs.cuteframework.web.bean;

import java.util.HashMap;
import java.util.Map;

import com.qiuxs.cuteframework.core.basic.utils.ExceptionUtils;
import com.qiuxs.cuteframework.core.basic.utils.MapUtils;
import com.qiuxs.cuteframework.core.basic.utils.StringUtils;
import com.qiuxs.cuteframework.web.action.ActionConstants;

/**
 * 简单封装请求参数
 * @author qiuxs
 *
 */
public class ReqParam extends HashMap<String, String> {
	private static final long serialVersionUID = 5848102328239783017L;

	public ReqParam(Map<String, String> initVal) {
		super(initVal);
	}

	public Long getLong(String key) {
		return MapUtils.getLong(this, key);
	}

	public String getString(String key) {
		return MapUtils.getString(this, key);
	}

	public String getStringMust(String key) {
		String str = MapUtils.getString(this, key);
		if (StringUtils.isBlank(str)) {
			ExceptionUtils.throwLogicalException("param_required", key);
		}
		return str; 
	}

	public Integer getInteger(String key) {
		return MapUtils.getInteger(this, key);
	}

	/**
	 * 获得sessionId
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public String getSessionId() {
		return MapUtils.getString(this, ActionConstants.PARAM_SESSION_ID);
	}
	
	/**
	 * 请求地址
	 * @return
	 */
	public String getRequestUrl() {
		return MapUtils.getStringMust(this, ActionConstants.REQUEST_URL);
	}

	/**
	 * 客户端ip
	 * @return
	 */
	public String getCliIp() {
		return MapUtils.getString(this, ActionConstants.CLIENT_IP);
	}

	/**
	 * 服务端端口
	 * @return
	 */
	public int getPort() {
		return MapUtils.getInteger(this, ActionConstants.REQUEST_PORT);
	}

	/**
	 * 协议
	 * @return
	 */
	public String getScheme() {
		return MapUtils.getString(this, ActionConstants.REQUEST_SCHEME);
	}

	/**
	 * 域名
	 * @return
	 */
	public String getHost() {
		return MapUtils.getString(this, ActionConstants.SERVER_HOST);
	}

	/**
	 * 是否需要翻译
	 * @return
	 */
	public boolean getWrapper() {
		return MapUtils.getBooleanValue(this, ActionConstants.PARAM_WRAPPER, false);
	}

}
