package com.potatoprogrammers.doit.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.potatoprogrammers.doit.models.User;
import com.potatoprogrammers.doit.models.UserActivity;
import com.potatoprogrammers.doit.R;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserActivitiesFragment extends AbstractFragment {
    private ListView activitiesListView;
    private Button addActivityButton;

    public UserActivitiesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_activities, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activitiesListView = view.findViewById(R.id.activitiesListView);
        addActivityButton = view.findViewById(R.id.addActivityButton);

        activitiesListView.setClickable(true);
        activitiesListView.setLongClickable(true);
        activitiesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        activitiesListView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked, User.getLoggedInUser().getActivities()));

        activitiesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                activitiesListView.setItemChecked(position, User.getLoggedInUser().getActivities().get(position).toggleActive());
                return true;
            }
        });

        activitiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activitiesListView.setItemChecked(position, !activitiesListView.isItemChecked(position)); //todo make it better
                swapFragment(new UserActivityFragment(), prepareArgumentsForUserActivity(position));
            }
        });

        addActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.getLoggedInUser().getActivities().add(new UserActivity("NEW ACTIVITY" + User.getLoggedInUser().getActivities().size()));
                activitiesListView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked, User.getLoggedInUser().getActivities()));
            }
        });
    }

    private Bundle prepareArgumentsForUserActivity(int userActivityPosition) {
        Bundle args = new Bundle();
        args.putInt("userActivityPosition",userActivityPosition);
        return args;
    }
}
