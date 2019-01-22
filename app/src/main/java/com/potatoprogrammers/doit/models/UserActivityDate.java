package com.potatoprogrammers.doit.models;

import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserActivityDate {
    private DayOfWeek day;
    private LocalTime time;
}
