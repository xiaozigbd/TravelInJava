package jarTest;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by yangzhi on 2017/12/11.
 *
 * @author yangzhi
 */
public class TestJodaDate {
    public static void main(String[] args){
        compareDate();
    }

    /**
     * 1、格式化输出
     */
    public static void dateToString(){
        DateTime dateTime = new DateTime(2015, 12, 21, 0, 0, 0, 333);
        System.out.println(dateTime.toString("yyyy/MM/dd HH:mm:ss EE"));
    }

    /**
     * 2、解析文本格式时间
     */
    public static void stringToDate(){
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime = DateTime.parse("2015-12-21 23:22:45", format);
        System.out.println(dateTime.toString("yyyy/MM/dd HH:mm:ss EE"));
    }

    /**
     * 3、在某个日期上加上90天并输出结果
     */
    public static void plusDays(){
        DateTime dateTime = new DateTime(2016, 1, 1, 0, 0, 0, 0);
        System.out.println(dateTime.plusDays(90).toString("E MM/dd/yyyy HH:mm:ss.SSS"));
    }

    /**
     * 4、到新年还有多少天
     * @param fromDate
     * @return
     */
    public static Days daysToNewYear(LocalDate fromDate) {
        LocalDate newYear = fromDate.plusYears(1).withDayOfYear(1);
        return Days.daysBetween(fromDate, newYear);
    }

    /**
     * 5、与JDK日期对象的转换
     * @return
     */
    public static void transToJdkDate() {
        DateTime dt = new DateTime();
        //转换成java.util.Date对象
        Date d1 = new Date(dt.getMillis());
        Date d2 = dt.toDate();

        System.out.println(d1);
        System.out.println(d2);
    }

    /**
     * 6、时区
     * @return
     */
    public static void timeZone() {
        DateTimeZone.setDefault(DateTimeZone.forID("Asia/Tokyo"));
        DateTime dt1 = new DateTime();
        System.out.println(dt1.toString("yyyy-MM-dd HH:mm:ss"));

        //伦敦时间
        DateTime dt2 = new DateTime(DateTimeZone.forID("Europe/London"));
        System.out.println(dt2.toString("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 7、计算间隔和区间
     * @return
     */
    public static void durationAndPeriod() {
        DateTime begin = new DateTime("2015-02-01");
        DateTime end = new DateTime("2016-05-01");

        //计算区间毫秒数
        Duration d = new Duration(begin, end);
        long millis = d.getMillis();
        System.out.println("计算区间毫秒数" + millis);

        //计算区间天数
        Period p = new Period(begin, end, PeriodType.days());
        int days = p.getDays();
        System.out.println("计算区间天数" + days);

        //计算特定日期是否在该区间内
        Interval interval = new Interval(begin, end);
        boolean contained = interval.contains(new DateTime("2015-03-01"));
        System.out.println("计算特定日期是否在该区间内" + contained);
    }

    /**
     * 8、日期比较
     * @return
     */
    public static void compareDate() {
        DateTime d1 = new DateTime("2015-10-01");
        DateTime d2 = new DateTime("2016-02-01");

        //和系统时间比
        boolean b1 = d1.isAfterNow();
        boolean b2 = d1.isBeforeNow();
        boolean b3 = d1.isEqualNow();

        //和其他日期比
        boolean f1 = d1.isAfter(d2);
        boolean f2 = d1.isBefore(d2);
        boolean f3 = d1.isEqual(d2);
    }
}
