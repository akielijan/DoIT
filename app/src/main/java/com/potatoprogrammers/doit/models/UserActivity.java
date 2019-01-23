package com.potatoprogrammers.doit.models;

import com.potatoprogrammers.doit.enums.DayOfTheWeek;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserActivity {
    public UserActivity(String name) {
        this.setName(name);
        this.initializeUserActivity();
    }

    private String name;
    private boolean isActive;
    private List<UserActivityStep> userActivitySteps = new ArrayList<>(Collections.singletonList(new UserActivityStep()));
    private List<UserActivityDate> userActivityDates = new ArrayList<>();

    public boolean toggleActive() {
        this.setActive(!this.isActive());
        return this.isActive();
    }

    public boolean toggleDayActive(DayOfTheWeek day) {
        UserActivityDate dayToChange = this.getUserActivityDates().get(day.ordinal());
        dayToChange.setActive(!dayToChange.isActive());
        return dayToChange.isActive();
    }

    public boolean isDayActive(DayOfTheWeek day) {
        return getUserActivityDates().stream().anyMatch(x -> x.getDay().equals(day) && x.isActive());
    }

    private void initializeUserActivity() {
        this.userActivitySteps = new ArrayList<>();
        this.getUserActivitySteps().add(new UserActivityStep());
        this.userActivityDates = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            this.getUserActivityDates().add(new UserActivityDate(DayOfTheWeek.getDayOfWeek(i)));
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
