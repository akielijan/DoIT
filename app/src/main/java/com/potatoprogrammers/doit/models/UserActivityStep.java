package com.potatoprogrammers.doit.models;

import java.util.Base64;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserActivityStep {
    private Base64 image;
    private String description;
}
