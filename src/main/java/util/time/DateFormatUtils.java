package util.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 时间处理工具类<br>
 *
 * @author haiweizhu
 */
public class DateFormatUtils {

    /**
     * 默认时间格式
     */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 归属年份
     */
    public static final String BELONG_YEAR="yyyy";

    /**
     * 归属日
     */
    public static final String BELONG_DAY="d";


    /**
     * 订单时间格式
     */
    public static final String CASE_DATE_PATTERN = "yyyyMMdd";

    /**
     * 默认时间格式 年-月
     */
    public static final String DEFAULT_DATE_YEAR_MONTH = "yyyy-MM";

    public  static  final  String DEFAULT_MONTH = "MM 月";

    public  static  final  String SEPARATE_MONTH = "M";
    /**
     * 当天时间，二十四小时制
     */
    public static final String DEFAULT_TODAY_PATTERN = "HH:mm";

    /**
     * 昨天格式
     */
    public static final String DEFUALT_YESTERDAY_PATTERN = "昨天 HH:mm";

    /**
     * 月日格式
     */
    public static final String DEFAULT_MONTH_AND_DAY = "M月d日 HH:mm";
    /**
     * 年月日格式
     */
    public static final String DEFAULT_YEAR_MONTH_DAY = "yyyy-MM-dd HH:mm";

    /**
     * MONDAY
     */
    public static final String MONDAY = "星期一";
    /**
     * TUESDAY
     */
    public static final String TUESDAY = "星期二";
    /**
     * WEDNESDAY
     */
    public static final String WEDNESDAY = "星期三";
    /**
     * THURSDAY
     */
    public static final String THURSDAY = "星期四";
    /**
     * FRIDAY
     */
    public static final String FRIDAY = "星期五";
    /**
     * SATURDAY
     */
    public static final String SATURDAY = "星期六";
    /**
     * SUNDAY
     */
    public static final String SUNDAY = "星期日";

    /**
     * 12月份
     */
    public static final String JAN="1";
    public static final String FEB="2";
    public static final String MAR="3";
    public static final String APR="4";
    public static final String MAY="5";
    public static final String JUN="6";
    public static final String JUL="7";
    public static final String AUG="8";
    public static final String SEP="9";
    public static final String OCT="10";
    public static final String NOV="11";
    public static final String DECE="12";

    /**
     * 日期不能为空提示信息
     */
    private static final String DATE_NULL_MSG = "The date must not be null";

    public static final String DEFAULT_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_TIME_PATTERN2 = "yyyy/MM/dd HH:mm:ss";
    public static final String DEFAULT_TIME_PATTERN3 = "yyyy/MM/dd";

