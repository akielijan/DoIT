package com.potatoprogrammers.doit.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.potatoprogrammers.doit.R;
import com.potatoprogrammers.doit.models.User;
import com.potatoprogrammers.doit.models.UserSettings;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends AbstractFragment {
    private TimePicker timePicker;
    private Button saveSettingsButton;
    private Switch notificationsSwitch;

    public SettingsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserSettings settings = User.getLoggedInUser().getSettings();

        Toast.makeText(getContext(), "settings" + settings.isNotificationActive(), Toast.LENGTH_SHORT);

        notificationsSwitch = view.findViewById(R.id.notificationsSwitch);
        notificationsSwitch.setChecked(settings.isNotificationActive());

        timePicker = view.findViewById(R.id.notificationTimePicker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(settings.getNotificationHour());
        timePicker.setMinute(settings.getNotificationMinutes());
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                settings.setNotificationHour(hourOfDay);
                settings.setNotificationMinutes(minute);
            }
        });

        saveSettingsButton = view.findViewById(R.id.saveSettingsButton);
        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setNotificationActive(notificationsSwitch.isChecked());
                updateUserInDatabase();
            }
        });
    }
}
