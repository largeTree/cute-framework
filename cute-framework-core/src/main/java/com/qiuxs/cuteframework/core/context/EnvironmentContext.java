package com.qiuxs.cuteframework.core.context;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties(prefix = EnvironmentContext.PREFIX)
public class EnvironmentContext {

	protected static final String PREFIX = "env";

	/** 应用名 */
	private String appName;
	/** 序列类型 */
	private String seqType;
	/** 序列所在数据库索引 */
	private int seqDbIndex;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getSeqType() {
		return seqType;
	}

	public void setSeqType(String seqType) {
		this.seqType = seqType;
	}

	public int getSeqDbIndex() {
		return seqDbIndex;
	}

	public void setSeqDbIndex(int seqDbIndex) {
		this.seqDbIndex = seqDbIndex;
	}

}
