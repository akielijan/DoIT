package com.potatoprogrammers.doit.models;

import com.potatoprogrammers.doit.enums.DayOfTheWeek;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserActivityDate {

    public UserActivityDate(DayOfTheWeek day) {
        this.setDay(day);
        this.initializeActivityDate();
    }

    private DayOfTheWeek day;
    private int hour;
    private int minute;
    private boolean isActive;

    public void setTime(int hour, int minute) {
        this.setHour(hour);
        this.setMinute(minute);
    }

    private void initializeActivityDate() {
        this.setHour(0);
        this.setMinute(0);
        this.setActive(false);
    }
}
