package com.potatoprogrammers.doit.models;

import java.util.Base64;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserActivityStep {
    private Base64 image; //todo format
    private String description;
}
