package com.junhetang.doctor.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mayakun on 2017/11/21.
 * 日期相关工具类
 */

public class DateUtil {

    /**
     * HH:mm    15:44
     * h:mm a    3:44 下午
     * HH:mm z    15:44 CST
     * HH:mm Z    15:44 +0800
     * HH:mm zzzz    15:44 中国标准时间
     * HH:mm:ss    15:44:40
     * yyyy-MM-dd    2016-08-12
     * yyyy-MM-dd HH:mm    2016-08-12 15:44
     * yyyy-MM-dd HH:mm:ss    2016-08-12 15:44:40
     * yyyy-MM-dd HH:mm:ss zzzz    2016-08-12 15:44:40 中国标准时间
     * EEEE yyyy-MM-dd HH:mm:ss zzzz    星期五 2016-08-12 15:44:40 中国标准时间
     * yyyy-MM-dd HH:mm:ss.SSSZ    2016-08-12 15:44:40.461+0800
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
     * yyyy.MM.dd G 'at' HH:mm:ss z    2016.08.12 公元 at 15:44:40 CST
     * K:mm a    3:44 下午
     * EEE, MMM d, ''yy    星期五, 八月 12, '16
     * hh 'o''clock' a, zzzz    03 o'clock 下午, 中国标准时间
     * yyyyy.MMMMM.dd GGG hh:mm aaa    02016.八月.12 公元 03:44 下午
     * EEE, d MMM yyyy HH:mm:ss Z    星期五, 12 八月 2016 15:44:40 +0800
     * yyMMddHHmmssZ    160812154440+0800
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
     * EEEE 'DATE('yyyy-MM-dd')' 'TIME('HH:mm:ss')' zzzz    星期五 DATE(2016-08-12) TIME(15:44:40) 中国标准时间
     * <p>
     * 注意：SimpleDateFormat不是线程安全的，线程安全需用{@code ThreadLocal<SimpleDateFormat>}
     */

    public static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String FORMAT_2 = "yyyy-MM-dd HH:mm";
    public static String FORMAT_3 = "yyyy-MM-dd";

    /**
     * 获取当前时间
     *
     * @param format "yyyy-MM-dd HH:mm:ss"
     * @return 当前时间
     */
    public static String getNowString(String format) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(date);
    }


    /**
     * 将时间字符串转为YMD
     *
     * @param time 时间字符串
     * @return str
     */
    public static String formatToYMD(String time) {
        return formatToString(time, FORMAT_3);
    }
    /**
     * 将时间字符串转为YMD hh:ss
     *
     * @param time 时间字符串
     * @return str
     */
    public static String formatToYMDHS(String time) {
        return formatToString(time, FORMAT_2);
    }

    /**
     * 将时间字符串转为  指定字符串格式
     *
     * @param time        时间字符串
     * @param formatStyle 时间格式
     * @return 毫秒时间戳
     */
    public static String formatToString(String time, String formatStyle) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT);
            Date date = format.parse(time);
            SimpleDateFormat dateFormat = new SimpleDateFormat(TextUtils.isEmpty(formatStyle) ? DEFAULT_FORMAT : formatStyle);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public static long getNowMills() {
        return System.currentTimeMillis();
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return "yyyy-MM-dd HH:mm"
     */
    public static String format_yyyy_MM_dd_HH_ss(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_2, Locale.getDefault());
        return simpleDateFormat.format(time);
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return "yyyy-MM-dd"
     */
    public static String format_yyyy_MM_dd(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_3, Locale.getDefault());
        return simpleDateFormat.format(time);
    }


    /**
     * 将时间字符串转为时间戳
     * <p>time格式为format</p>
     *
     * @param time 时间字符串
     * @return 毫秒时间戳
     */
    public static long string2Millis(String time) {
        return string2Millis(time, DEFAULT_FORMAT);
    }

    /**
     * 将时间字符串转为时间戳
     * <p>time格式为format</p>
     *
     * @param time        时间字符串
     * @param formatStyle 时间格式
     * @return 毫秒时间戳
     */
    public static long string2Millis(final String time, String formatStyle) {
        try {
            DateFormat format = new SimpleDateFormat(TextUtils.isEmpty(formatStyle) ? DEFAULT_FORMAT : formatStyle, Locale.getDefault());
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 将时间字符串转为Date类型
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return Date类型
     */
    public static Date string2Date(final String time) {
        return string2Date(time, DEFAULT_FORMAT);
    }

    /**
     * 将时间字符串转为Date类型
     * <p>time格式为format</p>
     *
     * @param time        时间字符串
     * @param formatStyle 时间格式
     * @return Date类型
     */
    public static Date string2Date(final String time, String formatStyle) {
        try {
            DateFormat format = new SimpleDateFormat(TextUtils.isEmpty(formatStyle) ? DEFAULT_FORMAT : formatStyle, Locale.getDefault());
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
