package com.ms.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateUtil {
	
	private static Log logger = LogFactory.getLog(DateUtil.class);
	
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

	private final static SimpleDateFormat sdfDay = new SimpleDateFormat(
			"yyyy-MM-dd");

	private final static SimpleDateFormat sdfDays = new SimpleDateFormat(
			"yyyyMMdd");

	private final static SimpleDateFormat sdfTime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 获取YYYY格式
	 * 
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 * 
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}

	/**
	 * 获取YYYYMMDD格式
	 * 
	 * @return
	 */
	public static String getDays() {
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 * 
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}

	/**
	 * 获取今天是星期几
	 */
	public static String getWeekName() {
		String weekName = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int num = cal.get(Calendar.DAY_OF_WEEK);
		if (num == 1) {
			weekName = "星期日";
		} else if (num == 2) {
			weekName = "星期一";
		} else if (num == 3) {
			weekName = "星期二";
		} else if (num == 4) {
			weekName = "星期三";
		} else if (num == 5) {
			weekName = "星期四";
		} else if (num == 6) {
			weekName = "星期五";
		} else if (num == 7) {
			weekName = "星期六";
		}
		return weekName;
	}

	/**
	 * @Title: compareDate
	 * @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
	 * @param s
	 * @param e
	 * @return boolean
	 * @throws
	 * @author luguosui
	 */
	public static boolean compareDate(String s, String e) {
		if (fomatDate(s) == null || fomatDate(e) == null) {
			return false;
		}
		return fomatDate(s).getTime() >= fomatDate(e).getTime();
	}

	/**
	 * 格式化日期
	 * 
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 校验日期是否合法
	 * 
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}

	public static int getDiffYear(String startTime, String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			long aa = 0;
			int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(
					startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}

	/**
	 * <li>功能描述：时间相减得到天数
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author Administrator
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		java.util.Date beginDate = null;
		java.util.Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);

		return day;
	}

	/**
	 * 得到n天之后的日期
	 * 
	 * @param days
	 * @return
	 */
	public static String getAfterDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * 得到n天之后是周几
	 * 
	 * @param days
	 * @return
	 */
	public static String getAfterDayWeek(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("E");
		String dateStr = sdf.format(date);

		return dateStr;
	}

	public static void main(String[] args) {
		System.out.println(getDays());
		System.out.println(getAfterDayWeek("3"));
	}

	/**
	 * 日期格式字符串转换成时间戳
	 * 
	 * @param date
	 *            字符串日期
	 * @param format
	 *            如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String date2TimeStamp(String date_str, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return String.valueOf(sdf.parse(date_str).getTime() / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取本周一时间 number 0:本周
	 * 
	 * @return
	 */
	public static Date getWeekStartDate(int number) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.add(Calendar.WEEK_OF_YEAR, number);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date date = cal.getTime();
		return date;
	}

	/**
	 * 获取本周日时间
	 * 
	 * @return
	 */
	public static Date getWeekEndDate(int number) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		cal.add(Calendar.WEEK_OF_YEAR, number);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date date = cal.getTime();
		return date;
	}

	/**
	 * 得到n天之后的日期
	 * 
	 * @param days
	 * @return
	 */
	public static String getAfterDayDate(int days, Date time) {

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.setTime(time);
		canlendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(date);

		return dateStr;
	}
	/**
	 * 得到时间相差毫秒数
	 * @param sDate
	 * @param bDate
	 * @return
	 */
	public static long mSecondDifference(long sDate, String bDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long diff = 0;
		try {
			long bd = sdf.parse(bDate).getTime();
			diff = (bd - sDate);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return diff;
	}
	/**
	 * 得到时间相差毫秒数
	 * @param sDate
	 * @param bDate
	 * @return
	 */
	public static long mSecondDifference(String sDate, String bDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long diff = 0;
		try {
			long sd = sdf.parse(sDate).getTime();
			long bd = sdf.parse(bDate).getTime();
			diff = (bd - sd);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return diff;
	}
	/**
	 * 得到时间在多久之前
	 * @param timeStr
	 * @return
	 */
	public static String timeBefore(String timeStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long time;
		try {
			time = sdf.parse(timeStr).getTime();
			Calendar newCalendar = Calendar.getInstance();
	        Calendar oldCalendar = Calendar.getInstance();
	        oldCalendar.setTime(new Date(time));
	        if (newCalendar.get(Calendar.YEAR) > oldCalendar.get(Calendar.YEAR)) {
	        	int yearDiff = newCalendar.get(Calendar.YEAR) - oldCalendar.get(Calendar.YEAR);
	        	if(yearDiff == 1){
	        		return newCalendar.get(Calendar.MONTH) + 12 - oldCalendar.get(Calendar.MONTH) + "个月前";
	        	}else{
	        		return getFormatTime(time);
	        	}
	        } else if (newCalendar.get(Calendar.MONTH) > oldCalendar.get(Calendar.MONTH)) {
	        	int monthDiff = newCalendar.get(Calendar.MONTH) - oldCalendar.get(Calendar.MONTH);
	        	if(monthDiff == 1){
	        		return (newCalendar.get(Calendar.DAY_OF_YEAR) - oldCalendar.get(Calendar.DAY_OF_YEAR)) + "天前";
	        	} else {
	        		return (newCalendar.get(Calendar.MONTH) - oldCalendar.get(Calendar.MONTH)) + "个月前";
	        	}
	        } else if (newCalendar.get(Calendar.DAY_OF_MONTH) > oldCalendar.get(Calendar.DAY_OF_MONTH)) {
	            return (newCalendar.get(Calendar.DAY_OF_MONTH) - oldCalendar.get(Calendar.DAY_OF_MONTH)) + "天前";
	        } else if (newCalendar.get(Calendar.HOUR) > oldCalendar.get(Calendar.HOUR)) {
	            return (newCalendar.get(Calendar.HOUR) - oldCalendar.get(Calendar.HOUR)) + "小时前";
	        } else if (newCalendar.get(Calendar.MINUTE) > oldCalendar.get(Calendar.MINUTE)) {
	            return (newCalendar.get(Calendar.MINUTE) - oldCalendar.get(Calendar.MINUTE)) + "分钟前";
	        } else {
	            return "刚刚";
	        }
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}
	private static String getFormatTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(time));
    }
}
