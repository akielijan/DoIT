package com.potatoprogrammers.doit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.potatoprogrammers.doit.R;
import com.potatoprogrammers.doit.enums.DayOfTheWeek;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import lombok.NonNull;

public class PlanFragment extends AbstractFragment {
    private TextView[] visiblePlanDays;
    private DayOfTheWeek todayDayOfTheWeek;

    public PlanFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        todayDayOfTheWeek = DayOfTheWeek.getTodayDayOfTheWeek();
        initializeViewElements(view);
        initializeDaysList(view);

        for (TextView tv:visiblePlanDays) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDayView(v.getId());
                }
            });
        }
    }

    private void initializeViewElements(@NonNull View view) {
        visiblePlanDays = new TextView[]{view.findViewById(R.id.threeDaysAgoTextView),
                view.findViewById(R.id.twoDaysAgoTextView),
                view.findViewById(R.id.dayAgoTextView),
                view.findViewById(R.id.todayTextView),
                view.findViewById(R.id.dayAheadTextView),
                view.findViewById(R.id.twoDaysAheadTextView),
                view.findViewById(R.id.threeDaysAheadTextView)};
    }

    private void initializeDaysList(@NonNull View view) {
        visiblePlanDays[0].setText(todayDayOfTheWeek.prev().prev().prev().getDayName());
        visiblePlanDays[1].setText(todayDayOfTheWeek.prev().prev().getDayName());
        visiblePlanDays[2].setText(todayDayOfTheWeek.prev().getDayName());
        visiblePlanDays[3].setText(DayOfTheWeek.getDayName(todayDayOfTheWeek));
        visiblePlanDays[4].setText(todayDayOfTheWeek.next().getDayName());
        visiblePlanDays[5].setText(todayDayOfTheWeek.next().next().getDayName());
        visiblePlanDays[6].setText(todayDayOfTheWeek.next().next().next().getDayName());
    }

    private void openDayView(int id) {
        Calendar cal = Calendar.getInstance();

        switch (id) {
            case R.id.threeDaysAgoTextView:
                cal.add(Calendar.DAY_OF_MONTH, -3);
                break;
            case R.id.twoDaysAgoTextView:
                cal.add(Calendar.DAY_OF_MONTH, -2);
                break;
            case R.id.dayAgoTextView:
                cal.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case R.id.todayTextView:
                break;
            case R.id.dayAheadTextView:
                cal.add(Calendar.DAY_OF_MONTH, 1);
                break;
            case R.id.twoDaysAheadTextView:
                cal.add(Calendar.DAY_OF_MONTH, 2);
                break;
            case R.id.threeDaysAheadTextView:
                cal.add(Calendar.DAY_OF_MONTH, 3);
                break;
            default:
                return;
        } //todo handle open day view, create plan structure in db
        swapFragment(new PlanDayDetailsFragment(), prepareArgumentsForPlayDayDetails(cal.getTime()));
        //String strDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.getTime());
    }

    private Bundle prepareArgumentsForPlayDayDetails(Date date) {
        Bundle args = new Bundle();
        args.putSerializable("date", date);
        return args;
    }
}
