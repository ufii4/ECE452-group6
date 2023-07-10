package com.ece452.watfit.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.ece452.watfit.CalorieDisplayAdapter;
import com.ece452.watfit.EditPostActivity;
import com.ece452.watfit.R;
import com.ece452.watfit.data.Ingredient;
import com.ece452.watfit.databinding.FragmentDashboardBinding;
import com.ece452.watfit.ui.dashboard.CircleDiagramView;
import dagger.hilt.android.AndroidEntryPoint;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private Timestamp localDate = null;
    private List<Double> dailyCalorie = new ArrayList<>();
    private List<Double> exerciseCalorie = new ArrayList<>();
    private List<BarEntry> calorieEntries = new ArrayList<>();
    private List<BarEntry> exerciseEntries = new ArrayList<>();

    @Inject
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getUid());

        //Add date to entries
        List<String> dateEntries = new ArrayList<>();

        docRef.collection("calorieLogs").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                //is it possible to have an empty collection?
                                Date logDate = documentSnapshot.getTimestamp("date").toDate();
                                if (logDate != null && isSameDate(logDate)) {
                                    dailyCalorie.add(documentSnapshot.getDouble("dailyCalorie"));
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    String formattedDate = dateFormat.format(logDate);
                                    dateEntries.add(formattedDate);
                                }
                            }
                            // Add data to the chart
                            for (int i = 0; i < dailyCalorie.size(); i++) {
                                calorieEntries.add(new BarEntry(i,dailyCalorie.get(i).floatValue()));
                            }
                            BarChart barChart = root.findViewById(R.id.barChart);
                            // Configure the chart
                            barChart.setTouchEnabled(true);
                            barChart.setDragEnabled(true);
                            barChart.setScaleEnabled(true);
                            barChart.setPinchZoom(true);
                            // Customize the X-axis
                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setLabelRotationAngle(45f);
                            xAxis.setTextSize(12f);
                            xAxis.setEnabled(true);
                            xAxis.setDrawAxisLine(true);
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(dateEntries));
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                            // Customize the Y-axis
                            YAxis yAxis = barChart.getAxisLeft();
                            BarDataSet dataSet = new BarDataSet (calorieEntries, "Calorie Intake");
                            dataSet.setColor(Color.BLUE);
                            dataSet.setValueTextColor(Color.RED);
                            BarData barData = new BarData(dataSet);
                            barChart.setData(barData);
                            barChart.invalidate();
                        }
                    }
                });
        docRef.collection("exerciseLogs").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                //is it possible to have an empty collection?
                                Date logDate = documentSnapshot.getTimestamp("date").toDate();
                                if (logDate != null && isSameDate(logDate)) {
                                    exerciseCalorie.add(documentSnapshot.getDouble("dailyCalorie"));
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    String formattedDate = dateFormat.format(logDate);
                                    dateEntries.add(formattedDate);
                                }
                            }
                            // Add data to the chart
                            for (int i = 0; i < exerciseCalorie.size(); i++) {
                                exerciseEntries.add(new BarEntry(i,exerciseCalorie.get(i).floatValue()));
                            }
                            BarChart barChart = root.findViewById(R.id.barChartExercise);
                            // Configure the chart
                            barChart.setTouchEnabled(true);
                            barChart.setDragEnabled(true);
                            barChart.setScaleEnabled(true);
                            barChart.setPinchZoom(true);
                            // Customize the X-axis
                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setLabelRotationAngle(45f);
                            xAxis.setTextSize(12f);
                            xAxis.setEnabled(true);
                            xAxis.setDrawAxisLine(true);
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(dateEntries));
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                            // Customize the Y-axis
                            YAxis yAxis = barChart.getAxisLeft();
                            BarDataSet dataSet = new BarDataSet (exerciseEntries, "Exercise");
                            dataSet.setColor(Color.GREEN);
                            dataSet.setValueTextColor(Color.RED);
                            BarData barData = new BarData(dataSet);
                            barChart.setData(barData);
                            barChart.invalidate();
                        }
                    }
                });

        return root;
    }

    /********* Sharing Button ***********/
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }
    // add account button to action bar (header)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share_button, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // share button is clicked
        if (item.getItemId() == R.id.share_post_button) {
            // handle account button click
            // TODO: take a screenshot on the current Dashboard fragment before navigate to EditPostActivity
            startActivity(new Intent(getActivity(), EditPostActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    //decide whether the date is within 7 days
    private boolean isSameDate(Date date1) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();

        int year1 = cal1.get(Calendar.YEAR);
        int month1 = cal1.get(Calendar.MONTH);
        int day1 = cal1.get(Calendar.DAY_OF_MONTH);

        int year2 = cal2.get(Calendar.YEAR);
        int month2 = cal2.get(Calendar.MONTH);
        int day2 = cal2.get(Calendar.DAY_OF_MONTH);

        return (year1 == year2 && month1 == month2 && (day1 >= day2-6));
    }
}