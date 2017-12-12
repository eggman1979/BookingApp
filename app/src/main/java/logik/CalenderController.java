package logik;

import android.util.Log;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by KimdR on 21-10-2017.
 */

public class CalenderController {

    /**
     * @param date
     * @return Returnerer en given dato til millisekunder ved midnat
     */

    public static long dateToMillis(LocalDate date) {
        return new LocalDate(date).toDateTime(LocalTime.MIDNIGHT).getMillis();
    }

    public static LocalDate millisToDate(long millis) {
        return new LocalDate(millis); // skal testes
    }

    public static LocalDate getToday() {
        return LocalDate.now();
    }

    public static LocalDate getFirstMondayInCalender(int month) {
        LocalDate first = getToday().withDayOfMonth(1);
        int monthNow = first.getMonthOfYear();
        int difference = month - monthNow;
        LocalDate monthDate = first.plusMonths(difference);
        LocalDate firstDayOfMonth = monthDate.withDayOfMonth(1);
        LocalDate firstMondayInCalender = firstDayOfMonth.minusDays(firstDayOfMonth.getDayOfWeek() - 1);
        return firstMondayInCalender;
    }

    public static String getMonthInText(int month) {
        LocalDate first = getToday().withDayOfMonth(1);
        int monthNow = first.getMonthOfYear();
        int difference = month - monthNow;
        LocalDate monthDate = first.plusMonths(difference);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMMM");
        String monthString = formatter.print(monthDate);
        return monthString.substring(0, 1).toUpperCase() + monthString.substring(1);

    }

    public static LocalDate getLastDayInCalender(int month) {
        return getFirstMondayInCalender(month).plusDays(42);
    }

    public static LocalDate getFirstDayOfWeek(int week) {
        LocalDate first = getToday().withDayOfWeek(1);
        int weekNow = first.getWeekOfWeekyear();
        int difference = week - weekNow;
        LocalDate weekDate = first.plusWeeks(difference);
        LocalDate firstDayOfWeek = weekDate.withDayOfWeek(1);
        return firstDayOfWeek;
    }

    public static LocalDate getLastDayOfWeek(int week) {
        LocalDate slutDag = getFirstDayOfWeek(week).plusDays(6);
        return slutDag;
    }

    public static int getDaysBetween(LocalDate start, LocalDate slut){
        if(slut == null){
            return 42; // antalDage i en kalender måned
        }
        return Days.daysBetween(start,slut).getDays()+1;
    }

    public static String getWeekDay(LocalDate dato){
        DateTimeFormatter format = DateTimeFormat.forPattern("E");
        String weekDay = format.print(dato);
        return weekDay;
    }


    public static int getWeek(int week) {
        int weekYear = getFirstDayOfWeek(week).getWeekOfWeekyear();
        return weekYear;
    }
}
