package com.qiuxs.cuteframework.core.basic.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.qiuxs.cuteframework.core.basic.Constants;

/**
 * 数字工具类
 * 
 * @author qiuxs
 *
 */
public class NumberUtils {

	/**
	 * 两个数值相减。数值类型可为BigDecimal double float long int short byte
	 * 
	 * @author lsh
	 * @param value1
	 *            被减数
	 * @param value2
	 *            减数
	 * @return 差。类型为value1和value2中精度较高的类型。其中精度从大到小定义为：BigDecimal > double >
	 *         float > long > int > short > byte
	 */
	public static Number subtractNumricalValue(Number value1, Number value2) {
		// value1或value2为null时，作0值处理
		if (value2 == null) {
			return value1;
		}

		// value1为null时，返回value2的负值
		if (value1 == null) {
			if (value2 instanceof BigDecimal) {
				return ((BigDecimal) value2).negate();
			} else {
				value1 = new Byte("0");
			}
		}

		// 按照数值的精度，将两个值处理成相同类型：BigDecimal > double > float > long > int > short
		// > byte
		if (value1 instanceof BigDecimal || value2 instanceof BigDecimal) {
			BigDecimal val1 = value1 instanceof BigDecimal ? (BigDecimal) value1 : new BigDecimal(value1.toString());
			BigDecimal val2 = value2 instanceof BigDecimal ? (BigDecimal) value2 : new BigDecimal(value2.toString());
			return val1.subtract(val2);
		} else if (value1 instanceof Double || value2 instanceof Double) {
			return value1.doubleValue() - value2.doubleValue();
		} else if (value1 instanceof Float || value2 instanceof Float) {
			return value1.floatValue() - value2.floatValue();
		} else if (value1 instanceof Long || value2 instanceof Long) {
			return value1.longValue() - value2.longValue();
		} else if (value1 instanceof Integer || value2 instanceof Integer) {
			return value1.intValue() - value2.intValue();
		} else if (value1 instanceof Short || value2 instanceof Short) {
			return value1.shortValue() - value2.shortValue();
		} else if (value1 instanceof Byte || value2 instanceof Byte) {
			return value1.byteValue() - value2.byteValue();
		} else {
			throw new RuntimeException("不支持执行计算的类型");
		}
	}

	private static int genScale(BigDecimal bd) {
		int scale = bd.scale();
		if (scale < Constants.DEFAULT_SCALE_INNER) {
			scale = Constants.DEFAULT_SCALE_INNER;
		}
		return scale;
	}

	/**
	 * 两个数相除：指定舍入模式
	 * -精度：max(num1的精度, 10)
	 * -舍入模式：roundingMode
	 * 
	 * @author qiuxs
	 * @param num1
	 *            除数
	 * @param num2
	 *            被除数
	 * @param roundingMode
	 *            舍入模式
	 * @return
	 */
	public static BigDecimal divide(BigDecimal num1, BigDecimal num2, RoundingMode roundingMode) {
		if (num1 == null || num2 == null) {
			return null;
		}
		if (NumberUtils.equals(num2, BigDecimal.ZERO)) {
			ExceptionUtils.throwLogicalException("arith_divide_zero_divisor");
		}
		int scale = genScale(num1);
		return divide(num1, num2, scale, roundingMode);
	}

	/**
	 * 两个数相除：指定精度和舍入模式
	 * 
	 * @author fengdg
	 * @param num1
	 *            除数
	 * @param num2
	 *            被除数
	 * @param scale
	 *            精度
	 * @param roundingMode
	 *            舍入模式
	 * @return
	 */
	public static BigDecimal divide(BigDecimal num1, BigDecimal num2, int scale, RoundingMode roundingMode) {
		if (num1 == null || num2 == null) {
			return null;
		}
		if (NumberUtils.equals(num2, BigDecimal.ZERO)) {
			ExceptionUtils.throwLogicalException("arith_divide_zero_divisor");
		}
		return num1.divide(num2, scale, roundingMode);
	}

	public static Long longValueOf(Integer integer) {
		if (null == integer) {
			return null;
		}
		return Long.valueOf(integer);
	}

	/**
	 * 比较数字大小
	 * @author qiuxs
	 *
	 * @param num1
	 * @param num2
	 * @return
	 *
	 * 创建时间：2018年7月26日 下午10:21:23
	 */
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

	/**
	 * 判断数字是否为空
	 * @author qiuxs
	 *
	 * @param num
	 * @return
	 *
	 * 创建时间：2018年8月20日 下午6:38:29
	 */
	public static boolean isEmpty(Number num) {
		return num == null || num.longValue() == 0;
	}

	/**
	 * 如果参数为null则返回0
	 * @param totalResults
	 * @return
	 */
	public static Long defaultLong(Long num) {
		return num == null ? 0L : num;
	}

	/**
	 * 字符串转BigDecimal
	 *  new BigDecimal(strNum);
	 * @author qiuxs  
	 * @param strNum
	 * @return
	 */
	public static BigDecimal parseBigDecimal(String strNum) {
		if (StringUtils.isBlank(strNum)) {
			return BigDecimal.ZERO;
		}
		return new BigDecimal(strNum);
	}

}
