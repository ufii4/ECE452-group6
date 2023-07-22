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
import com.ece452.watfit.data.Choice;
import com.ece452.watfit.data.Exercise;
import com.ece452.watfit.data.Ingredient;
import com.ece452.watfit.data.CalorieLog;
import com.ece452.watfit.data.ExerciseLog;
import com.ece452.watfit.data.Suggestion;
import com.ece452.watfit.databinding.FragmentDashboardBinding;
import com.ece452.watfit.ui.dashboard.CircleDiagramView;
import com.ece452.watfit.data.source.remote.OpenAIDataSource;
import com.ece452.watfit.data.source.remote.SuggestionService;
import dagger.hilt.android.AndroidEntryPoint;

import com.ece452.watfit.ui.post.PostActivityHelper;
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
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Arrays;

import javax.inject.Inject;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private TextView suggestion1;
    private TextView suggestion2;
    private TextView suggestion3;
    private Timestamp localDate = null;
    private List<Double> dailyCalorie = new ArrayList<>(7);
    private List<Double> exerciseCalorie = new ArrayList<>();
    private List<BarEntry> calorieEntries = new ArrayList<>();
    private List<Entry> exerciseEntries = new ArrayList<>();
    private double exerciseCalorieTotal = 0;
    private double dailyCalorieTotal = 0;

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
        List<String> dateEntries = new ArrayList<>(7);

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Calculate the date 7 days ago
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date sevenDaysAgo = calendar.getTime();

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    com.google.firebase.Timestamp suggestionTime = documentSnapshot.getTimestamp("suggestionDate");
                    Date suggestionDate = (suggestionTime != null)?suggestionTime.toDate() : currentDate;
                    if (suggestionTime != null && isSameDate(suggestionDate)) {
                        ArrayList suggestions = (ArrayList) documentSnapshot.get("suggestions");
                        suggestion1 = root.findViewById(R.id.text_suggestion1);
                        suggestion1.setText((String)suggestions.get(0));
                        suggestion2 = root.findViewById(R.id.text_suggestion2);
                        suggestion2.setText((String)suggestions.get(1));
                        suggestion3 = root.findViewById(R.id.text_suggestion3);
                        suggestion3.setText((String)suggestions.get(2));
                    } else {
                        Task<QuerySnapshot> calorieTask = docRef.collection("calorieLogs")
                        .whereGreaterThanOrEqualTo("date", sevenDaysAgo)
                        .whereLessThanOrEqualTo("date", currentDate)
                        .get();
    
                        // Fetch exercise logs within 7 days
                        Task<QuerySnapshot> exerciseTask = docRef.collection("exerciseLogs")
                                .whereGreaterThanOrEqualTo("date", sevenDaysAgo)
                                .whereLessThanOrEqualTo("date", currentDate)
                                .get();
            
                        // Fetch user information
                        Task<DocumentSnapshot> userTask = docRef.get();
            
                        // Combine all tasks
                        Task<List<Object>> combinedTask = Tasks.whenAllSuccess(calorieTask, exerciseTask, userTask)
                                .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                    @Override
                                    public void onSuccess(List<Object> results) {
                                        QuerySnapshot foodQuery = (QuerySnapshot) results.get(0);
                                        QuerySnapshot exerciseQuery = (QuerySnapshot) results.get(1);
                                        DocumentSnapshot userSnapshot = (DocumentSnapshot) results.get(2);
                                        int age = userSnapshot.getDouble("age").intValue();
                                        int height = userSnapshot.getDouble("height").intValue();
                                        int hip = userSnapshot.getDouble("hip").intValue();
                                        int waist = userSnapshot.getDouble("waist").intValue();
                                        int weight = userSnapshot.getDouble("weight").intValue();
                                        String gender = userSnapshot.getString("gender");
            
                                        // Generating the food list sentence
                                        StringBuilder foodSentence = new StringBuilder("The foods I have taken are ");
                                        for (DocumentSnapshot documentSnapshot : foodQuery.getDocuments()) {
                                            ArrayList foodList = (ArrayList) documentSnapshot.get("foodList");
                                            for (int j = 0; j < foodList.size(); j++) {
                                                HashMap food = (HashMap) foodList.get(j);
                                                foodSentence.append(food.get("name"));
                                                if (j < foodList.size() - 1) {
                                                    foodSentence.append(", ");
                                                }
                                            }
                                        }
            
                                        // Generating the exercise list sentence
                                        StringBuilder exerciseSentence = new StringBuilder("The exercise I have taken is ");
                                        for (DocumentSnapshot documentSnapshot : exerciseQuery.getDocuments()) {
                                            ArrayList exerciseList = (ArrayList) documentSnapshot.get("exerciseList");
                                            for (int j = 0; j < exerciseList.size(); j++) {
                                                HashMap exerciseData = (HashMap) exerciseList.get(j);
                                                exerciseSentence.append(exerciseData.get("name"));
                                                if (j < exerciseList.size() - 1) {
                                                    exerciseSentence.append(" and ");
                                                }
                                            }
                                        }
            
                                        // Calculate average daily calorie intake
                                        double totalDailyCalorieIntake = foodQuery.getDocuments().stream()
                                        .mapToDouble(dc -> dc.getDouble("dailyCalorie"))
                                        .average()
                                        .orElse(0);
            
                                        // Calculate average daily calorie consumption
                                        double totalDailyCalorieConsumption = exerciseQuery.getDocuments().stream()
                                        .mapToDouble(ec -> ec.getDouble("dailyCalorie"))
                                        .average()
                                        .orElse(0);
            
                                        // Generating the final complete sentence with average values
                                        String finalSentence = "I am a " + age + " years old " + gender + ", with " + height + " height, " + hip + " hip, " +
                                        waist + " waist, " + weight + " weight, having an average daily Calorie intake of " +
                                        totalDailyCalorieIntake + " and an average daily Calorie consumption of " +
                                        totalDailyCalorieConsumption + ". " + foodSentence.toString() + ". " +
                                        exerciseSentence.toString() + ". Could you provide me 3 health suggestions with 3 bullets (3 short sentences less than 12 words without other sentences)?";
                                        System.out.print(finalSentence);
                                        OpenAIDataSource openAIDataSource = new OpenAIDataSource();
                                        SuggestionService suggestionService = openAIDataSource.suggestionService;
                                        List<SuggestionService.ChatMessage> messages = new ArrayList<>();
                                        messages.add(new SuggestionService.ChatMessage("user", finalSentence));
                                        Suggestion suggest = suggestionService.getSuggestions(new SuggestionService.ReqBody("gpt-3.5-turbo", messages)).blockingFirst();
                                        List<Choice> choices = suggest.choices;
                                        String answer = choices.get(0).message.content;
                                        List<String> lines = Arrays.asList(answer.split("\\n"));
                                        for (int i = 0; i < lines.size(); i++) {
                                            lines.set(i,lines.get(i).replaceAll("^\\d+\\.\\s+", ""));
                                        }
                                        docRef.update("suggestions", lines);
                                        docRef.update("suggestionDate", currentDate);
                                        suggestion1 = root.findViewById(R.id.text_suggestion1);
                                        suggestion1.setText(lines.get(0));
                                        suggestion2 = root.findViewById(R.id.text_suggestion2);
                                        suggestion2.setText(lines.get(1));
                                        suggestion3 = root.findViewById(R.id.text_suggestion3);
                                        suggestion3.setText(lines.get(2));
                                    }
                                });
            
                        // Execute the combined task
                        combinedTask.addOnCompleteListener(task -> {
                            // Handle completion if needed
                        });
                    }
                }
            }
        });

        docRef.collection("calorieLogs").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                //is it possible to have an empty collection?
                                Date logDate = documentSnapshot.getTimestamp("date").toDate();
                                long daysDifference = isIn7Days(logDate);
                                // Check if the date is within 7 days
                                if (logDate != null && (daysDifference <= 7 && daysDifference >= 0)) {
                                    dailyCalorie.add(documentSnapshot.getDouble("dailyCalorie"));
                                    dailyCalorieTotal+=documentSnapshot.getDouble("dailyCalorie");
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    String formattedDate = dateFormat.format(logDate);
                                    dateEntries.add(formattedDate);
                                }
                            }
                            List<String> unsortedDate = new ArrayList<>();
                            unsortedDate.addAll(dateEntries);
                            // Sort the ArrayList
                            Collections.sort(dateEntries);

                            // Create a temporary ArrayList of doubles to store the sorted order
                            ArrayList<Double> sortedDoubleList = new ArrayList<>();

                            // Reorder the ArrayList of doubles based on the sorted order of strings
                            for (String str : dateEntries) {
                                int index = unsortedDate.indexOf(str); // Get the index of the string in the sorted list
                                sortedDoubleList.add(dailyCalorie.get(index)); // Add the corresponding double to the sorted list
                            }

                            // Update the original ArrayList of doubles with the sorted values
                            dailyCalorie.clear();
                            dailyCalorie.addAll(sortedDoubleList);
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
                            xAxis.setLabelCount(dateEntries.size());
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(dateEntries));
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                            // Customize the Y-axis
                            YAxis yAxis = barChart.getAxisLeft();
                            BarDataSet dataSet = new BarDataSet (calorieEntries, "Calorie Intake");
                            dataSet.setColor(Color.BLUE);
                            dataSet.setValueTextColor(Color.RED);
                            dataSet.setValueTextSize(12f);
                            BarData barData = new BarData(dataSet);
                            barChart.setData(barData);
                            barChart.invalidate();
                        }
                    }
                });
        //Add date to entries
        List<String> dateEntriesExercise = new ArrayList<>();
        docRef.collection("exerciseLogs").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                //is it possible to have an empty collection?
                                Date logDate = documentSnapshot.getTimestamp("date").toDate();
                                long daysDifference = isIn7Days(logDate);
                                // Check if the date is within 7 days
                                if (logDate != null && (daysDifference <= 7 && daysDifference >= 0)) {
                                    exerciseCalorie.add(documentSnapshot.getDouble("dailyCalorie"));
                                    exerciseCalorieTotal+=documentSnapshot.getDouble("dailyCalorie");
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    String formattedDate = dateFormat.format(logDate);
                                    dateEntriesExercise.add(formattedDate);
                                }
                            }
                            handleWeightForecast(db,root);
                            List<String> unsortedDate = new ArrayList<>();
                            unsortedDate.addAll(dateEntriesExercise);
                            // Sort the ArrayList
                            Collections.sort(dateEntriesExercise);

                            // Create a temporary ArrayList of doubles to store the sorted order
                            ArrayList<Double> sortedDoubleList = new ArrayList<>();

                            // Reorder the ArrayList of doubles based on the sorted order of strings
                            for (String str : dateEntriesExercise) {
                                int index = unsortedDate.indexOf(str); // Get the index of the string in the sorted list
                                sortedDoubleList.add(exerciseCalorie.get(index)); // Add the corresponding double to the sorted list
                            }

                            // Update the original ArrayList of doubles with the sorted values
                            exerciseCalorie.clear();
                            exerciseCalorie.addAll(sortedDoubleList);
                            // Add data to the chart
                            for (int i = 0; i < exerciseCalorie.size(); i++) {
                                exerciseEntries.add(new Entry(i,exerciseCalorie.get(i).floatValue()));
                            }
                            LineChart barChart = root.findViewById(R.id.barChartExercise);
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
                            xAxis.setLabelCount(dateEntriesExercise.size()+1);
                            xAxis.setDrawAxisLine(true);
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(dateEntriesExercise));
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                            // Customize the Y-axis
                            YAxis yAxis = barChart.getAxisLeft();
                            LineDataSet dataSet = new LineDataSet (exerciseEntries, "Exercise");
                            dataSet.setColor(Color.GREEN);
                            dataSet.setValueTextColor(Color.RED);
                            LineData barData = new LineData(dataSet);
                            barChart.setData(barData);
                            dataSet.setValueTextSize(12f);
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
            PostActivityHelper.startEditPostActivity(new Intent(getActivity(), EditPostActivity.class), getView(), getActivity());
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
    private long isIn7Days(Date date1) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        long timeDifferenceInMillis = Math.abs(cal2.getTimeInMillis()-cal1.getTimeInMillis());
        long daysDifference = timeDifferenceInMillis / (1000 * 60 * 60 * 24);
        return daysDifference;
    }

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

        return (year1 == year2 && month1 == month2 && day1 == day2);
    }

    private void handleWeightForecast(FirebaseFirestore db, View root) {
        // Use the updated variable here
        for (Double e:
             exerciseCalorie) {
            exerciseCalorieTotal+=e;
        }
        for (Double d:
                dailyCalorie) {
            dailyCalorieTotal+=d;
        }
        db.collection("users")
                .document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String gender = documentSnapshot.get("gender").toString();
                        double height = documentSnapshot.getDouble("height");
                        int age = documentSnapshot.getLong("age").intValue();
                        double weight = documentSnapshot.getDouble("weight");
                        double BMR = 0.0;
                        if(gender.equals("Male")){
                            BMR = 88.362 + (13.397 *weight)+(4.799*height)-(5.677*age);
                        } else if (gender.equals("Female")) {
                            BMR = 447.593 + (9.247 *weight)+(3.098*height)-(4.330*age);
                        }
                        double calorieLoss = BMR*7+exerciseCalorieTotal-dailyCalorieTotal;
                        double poundLoss = calorieLoss/3500;
                        TextView weightTrend = root.findViewById(R.id.weightTrend);
                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        if(calorieLoss >0) {
                            if(poundLoss > 2){
                                weightTrend.setText("You will lose "+decimalFormat.format(poundLoss)+" pound next week. You are losing too much weight! Remember to have a healthy diet!");
                            }else{
                                weightTrend.setText("You will lose "+decimalFormat.format(poundLoss)+" pound next week. Good job!");
                            }
                        }else{
                            weightTrend.setText("You will gain "+decimalFormat.format(poundLoss)+" pound next week. Don't worry! Keep your healthy lifestyle!");
                        }
                        System.out.println(poundLoss);
                    }
                });
        System.out.println("Updated variable: " + exerciseCalorieTotal);
        System.out.println("Updated variable: " + dailyCalorieTotal);
    }

}