package com.potatoprogrammers.doit.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserActivity {
    public UserActivity(String name) {
        this.setName(name);
    }

    private String name;
    private Boolean isActive;
    private List<UserActivityStep> userActivitySteps;
    private List<UserActivityDate> userActivityDates;
}
