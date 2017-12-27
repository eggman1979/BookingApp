package logik;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by KimdR on 21-10-2017.
 */

public class CalenderController {

    /**
     * @param date
     * @return Returnerer en given dato til millisekunder ved midnat
     */

    public static long dateToMillis(DateTime date) {
  System.out.println("Finder millisekunder i dateToMillisMetoden i Calendar controller" + date.withZone(DateTimeZone.forID("Europe/Copenhagen")).withTimeAtStartOfDay().getMillis());
        return date.withZone(DateTimeZone.forID("Europe/Copenhagen")).withTimeAtStartOfDay().getMillis();
    }

    public static DateTime millisToDate(long millis) {
        return new DateTime(millis).withZone(DateTimeZone.forID("Europe/Copenhagen")).withTimeAtStartOfDay(); // skal testes
    }

    public static DateTime getToday() {
        DateTime now = DateTime.now().withZone(DateTimeZone.forID("Europe/Copenhagen")).withTimeAtStartOfDay();
        System.out.println("getToday fra calCont " +now.toString());
        return now;
    }

    public static DateTime getFirstMondayInCalender(int month) {
        DateTime first = getToday().withDayOfMonth(1);
        int monthNow = first.getMonthOfYear();
        int difference = month - monthNow;
        DateTime monthDate = first.plusMonths(difference);
        DateTime firstDayOfMonth = monthDate.withDayOfMonth(1);
        DateTime firstMondayInCalender = firstDayOfMonth.minusDays(firstDayOfMonth.getDayOfWeek() - 1).withZone(DateTimeZone.forID("Europe/Copenhagen")).withTimeAtStartOfDay();
        return firstMondayInCalender;
    }

    public static String getMonthInText(int month) {
        DateTime first = getToday().withDayOfMonth(1);
        int monthNow = first.getMonthOfYear();
        int difference = month - monthNow;
        DateTime monthDate = first.plusMonths(difference);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMMM");
        String monthString = formatter.print(monthDate);
        return monthString.substring(0, 1).toUpperCase() + monthString.substring(1);

    }

    public static DateTime getLastDayInCalender(int month) {
        return getFirstMondayInCalender(month).plusDays(42);
    }

    public static DateTime getFirstDayOfWeek(int week) {
       DateTime first = getToday().withDayOfWeek(1);
        int weekNow = first.getWeekOfWeekyear();
        int difference = week - weekNow;
        DateTime weekDate = first.plusWeeks(difference);
       DateTime firstDayOfWeek = weekDate.withDayOfWeek(1);
        return firstDayOfWeek;
    }

    public static DateTime getLastDayOfWeek(int week) {
        DateTime slutDag = getFirstDayOfWeek(week).plusDays(6);
        return slutDag;
    }

    public static int getDaysBetween(DateTime start, DateTime slut){
        if(slut == null){
            return 42; // antalDage i en kalender måned
        }
        return Days.daysBetween(start,slut).getDays()+1;
    }

    public static String getWeekDay(DateTime dato){
        DateTimeFormatter format = DateTimeFormat.forPattern("E");
        String weekDay = format.print(dato);
        return weekDay;
    }


    public static int getWeek(int week) {
        int weekYear = getFirstDayOfWeek(week).getWeekOfWeekyear();
        return weekYear;
    }

    public static boolean erMindre(DateTime today, DateTime date) {
        if(today.compareTo(date)>0){
            return true;
        }
        else return false;
    }
}
