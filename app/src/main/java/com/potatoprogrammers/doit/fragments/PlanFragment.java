package com.potatoprogrammers.doit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.potatoprogrammers.doit.R;
import com.potatoprogrammers.doit.enums.DayOfTheWeek;

import java.util.Calendar;

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

        todayDayOfTheWeek = getTodayDayOfTheWeek();
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
        visiblePlanDays[3].setText(DayOfTheWeek.getDayName(todayDayOfTheWeek));
        //todo initialize other days
    }

    private void openDayView(int id) {
        /*switch (id) {
            case R.id.threeDaysAgoTextView:

            case R.id.twoDaysAgoTextView:

            case R.id.dayAgoTextView:

            case R.id.todayTextView:

            case R.id.dayAheadTextView:

            case R.id.twoDaysAheadTextView:

            case R.id.threeDaysAheadTextView:

            default:

        }todo handle open day view, create plan structure in db*/
    }

    private DayOfTheWeek getTodayDayOfTheWeek() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                return DayOfTheWeek.MONDAY;
            case Calendar.TUESDAY:
                return DayOfTheWeek.TUESDAY;
            case Calendar.WEDNESDAY:
                return DayOfTheWeek.WEDNESDAY;
            case Calendar.THURSDAY:
                return DayOfTheWeek.THURSDAY;
            case Calendar.FRIDAY:
                return DayOfTheWeek.FRIDAY;
            case Calendar.SATURDAY:
                return DayOfTheWeek.SATURDAY;
            case Calendar.SUNDAY:
                return DayOfTheWeek.SUNDAY;
            default:
                return DayOfTheWeek.MONDAY;
        }
    }
}
