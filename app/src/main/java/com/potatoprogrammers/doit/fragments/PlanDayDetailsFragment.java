package com.potatoprogrammers.doit.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.potatoprogrammers.doit.R;
import com.potatoprogrammers.doit.enums.DayOfTheWeek;
import com.potatoprogrammers.doit.models.User;
import com.potatoprogrammers.doit.models.UserActivity;
import com.potatoprogrammers.doit.models.UserActivityDate;
import com.potatoprogrammers.doit.models.UserStats;
import com.potatoprogrammers.doit.utilities.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.NoArgsConstructor;

/**
 * A simple {@link Fragment} subclass.
 */
@NoArgsConstructor
public class PlanDayDetailsFragment extends AbstractFragment {
    private Date date;
    private EditText notesEditText;
    private TextView dayName;
    private ListView activitiesForCurrentDay;
    private UserStats stats;

    private void updateTaskDone(AdapterView<?> parent, View view1, int position, long id) {
        UserActivity changedActivity = this.getActivitiesForToday().get(position);
        Map<String, UserStats> userStats = User.getLoggedInUser().getStats();
        String dateAsString = Utils.getDateAsString(date);
        if (!userStats.containsKey(dateAsString)) {
            userStats.put(dateAsString, new UserStats());
        }
        UserStats dailyStats = userStats.get(dateAsString);
        boolean isActivityDoneThatDay = dailyStats.getActivitiesStatus().getOrDefault(changedActivity.getUuid(), false);
        isActivityDoneThatDay = !isActivityDoneThatDay;
        dailyStats.getActivitiesStatus().put(changedActivity.getUuid(), isActivityDoneThatDay);
        this.activitiesForCurrentDay.setItemChecked(position, isActivityDoneThatDay);
        this.updateUserInDatabase();
    }

    private boolean onItemLongClick(AdapterView<?> parent, View view1, int position, long id) {
        this.updateTaskDone(parent, view1, position, id);
        return false;
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
        String dateAsString = Utils.getDateAsString(date);
        if (!User.getLoggedInUser().getStats().containsKey(dateAsString)) {
            User.getLoggedInUser().getStats().put(dateAsString, new UserStats());
            this.updateUserInDatabase();
        }
        stats = User.getLoggedInUser().getStats().get(dateAsString);

        dayName = view.findViewById(R.id.dayTextView);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        dayName.setText(DayOfTheWeek.getDayOfTheWeekFromCalendar(cal).getDayName());

        if (!DayOfTheWeek.getDayOfTheWeekFromCalendar(cal).equals(DayOfTheWeek.getTodayDayOfTheWeek())) {
            LinearLayout dayLayout = view.findViewById(R.id.dayLayout);
            dayLayout.setBackground(new ColorDrawable(0xFFAAAAAA));
        }

        setupActivitiesListView(view);
        updateActivitiesList(getActivitiesForToday());
        handleNotes();
    }

    private void setupActivitiesListView(@NonNull View view) {
        activitiesForCurrentDay = view.findViewById(R.id.dayActivitiesListView);
        activitiesForCurrentDay.setClickable(true);
        activitiesForCurrentDay.setLongClickable(true);
        activitiesForCurrentDay.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        activitiesForCurrentDay.setOnItemClickListener(this::updateTaskDone);
        activitiesForCurrentDay.setOnItemLongClickListener(this::onItemLongClick);
    }

    private void handleNotes() {
        notesEditText = getView().findViewById(R.id.notesEditText);
        notesEditText.setText(queryNotesFromDate());

        notesEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                stats.setNote(notesEditText.getText().toString());
                this.updateUserInDatabase();
            }
        });
    }

    private String queryNotesFromDate() {
        if (TextUtils.isEmpty(stats.getNote())) {
            this.stats.setNote("");
            this.updateUserInDatabase();
        }
        return stats.getNote();
    }

    private void updateActivitiesList(List<UserActivity> activities) {
        activitiesForCurrentDay.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked, activities));

        activities.stream()
                .filter(activity -> stats.getActivitiesStatus().getOrDefault(activity.getUuid(), false))
                .forEach(x -> this.activitiesForCurrentDay.setItemChecked(activities.indexOf(x), true));
    }

    private List<UserActivity> getActivitiesForToday() {
        List<UserActivity> result = new ArrayList<>();
        List<UserActivity> all = User.getLoggedInUser().getActivities();
        for (UserActivity activity : all) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (activity.isActive() && activity.getUserActivityDates().stream().filter(UserActivityDate::isActive).anyMatch(x -> x.getDay().equals(DayOfTheWeek.getDayOfTheWeekFromCalendar(cal)))) {
                result.add(activity);
            }
        }
        return result;
    }
}
