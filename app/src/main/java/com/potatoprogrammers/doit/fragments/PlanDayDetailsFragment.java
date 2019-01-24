package com.potatoprogrammers.doit.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;

import com.potatoprogrammers.doit.R;
import com.potatoprogrammers.doit.models.User;
import com.potatoprogrammers.doit.enums.DayOfTheWeek;
import com.potatoprogrammers.doit.models.UserActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanDayDetailsFragment extends AbstractFragment {
    private Date date;
    private EditText notes;
    private TextView dayName;
    private ListView activitiesForCurrentDay;

    public PlanDayDetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan_day_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        date = (Date) getArguments().getSerializable("date");
        dayName = view.findViewById(R.id.dayTextView);
        //dayName.setText(getDayOfTheWeek().getDayName());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        dayName.setText(DayOfTheWeek.getTodayDayOfTheWeekFromCalendar(cal).getDayName());

        activitiesForCurrentDay = view.findViewById(R.id.dayActivitiesListView);
        activitiesForCurrentDay.setClickable(false);
        activitiesForCurrentDay.setLongClickable(false);
        updateActivitiesList(getActivitiesForToday());
        handleNotes();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void handleNotes() {
        notes = getView().findViewById(R.id.notesEditText);
        notes.setText(queryNotesFromDate(date));

        notes.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                User.getLoggedInUser().getNotes().put(getDateAsString(date), notes.getText().toString());
                this.updateUserInDatabase();
            }
        });
    }

    private String getDateAsString(Date date) {
        return new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date);
    }

    private String queryNotesFromDate(Date date) {
        return queryNotesFromDate(getDateAsString(date));
    }

    private String queryNotesFromDate(String date) {
        Map<String, String> userNotes = User.getLoggedInUser().getNotes();
        if (!userNotes.containsKey(date)) {
            userNotes.put(date, "");
        }
        return userNotes.get(date);
    }

    private void updateActivitiesList(List<UserActivity> activities) {
        ArrayList<String> names = new ArrayList<>();
        for(UserActivity activity: activities) {
            names.add(activity.getName());
        }
        activitiesForCurrentDay.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names));
    }

    private List<UserActivity> getActivitiesForToday() {
        List<UserActivity> result = new ArrayList<>();
        List<UserActivity> all = User.getLoggedInUser().getActivities();
        for (UserActivity activity: all) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if(activity.getUserActivityDates().stream().anyMatch(x->x.getDay().equals(DayOfTheWeek.getTodayDayOfTheWeekFromCalendar(cal)))) {
                result.add(activity);
            }
        }
        return result;
    }

    private DayOfTheWeek getDayOfTheWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return DayOfTheWeek.getDayOfWeek(c.get(Calendar.DAY_OF_WEEK));
    }
}
