package com.ece452.watfit.ui.dashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CircleDiagramView extends View {
    private int score;

    private Paint circlePaint;
    private Paint fillPaint;
    private Paint textPaint;
    private List<Double> dailyCalorie = new ArrayList<>();
    private List<Double> exerciseCalorie = new ArrayList<>();

    private static final int MAX_SCORE = 100;
    private static final double MAX_BMI = 30.0;
    private static final double MAX_WHR_MALE = 1.0;
    private static final double MAX_WHR_FEMALE = 0.9;

    public CircleDiagramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        fetchData();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.STROKE);

        fillPaint = new Paint();
        fillPaint.setColor(Color.YELLOW);
        fillPaint.setStyle(Paint.Style.STROKE);
        fillPaint.setStrokeWidth(80);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    private void fetchData() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Calculate the date 7 days ago
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date sevenDaysAgo = calendar.getTime();

        String uid = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(uid);

        // Fetch calorie logs within 7 days
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
                        dailyCalorie = extractCalorieLogs(results.get(0));
                        exerciseCalorie = extractCalorieLogs(results.get(1));

                        DocumentSnapshot userSnapshot = (DocumentSnapshot) results.get(2);
                        int age = userSnapshot.getDouble("age").intValue();
                        int height = userSnapshot.getDouble("height").intValue();
                        int hip = userSnapshot.getDouble("hip").intValue();
                        int waist = userSnapshot.getDouble("waist").intValue();
                        int weight = userSnapshot.getDouble("weight").intValue();
                        String gender = userSnapshot.getString("gender");

                        // Calculate the score based on the fetched data
                        calculateScore(dailyCalorie, exerciseCalorie, age, height, hip, waist, weight, gender);
                        setScore(score);
                    }
                });

        // Execute the combined task
        combinedTask.addOnCompleteListener(task -> {
            // Handle completion if needed
        });
    }

    private List<Double> extractCalorieLogs(Object result) {
        List<Double> calorieLogs = new ArrayList<>();
        if (result instanceof QuerySnapshot) {
            QuerySnapshot querySnapshot = (QuerySnapshot) result;
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                Double calorie = documentSnapshot.getDouble("dailyCalorie");
                if (calorie != null) {
                    calorieLogs.add(calorie);
                }
            }
        }
        return calorieLogs;
    }

    private void calculateScore(List<Double> dailyCalorie, List<Double> exerciseCalorie, int age, int height, int hip, int waist, int weight, String gender) {
        double calorieScore = calculateCalorieScore(dailyCalorie);
        double exerciseScore = calculateExerciseScore(exerciseCalorie, gender, weight);
        double bodyScore = calculateBodyScore(age, height, hip, waist, weight, gender);

        // You can customize the weights of each score to fit your specific criteria
        double totalScore = calorieScore + exerciseScore + bodyScore;

        // Normalize the total score to a value between 0 and 100
        double normalizedScore = (totalScore / (3 * MAX_SCORE)) * 100;

        score = (int) Math.round(normalizedScore);
    }

    private double calculateCalorieScore(List<Double> dailyCalorie) {
        // Calculate the average daily calorie intake
        double totalCalorie = 0;
        for (Double calorie : dailyCalorie) {
            totalCalorie += calorie;
        }
        double averageCalorie = totalCalorie / dailyCalorie.size();

        // Define thresholds for low and high calorie intake
        double lowThreshold = 1500;
        double highThreshold = 2500;

        // Calculate the score based on the average daily calorie intake
        if (averageCalorie < lowThreshold) {
            return 0;
        } else if (averageCalorie > highThreshold) {
            return MAX_SCORE;
        } else {
            return (averageCalorie - lowThreshold) / (highThreshold - lowThreshold) * MAX_SCORE;
        }
    }

    private double calculateExerciseScore(List<Double> exerciseCalorie, String gender, int weight) {
        // Calculate the total exercise calorie burned
        double totalCalorie = 0;
        for (Double calorie : exerciseCalorie) {
            totalCalorie += calorie;
        }

        // Define the target goals for exercise calorie burn based on gender and weight
        double targetGoalMale = weight * 20;     // Adjust the multiplier as needed
        double targetGoalFemale = weight * 18;   // Adjust the multiplier as needed

        // Calculate the percentage of exercise calories burned compared to the target goal
        double percentage;
        if (gender.equalsIgnoreCase("Male")) {
            percentage = (totalCalorie / targetGoalMale) * 100;
        } else {
            percentage = (totalCalorie / targetGoalFemale) * 100;
        }

        // Assign a score based on the percentage
        if (percentage >= 100) {
            return MAX_SCORE;
        } else {
            return (percentage / 100) * MAX_SCORE;
        }
    }

    private double calculateBodyScore(int age, int height, int hip, int waist, int weight, String gender) {
        // Calculate BMI score
        double bmi = calculateBMI(height, weight);
        double bmiScore = calculateBMIScore(bmi);

        // Calculate WHR score
        double whr = calculateWHR(hip, waist, gender);
        double whrScore = calculateWHRScore(whr, gender);

        // Calculate the total body score
        return (bmiScore + whrScore) / 2;
    }

    private double calculateBMI(int height, int weight) {
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }

    private double calculateBMIScore(double bmi) {
        if (bmi < MAX_BMI) {
            return MAX_SCORE;
        } else {
            return (1 - (bmi - MAX_BMI) / MAX_BMI) * MAX_SCORE;
        }
    }

    private double calculateWHR(int hip, int waist, String gender) {
        double whr = (double) waist / hip;
        if (gender.equalsIgnoreCase("Male")) {
            return whr;
        } else {
            return 1 / whr;
        }
    }

    private double calculateWHRScore(double whr, String gender) {
        double maxWhr = (gender.equalsIgnoreCase("Male")) ? MAX_WHR_MALE : MAX_WHR_FEMALE;
        if (whr <= maxWhr) {
            return MAX_SCORE;
        } else {
            return (1 - (whr - maxWhr) / maxWhr) * MAX_SCORE;
        }
    }

    public void setScore(int score) {
        this.score = score;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 100;

        float sweepAngle = score * 3.6f;

        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        float startAngle = -90;
        float endAngle = startAngle + sweepAngle;

        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, startAngle, sweepAngle, false, fillPaint);

        canvas.drawText(String.valueOf(score), centerX, centerY + textPaint.getTextSize() / 3, textPaint);
    }
}
