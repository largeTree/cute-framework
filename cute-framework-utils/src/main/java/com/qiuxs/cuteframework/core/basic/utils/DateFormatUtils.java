package com.qiuxs.cuteframework.core.basic.utils;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

public class DateFormatUtils {

	public static final String yyyy_MM_dd = "yyyy-MM-dd";

	public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

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

}
