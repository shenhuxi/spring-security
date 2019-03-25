
package com.zpself.manage.common.controller.util;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 
 * @author luozb
 * @version 2018-9-15
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {

	private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

	/**
	 * 得到本周周一
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getMondayOfThisWeek() {

		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 1);
		return df2.format(c.getTime());
	}

	/**
	 * 得到本周周日
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getSundayOfThisWeek() {
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 7);
		return df2.format(c.getTime());
	}

	/**
	 * 得到上周周一
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getMondayOfLastWeek() {

		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week - 6);
		return df2.format(c.getTime());
	}

	/**
	 * 得到上周周几 num传6为上周1，5为上周2，4为上周3，3为上周4，2为上周5，1为上周6，0为上周日
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getDayOfLastWeek(int num) {
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week - num);
		return df2.format(c.getTime());
	}

	/**
	 * 得到上周周日
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getSundayOfLastWeek() {
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week);
		return df2.format(c.getTime());
	}

	// 比较两个时间 返回1，表示d1 >dt2,返回-1，表示d1 <dt2,返回0，表示d1 =dt2
	public static int compareDate(Date d1, Date d2) {
		if (d1.getTime() > d2.getTime()) {
			return 1;
		} else if (d1.getTime() < d2.getTime()) {
			return -1;
		} else {// 相等
			return 0;
		}
	}

	/*
	 * 通过传入起始与结束日期，判断两个日期相差几周 (本方法采用中国自然周，周一为第一天，周日为一周的最后一天) 同时可以判断两个日期是否为同一周
	 * 
	 */
	// -----------------------------------------------------------------------------------------------------------------------------------------------
	public static String getWeekCounterByEventStartDate(String event_s_date, String dailyDate) {
		if (isSameWeek(event_s_date, dailyDate)) {
			return "1";
		}
		Calendar c_base = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		Calendar tempC = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		int week = 0;
		try {
			c_base.setTime(df.parse(event_s_date));
			c2.setTime(df.parse(dailyDate));
			Long time1 = c2.getTimeInMillis();
			Long time2 = c_base.getTimeInMillis();
			Long dayDiffer = Math.abs(time1 - time2) / (1000 * 60 * 60 * 24);// 毫秒*秒*分*小时
			if (dayDiffer.intValue() == 0) {
				week++;
				return "" + week;
			}
			int curTodayWeek = c_base.get(Calendar.DAY_OF_WEEK) - 1;
			int differIsWeek = 7 - curTodayWeek;
			tempC = (Calendar) c_base.clone();

			if (differIsWeek != 0) {
				week++;
			}
			if (differIsWeek != 7) {
				tempC.add(Calendar.DAY_OF_YEAR, differIsWeek);
			}
			time1 = c2.getTimeInMillis();
			time2 = tempC.getTimeInMillis();
			dayDiffer = Math.abs(time1 - time2) / (1000 * 60 * 60 * 24);// 毫秒*秒*分*小时
			if (dayDiffer.intValue() == 1) {// 差一天
				week++;
			} else if (dayDiffer.intValue() < 7) {
				week++;
			} else {
				DecimalFormat decimalFormat = new DecimalFormat("#.0");
				String dayNum = decimalFormat.format((double) dayDiffer.intValue() / 7);
				int day = Integer.parseInt(dayNum.substring(0, dayNum.indexOf(".")));
				week += day;
				int dec = Integer.parseInt(dayNum.substring(dayNum.indexOf(".") + 1, dayNum.length()));
				if (dec != 0) {
					week++;
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return "";
		}
		return "" + week;
	}

	// 第一种 isSameWeek

	public static boolean isSameWeek(String date1, String date2) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(date1);
			d2 = format.parse(date2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(d1);
		cal2.setTime(d2);

		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		int subMonth = (cal1.get(Calendar.MONTH) + 1) - (cal2.get(Calendar.MONTH) + 1);

		// 同一年，同一月
		if (subYear == 0 && subMonth == 0) {
			int week1 = cal1.get(Calendar.WEEK_OF_MONTH);
			int week2 = cal2.get(Calendar.WEEK_OF_MONTH);
			int wd1 = cal1.get(Calendar.DAY_OF_WEEK);
			int wd2 = cal2.get(Calendar.DAY_OF_WEEK);
			if (wd1 == 1) {
				week1 -= 1;
			}
			if (wd2 == 1) {
				week2 -= 1;
			}
			return week1 == week2;
		}
		return false;
	}
	// -----------------------------------------------------------------------------------------------------------------------------------------------

	/*
	 * 通过传入第几周的周数，确定那一周的周一与周日的日期 (本方法采用中国自然周，周一为第一天，周日为一周的最后一天)
	 */
	public static String getDateByWeekCount(int n) {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, n);// n为周数

		Calendar cal1 = (Calendar) cal.clone();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		cal.add(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_WEEK) - dayOfWeek);
		cal.add(Calendar.DATE, 1);
		Date d = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(d));
		cal1.add(Calendar.DATE, cal1.getActualMaximum(Calendar.DAY_OF_WEEK) - dayOfWeek);
		cal1.add(Calendar.DATE, 1);
		System.out.println(sdf.format(cal1.getTime()));
		return sdf.format(cal1.getTime());
	}

	/**
	 * start 本周开始时间戳 - (本方法采用中国自然周，周一为第一天，周日为一周的最后一天)
	 */
	public static String getWeekStartTime() {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
		Calendar cal = Calendar.getInstance();
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0) {
			day_of_week = 7;
		}
		cal.add(Calendar.DATE, -day_of_week + 1);
		return simpleDateFormat.format(cal.getTime());
		// return simpleDateFormat.format(cal.getTime()+"000000000");
	}

	/**
	 * end 本周结束时间戳 - 以星期一为本周的第一天
	 */
	public static String getWeekEndTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
		Calendar cal = Calendar.getInstance();
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0) {
			day_of_week = 7;
		}
		cal.add(Calendar.DATE, -day_of_week + 7);
		return simpleDateFormat.format(cal.getTime());
		// return simpleDateFormat.format(cal.getTime()) + "235959999";
	}

	/**
	 * 获取指定日期的年
	 * 
	 * @param
	 * @return
	 */
	public static int getYear(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=sdf.format(date);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime ldt = LocalDateTime.parse(time, formatter);
		return ldt.getYear();
}

	/**
	 * 获取指定日期的月
	 * 
	 * @param
	 * @return
	 */
	public static int getMonth(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=sdf.format(date);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime ldt = LocalDateTime.parse(time, formatter);
		return  ldt.getMonth().getValue();
	}

	/**
	 * 获取指定日期的日
	 * 
	 * @param
	 * @return
	 */
	public static int getDay(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=sdf.format(date);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime ldt = LocalDateTime.parse(time, formatter);
		return ldt.getDayOfMonth();
	}

	/**
	 * 获取某年第一天日期
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public static Date getYearFirst(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 获取某年最后一天日期
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public static Date getYearLast(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();

		return currYearLast;
	}

	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd",
			"yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" };

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 得到几天以前的星期字符串 格式（E）星期几
	 */
	public static String getWeek(int day) {
		Date date = new Date(); // 当前时间
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -day); // 设置为前一天
		Date dBefore = calendar.getTime();
		return formatDate(dBefore, "E");
	}

	/**
	 * 得到指定日期的星期字符串 格式（E）星期几
	 */
	public static String getWeekNum(Date time) {
		return formatDate(time, "E");
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		if (str instanceof Long) {
			return new Date((Long) str);
		}
		if (str instanceof Integer) {
			return new Date((Integer) str);
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parseDate(String str, String patterns) throws ParseException {
		Date date = null;
		if (StringUtils.isBlank(str)) {
			return date;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(patterns);
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			logger.error("ParseDate Exception", e);
			throw e;
		}
		return date;
	}

	public static String forDate(Date date) {
		Date endDate = date;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
		// yyyy-MM-dd'T'HH:mm:ssz
		String endDataStr = f.format(endDate);
		// System.out.println("时间："+Constants.FOR_MATTER.format(endDate));
		return endDataStr;
	}

	public static String forDateEnd(Date date) {
		Date endDate = date;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:59:59");
		// yyyy-MM-dd'T'HH:mm:ssz
		String endDataStr = f.format(endDate);
		// System.out.println("时间："+Constants.FOR_MATTER.format(endDate));
		return endDataStr;
	}

	public static String forDates(Date date) {
		Date endDate = date;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		// yyyy-MM-dd'T'HH:mm:ssz
		String endDataStr = f.format(endDate);
		// System.out.println("时间："+Constants.FOR_MATTER.format(endDate));
		return endDataStr;
	}

	public static String forBeginMatterDatessZ(Date date) {
		Date endDate = date;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:00");
		// yyyy-MM-dd'T'HH:mm:ssz
		String endDataStr = f.format(endDate) + ".000+08:00";
		// System.out.println("时间："+Constants.FOR_MATTER.format(endDate));
		return endDataStr;
	}

	public static String forBeginMatterDatessZS(Date date) {
		Date endDate = date;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:59");
		// yyyy-MM-dd'T'HH:mm:ssz
		String endDataStr = f.format(endDate) + ".999+08:00";
		// System.out.println("时间："+Constants.FOR_MATTER.format(endDate));
		return endDataStr;
	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	public static Date getDateTimeSave(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
		try {
			date = sdf.parse(formatDate(date, "yyyy-MM-dd HH:00:00"));
		} catch (ParseException e) {
			// e.printStackTrace();
			logger.error("Exception", e);
		}
		return date;
	}

	public static Date getDateSave(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(formatDate(date, "yyyy-MM-dd HH:mm:00"));
		} catch (ParseException e) {
			// e.printStackTrace();
			logger.error("Exception", e);
		}
		return date;
	}

	public static Date getDateStarts(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(formatDate(date, "yyyy-MM-dd"));
		} catch (ParseException e) {
			// e.printStackTrace();
			logger.error("Exception", e);
		}
		return date;
	}

	public static Date getDateStart(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 00:00:00");
		} catch (ParseException e) {
			// e.printStackTrace();
			logger.error("Exception", e);
		}
		return date;
	}

	// 获取当日最后一秒的时间
	public static Date getDateEnd(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 23:59:59");
		} catch (ParseException e) {
			// e.printStackTrace();
			logger.error("Exception", e);
		}
		return date;
	}

	/**
	 * Adjust the date by days
	 * 
	 * @param date
	 *            Date the date which you want to adjust
	 * @param days
	 *            int how many day will adjust.
	 * @return Date the day which have already been delayed.
	 */
	public static Date adjustDateByDay(Date date, int days) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}

	@SuppressWarnings("deprecation")
	public static Date dateAddHouse(Date date, int house) {
		date.setHours(date.getHours() + house);
		return date;
	}

	@SuppressWarnings("deprecation")
	public static Date dateMinusHouse(Date date, int house) {
		date.setHours(date.getHours() - house);
		return date;
	}

	public static Date getDateAll(String date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return myDateFormat.parse(date);
		} catch (ParseException e) {
			logger.error("试图将" + date + "转换成yyyy-MM-dd格式的日期类型，转换失败！", e);
			return null;
		}
	}

	// 得到当前N分钟前的时间
	public static Date getTimeByMinute(int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minute);
		return calendar.getTime();
	}

	// 得到输入时间的 前N分钟前的时间
	public static Date getTimeByMinute(Date d, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minute);
		return calendar.getTime();
	}

	/**
	 * 获取两个时间差（分钟数）
	 * 
	 * @param startDT
	 * @param endDT
	 * @return
	 */
	public static Long getMiuteSpace(Date startDT, Date endDT) {
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		startCal.setTime(startDT);
		endCal.setTime(endDT);
		return (endCal.getTimeInMillis() - startCal.getTimeInMillis()) / (1000 * 60);// 转化minute
	}

	/**
	 * 
	 * 将日期格式转换成另一种日期格式
	 * 
	 * @param date
	 *            pattern(如"yyyy-MM-dd HH:mm:00")
	 * @return
	 * @author:
	 */
	public static Date getDateCure(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String format2 = format.format(date);
		ParsePosition pos = new ParsePosition(0);
		Date data = format.parse(format2, pos);
		return data;
	}

	public static String getDateMoveStart(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:00");
		String format2 = format.format(date);
		return format2;
	}

	public static String getDateMoveEnd(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:59");
		String format2 = format.format(date);
		return format2;
	}

	public static boolean shouldSumHour(String endtime) {
		return endtime.endsWith("59:59");
	}

	public static boolean shouldSumDay(String endtime) {
		return endtime.endsWith("23:59:59");
	}

	public static Date adjustDateByHour(Date date, int hour) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}

	public static void getStartEndTime(Date scheduleTime, Date[] dates) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(scheduleTime);
		calendar.add(Calendar.MINUTE, -11);
		dates[1] = calendar.getTime();
		calendar.setTime(scheduleTime);
		calendar.add(Calendar.MINUTE, -20);
		dates[0] = calendar.getTime();
	}

	public static Date adjustDateByYear(Date date, int year) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, year);
		return cal.getTime();
	}

	public static String forIncreDate(Date date) {
		Date endDate = date;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		// yyyy-MM-dd'T'HH:mm:ssz
		String endDataStr = f.format(endDate);
		// System.out.println("时间："+Constants.FOR_MATTER.format(endDate));
		return endDataStr;
	}

	public static String forIncreDateEnd(Date date) {
		Date endDate = date;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		// yyyy-MM-dd'T'HH:mm:ssz
		String endDataStr = f.format(endDate);
		// System.out.println("时间："+Constants.FOR_MATTER.format(endDate));
		return endDataStr;
	}

	public static boolean isMonth(String startDate, String endDate) {
		if (margin(startDate, endDate) > 31) {
			return false;
		}
		int startMonth = Integer.parseInt(startDate.substring(5, 7));
		int endMonth = Integer.parseInt(endDate.substring(5, 7));
		if (startMonth == endMonth) {
			return true;
		} else {
			return false;
		}
	}

	private static int margin(String startDate, String endDate) {
		ParsePosition pos = new ParsePosition(0);
		ParsePosition pos2 = new ParsePosition(0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date ds = sdf.parse(startDate, pos);
		Date de = sdf.parse(endDate, pos2);
		long l = de.getTime() - ds.getTime();
		int margin = (int) (l / 24 * 60 * 60 * 1000);
		return margin;
	}

	/* 判断时间starttime是否在endtime的七天之内，如果是返回true,反之返回false */
	public static boolean isLatestWeek(Date starttime, Date endtime) {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(endtime);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -7); // 设置为7天前
		Date before7days = calendar.getTime(); // 得到7天前的时间
		if (before7days.getTime() < starttime.getTime()) {
			return true;
		} else {
			return false;
		}
	}

	public static String getFirstDayOfCurrYear() {
		Date d = new Date();
		Calendar cc = Calendar.getInstance();
		cc.setTime(d);
		cc.set(Calendar.DAY_OF_YEAR, 1);
		Date dd = cc.getTime(); // 第一天
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String firstDay = sdf.format(dd);
		return firstDay;
	}

	/* 取指定传入时间所在周的周日 */
	public static String convertWeekDate(Date time) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		// cal.add(Calendar.DATE,
		// cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		// String imptimeBegin = sdf.format(cal.getTime()); //周一时间
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day + 6);
		String weekend = sdf.format(cal.getTime()); // 周日时间
		return weekend;

	}

	// 获取传入日期所在月的第一天
	public static Date getFirstDayDateOfMonth(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		final int last = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, last);
		return cal.getTime();
	}

	// 获取传入日期所在月的最后一天
	public static Date getLastDayOfMonth(final Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		final int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, last);
		return cal.getTime();
	}

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		// Date date = dateAddHouse(new Date(),2);
		// System.out.println(formatDateTime(date));
		// Date date = parseDate("2017-06-23 15:45:23");
		 Date time = new Date();
		Date dateForm = DateUtils.getDateCure(time, "yyyy-MM");
		System.out.println(getYear(time));
		System.out.println(getMonth(time));
		System.out.println(getDay(time));
	}

}