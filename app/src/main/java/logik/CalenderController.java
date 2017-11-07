package logik;

import android.util.Log;

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

        Log.w("ttt", "getMonthInText: " + difference + " " + monthDate.toString());
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMMM");
        String monthString = formatter.print(monthDate);
        return monthString.substring(0, 1).toUpperCase() + monthString.substring(1);

    }

    public static LocalDate getLastDayInCalender(int month) {
        return getFirstMondayInCalender(month).plusDays(42);
    }




}
