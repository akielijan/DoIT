package com.potatoprogrammers.doit.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.potatoprogrammers.doit.R;
import com.potatoprogrammers.doit.models.User;
import com.potatoprogrammers.doit.models.UserActivityStep;

import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserActivityFragment extends AbstractFragment {
    private ImageView editUserActivity;
    private ImageView addStep;
    private ImageView deleteStep;
    private ImageView nextStep;
    private ImageView prevStep;
    private ImageView stepImage;
    private TextView stepDescription;
    private TextView activityName;
    private TextView stepNo;
    private int activityPosition;
    private int currentStep;

    public UserActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activityPosition = getArguments().getInt("userActivityPosition");
        currentStep = 0;
        editUserActivity = view.findViewById(R.id.editUserActivityImageView);
        stepImage = view.findViewById(R.id.stepImageView);
        stepDescription = view.findViewById(R.id.stepDescriptionTextView);
        nextStep = view.findViewById(R.id.nextStepImageView);
        prevStep = view.findViewById(R.id.prevStepImageView);
        activityName = view.findViewById(R.id.userActivityNameTextView);
        addStep = view.findViewById(R.id.addNewStepImageView);
        deleteStep = view.findViewById(R.id.deleteNewStepImageView);
        stepNo = view.findViewById(R.id.stepNoTextView);

        activityName.setText(User.getLoggedInUser().getActivities().get(activityPosition).getName());
        stepNo.setText(String.format(Locale.getDefault(), "Step %d", currentStep));


        editUserActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment(new EditUserActivityFragment(), getArguments());
            }
        });

        stepImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //todo update current step photo
                return true;
            }
        });

        stepDescription.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //todo update current step description
                return true;
            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo change step to previous / new one on the beginning
            }
        });

        prevStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo change step to next / new one on the end
            }
        });
    }
}
