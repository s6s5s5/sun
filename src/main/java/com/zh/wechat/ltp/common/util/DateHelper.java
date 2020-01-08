package com.zh.wechat.ltp.common.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author zhangLin
 */
public class DateHelper {
	public static final String ENG_DATE_FROMAT = "EEE, d MMM yyyy HH:mm:ss z";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM = "yyyy-MM";
	public static final String YYYYMM = "yyyyMM";
	public static final String YYYY = "yyyy";
	public static final String MM = "MM";
	public static final String DD = "dd";
	public static final String HH = "HH";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String HHMMSS = "HHmmss";

	/**
	 * 获取前一天日期yyyy-MM-dd
	 *
	 * @return
	 */
	public static String getYestoday() {
		return DateTime.now().minusDays(1).toString(YYYY_MM_DD);
	}

	/**
	 * 获取当前的日期yyyy-MM-dd
	 */
	public static String getCurrentDate() {
		return DateTime.now().toString(YYYY_MM_DD);
	}

	/**
	 * 获取当前的时间yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentTime() {
		return DateTime.now().toString(YYYY_MM_DD_HH_MM_SS);
	}
	/**
	 * 获取当前的时间yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrent() {
		return DateTime.now().toString(ENG_DATE_FROMAT);
	}
	/**
	 * 获取当前的时间(毫秒)yyyyMMddHHmmssSSS
	 */
	public static String getCurrentTimess() {
		return DateTime.now().toString(YYYYMMDDHHMMSSSSS);
	}

	/**
	 * String类型转化成Date类型，并格式化
	 *
	 * @param dateString
	 * @param formatString
	 * @return
	 */
	public static Date string2Date(String dateString, String formatString) {
		return DateTime.parse(dateString, DateTimeFormat.forPattern(formatString)).toDate();
	}

	/**
	 * Date类型转String类型，并格式化
	 *
	 * @param date
	 * @param formatStr
	 * @return
	 */
	public static String date2string(Date date, String formatStr) {
		return new DateTime(date).toString(DateTimeFormat.forPattern(formatStr));
	}

	/**
	 * 判断是否生日月份
	 *
	 * @param dateStr
	 * @return
	 */
	public static boolean isBrithDay(String dateStr) {
		try {
			Date date = new SimpleDateFormat("yyyyMMdd").parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int month = cal.get(Calendar.MONTH);
			Calendar todayCal = Calendar.getInstance();
			if (month == todayCal.get(Calendar.MONTH)) {
				return true;
			} else {
				return false;
			}

		} catch (ParseException e) {
			return false;
		}
	}

	public static int getWeekOfNow(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int w = cal.get(Calendar.DAY_OF_WEEK)-1;
		if (w < 0){
			w = 0;
		}
		return w;
	}

	/**
	 * 返回指定日期格式yyyyMMdd输出
	 *
	 * @param date
	 * @return
	 */
	public static String getyyyyMMdd(Date date) {
		return new DateTime(date).toString(YYYYMMDD);
	}

	/**
	 * 返回当前日期格式yyyyMMdd
	 *
	 * @return
	 */
	public static String getyyyyMMdd() {
		return DateTime.now().toString(YYYYMMDD);
	}

	/**
	 * 返回当前时间
	 *
	 * @return
	 */
	public static String getHHmmss() {
		return DateTime.now().toString(HHMMSS);
	}

	/**
	 * 返回传入日期格式yyyyMM
	 *
	 * @param date
	 * @return
	 */
	public static String getyyyyMM(Date date) {
		return new DateTime(date).toString(YYYYMM);
	}


	public static String getyyyyMMDD(Date date) {
		return new DateTime(date).toString(YYYY_MM_DD);
	}

	/**
	 * 返回日期格式yyyyMM
	 *
	 * @return
	 */
	public static String getyyyyMM() {
		return DateTime.now().toString(YYYYMM);
	}

	/**
	 * 返回传入时间的时分秒
	 *
	 * @param date
	 * @return
	 */
	public static String getHHmmss(Date date) {
		return new DateTime(date).toString(HHMMSS);
	}

