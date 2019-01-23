package com.potatoprogrammers.doit.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.potatoprogrammers.doit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserActivityFragment extends AbstractFragment {
    private ImageView editUserActivity;
    private ImageView nextStep;
    private ImageView prevStep;
    private ImageView stepImage;
    private TextView stepDescription;

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

        editUserActivity = view.findViewById(R.id.editUserActivityImageView);
        stepImage = view.findViewById(R.id.stepImageView);
        stepDescription = view.findViewById(R.id.stepDescriptionTextView);
        nextStep = view.findViewById(R.id.nextStepImageView);
        prevStep = view.findViewById(R.id.prevStepImageView);

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
