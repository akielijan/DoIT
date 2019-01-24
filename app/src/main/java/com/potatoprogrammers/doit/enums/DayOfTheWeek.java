package com.potatoprogrammers.doit.enums;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.util.Arrays;
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
        return Stream.of(DayOfTheWeek.values()).filter(x -> x.ordinal() == this.ordinal() + 1 % 7).findFirst().get();
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

    public String getDayName() {
        return DayOfTheWeek.getDayName(this);
    }
}
