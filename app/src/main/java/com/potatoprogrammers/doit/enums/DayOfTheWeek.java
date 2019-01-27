package com.potatoprogrammers.doit.enums;

import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Stream;

public enum DayOfTheWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    public static DayOfTheWeek getDayOfWeek(int ordinal) {
        return Arrays.stream(DayOfTheWeek.values()).filter(x -> x.ordinal() == ordinal).findFirst().get();
    }

    public DayOfTheWeek next() {
        return Stream.of(DayOfTheWeek.values()).filter(x -> x.ordinal() == (this.ordinal() + 1) % 7).findFirst().get();
    }

    public DayOfTheWeek prev() {
        int ord = this.ordinal();
        --ord;
        if (ord < 0) ord += 7;
        return DayOfTheWeek.getDayOfWeek(ord);
    }

    public static String getDayName(DayOfTheWeek day) {
        return day.toString().substring(0, 1).toUpperCase() + day.toString().substring(1).toLowerCase();
    }

    public static DayOfTheWeek getTodayDayOfTheWeek() {
        return getTodayDayOfTheWeekFromCalendar(Calendar.getInstance());
    }

    public static DayOfTheWeek getTodayDayOfTheWeekFromCalendar(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                return DayOfTheWeek.MONDAY;
            case Calendar.TUESDAY:
                return DayOfTheWeek.TUESDAY;
            case Calendar.WEDNESDAY:
                return DayOfTheWeek.WEDNESDAY;
            case Calendar.THURSDAY:
                return DayOfTheWeek.THURSDAY;
            case Calendar.FRIDAY:
                return DayOfTheWeek.FRIDAY;
            case Calendar.SATURDAY:
                return DayOfTheWeek.SATURDAY;
            case Calendar.SUNDAY:
                return DayOfTheWeek.SUNDAY;
            default:
                return DayOfTheWeek.MONDAY;
        }
    }

    public String getDayName() {
        return DayOfTheWeek.getDayName(this);
    }
}