    /**
     * 默认格式化类
     */
    public static final SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_PATTERN);

    /**
     * 私有化构造函数
     */
    private DateFormatUtils() {
    }

    public static String format(Date date) {
        return sdf.format(date);
    }

    public static Date parse(String date) {
        if (null == date) {
            return null;
        }
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 处理时间戳为格式化字符串<br>
     *
     * @param timestamp
     * @param pattern
     * @return
     */
    public static String format(long timestamp, String pattern) {
        Date date = new Date(timestamp);
        return format(date, pattern);
    }

    /**
     * 处理时间为格式化字符串<br>
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * 解析时间字符串<br>
     *
     * @param source
     * @param pattern
     * @return
     */
    public static Date parse(String source, String pattern) {
        if (null == source) {
            return null;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.parse(source);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 是否同一天<br>
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameday(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException(DATE_NULL_MSG);
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameday(cal1, cal2);
    }

    /**
     * 是否同一天<br>
     *
     * @param cal1
     * @param cal2
     * @return
     */
    public static boolean isSameday(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException(DATE_NULL_MSG);
        }
        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 是否昨天<br>
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isYesterday(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException(DATE_NULL_MSG);
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isYesterday(cal1, cal2);
    }

    /**
     * 是否昨天<br>
     *
     * @param cal1
     * @param cal2
     * @return
     */
    public static boolean isYesterday(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException(DATE_NULL_MSG);
        }

        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && (cal1.get(Calendar.DAY_OF_YEAR) - 1 == cal2.get(Calendar.DAY_OF_YEAR) || cal1
                .get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) - 1);
    }

    /**
     * 是否本周<br>
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameweek(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException(DATE_NULL_MSG);
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameweek(cal1, cal2);
    }

    /**
     * 是否本周<br>
     *
     * @param cal1
     * @param cal2
     * @return
     */
    public static boolean isSameweek(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException(DATE_NULL_MSG);
        }

        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 是否本年<br>
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameyear(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException(DATE_NULL_MSG);
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameyear(cal1, cal2);
    }

    /**
     * 是否本年<br>
     *
     * @param cal1
     * @param cal2
     * @return
     */
    public static boolean isSameyear(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException(DATE_NULL_MSG);
        }

        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    /**
     * 取一天内的小时数，二十四小时制<br>
     *
     * @param date
     * @return
     */
    public static String toToday(Date date) {
        return format(date, DEFAULT_TODAY_PATTERN);
    }

    /**
     * 取一天内的小时数，二十四小时制<br>
     *
     * @param date
     * @return
     */
    public static String toTodayTime(Date date) {
        return format(date, DEFAULT_TIME_PATTERN);
    }

    /**
     * 昨天时间格式<br>
     *
     * @param date
     * @return
     */
    public static String toYesterday(Date date) {
        return format(date, DEFUALT_YESTERDAY_PATTERN);
    }

    /**
     * 取星期几<br>
     *
     * @param calendar
     * @return
     */
    public static String toDayOfWeek(Calendar calendar) {
        String when = null;
        if (Calendar.MONDAY == calendar.get(Calendar.DAY_OF_WEEK)) {

            when = MONDAY;
        } else if (Calendar.TUESDAY == calendar.get(Calendar.DAY_OF_WEEK)) {

            when = TUESDAY;
        } else if (Calendar.WEDNESDAY == calendar.get(Calendar.DAY_OF_WEEK)) {

            when = WEDNESDAY;
        } else if (Calendar.THURSDAY == calendar.get(Calendar.DAY_OF_WEEK)) {

            when = THURSDAY;
        } else if (Calendar.FRIDAY == calendar.get(Calendar.DAY_OF_WEEK)) {

            when = FRIDAY;
        } else if (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)) {

            when = SATURDAY;
        } else if (Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK)) {

            when = SUNDAY;
        }
        return when;
    }

    /**
     * 返回月日<br>
     *
     * @param date
     * @return
     */
    public static String toMonthAndDay(Date date) {
        return format(date, DEFAULT_MONTH_AND_DAY);
    }

    /**
     * 返回年月日<br>
     *
     * @param date
     * @return
     */
    public static String toYearMonthDay(Date date) {
        return format(date, DEFAULT_YEAR_MONTH_DAY);
    }


    public static String getLongStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_TIME_PATTERN);
        return sdf.format(date);
    }

    public static String getLongStr(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_TIME_PATTERN);
        return sdf.format(time);
    }

    /**
     * 获取timeAdd天后的日期
     *
     * @param date
     * @param timeAdd
     * @return
     */
    public static Date getDayAddDate(String date, int timeAdd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateFormatUtils.parse(date, DEFAULT_DATE_PATTERN));
        cal.add(Calendar.DATE, timeAdd);
        return cal.getTime();
    }

    /**
     * 获取timeAdd天后的日期
     *
     * @param date
     * @param timeAdd
     * @return
     */
    public static Date getDayAddDate(Date date, int timeAdd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, timeAdd);
        return cal.getTime();
    }


    /**
     * 获取timeAdd月后的日期
     *
     * @param date
     * @param timeAdd
     * @return
     */
    public static Date getMonthAddDate(Date date, int timeAdd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, timeAdd);
        return cal.getTime();
    }

    /**
     * 获得指定日期的后日期
     *
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay, int timeAdd) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        c.add(Calendar.DATE, timeAdd);
        String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayAfter;
    }

    /**
     * @param day1
     * @param day2
     * @return boolean
     * @throws
     * @Title: 查看是否为同一天
     * @Description:
     */
    public static boolean isSameDay(Date day1, Date day2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ds1 = sdf.format(day1);
        String ds2 = sdf.format(day2);
        if (ds1.equals(ds2)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String args[]) {
        System.out.println(DateFormatUtils.format(getMonthAddDate(new Date(), -1), DEFAULT_DATE_YEAR_MONTH));
    }

    public   static   int   getLastDayOfMonth(Date   sDate1)   {
        Calendar   cDay1   =   Calendar.getInstance();
        cDay1.setTime(sDate1);
        final   int   lastDay   =   cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
        return   lastDay;
    }
}
