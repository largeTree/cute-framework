package com.qiuxs.cuteframework.core.basic.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 位操作工具类
 * 
 * @author qiuxs
 *
 */
public class BitUtils {
	/**
	 * 获取指定的位集值有哪几位组成的,例如把15拆成1、2、4、8
	 * 
	 * @param bitsValue
	 * @return 从低位到高位
	 * @author qiuxs created on 2018-06-09
	 */
	public static List<Long> splitBits(Long bitsValue) {
		long bigFlag = 1;
		List<Long> rets = new ArrayList<Long>();
		if (bitsValue == null)
			return rets;
		do {
			if ((bigFlag & bitsValue) > 0) {
				rets.add(bigFlag);
			}
			bigFlag *= 2;
		} while (bigFlag <= bitsValue);

		return rets;
	}

	/**
	 * 把独立的位值合并成一个位集值，例如把1、2、4、8合并成15
	 * 
	 * @param bitList
	 * @return
	 * @author qiuxs created on 2018-06-09
	 */
	public static Long mergerBits(List<Long> bitList) {
		long ret = 0;
		if (bitList != null) {
			for (Long bit : bitList) {
				ret = ret | bit;
			}
		}
		return ret;
	}

	/**
	 * 执行位与操作
	 * 
	 * @author qiuxs
	 * @param v1
	 *            执行操作的第一个值
	 * @param v2
	 *            执行操作的第二个值
	 * @return 位与操作结果
	 */
	public static long bitAnd(long v1, long v2) {
		return v1 & v2;
	}

	/**
	 * 合并mode，已宽松为准
	 * 
	 * @author qiuxs
	 * @param mode
	 * @return
	 */
	public static Integer bitOr(Integer mode, Integer otherMode) {
		if (mode == null) {
			return otherMode;
		}
		if (otherMode == null) {
			return mode;
		}
		return mode | otherMode;
	}

	/**
	 * v1中包含v2的位
	 * 
	 * @author qiuxs
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static boolean contains(long v1, long v2) {
		return (v1 & v2) == v2;
	}

	/**
	 * v1中包含v2的位
	 * 
	 * @author qiuxs
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static boolean contains(int v1, int v2) {
		return (v1 & v2) == v2;
	}

	/**
	 * 00000000000000000000000000000001 = 1
	 * 00000000000000000000000000000111 = 7
	 * 00000000000000000000000100000111 = 263
	 * 
	 * @author qiuxs  
	 * @param unionTag
	 * @return
	 */
	public static Long stringBitToLong(String unionTag) {
		Long bit = null;
		if (unionTag == null) {
			return bit;
		}
		bit = 0L;
		int bitNum = 0;
		for (int i = unionTag.length() - 1; i >= 0; i--) {
			char c = unionTag.charAt(i);
			bit |= (Long.parseLong(String.valueOf(c)) << bitNum);
			bitNum++;
		}
		return bit;
	}
	
	
}
