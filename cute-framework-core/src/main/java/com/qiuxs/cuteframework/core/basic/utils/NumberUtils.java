package com.qiuxs.cuteframework.core.basic.utils;

import java.math.BigDecimal;

/**
 * 数字工具类
 * 
 * @author qiuxs
 *
 */
public class NumberUtils {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends Number> int compareTo(T num1, T num2) {
		// null认为是最小值
		if (num1 != null && num2 != null) {
			// 所有Number的实现，如Long、BigDecimal等，都实现了Comparable接口，这里使用Comparable接口的compareTo比较大小
			return ((Comparable) num1).compareTo(num2);
		} else if (num1 == null && num2 == null) {
			return 0;
		} else if (num1 == null) { // num2肯定不是null
			return -1;
		} else { // num2为null
			return 1;
		}
	}

	/**
	 * 比较两个数值是否相等。注意比较的两个数值类型必须完全相同。
	 * 
	 * @author lsh
	 * @param num1
	 *            待比较数值1
	 * @param num2
	 *            待比较数值2
	 * @return 数值相等或同时为null时，返回true；否则返回false
	 */
	public static <T extends Number> boolean equals(T num1, T num2) {
		return compareTo(num1, num2) == 0;
	}

	/**
	 * 判断bd1是否大于bd2
	 * 
	 * @author qiuxs
	 * @param bd1
	 * @param bd2
	 * @return true if bd1 > bd2
	 */
	public static boolean greaterThan(BigDecimal bd1, BigDecimal bd2) {
		return bd1.compareTo(bd2) > 0;
	}

	/**
	 * 判断bd1是否大于等于bd2
	 * 
	 * @author qiuxs
	 * @param bd1
	 * @param bd2
	 * @return
	 */
	public static boolean greaterThanEqual(BigDecimal bd1, BigDecimal bd2) {
		return bd1.compareTo(bd2) >= 0;
	}

	/**
	 * 判断bd1是否小于bd2
	 * 
	 * @author qiuxs
	 * @param bd1
	 * @param bd2
	 * @return true if bd1 < bd2
	 */
	public static boolean lessThan(BigDecimal bd1, BigDecimal bd2) {
		return bd1.compareTo(bd2) < 0;
	}

	/**
	 * 判断bd1是否小于等于bd2
	 * 
	 * @author qiuxs
	 * @param bd1
	 * @param bd2
	 * @return
	 */
	public static boolean lessThanEqual(BigDecimal bd1, BigDecimal bd2) {
		return bd1.compareTo(bd2) <= 0;
	}

	/**
	 * 判断bd1是否大于0
	 * 
	 * @author qiuxs
	 * @param bd1
	 * @return
	 */
	public static boolean greaterThanZero(BigDecimal bd1) {
		return bd1.compareTo(BigDecimal.ZERO) > 0;
	}

	/**
	 * 判断bd1是否大于等于0
	 * 
	 * @author qiuxs
	 * @param bd1
	 * @return
	 */
	public static boolean greaterThanEqualZero(BigDecimal bd1) {
		return bd1.compareTo(BigDecimal.ZERO) >= 0;
	}

	/**
	 * 判断bd1是否小于0
	 * 
	 * @author qiuxs
	 * @param bd1
	 * @return
	 */
	public static boolean lessThanZero(BigDecimal bd1) {
		return bd1.compareTo(BigDecimal.ZERO) < 0;
	}

	/**
	 * 判断bd1是否小于等于0
	 * 
	 * @author qiuxs
	 * @param bd1
	 * @return
	 */
	public static boolean lessThanEqualZero(BigDecimal bd1) {
		return bd1.compareTo(BigDecimal.ZERO) <= 0;
	}

}
