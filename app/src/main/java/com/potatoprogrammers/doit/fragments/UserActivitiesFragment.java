package com.potatoprogrammers.doit.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.potatoprogrammers.doit.models.User;
import com.potatoprogrammers.doit.models.UserActivity;
import com.potatoprogrammers.doit.R;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserActivitiesFragment extends AbstractFragment {
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
        activities = User.getLoggedInUser().getActivities();
        activitiesListView = view.findViewById(R.id.activitiesListView);
        addActivityButton = view.findViewById(R.id.addActivityButton);

        activitiesListView.setClickable(true);
        activitiesListView.setLongClickable(true);
        activitiesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        activitiesListView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked, activities));

        activities.stream()
                .filter(UserActivity::isActive)
                .forEach(x -> activitiesListView.setItemChecked(activities.indexOf(x), true));

        activitiesListView.setOnItemLongClickListener((parent, view12, position, id) -> {
            activitiesListView.setItemChecked(position, activities.get(position).toggleActive());
            this.updateUserInDatabase();
            return true;
        });

        activitiesListView.setOnItemClickListener((parent, view1, position, id) -> {
            activitiesListView.setItemChecked(position, !activitiesListView.isItemChecked(position)); //todo make it better
            swapFragment(new UserActivityFragment(), prepareArgumentsForUserActivity(position));
        });

        addActivityButton.setOnClickListener(this::createActivity);
    }

    private Bundle prepareArgumentsForUserActivity(int userActivityPosition) {
        Bundle args = new Bundle();
        args.putInt("userActivityPosition",userActivityPosition);
        return args;
    }

    private void createActivity(View v) {
        activities.add(
                new UserActivity(
                        String.format(Locale.getDefault(), "%s %d", NEW_ACTIVITY_NAME, getNextNumberForNewActivity())
                )
        );
        activitiesListView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked, activities));
        this.updateUserInDatabase();
    }

    private int getNextNumberForNewActivity() {
        int res = activities.stream()
                .filter(x -> x.getName().contains(NEW_ACTIVITY_NAME))
                .map(x -> x.getName().replace(NEW_ACTIVITY_NAME, "").trim())
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
