package com.potatoprogrammers.doit.models;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserStats {
    private Map<String, Boolean> activitiesStatus = new HashMap<>();
    private String note = "";
}
