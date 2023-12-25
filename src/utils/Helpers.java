package utils;

import java.time.LocalDateTime;

public class Helpers {
    public static final int COUNT_DAY_IN_LEAP_YEAR = 366;
    public static final int COUNT_DAY_IN_NOT_LEAP_YEAR = 365;

    public static final int MINUTES_IN_INTERVAL = 15;

    private Helpers() {
    }

    public static int isLeapYear(int year) {
        if (year % 4 != 0) {
            return COUNT_DAY_IN_NOT_LEAP_YEAR;
        } else if (year % 400 == 0) {
            return COUNT_DAY_IN_LEAP_YEAR;
        } else if (year % 100 == 0) {
            return COUNT_DAY_IN_NOT_LEAP_YEAR;
        } else {
            return COUNT_DAY_IN_LEAP_YEAR;
        }
    }

    public static int calculateIntervalFromDate(LocalDateTime dateTime) {
        final int dayInYear = dateTime.getDayOfYear();
        final int minutesInDay = 24 * 60;
        final int minutesInHour = 60;
        int minutes = ((dayInYear - 1) * minutesInDay) + (dateTime.getHour() * minutesInHour + dateTime.getMinute());
        return minutes / MINUTES_IN_INTERVAL;
    }

}
