package com.qiuxs.cuteframework.core.basic.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

public class DateFormatUtils {
	
	/** 每天有多少秒 */
	public static final int DAY_SECONDS = 24 * 60 * 60;
	/** 每小时有多少秒 */
	public static final int HOUR_SECONDS = 60 * 60;
	/** 每分钟有多少秒 */
	public static final int MINUTE_SECONDS = 60;

	public static final String yyyy_MM_dd = "yyyy-MM-dd";
	public static final String yyyyMMdd = "yyyyMMdd";
	public static final String yyyyMMddHHmmss = "yyyyMMddHHmm";
	public static final String yyyyMMddHHmm = "yyyyMMddHH";

	public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static final String yyyy_MM_dd_HH_mm_ss_UNDERLINE = "yyyy_MM_dd_HH_mm_ss";

	public static final String yyyy_MM_dd_HH_MM_SS_CN = "yyyy年MM月dd日HH时mm分ss秒";

	private static FastDateFormat dataParser = FastDateFormat.getInstance(yyyy_MM_dd);
	private static FastDateFormat timeParser = FastDateFormat.getInstance(yyyy_MM_dd_HH_mm_ss);
	
	public static Date parseDate(String dateString) {
		try {
			return dataParser.parse(dateString);
		} catch (ParseException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	public static String formatDate(Date date) {
		return dataParser.format(date);
	}

	public static Date parseTime(String timeString) {
		try {
			return timeParser.parse(timeString);
		} catch (ParseException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	public static String formatTime(Date time) {
		return timeParser.format(time);
	}

	public static String format(Date date, String pattern) {
		return FastDateFormat.getInstance(pattern).format(date);
	}
	
	/**
	 * 秒数转
	 * xx天xx小时xx分钟xx秒
	 *  
	 * @author qiuxs  
	 * @param second
	 * @return
	 */
	public static String secondToChinese(long second) {
		int day = (int) (second / DAY_SECONDS);
		int hours = (int) (second % DAY_SECONDS / HOUR_SECONDS);
		int minute = (int) (second % DAY_SECONDS % HOUR_SECONDS / MINUTE_SECONDS);
		int sec = (int) (second % DAY_SECONDS % HOUR_SECONDS % MINUTE_SECONDS);
		StringBuilder sb = new StringBuilder();
		if (day > 0) {
			sb.append(day).append("天");
		}
		if (hours > 0) {
			sb.append(hours).append("小时");
		}
		if (minute > 0) {
			sb.append(minute).append("分钟");
		}
		if (sec > 0) {
			sb.append(sec).append("秒");
		}
		return sb.toString();
	}
	
	/**
	 * 获取下次指定的时间点，小时:分钟
	 *  
	 * @author qiuxs  
	 * @param hour
	 * @param min
	 * @return
	 */
	public static Date getNextTime(int hour, int min) {
		Calendar cal = Calendar.getInstance();
		int curHour = cal.get(Calendar.HOUR_OF_DAY);
		if (curHour > hour) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
		} else {
			int curMin = cal.get(Calendar.MINUTE);
			if (curHour == hour && curMin > min) {
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
		}
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取昨天
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static Date getYesterday() {
		return getDaysAgo(1);
	}
	
	/**
	 * 获取近days天
	 *  
	 * @author qiuxs  
	 * @param days
	 * @return
	 */
	public static Date getDaysAgo(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -days);
		return cal.getTime();
	}

	/**
	 * 清理掉时间，仅保留日期
	 *  
	 * @author qiuxs  
	 * @param date
	 * @return
	 */
	public static Date clearNoTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取下一个整点小时
	 *  
	 * @author qiuxs  
	 * @return
	 */
	public static Date getNextHour() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, 1);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

}
