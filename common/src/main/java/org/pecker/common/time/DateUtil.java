package org.pecker.common.time;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.pecker.common.map.DefaultValueHashMap;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by chenwenjie on 16/12/24.
 */
@Slf4j
public class DateUtil {

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final String DEFAULT_DAY_PATTERN = "yyyy-MM";
    public static final String DEFAULT_YEAR_PATTERN = "yyyy";
    public static final String HD_DATE = "yyyyMM";
    public static final String GMT = "yyyy-MM-dd'T'HH:mm:ss.sssssssXXX";
    public static final String EASY_DATE_PATTERN_THREE = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String EASY_MILL_DATE_PATTERN_THREE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String EASY_MILL_DATE_PATTERN_THREE_V_ONE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String EASY_DATE_PATTERN_TWO = "yyMMdd";
    public static final String EASY_DATE_PATTERN = "yyyyMMdd";
    public static final String EASY_DATE_PATTERNS = "ddMMyyyy";
    public static final String DATE_CHINA_PATTERN = "yyyy年MM月dd日";
    public static final String CHINA_DATE_TIME = "yyyy年MM月dd日  HH时mm分ss秒";
    public static final String CHINA_MONTH_DAY_TIME = "MM月dd日";
    public static final String COMPLICATED_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String COMPLICATED_DATE_PATTERN_TWO = "yyyy/MM/dd HH:mm:ss";
    public static final String COMPLICATED_DATE_PATTERN_THREE = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String COMPLICATED_DATE_PATTERNS = "yyyy-MM-dd";
    public static final String STR_TIME = "yyyyMMddHHmmss";
    public static final String TIMEHOM = "yyyyMMddHHmmssSSS";
    public static final String TASK_TIME = "yyyyMMddHHmm";
    public static final String OUT_TIME = "yyyyMMdd HH:mm:ss";
    public static final String REFUND_TIME = "MM-dd HH:mm";
    public static final String ORDER_DATE = "yyyy/MM/dd";
    public static final String TMIN = "HH:mm:ss";
    public static final String QIEGE = "HH:mm";
    public static final String ESBTMIN = "HHmmss";
    public static final String HHMM = "HHmm";
    public static final String MM = "MM";
    public static final String YY = "yy";

    public static final Map<String,ThreadLocal<SimpleDateFormat>> SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP
            = new DefaultValueHashMap<>(new HashMap<>(), key -> ThreadLocal.withInitial(()->new SimpleDateFormat(key)));

    private static final Date MAX_DATE = new Date(7956806400000L);

    private DateUtil() {
    }

