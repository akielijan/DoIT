package com.potatoprogrammers.doit.fragments;


import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.potatoprogrammers.doit.R;
import com.potatoprogrammers.doit.enums.DayOfTheWeek;
import com.potatoprogrammers.doit.models.User;
import com.potatoprogrammers.doit.models.UserActivity;
import com.potatoprogrammers.doit.models.UserActivityDate;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditUserActivityFragment extends AbstractFragment {
    private TextView currentActivityName;
    private ListView checkableOptionsList;
    private ListView checkableDayOptionsList;
    private TimePicker activityStartTimePicker;
    private Button saveChangesButton;
    private Button deleteActivityButton;
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

        openedDayOfWeek = DayOfTheWeek.getTodayDayOfTheWeek();

        initActivityElements(view);
        initDayElements(view);
        setStartValues();

        checkableOptionsList.setOnItemLongClickListener((parent, view14, position, id) -> false);
        checkableOptionsList.setOnItemClickListener((parent, view13, position, id) -> checkableOptionsList.setItemChecked(position, currentUserActivity.toggleActive()));

        checkableDayOptionsList.setOnItemLongClickListener((parent, view12, position, id) -> false);
        checkableDayOptionsList.setOnItemClickListener((parent, view1, position, id) -> {
            checkableDayOptionsList.setItemChecked(position, currentUserActivity.toggleDayActive(openedDayOfWeek));
            setColorsToDayTextView(openedDayOfWeek);
        });

        activityStartTimePicker.setOnTimeChangedListener((view15, hourOfDay, minute) -> currentUserActivity.getUserActivityDates().get(openedDayOfWeek.ordinal()).setTime(hourOfDay, minute));

        saveChangesButton.setOnClickListener(v -> {
            this.updateUserInDatabase();
            this.swapFragment(new UserActivitiesFragment(), getArguments());
        });

        for (TextView daysOfTheWeekTextView : daysOfTheWeekTextViews) {
            daysOfTheWeekTextView.setOnClickListener(this::daysOfWeekListener);
        }

        deleteActivityButton = view.findViewById(R.id.deleteActivityButton);
        deleteActivityButton.setOnClickListener(v -> {
            TextView label = new TextView(getContext());
            label.setText("Are you sure you want to delete this activity?");
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete activity")
                    .setView(new TextView(getContext()))
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        User.getLoggedInUser().getActivities().remove(currentUserActivity);
                        this.updateUserInDatabase();
                        this.swapFragment(new UserActivitiesFragment(), getArguments());
                    })
                    .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    private void initActivityElements(@NonNull View view) {
        currentUserActivity = User.getLoggedInUser().getActivities().get(getArguments().getInt("userActivityPosition"));
        currentActivityName = view.findViewById(R.id.currentUserActivityNameTextView);
        currentActivityName.setText(currentUserActivity.getName());

        checkableOptionsList = view.findViewById(R.id.checkableOptionsListView);
        checkableOptionsList.setClickable(true);
        checkableOptionsList.setLongClickable(true);
        checkableOptionsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        saveChangesButton = view.findViewById(R.id.saveUserActivityButton);
    }

    private void initDayElements(@NonNull View view) {
        daysOfTheWeekTextViews = new TextView[]{view.findViewById(R.id.mondayTextView),
                view.findViewById(R.id.tuesdayTextView),
                view.findViewById(R.id.wednesdayTextView),
                view.findViewById(R.id.thursdayTextView),
                view.findViewById(R.id.fridayTextView),
                view.findViewById(R.id.saturdayTextView),
                view.findViewById(R.id.sundayTextView)};

        checkableDayOptionsList = view.findViewById(R.id.checkableDayOptionsListView);
        checkableDayOptionsList.setClickable(true);
        checkableDayOptionsList.setLongClickable(true);
        checkableDayOptionsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        activityStartTimePicker = view.findViewById(R.id.activityStartTimePicker);
        activityStartTimePicker.setIs24HourView(true);
    }

    private void setStartValues() {
        String[] checkableOptions = {"Is active"};
        checkableOptionsList.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked, new ArrayList<>(Arrays.asList(checkableOptions))));
        checkableOptionsList.setItemChecked(0, currentUserActivity.isActive());

        switchDay(openedDayOfWeek, daysOfTheWeekTextViews[openedDayOfWeek.ordinal()]);

        for (UserActivityDate date : currentUserActivity.getUserActivityDates()) {
            setColorsToDayTextView(date.getDay());
        }
    }

    private void switchDay(DayOfTheWeek day, TextView clickedDay) {
        for(TextView tv : daysOfTheWeekTextViews) {
            tv.setTypeface(null, Typeface.NORMAL);
        }

        clickedDay.setTypeface(null, Typeface.BOLD);

        String[] checkableDayOptions = {"Active on " + DayOfTheWeek.getDayName(day)};
        checkableDayOptionsList.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked, new ArrayList<>(Arrays.asList(checkableDayOptions))));
        checkableDayOptionsList.setItemChecked(0, currentUserActivity.isDayActive(day));

        updateTimePicker();
    }

    private void updateTimePicker() {
        UserActivityDate openedUserActivityDate1 = currentUserActivity.getUserActivityDates().get(openedDayOfWeek.ordinal());
        activityStartTimePicker.setHour(openedUserActivityDate1.getHour());
        activityStartTimePicker.setMinute(openedUserActivityDate1.getMinute());
    }

    private void setColorsToDayTextView(DayOfTheWeek dayOfTheWeek) {
        final int color = currentUserActivity.isDayActive(dayOfTheWeek) ? R.color.colorAccent : R.color.colorPrimaryDark;
        daysOfTheWeekTextViews[dayOfTheWeek.ordinal()].setTextColor(ContextCompat.getColor(getContext(), color));
    }

    private void daysOfWeekListener(View v) {
        switch (v.getId()) {
            case R.id.mondayTextView:
                openedDayOfWeek = DayOfTheWeek.MONDAY;
                break;
            case R.id.tuesdayTextView:
                openedDayOfWeek = DayOfTheWeek.TUESDAY;
                break;
            case R.id.wednesdayTextView:
                openedDayOfWeek = DayOfTheWeek.WEDNESDAY;
                break;
            case R.id.thursdayTextView:
                openedDayOfWeek = DayOfTheWeek.THURSDAY;
                break;
            case R.id.fridayTextView:
                openedDayOfWeek = DayOfTheWeek.FRIDAY;
                break;
            case R.id.saturdayTextView:
                openedDayOfWeek = DayOfTheWeek.SATURDAY;
                break;
            case R.id.sundayTextView:
                openedDayOfWeek = DayOfTheWeek.SUNDAY;
                break;
        }

        switchDay(openedDayOfWeek, (TextView) v);
    }
}