	/**
	 * 返回传入时间的yyyyMMddHHmmss
	 *
	 * @param date
	 * @return
	 */
	public static String getyyyyMMddHHmmss(Date date) {
		return new DateTime(date).toString(YYYYMMDDHHMMSS);
	}

	/**
	 * 获取一周前的日期 即七天 格式yyyyMMdd
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastWeek(String date) {
		return DateTime.parse(date, DateTimeFormat.forPattern(YYYYMMDD)).plusDays(7).toDate();
	}

	public static Date getLastWeekYYYYMMDD(String date) {
		return DateTime.parse(date, DateTimeFormat.forPattern(YYYY_MM_DD)).plusDays(-7).toDate();
	}

	/**
	 * 获取一个月前的日期 格式yyyyMMdd
	 *
	 * @return
	 */
	public static Date getLastMonth(String date) {
		return DateTime.parse(date, DateTimeFormat.forPattern(YYYYMMDD)).minusMonths(1).toDate();
	}

	/**
	 * 返回加i小时后的日期
	 *
	 * @param date 日期
	 * @param i 小时数
	 * @return
	 */
	public static Date addHour(Date date, int i) {
		return new DateTime(date).plusHours(i).toDate();
	}

	/**
	 * 返回加减i天后的日期，传入整数为加，负数为减
	 *
	 * <pre>
	 * 这里使用字符串表示时间：
	 * DateHelper.addDay(null, 1)  =  null ;
	 * DateHelper.addDay("2010-01-01", 0)  =  "2010-01-01" ;
	 * DateHelper.addDay("2010-01-01", 1)  =  "2010-01-02" ;
	 * DateHelper.addDay("2010-01-01", -1)  =  "2009-12-31" ;
	 * </pre>
	 */
	public static Date addDay(Date date, int i) {
		if (date == null) {
			return null;
		}
		if (i == 0) {
			return date;
		}
		return new DateTime(date).plusDays(i).toDate();
	}

	/**
	 * 返回加减i分钟后的日期字符串[yyyyMMdd]，传入整数为加，负数为减
	 *
	 */
	public static Date addMin(Date date, int i) {
		return new DateTime(date).plusMinutes(i)
				.toDate();
	}

	/**
	 * 返回加减i天后的日期字符串[yyyyMMdd]，传入整数为加，负数为减
	 *
	 * <pre>
	 * 这里使用字符串表示时间：
	 * DateHelper.addDay(null, 1)  =  null ;
	 * DateHelper.addDay("20100101", 0)  =  "20100101" ;
	 * DateHelper.addDay("20100101", 1)  =  "20100102" ;
	 * DateHelper.addDay("20100101", -1)  =  "20091231" ;
	 * </pre>
	 */
	public static String addDay(String day, int i) {
		return DateTime.parse(day, DateTimeFormat.forPattern(YYYYMMDD)).plusDays(i)
				.toString(DateTimeFormat.forPattern(YYYYMMDD));
	}

	/**
	 * 使用指定的格式解释时间字符串，并对其进行天数的加减操作
	 *
	 * <pre>
	 * 这里使用字符串表示时间：
	 * DateHelper.addDay(null, 1)  =  null ;
	 * DateHelper.addDay("20100101", 0, 'yyyyMMdd')  =  "20100101" ;
	 * DateHelper.addDay("20100101", 1, 'yyyyMMdd')  =  "20100102" ;
	 * DateHelper.addDay("20100101", -1, 'yyyyMMdd')  =  "20091231" ;
	 * </pre>
	 */
	public static String addDay(String day, int i, String pattern) {
		return DateTime.parse(day, DateTimeFormat.forPattern(pattern)).plusDays(i)
				.toString(DateTimeFormat.forPattern(pattern));
	}

	/**
	 * 返回加减i月后的日期，传入整数为加，负数为减
	 *
	 * @param date
	 * @param i
	 * @return 加减i月后的日期
	 */
	public static Date addMonth(Date date, int i) {
		return new DateTime(date).plusMonths(i).toDate();
	}

