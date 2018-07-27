package com.qiuxs.cuteframework.core.basic;

public class Constants {

	public static final String DEFAULT_LANG = "cn";

	public static final String UTF_8 = "utf-8";
	public static final String DEFAULT_CHARSET = UTF_8;

	public enum DsType {
		/**数据源类型:入口库*/
		ENTRY,
		/**数据源类型:业务库*/
		BIZ,
		/**数据源类型:日志库*/
		LOG,
		/**数据源类型:序列库*/
		SEQ,
		/**不支持，用于手动设置*/
		UNKNOWN;
		public String value() {
			return toString().toLowerCase();
		}
	}
}
