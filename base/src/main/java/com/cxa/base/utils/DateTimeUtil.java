package com.cxa.base.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * 日期时间格式转换工具
 *
 * @author Eric
 */
public class DateTimeUtil {

    public static String defaultFormatter = "yyyy-MM-dd HH:mm:ss";
    public static String defaultDateFormatter = "yyyy-MM-dd";
    public static String defaultTimeFormatter = "HH:mm:ss";
    public static String chinaFormatter = "yyyy-MM-dd HH:mm";
    public static Locale locale = Locale.US;

    /**
     * @return
     */
    public static String getDefaultFormatter() {
        return defaultFormatter;
    }

    /**
     * @return
     */
    public static String getDefaultDateFormatter() {
        return defaultDateFormatter;
    }

    /**
     * @return
     */
    public static String getDefaultTimeFormatter() {
        return defaultTimeFormatter;
    }

    /**
     * @param defaultFormatter
     */
    public static void setDefaultFormatter(String defaultFormatter) {
        DateTimeUtil.defaultFormatter = defaultFormatter;
    }

    /**
     * @param defaultDateFormatter
     */
    public static void setDefaultDateFormatter(String defaultDateFormatter) {
        DateTimeUtil.defaultDateFormatter = defaultDateFormatter;
    }

    /**
     * @param defaultTimeFormatter
     */
    public static void setDefaultTimeFormatter(String defaultTimeFormatter) {
        DateTimeUtil.defaultTimeFormatter = defaultTimeFormatter;
    }

    /**
     * 将字符串解析为Date
     *
     * @param strTime 待解析的字符串
     * @return 解析出来的Date
     * @throws ParseException
     */
    public static Date getDate(String strTime)
            throws ParseException {
        if (strTime == null || strTime.trim().equals("")) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(chinaFormatter, locale);
        return sdf.parse(strTime);
    }

