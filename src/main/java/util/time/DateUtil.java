package util.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by wanghailing on 2016/11/23.
 */
public class DateUtil {
    //时间校验
    private static final String TIME_FORMAT1 = "^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)$";
    private static final String TIME_FORMAT2 = "^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})/(((0[13578]|1[02])/(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)/(0[1-9]|[12][0-9]|30))|(02/(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))/02/29)$";
    private static final String TIME_FORMAT3 = "^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-((([13578]|1[02])-([1-9]|[12][0-9]|3[01]))|(([469]|11)-([1-9]|[12][0-9]|30))|(2-([1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-2-29)$";
    private static final String TIME_FORMAT4 = "^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})/((([13578]|1[02])/([1-9]|[12][0-9]|3[01]))|(([469]|11)/([1-9]|[12][0-9]|30))|(2/([1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))/2/29)$";

    /**
     * 检查时间戳是否是精确到分钟
     *
     * @param time
     * @return
     */
    public static boolean checkFormatTime(String time) {
        if (time.isEmpty()) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            sdf.parse(time);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 是否大于当前时间
     *
     * @param time
     * @return
     */
    public static boolean biggerThanCurrentTime(String time) {
        if (time.isEmpty()) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date smdate = null;
        try {
            smdate = sdf.parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);

            Date currentDate = new Date();
            Calendar currentL = Calendar.getInstance();
            currentL.setTime(currentDate);

            long stime = cal.getTimeInMillis();
            long currentTime = currentL.getTimeInMillis();
            if (smdate.getYear() == currentDate.getYear() && smdate.getMonth() == currentDate.getMonth() && smdate.getDay() == currentDate.getDay()) {
                return true;
            } else if (stime - currentTime > 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获得上个月，倒数第delta天的日期参数，delta >= 0
     *
     * @param monthDelta 月偏差，负数时往前，正数时往后
     * @param dayDelta   日偏差，必须为正数；为0时，是最后一天
     * @return
     */
    public static String getDateStrDesc(int monthDelta, int dayDelta) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month + monthDelta);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DATE, -dayDelta);
        Date strDateTo = calendar.getTime();
        return sdf.format(strDateTo);
    }

    /**
     * 获得当月第一天日期时间戳
     *
     * @return
     */
    public static String getFirstDateCurrentMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = sdf.format(c.getTime());
        System.out.println("===============first:" + first);
        return first;
    }

    /**
     * 获得某个月，正数数第dayDelta天的日期参数，dayDelta >= 0
     *
     * @param monthDelta 月偏差，负数时往前，正数时往后
     * @param dayDelta   日偏差，必须为正数；为0时，是最后一天
     * @return
     */
    public static String getFirstDayDate(int monthDelta, int dayDelta) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, monthDelta);
        c.set(Calendar.DAY_OF_MONTH, dayDelta);//设置为1号,当前日期既为本月第一天
        String first = sdf.format(c.getTime());
        return first;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        if (time2 - time1 < 0) {
            return 0;
        }
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间相差的小时
     *
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int hoursBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        if (time2 - time1 < 0) {
            return 0;
        }
        long between_hours = (time2 - time1) / (1000 * 3600);
        return Integer.parseInt(String.valueOf(between_hours));
    }

    /**
     * 计算两个日期之间相差的秒数
     *
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int secondsBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        if (time2 - time1 < 0) {
            return 0;
        }
        long between_seconds = (time2 - time1) / 1000;
        return Integer.parseInt(String.valueOf(between_seconds));
    }

    /**
     * 判断当前日期是否为今天
     *
     * @param dayDate
     * @return
     */
    public static boolean ifToday(Date dayDate) {

        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();    //今天
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        current.setTime(dayDate);

        if (current.after(today)) {
            return true;
        }
        return false;
    }

    /**
     * 将String时间转成long型
     *
     * @param time
     * @return
     */
    public static Long getLongTime(String time) {
        if (null == time || time.isEmpty()) {
            return 0L;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date smdate = sdf.parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            Date currentDate = new Date();
            Calendar currentL = Calendar.getInstance();
            currentL.setTime(currentDate);
            long stime = cal.getTimeInMillis();
            return stime;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /** * 计算得到MongoDB存储的日期，（默认情况下mongo中存储的是标准的时间，中国时间是东八区，存在mongo中少8小时，所以增加8小时）
     * */
    public static Date getMongoDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.HOUR_OF_DAY, 8);
        Date doo =  DateFormatUtils.parse(sdf.format(ca.getTime()), DateFormatUtils.DEFAULT_TIME_PATTERN);
        return doo;
    }

    /**
     * 校验时间格式
     * TIME_FORMAT1,TIME_FORMAT2 是2012-09-09这种带0的。
     * TIME_FORMAT3, TIME_FORMAT4 是2019-9-9不带0的
     * @param source
     * @return
     */
    public static Date parseDate(String source) throws Exception {
        if (null == source || "".equalsIgnoreCase(source)) {
            return null;
        }
        Date date = new Date();
        if (Pattern.matches(TIME_FORMAT3, source) == true) {
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            date = simpleDateFormat1.parse(source);
        } else if (Pattern.matches(TIME_FORMAT4, source) == true) {
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
            date = simpleDateFormat2.parse(source);
        }else if (Pattern.matches(TIME_FORMAT1, source) == true) {
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            date = simpleDateFormat1.parse(source);
        } else if (Pattern.matches(TIME_FORMAT2, source) == true) {
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
            date = simpleDateFormat2.parse(source);
        } else {
            throw new Exception();
        }
        return date;
    }

    /**
     * 校验时间格式
     * TIME_FORMAT1,TIME_FORMAT2 是2012-09-09这种带0的。
     * TIME_FORMAT3, TIME_FORMAT4 是2019-9-9不带0的
     * @param source
     * @return
     */
    public static void checDate(String source) throws Exception {
        if (null == source || "".equalsIgnoreCase(source)) {
            throw new Exception();
        }
        if (Pattern.matches(TIME_FORMAT3, source)|| Pattern.matches(TIME_FORMAT4, source) ||
                Pattern.matches(TIME_FORMAT1, source) || Pattern.matches(TIME_FORMAT2, source)) {
            return;
        } else {
            throw new Exception();
        }
    }
}