    public static Date addHour(Date currentDate, Integer hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);   //设置时间
        c.add(Calendar.HOUR, hours);
        Date date = c.getTime(); //结果
        return date;
    }

    public static Date addMinute(Date currentDate, Integer minutes) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);   //设置时间
        c.add(Calendar.MINUTE, minutes);
        Date date = c.getTime(); //结果
        return date;
    }

    public static Date addSecond(Date currentDate, Integer seconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);   //设置时间
        c.add(Calendar.SECOND, seconds);
        Date date = c.getTime(); //结果
        return date;
    }

    public static Date getEndOfDay(Date date) {
        return DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DATE), -1);
    }

    public static Date getStartOfDay(Date date) {
        return DateUtils.truncate(date, Calendar.DATE);
    }

    public static Date addTime(Date currentDate, Integer hours, Integer minutes, Integer seconds) {
        if (currentDate == null) {
            return null;
        }
        return addSecond(addMinute(addHour(currentDate, hours), minutes), seconds);
    }


    public static String getCurrentDate(Date date) {

        SimpleDateFormat simpleDateFormat = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get("yyyy/MM/HH:mm:ss").get();
        return simpleDateFormat.format(date);
    }

    public static String getMovieDate() {
        SimpleDateFormat simpleDate = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get("MM-DD HH:mm").get();
        return simpleDate.format(new Date());
    }

    public static String strTime(String formax) {
        DateFormat format = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(formax).get();
        return format.format(new Date());
    }

    /**
     * 返回日子 YYYYMM
     */
    public static String systemHdDate() {
        DateFormat format = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(HD_DATE).get();
        return format.format(new Date());
    }

    /**
     * 返回系统当前时间
     * "yyyy-MM-dd HH:mm:ss"
     *
     * @return
     */
    public static String systemDate() {
        DateFormat format = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(
                COMPLICATED_DATE_PATTERN).get();
        return format.format(new Date());
    }

    public static Date getnewDate() {
        return new Date();
    }

    public static int getWeekDay(long day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(day));
        int weekIndex = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekIndex < 0) {
            weekIndex = 0;
        }
        return weekIndex;
    }

    public static int getHour(Date day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    public static int getYear(Date day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        int hour = cal.get(Calendar.YEAR);
        return hour;
    }

    public static int getDayIndex(Date day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        int hour = cal.get(Calendar.DAY_OF_YEAR);
        return hour;
    }

    /**
     * 得到目前系统时间，格式为："yyyy-MM-dd hh:mm:ss"
     *
     * @return 系统时间的字符串表示形式，格式为："yyMMdd"
     */
    public static String getEasySysDateAL() {
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(COMPLICATED_DATE_PATTERN).get();
        return sdf.format(getnewDate());
    }

    /**
     * 得到目前系统时间，格式为："yyMMdd"
     *
     * @return 系统时间的字符串表示形式，格式为："yyMMdd"
     */
    public static String getEasySysDate() {
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(EASY_DATE_PATTERN_TWO).get();
        return sdf.format(getnewDate());
    }

    /**
     * 得到目前系统时间，格式为："ddMMyyyy"
     *
     * @return 系统时间的字符串表示形式，格式为："ddMMyyyy"
     */
    public static String getEasySysDateend() {
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(EASY_DATE_PATTERNS).get();
        return sdf.format(getnewDate());
    }

    /**
     * 得到目前系统时间，格式为："yyyyMMddHHmmss"
     *
     * @return 系统时间的字符串表示形式，格式为："yyyyMMddHHmmss"
     */
    public static String getNowSystemTime() {

        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get("").get();
        return sdf.format(getnewDate());
    }

    /**
     * 得到目前系统时间的3位毫秒值，格式为："SSS"
     *
     * @return 系统时间的毫秒值，格式为："SSS"
     */
    public static String getNowSystemMillisecond() {

        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get("SSS").get();
        return sdf.format(getnewDate());
    }

    /**
     * 得到目前系统时间，格式为："yyyyMMdd"
     *
     * @return 系统时间的字符串表示形式，格式为："yyyyMMdd"
     */
    public static String getSysDate() {
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(EASY_DATE_PATTERN).get();
        return sdf.format(getnewDate());
    }

    /**
     * 返回系统时间  HH:mm:ss
     *
     * @return
     */
    public static String getSysTime() {
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(TMIN).get();
        return sdf.format(getnewDate());
    }

    /**
     * 返回系统时间  HHmm
     *
     * @return
     */
    public static Integer getSysHmTime() {
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(HHMM).get();
        String res = sdf.format(getnewDate());
        return Integer.valueOf(res);
    }

    public static int betweenDays(Date start, Date end) {
        if (start == null || end == null) {
            throw new RuntimeException("start or end is null");
        }
        start = initialDay(start);
        end = initialDay(end);
        long betweenTime = Math.abs(start.getTime() - end.getTime());
        long remainder = betweenTime % 86400000;
        long day = betweenTime / 86400000;
        return (int) (remainder == 0 ? day : day + 1);
    }

    public static Date initialDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 日期往前推或往后推
     *
     * @param datastr String datastr是YYYY-MM-dd格式的日期字符串
     * @param a       int 当a>0向前推n天后的日期,当a<0,向后退n天后的日期
     * @return 返回YYYY-MM-dd 格式处理后的日期
     */
    public static String compareDateOnDay(String datastr, int a) throws ParseException {
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(COMPLICATED_DATE_PATTERNS).get();
        Date date = sdf.parse(datastr);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, a);
        return sdf.format(calendar.getTime());
    }

    /**
     * 日期往前推或往后推
     *
     * @param datastr String datastr是YYYY-MM-dd HH:mm:ss格式的日期字符串
     * @param datastr 指定格式
     * @param a       int 当a>0向前推n天后的日期,当a<0,向后退n天后的日期
     * @return 返回YYYY-MM-dd 格式处理后的日期
     */
    public static String compareDateOnDay(String datastr, String farmaxt, int a) throws ParseException {
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(farmaxt).get();
        Date date = sdf.parse(datastr);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, a);
        return sdf.format(calendar.getTime());
    }

    /**
     * 将Str时间转化成long行时间
     *
     * @param date
     * @param inputformat
     * @return
     * @throws ParseException
     */
    public static long strDateToLong(String date, String inputformat) {
        Date enddate = parse(inputformat, date);
        if (enddate == null) {
            return -1;
        }
        return strDateToLong(enddate);
    }

    /**
     * 将date时间转long型时间
     *
     * @param date
     * @return
     */
    public static long strDateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 比较两个日期的大小
     *
     * @param format 日期格式字符串常量;
     * @param date1  时间1;
     * @param date2  时间2;
     * @return 如果时间1大于时间2前返回“1”,若时间1小于时间2后返回“-1”,相等返回0;
     */
    public static int compareDateDifference(String format, String date1, String date2) throws ParseException {
        DateFormat df = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(format).get();
        Date dt1 = df.parse(date1);
        Date dt2 = df.parse(date2);
        if (dt1.getTime() > dt2.getTime()) {
            return 1;
        } else if (dt1.getTime() < dt2.getTime()) {
            return -1;
        }else {
            return 0;
        }
    }

    /**
     * 日期往前推或往后推
     *
     * @param datastr String datastr是YYYY-MM-dd HH:mm:ss格式的日期字符串
     * @param a       String 当a>0向前推n天后的日期,当a<0,向后退n天后的日期
     * @return 返回YYYY-MM-dd 格式处理后的日期
     */
    public static String compareDateOnDay(String datastr, String a) throws ParseException {
        return compareDateOnDay(datastr, Integer.parseInt(a));
    }

    /**
     * 得到 当前日期和dateNum天数的日期
     *
     * @param dateNum 天数，可以是正整书，或者负整数
     * @return
     */
    public static String getAddDateString(int dateNum) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, dateNum);
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(DEFAULT_DATE_PATTERN).get();
        return sdf.format(cal.getTime());
    }

    /**
     * 比较两个日期，返回它们之间相差的天数,不足一天返回0
     *
     * @param date1 java.util.Date
     * @param date2 java.util.Date
     * @return 相差的天数，如果 date1 小于 date2 返回 负数 <br>
     */
    public static int compareDateOnDay(Date date1, Date date2) {
        long ss = date1.getTime() - date2.getTime();
        long day = 24L * 60L * 60L * 1000L;
        return Integer.parseInt(ss / day + "");
    }

    /**
     * 取an天后的时间
     *
     * @param an     大于0向前算，小于0向后算
     * @param farmat 以何种格式输出
     * @return
     * @throws ParseException
     */
    public static String dateDiffer(int an, String farmat)
            throws ParseException {
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(farmat).get();
        Date date = sdf.parse((farmat));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, an);
        return sdf.format(calendar.getTime());
    }

    /**
     * 取得一个yyyy年MM月dd日 HH时mm分ss秒 格式的时间
     *
     * @return
     */
    public static String getChinaDateTime() {
        SimpleDateFormat simpleDateFormat = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(CHINA_DATE_TIME).get();
        return simpleDateFormat.format(new Date());
    }

    /**
     * 格式化日期 原日期默认为"yyyymm"
     *
     * @param newformat 匹配模式
     * @param datestr   日期
     * @return
     * @throws ParseException
     */
    public static String format(String newformat, String datestr)
            throws ParseException {
        SimpleDateFormat simpleDateFormat = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(EASY_DATE_PATTERN).get();
        Date date = simpleDateFormat.parse(datestr);
        simpleDateFormat = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(newformat).get();
        return simpleDateFormat.format(date);
    }

    /**
     * 将string 日期转换成Date时间
     *
     * @param formatstr
     * @param datestr
     * @return
     * @throws ParseException
     */
    public static Date formatDate(String formatstr, String datestr)
            throws ParseException {
        SimpleDateFormat simpleDateFormat = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(formatstr).get();
        return simpleDateFormat.parse(datestr);
    }

    /**
     * 日期转换
     *
     * @param newformat 新日期格式
     * @param oldfarmat 原日期格式
     * @param datestr   日期
     * @return
     * @throws ParseException
     */
    public static String format(String newformat, String oldfarmat, String datestr)
            throws ParseException {
        SimpleDateFormat simpleDateFormat = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(oldfarmat).get();
        Date date = simpleDateFormat.parse(datestr);
        simpleDateFormat = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(newformat).get();
        return simpleDateFormat.format(date);
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @return
     */
    public static String format(Date date) {
        return format(EASY_DATE_PATTERN, date);
    }

    /**
     * 格式化日期
     *
     * @param pattern 格式化模式
     * @param date    日期
     * @return
     */
    public static String format(String pattern, Date date) {
        return SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(pattern == null ? DEFAULT_DATE_PATTERN
                : pattern).get().format(date);
    }

    /**
     * 转换日期
     *
     * @param pattern 匹配模式
     * @param datestr 日期
     * @return
     * @throws ParseException
     */
    public static Date parse(String pattern, String datestr) {
        SimpleDateFormat simpleDateFormat = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(pattern).get();
        try {
            return simpleDateFormat.parse(datestr);
        } catch (ParseException e) {
            log.error("错误：{}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换日期
     *
     * @param datestr 匹配模式
     * @param patterns 日期
     * @return
     * @throws ParseException
     */
    public static Date parseArrayStyle(String datestr,String... patterns) {
        for (String pattern : patterns) {
            SimpleDateFormat simpleDateFormat = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(pattern).get();
            try {
                return simpleDateFormat.parse(datestr);
            } catch (ParseException e) {
            }
        }
        throw new RuntimeException("don't find the patterns");
    }

    /**
     * 得到目前系统时间，格式为："yyyy-MM-dd"
     *
     * @return 系统时间的字符串表示形式，格式为："yyyy-MM-dd"
     */
    public static String getSysDateDefault() {
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(DEFAULT_DATE_PATTERN).get();
        return sdf.format(getnewDate());
    }

    /**
     * 得到目前系统详细时间，格式为："yyyy年MM月dd日"
     *
     * @return 系统时间的字符串表示形式，格式为："yyyy年MM月dd日"
     */
    public static String getSysDateChina() {
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(DATE_CHINA_PATTERN).get();
        return sdf.format(getnewDate());
    }

    /**
     * 月加减
     *
     * @param data 日期满足yyyyMM
     * @param ln   相差后的日期 n<倒退n月后的日期；n大于1为提前几月后的日期
     * @return
     * @throws ParseException
     */
    public static String compareMonthdifference(String data, int ln) {
        String year = null;
        String month = null;
        try {
            SimpleDateFormat df = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(HD_DATE).get();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(df.parse(data.substring(0, 6)));
            calendar.add(Calendar.MONTH, ln);
            year = (calendar.get(Calendar.YEAR)) + "";
            month = (calendar.get(Calendar.MONTH) + 1) + "";
            month = month.length() == 1 ? "0" + month : month;
        } catch (Exception e) {
            return data;
        }
        return year + month;
    }

    /**
     * 月加减
     *
     * @param data 日期满足yyyyMM
     * @param ln   相差后的日期 n<倒退n月后的日期；n大于1为提前几月后的日期
     * @return
     * @throws ParseException
     */
    public static String compareMonthdifference2(String data, int ln) {
        String year = null;
        String month = null;
        try {
            SimpleDateFormat df = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(HD_DATE).get();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(df.parse(data.substring(0, 6)));
            calendar.add(Calendar.MONTH, ln);
            year = (calendar.get(Calendar.YEAR)) + "";
            month = (calendar.get(Calendar.MONTH) + 1) + "";
            month = month.length() == 1 ? "0" + month : month;
        } catch (Exception e) {
            return data;
        }
        return year + "-" + month;
    }

    /**
     * 比较两个日期的大小
     *
     * @param date1 yyyyMM
     * @param date2 yyyyMM
     * @return date1>date2 返回 1； date1==date2返回 0 ；date1<date2 返回-1
     * @throws ParseException
     */
    public static int maxormin(String date1, String date2)
            throws ParseException {
        return maxormin(date1, date2, HD_DATE);
    }

    /**
     * 判断两个时间的大小
     *
     * @param date1
     * @param date2
     * @param formax 格式定义
     * @return date1>date2 返回 1； date1==date2返回 0 ；date1<date2 返回-1
     * @throws ParseException
     */
    public static int maxormin(String date1, String date2, String formax) throws ParseException {
        SimpleDateFormat dff = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(formax).get();
        Date dt1 = dff.parse(date1);
        Date dt2 = dff.parse(date2);
        if (dt1.getTime() == dt2.getTime()) {
            return 0;
        } else if (dt1.getTime() < dt2.getTime()) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * 日期字符串格式转换
     *
     * @param date    日期字符串
     * @param formax1 格式1输入字符串格式
     * @param formax2 格式2输出字符串格式
     * @return
     * @throws Exception
     */
    public static String formaxToformax(String date, String formax1, String formax2) throws ParseException {
        SimpleDateFormat dff1 = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(formax1).get();
        Date dt1 = dff1.parse(date);
        SimpleDateFormat dff2 = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(formax2).get();
        return dff2.format(dt1);
    }

    /**
     * 计算两个日期想差月份
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getMonthSpace(Date date1, Date date2) {
        int resut = 0;
        try {
            SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(DEFAULT_DATE_PATTERN).get();
            Calendar min = Calendar.getInstance();
            Calendar max = Calendar.getInstance();
            min.setTime(sdf.parse(sdf.format(date1)));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

            max.setTime(sdf.parse(sdf.format(date2)));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

            Calendar curr = min;
            while (curr.before(max)) {
                resut = resut + 1;
                curr.add(Calendar.MONTH, 1);
            }
            while (max.before(curr)) {
                resut = resut - 1;
                max.add(Calendar.MONTH, 1);
            }
        } catch (Exception e) {
            log.error("错误：{}", e);
        }
        return resut;
    }

    /**
     * 将日期date类型的获取指定格式的data类型
     *
     * @param date
     * @param format
     * @return
     * @throws Exception
     */
    public static Date formaTOformatDate(Date date, String format) throws ParseException {
        if (date == null) {
            return date;
        }
        SimpleDateFormat simpleDateFormat = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(format).get();
        return simpleDateFormat.parse(simpleDateFormat.format(date));
    }

    /**
     * 该方法主要作用是计算两个日期的相差天数
     *
     * @param smdate 日期1
     * @param bdate  日期2
     * @return
     * @throws Exception
     */
    public static int timedifference(Date smdate, Date bdate) throws ParseException {

        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(DEFAULT_DATE_PATTERN).get();
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long betweenDays = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(betweenDays));
    }

    /**
     * 该方法主要作用是计算两个日期的相差天数
     *
     * @param smdate  第一个字符串日期
     * @param format1 第一个字符串日期格式
     * @param bdate   第二个日期字符串
     * @param format2 第二个日期字符串格式
     * @return
     * @throws Exception
     */
    public static int timedifference(String smdate, String format1, String bdate, String format2) throws ParseException {
        SimpleDateFormat dff1 = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(format1).get();
        Date dt1 = dff1.parse(smdate);
        SimpleDateFormat dff2 = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(format2).get();
        Date dt2 = dff2.parse(bdate);
        return timedifference(dt1, dt2);
    }

    public static int getMonthDay(long day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(day));
        int monthIndex = cal.get(5);
        if (monthIndex < 0) {
            monthIndex = 0;
        }

        return monthIndex;
    }

    /**
     * 日期加减发后的日期
     *
     * @param date 开始日期
     * @param a    加减进度
     * @return 返回加减后的日期
     */
    public static Date addDate(Date date, int a) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.DAY_OF_YEAR, a);
        return rightNow.getTime();
    }

    /**
     * 日期加减发后的日期
     *
     * @param datestr 开始日期
     * @param a       加减进度
     * @return 返回加减后的日期
     * @throws ParseException
     */
    public static Date addDate(String datestr, String format, int a) throws ParseException {
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(format).get();
        Date date = sdf.parse(datestr);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(Calendar.DAY_OF_YEAR, a);
        return rightNow.getTime();
    }

    /**
     * 返回系统日期
     *
     * @param date
     * @return
     */
    public static String getDate(Object date) {
        if (date != null) {
            Date tDate = (Date) date;
            return format(COMPLICATED_DATE_PATTERN, tDate);
        }
        return "";
    }

    /**
     * 按天获取时间段列表
     *
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<String> findDatesDay(Date dBegin, Date dEnd) {
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(DEFAULT_DATE_PATTERN).get();
        List<String> lDate = new ArrayList<>();
        lDate.add(sdf.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sdf.format(calBegin.getTime()));
        }
        return lDate;
    }

    /**
     * 获取两个时间的月列表
     *
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<String> findDatesMoth(Date dBegin, Date dEnd) {
        List<String> result = new ArrayList<>();
        try {
            SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(DEFAULT_DAY_PATTERN).get();
            Calendar min = Calendar.getInstance();
            Calendar max = Calendar.getInstance();
            min.setTime(sdf.parse(sdf.format(dBegin)));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
            max.setTime(sdf.parse(sdf.format(dEnd)));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
            Calendar curr = min;
            while (curr.before(max)) {
                result.add(sdf.format(curr.getTime()));
                curr.add(Calendar.MONTH, 1);
            }
        } catch (Exception e) {
            log.error("error:{}", e);
        }
        return result;
    }

    /**
     * 获取两个时间的月列表
     *
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<String> findDatesMoth(Date dBegin, Date dEnd, String format) {
        List<String> result = new ArrayList<>();
        try {
            SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(format).get();
            Calendar min = Calendar.getInstance();
            Calendar max = Calendar.getInstance();
            min.setTime(sdf.parse(sdf.format(dBegin)));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
            max.setTime(sdf.parse(sdf.format(dEnd)));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
            Calendar curr = min;
            while (curr.before(max)) {
                result.add(sdf.format(curr.getTime()));
                curr.add(Calendar.MONTH, 1);
            }
        } catch (Exception e) {
            log.error("error:{}", e);
        }
        return result;
    }

    /**
     * 获取两个时间的年列表
     *
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<String> findDatesYear(Date dBegin, Date dEnd) {
        List<String> result = new ArrayList<>();
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get("yyyy").get();
        int iBegin = Integer.parseInt(sdf.format(dBegin));
        int iEnd = Integer.parseInt(sdf.format(dEnd));
        int cs = iEnd - iBegin;
        if (cs >= 0) {
            result.add(iBegin + "");
            for (int i = 0; i < cs; i++) {
                result.add((iBegin + (i + 1)) + "");
            }
        }
        return result;
    }


    public static Date parseDateByIndex(int index) {
        Date nowDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        int nyear = calendar.get(Calendar.YEAR);
        int nowIndex = calendar.get(Calendar.DAY_OF_YEAR);
        if (nowIndex < index - 182) {
            calendar.add(Calendar.DATE, -1);
            int byear = calendar.get(Calendar.YEAR);
            if (byear == nyear) {
                calendar.setTime(nowDate);
            }
        } else {
            calendar.setTime(nowDate);
            if (nowIndex > index) {
                calendar.add(Calendar.YEAR, 1);
            }
        }
        calendar.set(Calendar.DAY_OF_YEAR, 0);
        calendar.add(Calendar.DAY_OF_YEAR, index);
        return calendar.getTime();
    }

    public static String formatDateByIndex(String pattern, int index) {
        Date date = parseDateByIndex(index);
        return format(pattern, date);
    }

    public static int formatDateToIndex(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static Date today() {
        return initialDay(new Date());
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
        return localDate;
    }

    public static Date localDateTimeToDate(LocalDateTime localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }

    public static LocalDateTime stringToLocalDateTime(String str1) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(COMPLICATED_DATE_PATTERN);
        LocalDateTime parse = LocalDateTime.parse(str1, dtf);
        return parse;
    }

    public static String localDateTimeToString(LocalDateTime ldt) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(COMPLICATED_DATE_PATTERN);
        String format = ldt.format(dtf);
        return format;
    }

    public static int compareToByTimeSpan(Date var1, Date var2) {
        int total1 = var1.getHours() * 3600 + var1.getMinutes() * 60 + var1.getSeconds();
        int total2 = var2.getHours() * 3600 + var2.getMinutes() * 60 + var2.getSeconds();
        return total1 - total2;
    }

    public static Date transformDate(String date) {
        SimpleDateFormat sdf = SIMPLE_DATE_FORMAT_THREAD_LOCAL_MAP.get(DateUtil.DEFAULT_DATE_PATTERN).get();
        Date targetDate = null;
        try {
            targetDate = sdf.parse(date);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return targetDate;
    }

    public static Date getDawnDate(String dawnTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(dawnTime));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date initHourMinute(Date date,String hourMinuteString){
        return initHourMinute(date,hourMinuteString,12,0);
    }

    public static Date initHourMinute(Date date,String hourMinuteString,int defaultHour ,int defaultMinute){
        if (StringUtils.isNotBlank(hourMinuteString)) {
            String[] hourMinuteArray = hourMinuteString.split(":");
            if (hourMinuteArray.length>1){
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                if (hourMinuteArray.length>=1) {
                    cal.set(Calendar.HOUR_OF_DAY, NumberUtils.toInt(hourMinuteArray[0], defaultHour));
                }else {
                    cal.set(Calendar.HOUR_OF_DAY, defaultHour);
                }
                if (hourMinuteArray.length>=2) {
                    cal.set(Calendar.MINUTE, NumberUtils.toInt(hourMinuteArray[1], defaultMinute));
                }else {
                    cal.set(Calendar.MINUTE, defaultMinute);
                }
                if (hourMinuteArray.length>=3){
                    cal.set(Calendar.SECOND, NumberUtils.toInt(hourMinuteArray[2], 0));
                }

                cal.set(Calendar.MILLISECOND, 0);
                return cal.getTime();
            }
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, defaultHour);
        cal.set(Calendar.MINUTE, defaultMinute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date initHourMinute(Date date,int hour ,int minute){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取webservice XML文件中的日期时间
     *
     * @param date
     * @return
     */
    public static XMLGregorianCalendar getXmlDate(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar gc = null;
        try {
            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return gc;
    }

    public static Set<Date> rangeToSet(Date startDate, Date endDate) {
        Set<Date> result = new HashSet<>();
        if (startDate == null || endDate == null) {
            return result;
        }

        for (Date index = (Date) startDate.clone(); index.compareTo(endDate) <= 0; index = DateUtils.addDays(index, 1)) {
            result.add(index);
        }
        return result;
    }

    public static List<List<Date>> mergeDateRangeList(List<List<Date>> dateRangeList){

        dateRangeList = dateRangeList.stream().sorted((o1, o2) -> o1.get(0).compareTo(o2.get(0)))
                .collect(Collectors.toList());
        Iterator<List<Date>> dateRangeIterator = dateRangeList.iterator();
        Date nowStartDate = null;
        Date nowEndDate = null;
        Date nextEndDate = null;
        List<List<Date>> result = new LinkedList<>();
        while (dateRangeIterator.hasNext()){
            List<Date> dateRange = dateRangeIterator.next();
            if (nowEndDate == null){
                nowStartDate = dateRange.get(0);
                nowEndDate = dateRange.get(1)==null?MAX_DATE:dateRange.get(1);
                nextEndDate = DateUtils.addDays(nowEndDate,1);
            }else {
                if (nextEndDate.compareTo(dateRange.get(0))<0){
                    List<Date> resultItem = Arrays.asList(nowStartDate,nowEndDate);
                    result.add(resultItem);
                    nowStartDate = dateRange.get(0);
                    nowEndDate = dateRange.get(1)==null?MAX_DATE:dateRange.get(1);
                    nextEndDate = DateUtils.addDays(nowEndDate,1);
                }else if (nowEndDate.compareTo(dateRange.get(1)==null?MAX_DATE:dateRange.get(1))<0){
                    nowEndDate = dateRange.get(1)==null?MAX_DATE:dateRange.get(1);
                    nextEndDate = DateUtils.addDays(nowEndDate,1);
                }else {
                }
            }
        }
        List<Date> resultItem = Arrays.asList(nowStartDate,nowEndDate);
        result.add(resultItem);

        return result.stream().map(item->{
            if (item.get(1).compareTo(MAX_DATE)>=0){
                return Arrays.asList(item.get(0),null);
            }else {
                return item;
            }
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
       Date date = parse(DateUtil.EASY_MILL_DATE_PATTERN_THREE,"2020-08-27T09:39:34.154+0900");
       String dateString = format(DateUtil.COMPLICATED_DATE_PATTERN,date);
       System.out.println(dateString);
    }
}