    public static Date getDate(String strTime, String patten)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(patten, locale);
        return sdf.parse(strTime);
    }
    

    /**
     * 将字符串解析为Date
     *
     * @param strTime 待解析的字符串
     * @param patten  格式
     * @param locale  区域
     * @return 解析出来的Date
     * @throws ParseException
     */
    public static Date getDate(String strTime, String patten, Locale locale)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(patten, locale);
        return sdf.parse(strTime);
    }

    /**
     * 将Date转换为Timestamp
     *
     * @param date 待转换的Date
     * @return 转换出来的Timestamp
     */
    public static Timestamp getTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 将Timestamp转换为Date
     *
     * @param timestamp 待转换的Timestamp
     * @return 转换出来的Date
     */
    public static Date getDate(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }

    /**
     * 将Timestamp转换为String
     *
     * @param timestamp 源时间
     * @return 时间字符串
     */
    public static String getTimestampString(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        return DateTimeUtil.formatDateByFormat(DateTimeUtil.getDate(timestamp),
                defaultFormatter);
    }

    /**
     * 将Date转换为String "yyyy-mm-dd hh:mm:ss"
     *
     * @param date 源时间
     * @return 时间字符串
     */
    public static String getDateString(Date date) {
        if (date == null) {
            return "";
        }
        return DateTimeUtil.formatDateByFormat(date, defaultFormatter);
    }

    /**
     * 格式化date "yyyy-mm-dd hh:mm:ss"
     *
     * @param date
     * @return
     */
    public static String getFormatDateString(Date date) {
        if (date == null) {
            return null;
        }
        return DateTimeUtil.formatDateByFormat(date, defaultFormatter);
    }

    /**
     * 取当前时间的Timestamp
     *
     * @return 系统当前时间的Timestamp
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 根据格式返回当前系统时间.
     *
     * @param pattern 日期格式串
     * @return 指定格式的字符集表示的系统当前时间
     */
    public static String getCurrentTime(String pattern) {
        return DateTimeUtil.formatDateByFormat(new Date(), pattern);
    }

    /**
     * 以指定的格式来格式化日期.
     *
     * @param date    Date
     * @param pattern 日期格式串
     * @return 指定格式的字符集表示的时间
     */
    public static String formatDateByFormat(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(pattern, locale).format(date);
    }

    /**
     * 取系统当前时间
     *
     * @return 指定格式的字符集表示的系统当前时间
     */
    public static String getCurrentFormatTime() {
        return DateTimeUtil.getCurrentTime(DateTimeUtil.defaultTimeFormatter);
    }

    /**
     * 取系统当前日期
     *
     * @return 指定格式的字符集表示的系统当前日期
     */
    public static String getCurrentFormatDate() {
        return DateTimeUtil.getCurrentTime(DateTimeUtil.defaultDateFormatter);
    }

    /**
     * 取系统当前日期
     *
     * @return 指定格式的字符集表示的系统当前日期和时间
     */
    public static String getCurrentFormatDateTime() {
        return DateTimeUtil.getCurrentTime(DateTimeUtil.defaultFormatter);
    }

    /**
     * 取系统当前日期
     *
     * @return 指定格式的字符集表示的系统当前日期和时间
     */
    public static String getCurrentFormatDateHour() {
        return DateTimeUtil.getCurrentTime(DateTimeUtil.chinaFormatter);
    }


    /**
     * 给某一时间增加或减去指定天数。 当n为正数时为增加天数，当n为负数时为减去天数。
     *
     * @param date 给定的时间
     * @param n    待增加或减去的天数
     * @return 已经增加或减去天数后的时间
     */
    public static Date getDateAddDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, n);
        return cal.getTime();
    }

    /**
     * 给某一时间增加或减去指定月数。 当n为正数时为增加月数，当n为负数时为减去月数。
     *
     * @param date 给定的时间
     * @param n    待增加或减去的月数
     * @return 已经增加或减去月数后的时间
     */
    public static Date getDateAddMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /**
     * 给某一时间增加或减去指定年数。 当n为正数时为增加年数，当n为负数时为减去年数。
     *
     * @param date 给定的时间
     * @param n    待增加或减去的年数
     * @return 已经增加或减去年数后的时间
     */
    public static Date getDateAddYear(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, n);
        return cal.getTime();
    }

    /**
     * 返回一个默认的起始日期.此方法永远返回"1900-01-01 00:00:00"
     *
     * @return "1900-01-01 00:00:00"
     */
    public static String getDefaultDate() {
        return "1900-01-01 00:00:00";
    }

    /**
     * 格式化Date串。
     *
     * @param date   日期
     * @param pattern 格式
     * @param locale 区域
     * @return
     */
    public static String formatDateByFormat(Date date, String pattern,
                                            Locale locale) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(pattern, locale).format(date);
    }

    /**
     * 用String.format的方式格式化日期 "yyyy-MM-dd HH:mm"
     *
     * @param date 日期
     * @return
     */
    public static String StringFomratDate(Date date) {
        if (date == null)
            return "";
        return String.format("%1$tF %1$tR", date);
    }


    /**
     * 给指定的日期时间 加减 指定的 分钟
     *
     * @param date
     * @param minute 分钟数，正数加，负数减
     * @return 加减 指定的 分钟后的日期时间
     */
    public static Date getDateAddMinute(Date date, long minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long millis = cal.getTimeInMillis();
        long newMillis = minute * 60 * 1000;
        cal.setTimeInMillis(millis + newMillis);
        return cal.getTime();
    }

    /**
     * 给指定的日期时间 加减 指定的 小时
     *
     * @param date
     * @param hour 小时数，正数加，负数减
     * @return 加减 指定的 分钟后的日期时间
     */
    public static Date getDateAddHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long millis = cal.getTimeInMillis();
        long newMillis = hour *60 * 60 * 1000;
        cal.setTimeInMillis(millis + newMillis);
        Timestamp time = getTimestamp(cal.getTime());
        return time;
    }

}

