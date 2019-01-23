package com.potatoprogrammers.doit.models;

import java.util.ArrayList;
import java.util.Arrays;
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
    }

    private String name;
    private boolean isActive;
    private List<UserActivityStep> userActivitySteps = Arrays.asList(new UserActivityStep());//new ArrayList<>();
    private List<UserActivityDate> userActivityDates = new ArrayList<>();

    public boolean toggleActive() {
        this.setActive(!this.isActive());
        return this.isActive();
    }
}
