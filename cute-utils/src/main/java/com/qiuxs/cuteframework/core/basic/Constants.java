package com.qiuxs.cuteframework.core.basic;

import java.math.BigDecimal;

public class Constants {

	/** 翻译集合 */
	public static final String CAPTION_KEY = "_caption";

	public static final String DEFAULT_LANG = "cn";

	public static final String UTF_8 = "utf-8";
	public static final String DEFAULT_CHARSET = UTF_8;
	public static final String ISO_8859_1 = "ISO-8859-1";

	public static final int TRUE = 1;
	public static final int FALSE = 0;
	
	public static final String TRUE_STR = "1";
	public static final String FALSE_STR = "0";
	
	/** 系统默认支持精确度 */
	public static final int DEFAULT_SCALE_INNER = 10;

	public static final int DB_DEFAULT_INT = 0;

	public static final long DB_DEFAULT_LONG = 0;

	public static final short DB_DEFAULT_SHORT = 0;

	public static final double DB_DEFAULT_DOUBLE = 0.0D;

	public static final float DB_DEFAULT_FLOAT = 0.0F;

	public static final BigDecimal DB_DEFAULT_DECIMAL = BigDecimal.ZERO;
	
	public enum DsType {
		/**数据源类型:入口库*/
		ENTRY,
		/**数据源类型:业务库*/
		BIZ,
		/**数据源类型:日志库*/
		LOG,
		/**数据源类型:序列库*/
		SEQ,
		/** 直接指定dsId */
		DIRECT,
		/**不支持，用于手动设置*/
		UNKNOWN;
		public String value() {
			return toString().toLowerCase();
		}
	}
}
