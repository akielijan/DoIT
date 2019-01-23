package com.potatoprogrammers.doit.models;

import android.graphics.Bitmap;

import java.util.Base64;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityStep {
    private Bitmap image;
    private String description;
}
