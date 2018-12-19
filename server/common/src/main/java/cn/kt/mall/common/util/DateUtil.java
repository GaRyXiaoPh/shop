package cn.kt.mall.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类 Created by wqt on 2017/6/15.
 */
public class DateUtil {
	private static SimpleDateFormat msSdf = new SimpleDateFormat("yyyyMMddHHmmssS");
	private static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat mdf = new SimpleDateFormat("yyyy-MM");
	public final static long ND = 1000 * 24 * 60 * 60;// 一天的毫秒数
	public final static long NH = 1000 * 60 * 60;// 一小时的毫秒数
	public final static long NM = 1000 * 60;// 一分钟的毫秒数
	public final static long NS = 1000;// 一秒钟的毫秒数

	/**
	 * 计算两个时间相差天数
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static long diffDays(Date start, Date end) {
		long diff = diffTime(start, end);
		return BigDecimal.valueOf(diff).divide(BigDecimal.valueOf(ND), 0, RoundingMode.DOWN).longValue();
	}

	public static long diffHours(Date start, Date end) {
		long diff = diffTime(start, end);
		return BigDecimal.valueOf(diff).remainder(BigDecimal.valueOf(ND))
				.divide(BigDecimal.valueOf(NH), 0, RoundingMode.DOWN).longValue();
	}

	public static long diffMinutes(Date start, Date end) {
		long diff = diffTime(start, end);
		return BigDecimal.valueOf(diff).remainder(BigDecimal.valueOf(NH))
				.divide(BigDecimal.valueOf(NM), 0, RoundingMode.DOWN).longValue();
	}

	public static long diffTime(Date start, Date end) {
		long startMs, endMs;
		if (start == null) {
			return 0L;
		}
		startMs = start.getTime();
		if (end == null) {
			endMs = System.currentTimeMillis();
		} else {
			endMs = end.getTime();
		}

		if (startMs < endMs) {
			return BigDecimal.valueOf(endMs).subtract(BigDecimal.valueOf(startMs)).longValue();
		}

		return BigDecimal.valueOf(startMs).subtract(BigDecimal.valueOf(endMs)).longValue();
	}

	/**
	 * 是否是今天
	 * 
	 * @param time
	 *            时间
	 * @return true 是， false 否
	 */
	public static boolean isToday(Date time) {
		if (time == null) {
			return false;
		}
		int dayMs = 1000 * 60 * 60 * 24;
		int tDays = (int) (time.getTime() / dayMs);
		int nDays = (int) (System.currentTimeMillis() / dayMs);
		return tDays == nDays;
	}

	/**
	 * 到现在为止过了几天
	 * 
	 * @param start
	 *            起始时间
	 * @return int 天数
	 */
	public static long passeDays(Date start) {
		return diffDays(start, null);
	}

	/**
	 * 获取毫秒级时间戳
	 * 
	 * @return yyyyMMddHHmmssS
	 */
	public static String getDateMs() {
		return msSdf.format(new Date());
	}

	public static String getMonth(Date date) {
		return mdf.format(date);
	}

	/**
	 * 获取日期字符串
	 * 
	 * @param time
	 *            Date
	 * @return yyyy-MM-dd
	 */
	public static String getDateString(Date time) {
		return dateSdf.format(time);
	}

	public static String getTime(Date time) {
		return sdf.format(time);
	}

	public static Date getDateTime(String value) {
		if (value == null || value.length() != 19) {
			return null;
		}

		try {
			return sdf.parse(value);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date getDateSdfTime(String value) {
		if (value == null || value.length() != 10) {
			return null;
		}

		try {
			return dateSdf.parse(value);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date getDate(String date) {
		if (date == null || (date.length() != 10 && date.length() != 19)) {
			return null;
		}
		try {
			return dateSdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/*
	 * public boolean isToday(Date date) { if(date == null) { return false; }
	 * 
	 * Calendar time = Calendar.getInstance(); time.setTime(date);
	 * 
	 * Calendar now = Calendar.getInstance(); now.setTime(new Date());
	 * 
	 * return (now.get(Calendar.YEAR) == time.get(Calendar.YEAR) &&
	 * now.get(Calendar.MONTH) == time.get(Calendar.MONTH) &&
	 * now.get(Calendar.DAY_OF_MONTH) == time.get(Calendar.DAY_OF_MONTH)); }
	 */

	/**
	 *
	 * @param date
	 *            原来的时间
	 * @param time
	 *            格式是 1d, 2h, 3m, 分别代表1天，2小时，3分钟
	 * @return 增加后的时间
	 */
	public static Date plusTime(Date date, String time) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

		if (time == null) {
			time = "1d";
		}

		// 默认是天
		LocalDateTime newDateTime = dateTime.plusDays(Integer.valueOf(time.substring(0, time.length() - 1)));

		if (time.contains("h")) {
			newDateTime = dateTime.plusHours(Integer.valueOf(time.substring(0, time.length() - 1)));
		} else if (time.contains("m")) {
			newDateTime = dateTime.plusMinutes(Integer.valueOf(time.substring(0, time.length() - 1)));
		} else if (time.contains("M")) {
			newDateTime = dateTime.plusMonths(Integer.valueOf(time.substring(0, time.length() - 1)));
		}

		return Date.from(newDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 *
	 * @param date
	 *            原来的时间
	 * @param time
	 *            格式是 1d, 2h, 3m, 分别代表1天，2小时，3分钟
	 * @return 增加后的时间
	 */
	public static Date minusTime(Date date, String time) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

		if (time == null) {
			time = "1d";
		}

		// 默认是天
		LocalDateTime newDateTime = dateTime.minusDays(Integer.valueOf(time.substring(0, time.length() - 1)));

		if (time.contains("h")) {
			newDateTime = dateTime.minusHours(Integer.valueOf(time.substring(0, time.length() - 1)));
		} else if (time.contains("m")) {
			newDateTime = dateTime.minusMinutes(Integer.valueOf(time.substring(0, time.length() - 1)));
		} else if (time.contains("M")) {
			newDateTime = dateTime.minusMonths(Integer.valueOf(time.substring(0, time.length() - 1)));
		}

		return Date.from(newDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static long getDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static long getMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.MONTH);
	}

	public static void main(String[] args) {
		Date[] dates = getDateStartAndEnd(new Date());
		for (Date date : dates) {
			System.out.println(getTime(date));
		}
		
	}

	public static Date[] getDateStartAndEnd(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalDateTime newDateTime = dateTime.minusDays(1);
		LocalDateTime startYesterday = LocalDateTime.of(newDateTime.getYear(), newDateTime.getMonth(),
				newDateTime.getDayOfMonth(), 0, 0, 0);
		LocalDateTime endYesterday = LocalDateTime.of(newDateTime.getYear(), newDateTime.getMonth(),
				newDateTime.getDayOfMonth(), 23, 59, 59);

		return new Date[] { Date.from(startYesterday.atZone(ZoneId.systemDefault()).toInstant()),
				Date.from(endYesterday.atZone(ZoneId.systemDefault()).toInstant()) };
	}
}
