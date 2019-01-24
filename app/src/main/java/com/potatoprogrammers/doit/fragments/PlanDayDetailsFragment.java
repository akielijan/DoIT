package com.potatoprogrammers.doit.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.potatoprogrammers.doit.R;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanDayDetailsFragment extends AbstractFragment {
    private Date date;

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
    }
}
