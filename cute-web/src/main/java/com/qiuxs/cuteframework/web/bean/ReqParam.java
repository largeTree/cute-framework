package com.qiuxs.cuteframework.web.bean;

import java.math.BigDecimal;
import java.util.Date;
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
	
	public Boolean getBoolean(String key) {
		return MapUtils.getBoolean(this, key);
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
	
	public Integer getIntegerMust(String key) {
		return MapUtils.getIntegerMust(this, key);
	}
	
	public BigDecimal getBigDecimal(String key) {
		return MapUtils.getBigDecimal(this, key);
	}
	
	public BigDecimal getBigDecimalMust(String key) {
		return MapUtils.getBigDecimalMust(this, key);
	}
	
	public long getLong(String key, long defVal) {
		return MapUtils.getLongValue(this, key, defVal);
	}

	public String getString(String key, String defVal) {
		return MapUtils.getString(this, key, defVal);
	}

	public int getInteger(String key, int defVal) {
		return MapUtils.getIntValue(this, key, defVal);
	}

	public Date getDate(String key) {
		return MapUtils.getDate(this, key);
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
	
	/**
	 * 转为map
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public Map<String, Object> toMap(){
		Map<String, Object> map = new HashMap<String, Object>(this);
		return map;
	}

}
