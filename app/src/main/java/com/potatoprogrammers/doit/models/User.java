package com.potatoprogrammers.doit.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Getter
    @Setter
    private static User loggedInUser;
    public static final String COLLECTION = "users";

    @NonNull
    private List<UserActivity> activities = new ArrayList<>();
    @NonNull
    private Map<String, String> notes = new HashMap<>();
}
