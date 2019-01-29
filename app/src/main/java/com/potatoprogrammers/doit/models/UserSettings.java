package com.potatoprogrammers.doit.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings {
    private boolean isNotificationActive;
    private int notificationHour;
    private int notificationMinutes;

    public boolean toggleNotificationActive() {
        this.setNotificationActive(!this.isNotificationActive());
        return this.isNotificationActive();
    }
}
