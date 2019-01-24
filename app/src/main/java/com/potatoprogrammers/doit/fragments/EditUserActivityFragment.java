package com.potatoprogrammers.doit.fragments;


import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.potatoprogrammers.doit.R;
import com.potatoprogrammers.doit.enums.DayOfTheWeek;
import com.potatoprogrammers.doit.models.User;
import com.potatoprogrammers.doit.models.UserActivity;
import com.potatoprogrammers.doit.models.UserActivityDate;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditUserActivityFragment extends Fragment {
    private TextView currentActivityName;
    private ListView checkableOptionsList;
    private ListView checkableDayOptionsList;
    private TimePicker activityStartTimePicker;
    private Button saveChangesButton;
    private TextView[] daysOfTheWeekTextViews;
    private UserActivity currentUserActivity;
    private DayOfTheWeek openedDayOfWeek;

    public EditUserActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_user_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUserActivity = User.getLoggedInUser().getActivities().get(getArguments().getInt("userActivityPosition"));
        saveChangesButton = view.findViewById(R.id.saveUserActivityButton);
        openedDayOfWeek = DayOfTheWeek.MONDAY;

        daysOfTheWeekTextViews = new TextView[]{view.findViewById(R.id.mondayTextView),
                view.findViewById(R.id.tuesdayTextView),
                view.findViewById(R.id.wednesdayTextView),
                view.findViewById(R.id.thursdayTextView),
                view.findViewById(R.id.fridayTextView),
                view.findViewById(R.id.saturdayTextView),
                view.findViewById(R.id.sundayTextView)};

        currentActivityName = view.findViewById(R.id.currentUserActivityNameTextView);
        currentActivityName.setText(currentUserActivity.getName());

        checkableOptionsList = view.findViewById(R.id.checkableOptionsListView);
        checkableOptionsList.setClickable(true);
        checkableOptionsList.setLongClickable(true);
        checkableOptionsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        String[] checkableOptions = {"Is active"};
        checkableOptionsList.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked, new ArrayList<>(Arrays.asList(checkableOptions))));

        checkableDayOptionsList = view.findViewById(R.id.checkableDayOptionsListView);
        checkableDayOptionsList.setClickable(true);
        checkableDayOptionsList.setLongClickable(true);
        checkableDayOptionsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        String[] checkableDayOptions = {"Active on "+ DayOfTheWeek.getDayName(openedDayOfWeek)};
        checkableDayOptionsList.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked, new ArrayList<>(Arrays.asList(checkableDayOptions))));

        UserActivityDate openedUserActivityDate = currentUserActivity.getUserActivityDates().get(openedDayOfWeek.ordinal());
        activityStartTimePicker = view.findViewById(R.id.activityStartTimePicker);
        activityStartTimePicker.setIs24HourView(true);
        activityStartTimePicker.setHour(openedUserActivityDate.getHour());
        activityStartTimePicker.setMinute(openedUserActivityDate.getMinute());

        checkableOptionsList.setItemChecked(0, currentUserActivity.isActive());
        checkableDayOptionsList.setItemChecked(0, currentUserActivity.isDayActive(openedDayOfWeek));

        checkableOptionsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        checkableOptionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkableOptionsList.setItemChecked(position, currentUserActivity.toggleActive());
            }
        });

        checkableDayOptionsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        checkableDayOptionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkableDayOptionsList.setItemChecked(position, currentUserActivity.toggleDayActive(openedDayOfWeek));
                if(currentUserActivity.isDayActive(openedDayOfWeek)) {
                    daysOfTheWeekTextViews[openedDayOfWeek.ordinal()].setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                } else {
                    daysOfTheWeekTextViews[openedDayOfWeek.ordinal()].setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }
            }
        });

        activityStartTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                currentUserActivity.getUserActivityDates().get(openedDayOfWeek.ordinal()).setTime(hourOfDay,minute);
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo handle sync list of UserActivityDates with db
            }
        });

        for(int i=0; i<daysOfTheWeekTextViews.length; i++) {
            daysOfTheWeekTextViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.mondayTextView:
                            openedDayOfWeek = DayOfTheWeek.MONDAY; break;
                        case R.id.tuesdayTextView:
                            openedDayOfWeek = DayOfTheWeek.TUESDAY; break;
                        case R.id.wednesdayTextView:
                            openedDayOfWeek = DayOfTheWeek.WEDNESDAY; break;
                        case R.id.thursdayTextView:
                            openedDayOfWeek = DayOfTheWeek.THURSDAY; break;
                        case R.id.fridayTextView:
                            openedDayOfWeek = DayOfTheWeek.FRIDAY; break;
                        case R.id.saturdayTextView:
                            openedDayOfWeek = DayOfTheWeek.SATURDAY; break;
                        case R.id.sundayTextView:
                            openedDayOfWeek = DayOfTheWeek.SUNDAY; break;
                    }

                    UserActivityDate openedUserActivityDate = currentUserActivity.getUserActivityDates().get(openedDayOfWeek.ordinal());
                    activityStartTimePicker.setHour(openedUserActivityDate.getHour());
                    activityStartTimePicker.setMinute(openedUserActivityDate.getMinute());
                    String[] checkableDayOptions = {"Active on "+ DayOfTheWeek.getDayName(openedDayOfWeek)};
                    checkableDayOptionsList.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked, new ArrayList<>(Arrays.asList(checkableDayOptions))));
                }
            });
        }
    }
}
