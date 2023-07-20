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

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private List<Double> dailyCalorie = new ArrayList<>();
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
        List<String> dateEntries = new ArrayList<>();

        List<String> eatSuggestions = new ArrayList<>(Arrays.asList(
            "Try incorporating more fruits and vegetables into your meals for added nutrients.",
            "Experiment with different spices and herbs to enhance the flavors of your dishes.",
            "Include a source of protein in each of your meals, such as lean meats, tofu, or legumes.",
            "Opt for whole grain options like brown rice or whole wheat bread for added fiber.",
            "Don't forget to stay hydrated by drinking plenty of water throughout the day.",
            "Try to limit processed foods and opt for fresh, whole foods instead.",
            "Consider trying a new recipe or cooking technique to keep meals exciting.",
            "Include healthy fats in your diet, such as avocados, nuts, and olive oil.",
            "Balance your plate with a variety of colors by including different types of fruits and vegetables.",
            "Practice portion control to ensure you're eating an appropriate amount for your needs.",
            "Enjoy a balanced breakfast to kickstart your day with energy and nutrients.",
            "Pack nutritious snacks like cut-up veggies or trail mix to have on hand when hunger strikes.",
            "Try to limit sugary drinks and opt for water, herbal tea, or infused water instead.",
            "Don't skip meals, as it can lead to overeating later on.",
            "Listen to your body's hunger and fullness cues to guide your eating habits.",
            "Include probiotic-rich foods like yogurt or sauerkraut to support gut health.",
            "Plan and prepare your meals ahead of time to avoid unhealthy food choices on busy days.",
            "Choose lean sources of protein, such as fish or skinless poultry, for heart-healthy options.",
            "Enjoy a variety of different cuisines to explore new flavors and ingredients.",
            "Engage in mindful eating by savoring each bite and paying attention to your body's signals.",
            "Opt for homemade meals whenever possible, so you have control over the ingredients used.",
            "Try to include a source of calcium, such as dairy products or fortified plant-based alternatives.",
            "Consider reducing your sodium intake by seasoning your meals with herbs and spices instead.",
            "Include high-fiber foods like legumes, whole grains, and vegetables to support digestion.",
            "Experiment with meatless meals by incorporating plant-based proteins like tofu or lentils.",
            "Enjoy occasional treats in moderation to satisfy cravings without overindulging.",
            "Stay mindful of your eating environment by sitting down at a table and minimizing distractions.",
            "Incorporate healthy snacks like fresh fruit, nuts, or yogurt for a balanced diet.",
            "Practice gratitude for the food you eat and appreciate the nourishment it provides.",
            "Consult with a registered dietitian for personalized dietary recommendations and guidance."
        ));
        List<String> exerciseSuggestions = new ArrayList<>(Arrays.asList(
            "Embrace the joy of movement and dance your way to fitness.",
            "Discover the exhilarating world of outdoor cycling for a refreshing workout.",
            "Strengthen your core with invigorating Pilates sessions.",
            "Engage in uplifting group workouts to foster a sense of community.",
            "Unleash your inner athlete with a challenging HIIT routine.",
            "Find solace in serene yoga sessions that nurture your mind and body.",
            "Take a plunge into the pool for a refreshing and low-impact exercise experience.",
            "Power up your day with an energizing morning jog or run.",
            "Embrace the art of calisthenics to sculpt and tone your body.",
            "Challenge yourself with martial arts for a dynamic and empowering workout.",
            "Channel your inner child and jump rope your way to fitness and fun.",
            "Explore the beauty of nature through hiking for a revitalizing workout.",
            "Push your limits and conquer obstacles with thrilling obstacle course training.",
            "Engage in mindfulness and strengthen your muscles with Tai Chi.",
            "Get your heart pumping and body moving with exhilarating Zumba classes.",
            "Bask in the endorphin rush of a high-energy kickboxing workout.",
            "Boost your flexibility and balance with the graceful practice of ballet-inspired exercises.",
            "Discover the benefits of meditation and movement with gentle Tai Chi flows.",
            "Indulge in the freedom of aerial yoga and find tranquility while suspended in the air.",
            "Find your rhythm and groove with rhythmic gymnastics-inspired workouts.",
            "Experience the thrill of rock climbing and challenge your strength and agility.",
            "Tone and sculpt your body with the power of barre workouts.",
            "Dive into the world of aquatic fitness and enjoy a refreshing and joint-friendly exercise.",
            "Embrace the uplifting beats of a cardio dance class to get your heart rate up.",
            "Break a sweat and boost your endurance with a high-intensity indoor cycling class.",
            "Unleash your inner yogi with a challenging and empowering hot yoga session.",
            "Rejuvenate your mind and body with a peaceful and meditative nature walk.",
            "Strengthen your muscles and improve your posture with Pilates reformer workouts.",
            "Embrace the serenity of paddleboarding and work your core while connecting with nature.",
            "Get your groove on and burn calories with fun-filled dance workouts like salsa or hip-hop."
        ));
        ArrayList<String> healthySuggestions = new ArrayList<>(Arrays.asList(
            "Embrace the joy of fresh fruits and vegetables.",
            "Engage in invigorating outdoor activities for a vibrant lifestyle.",
            "Prioritize a good night's sleep for optimal well-being.",
            "Savor the flavors of wholesome, homemade meals.",
            "Cultivate mindfulness and find peace in the present moment.",
            "Stay hydrated and let water be your refreshing companion.",
            "Enjoy the benefits of regular exercise and feel the energy surge.",
            "Discover new recipes that nourish your body and tantalize your taste buds.",
            "Surround yourself with positive, supportive people who uplift your spirits.",
            "Take time to relax and rejuvenate through self-care activities.",
            "Explore the world of herbal teas for soothing and healing experiences.",
            "Practice gratitude daily to foster a positive mindset.",
            "Challenge yourself to try new, nutritious foods and expand your palate.",
            "Breathe deeply and feel the tranquility that flows within.",
            "Engage in creative endeavors that bring you joy and fulfillment.",
            "Share laughter with loved ones and embrace the healing power of humor.",
            "Step outside and bask in the warm embrace of sunlight.",
            "Find a physical activity you love and make it a part of your routine.",
            "Nourish your mind with books, podcasts, and stimulating conversations.",
            "Indulge in the sweetness of life while maintaining balance.",
            "Take breaks from screens and reconnect with the world around you.",
            "Set achievable goals and celebrate every milestone along the way.",
            "Explore different forms of meditation to find inner peace.",
            "Connect with nature and find solace in its beauty.",
            "Practice acts of kindness and experience the joy of giving.",
            "Embrace the power of positive affirmations to uplift your spirits.",
            "Seek professional help and guidance when needed for holistic well-being.",
            "Engage in hobbies that challenge your mind and spark your curiosity.",
            "Maintain a clean and organized living space for a calm and focused mind.",
            "Embrace a healthy work-life balance and prioritize self-care."
        ));
        Random random = new Random();
        suggestion1 = root.findViewById(R.id.text_suggestion1);
        int randomNumber = random.nextInt(30);
        suggestion1.setText(eatSuggestions.get(randomNumber));
        suggestion2 = root.findViewById(R.id.text_suggestion2);
        randomNumber = random.nextInt(30);
        suggestion2.setText(exerciseSuggestions.get(randomNumber));
        suggestion3 = root.findViewById(R.id.text_suggestion3);
        randomNumber = random.nextInt(30);
        suggestion3.setText(healthySuggestions.get(randomNumber));

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
                                    dailyCalorieTotal+=documentSnapshot.getDouble("dailyCalorie");
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
                                if (logDate != null && isSameDate(logDate)) {
                                    exerciseCalorie.add(documentSnapshot.getDouble("dailyCalorie"));
//                                    exerciseCalorieTotal+=documentSnapshot.getDouble("dailyCalorie");
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    String formattedDate = dateFormat.format(logDate);
                                    dateEntriesExercise.add(formattedDate);
                                }
                            }
                            handleWeightForecast(db,root);
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
                            xAxis.setLabelCount(dateEntriesExercise.size());
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