package com.potatoprogrammers.doit.enums;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.util.Arrays;

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

    public static String getDayName(DayOfTheWeek day) {
        return day.toString().toLowerCase();
    }
}
