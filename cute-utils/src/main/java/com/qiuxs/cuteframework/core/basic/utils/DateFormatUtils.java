package com.qiuxs.cuteframework.core.basic.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

public class DateFormatUtils {

	public static final String yyyy_MM_dd = "yyyy-MM-dd";

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

}
