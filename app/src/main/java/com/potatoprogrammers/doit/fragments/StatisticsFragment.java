package com.potatoprogrammers.doit.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.graphics.vector.SolidFill;
import com.potatoprogrammers.doit.R;
import com.potatoprogrammers.doit.models.User;
import com.potatoprogrammers.doit.models.UserStats;
import com.potatoprogrammers.doit.utilities.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lombok.NoArgsConstructor;


/**
 * A simple {@link Fragment} subclass.
 */
@NoArgsConstructor
public class StatisticsFragment extends AbstractFragment {

    private AnyChartView chartView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        chartView = getView().findViewById(R.id.any_chart_view);
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        List<DataEntry> dataEntries = getDataEntries();
        cartesian.data(dataEntries);
        cartesian.yScale().minimum(0);
        cartesian.yScale().maximum(100);
        cartesian.line(dataEntries).color(new SolidFill("#07D913", 0));
        chartView.setChart(cartesian);
    }

    @NonNull
    private List<DataEntry> getDataEntries() {
        List<DataEntry> data = new ArrayList<>();
        Map<String, UserStats> stats = User.getLoggedInUser().getStats();
        Calendar cal = Calendar.getInstance();
        final int DAYS = 7;
        cal.add(Calendar.DAY_OF_MONTH, -DAYS);
        for (int i = 0; i < DAYS; ++i) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            UserStats dailyStats = stats.get(Utils.getDateAsString(cal.getTime()));
            double percentage;
            try {
                long actives = 0;
                long total = 0;

                for (Map.Entry<String, Boolean> pair : dailyStats.getActivitiesStatus().entrySet()) {
                    total++;
                    if (pair.getValue()) {
                        actives++;
                    }
                }

                percentage = total == 0 ? 0.0 : (actives * 1.0) / (total * 1.0);
            } catch (Exception e) {
                percentage = 0.0;
            }
            data.add(new ValueDataEntry(getDateForChart(cal.getTime()), percentage * 100));
        }
        return data;
    }

    private String getDateForChart(Date date) {
        return new SimpleDateFormat("MM.dd", Locale.getDefault()).format(date);
    }
}
