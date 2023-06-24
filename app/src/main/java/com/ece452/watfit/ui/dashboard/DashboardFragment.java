package com.ece452.watfit.ui.dashboard;

import android.os.Bundle;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ece452.watfit.R;
import com.ece452.watfit.databinding.FragmentDashboardBinding;
import com.ece452.watfit.ui.dashboard.CircleDiagramView;
import dagger.hilt.android.AndroidEntryPoint;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        LineChart lineChart = root.findViewById(R.id.lineChart);
// Configure the chart
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);

        // Customize the X-axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Customize the Y-axis
        YAxis yAxis = lineChart.getAxisLeft();

        // Add data to the chart
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 80.0f));
        entries.add(new Entry(1, 80.35f));
        entries.add(new Entry(2, 80.45f));
        entries.add(new Entry(3, 81.35f));

        LineDataSet dataSet = new LineDataSet(entries, "Values");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.RED);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}