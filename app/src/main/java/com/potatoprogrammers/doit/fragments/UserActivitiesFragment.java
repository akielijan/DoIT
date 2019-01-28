package com.potatoprogrammers.doit.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.potatoprogrammers.doit.models.User;
import com.potatoprogrammers.doit.models.UserActivity;
import com.potatoprogrammers.doit.R;

import java.util.List;
import java.util.Locale;

import lombok.NonNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserActivitiesFragment extends AbstractFragment {
    private TextView search;
    private ListView activitiesListView;
    private Button addActivityButton;
    private List<UserActivity> activities;
    private final String NEW_ACTIVITY_NAME = "NEW ACTIVITY";

    public UserActivitiesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_activities, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initActivitiesList(view);
        this.addActivityButton = view.findViewById(R.id.addActivityButton);
        this.search = view.findViewById(R.id.searchTextView);

        this.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    UserActivity activity = activities.stream().filter(x -> x.getName().toLowerCase().contains(search.getText().toString().toLowerCase())).findFirst().get();
                    if(activity!=null) {
                        int i = activities.indexOf(activity);
                        activitiesListView.setSelection(i);
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Mach not found", Toast.LENGTH_SHORT);
                }
            }
        });

        this.activitiesListView.setOnItemLongClickListener((parent, view12, position, id) -> {
            this.activitiesListView.setItemChecked(position, activities.get(position).toggleActive());
            this.updateUserInDatabase();
            return true;
        });

        this.activitiesListView.setOnItemClickListener((parent, view1, position, id) -> {
            this.activitiesListView.setItemChecked(position, !this.activitiesListView.isItemChecked(position)); //todo make it better
            swapFragment(new UserActivityFragment(), prepareArgumentsForUserActivity(position));
        });

        this.addActivityButton.setOnClickListener(this::createActivity);
    }

    private void updateActivitiesList(List<UserActivity> newList) {
        this.activitiesListView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked, newList));
        this.activities.stream()
                .filter(UserActivity::isActive)
                .forEach(x -> this.activitiesListView.setItemChecked(this.activities.indexOf(x), true));
    }

    private void initActivitiesList(@NonNull View view) {
        this.activities = User.getLoggedInUser().getActivities();
        this.activitiesListView = view.findViewById(R.id.activitiesListView);
        this.activitiesListView.setClickable(true);
        this.activitiesListView.setLongClickable(true);
        this.activitiesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        this.updateActivitiesList(this.activities);
    }

    private Bundle prepareArgumentsForUserActivity(int userActivityPosition) {
        Bundle args = new Bundle();
        args.putInt("userActivityPosition", userActivityPosition);
        return args;
    }

    private void createActivity(View v) {
        this.activities.add(
                new UserActivity(
                        String.format(Locale.getDefault(), "%s %d", NEW_ACTIVITY_NAME, this.getNextNumberForNewActivity())
                )
        );
        this.activitiesListView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked, this.activities));
        this.updateActivitiesList(this.activities);
        this.updateUserInDatabase();
    }

    private int getNextNumberForNewActivity() {
        int res = this.activities.stream()
                .filter(x -> x.getName().contains(this.NEW_ACTIVITY_NAME))
                .map(x -> x.getName().replace(this.NEW_ACTIVITY_NAME, "").trim())
                .map(this::parseInt)
                .max(Integer::compareTo)
                .orElse(0);
        return res + 1;
    }

    private Integer parseInt(String toParse) {
        try {
            return Integer.parseInt(toParse);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