	/**
	 * 判断日期字符串是否合法
	 *
	 * @param date
	 * @return 如果是yyyyMMdd格式日期则返回true,否则返回false;
	 */
	public static boolean isDate(String date) {
		try {
			DateTime.parse(date, DateTimeFormat.forPattern(YYYYMMDD));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断日期字符
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static boolean isDate(String date, String pattern) {
		try {
			DateTime.parse(date, DateTimeFormat.forPattern(pattern));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 返回当前年的最后一天
	 *
	 * @return 当前年的最后一天
	 */
	public static Date getLastDayOfYear() {
		return DateTime.now().dayOfYear().withMaximumValue().toDate();
	}

	/**
	 * 返回当前年的最后一天
	 *
	 * @param year
	 * @return 当前年的最后一天
	 */
	public static Date getLastDateOfYear(int year) {
		return DateTime.parse(String.valueOf(year), DateTimeFormat.forPattern(YYYY)).dayOfYear().withMaximumValue()
				.toDate();
	}

	/**
	 * 返回当前年的最后一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfYear(Date date) {
		return new DateTime(date).dayOfYear().withMaximumValue().toDate();
	}

	/**
	 * 返回指定日期年的第一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfYear(Date date) {
		return new DateTime(date).dayOfYear().withMinimumValue().toDate();
	}

	/**
	 * 返回加减yy年mm月dd日后的日期，传入整数为加，负数为减
	 *
	 * @param date 日期
	 * @param yy 年
	 * @param mm 月
	 * @param dd 日
	 * @return 加减yy年mm月dd日后的日期
	 */
	public static Date addYYMMDD(Date date, int yy, int mm, int dd) {
		return new DateTime(date).plusDays(dd).plusMonths(mm).plusYears(yy).toDate();
	}

	/**
	 * 使用指定年、月、日和当前时、分、秒解析为Date对象
	 *
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static Date parseDate(int year, int month, int date) {
		DateTime now = DateTime.now();
		return new DateTime(year, month, date, now.getHourOfDay(), now.getMinuteOfHour(), now.getSecondOfMinute())
				.toDate();
	}

	/**
	 * 使用指定的格式解释日期字符串为Timestamp对象
	 *
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Timestamp parseTimestamp(String dateStr, String pattern) {
		return new Timestamp(DateTime.parse(dateStr, DateTimeFormat.forPattern(pattern)).getMillis());
	}

	/**
	 * 返回当前月的第一天
	 *
	 * @return
	 */
	public static Date getFirstDayOfMonth() {
		return DateTime.now().dayOfMonth().withMinimumValue().toDate();
	}

	/**
	 * 返回指定日期月的第一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date) {
		return new DateTime(date).dayOfMonth().withMinimumValue().toDate();
	}

	/**
	 * 返回当前月的最后一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfMonth(Date date) {
		return new DateTime(date).dayOfMonth().withMaximumValue().toDate();
	}

	/**
	 * 返回指定日期月的最后一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		return new DateTime(date).dayOfMonth().withMaximumValue().toDate();
	}
	/**
	 * 返回当月天数
	 */
	public static int getMonthDays(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}


	/**
	 * 返回当前月的几号
	 *
	 * @return
	 */
	public static int getDayOfMonth() {
		return DateTime.now().getDayOfMonth();
	}

	/**
	 * 返回指定日期为月的几号
	 *
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date) {
		return new DateTime(date).getDayOfMonth();
	}

	/**
	 * 返回输入日期为当年的几月
	 *
	 * @param date
	 * @return
	 */
	public static int getMonthOfYear(Date date) {
		return DateTime.now().getMonthOfYear();
	}

	/**
	 * 取出当前时间的偏移时间
	 *
	 * @param skewTime
	 * @return
	 */
	public static String getyyyyMMddHHTime(int skewTime) {
		return DateTime.now().plusSeconds(skewTime).toString(YYYYMMDDHHMMSS);
	}

	/**
	 * 获取时间1 与时间2 的日期差
	 * @return
	 */
	public static int getDifference(Date time1, Date time2) {
		return (int)(time1.getTime() - time2.getTime())/(1000*3600*24);
	}


	/**
	 * 返回传入日期当天起始时间
	 * @param date 日期
	 * @return
	 */
	public static Date getDayStart(Date date) {
		return new DateTime(string2Date(getyyyyMMdd(date), YYYYMMDD)).toDate();
	}

	/**
	 * 返回传入日期当天结束时间
	 * @param date 日期
	 * @return
	 */
	public static Date getDayEnd(Date date) {
		return new DateTime(string2Date(getyyyyMMdd(date), YYYYMMDD)).plusDays(1).minusMillis(1).toDate();
	}

	public static Long getNow() {
		return System.currentTimeMillis()/1000;
	}

	public static Long getDateSecond(String dateStr, String pattern) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(dateStr).getTime()/1000;
	}
	public static void main(String[] arg) {
		System.out.println(getDifference(new Date(), getLastMonth("20180930")));
		System.out.println("getYestoday " + getYestoday());
		System.out.println("getCurrentDate" + getCurrentDate());
		System.out.println("getCurrentTime " + getCurrentTime());
		System.out.println("getCurrentTimess " + getCurrentTimess());
		System.out.println("string2Date " + string2Date("2011-12-01 11:21", "yyyy-MM-dd HH:mm"));
		System.out.println("date2string " + date2string(new Date(), "yyyy-MM-dd HH:mm:ss"));
		System.out.println("getyyyyMMdd " + getyyyyMMdd(new Date()));
		System.out.println("getyyyyMMdd " + getyyyyMMdd());
		System.out.println("getHHmmss " + getHHmmss());
		System.out.println("getyyyyMM " + getyyyyMM(new Date()));
		System.out.println("getHHmmss " + getHHmmss(new Date()));
		System.out.println("getLastWeek " + getLastWeek("20160727"));
		System.out.println("getLastMonth " + getLastMonth("20160727"));
		System.out.println("getyyyyMMddHHmmss " + getyyyyMMddHHmmss(new Date()));
		System.out.println("addHour " + addHour(new Date(), 10));
		System.out.println("addDay " + addDay(new Date(), 10));
		System.out.println("addDay " + addDay(new Date(), -1));
		System.out.println("addDay " + addDay(new Date(), 0));
		System.out.println("isDate " + isDate("20150229"));
		System.out.println("getLastDayOfYear " + getLastDayOfYear());
		System.out.println("getLastDateOfYear " + getLastDateOfYear(2011));
		System.out.println("getLastDateOfYear " + getLastDateOfYear(new Date()));
		System.out.println("addYYMMDD " + addYYMMDD(new Date(), 2, 1, 2));
		System.out.println("parseDate " + parseDate(2017, 1, 2));
		System.out.println("parseTimestamp " + parseTimestamp("20160727232410123", "yyyyMMddHHmmssSSS"));
		System.out.println("getFirstDayOfMonth " + getFirstDayOfMonth());
		System.out.println("getFirstDayOfMonth " + getFirstDayOfMonth(new Date()));
		System.out.println("getDayOfMonth " + getDayOfMonth());
		System.out.println("getDayOfMonth " + getDayOfMonth(new Date()));
		System.out.println("getMonthOfYear " + getMonthOfYear(new Date()));

		System.out.println("getyyyyMMddHHTime " + getyyyyMMddHHTime(200));

	}

	/**
	 * 获得24小时前的所有时间（整点）
	 *
	 * @param date
	 * @return
	 */
	public static List<Date> getOneDayBefore(Date date) {
		List<Date> result = new ArrayList<>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
		Date pastDate = calendar.getTime();
		while (pastDate.before(date)) {
			calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 1);
			pastDate = calendar.getTime();
			result.add(pastDate);
		}

		return  result;
	}
}
