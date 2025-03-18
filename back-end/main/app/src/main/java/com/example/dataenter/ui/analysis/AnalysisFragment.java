package com.example.dataenter.ui.analysis;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dataenter.database.DatabaseHelper;
import com.example.dataenter.databinding.FragmentAnalysisBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AnalysisFragment extends Fragment {

    private FragmentAnalysisBinding binding;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnalysisBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        databaseHelper = new DatabaseHelper(requireContext());

        setupMoodChart();

        return root;
    }

    private void setupMoodChart() {
        LineChart lineChart = binding.moodChart;
        ArrayList<Entry> moodEntries = getMoodEntries();

        if (moodEntries.isEmpty()) {
            Log.d("Graph", "No mood data available to display.");
            lineChart.setNoDataText("No chart data available.");
            lineChart.invalidate();
            return; // No data, exit method
        }

        float averageDifference = calculateAverageMoodDifference(moodEntries);

        // Set message based on the mood variation
        String moodMessage = getMoodMessage(averageDifference);
        TextView moodMessageTextView = binding.moodMessage;
        moodMessageTextView.setText(moodMessage);

        LineDataSet dataSet = new LineDataSet(moodEntries, "Mood Trend");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.RED);
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(10f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // Configure X-Axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(4,true);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(1f);
        leftAxis.setAxisMaximum(5f);
        leftAxis.setLabelCount(5, true);

        // Refresh chart
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();

        float waterIntake = getWaterIntakeForToday();
        TextView waterIntakeMessageTextView = binding.waterIntakeMessage;
        Log.d("Analysis", String.valueOf(waterIntake));
        if (waterIntake < 1800) {
            waterIntakeMessageTextView.setText("You need to drink more water! Average adult needs 2 litres water a day!");
        } else {
            waterIntakeMessageTextView.setText("");
        }
    }

    private ArrayList<Entry> getMoodEntries() {
        ArrayList<Entry> moodEntries = new ArrayList<>();
        Cursor cursor = databaseHelper.getMoodDataLastThreeDays();

        Log.d("Graph", "Fetching mood data...");
        if (cursor.getCount() == 0) {
            Log.d("Graph", "No mood data found in the last 3 days.");
        }

        while (cursor.moveToNext()) {
            String mood = cursor.getString(0);
            String timeString = cursor.getString(1);
            Log.d("Graph", "Retrieved: Mood=" + mood + ", Time=" + timeString);

            int moodValue = convertMoodToNumber(mood);
            float timeValue = convertTimeToFloat(timeString);

            if (timeValue > 0) {
                moodEntries.add(new Entry(timeValue, moodValue));
            }
        }
        cursor.close();
        return moodEntries;
    }

    private int convertMoodToNumber(String mood) {
        switch (mood) {
            case "Very Sad": return 1;
            case "Sad": return 2;
            case "Neutral": return 3;
            case "Happy": return 4;
            case "Very Happy": return 5;
            default: return 3; // Default to Neutral if unknown
        }
    }

    private float convertTimeToFloat(String timeString) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = sdf.parse(timeString);
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                return hours + minutes / 60.0f;
            }
        } catch (Exception e) {
            Log.e("Graph", "Error parsing date: " + timeString, e);
        }
        return 0;
    }


    private float calculateAverageMoodDifference(ArrayList<Entry> moodEntries) {
        if (moodEntries.size() < 2) return 0;

        float totalDifference = 0;
        for (int i = 1; i < moodEntries.size(); i++) {
            totalDifference += Math.abs(moodEntries.get(i).getY() - moodEntries.get(i - 1).getY());
        }

        return totalDifference / (moodEntries.size() - 1);
    }

    private String getMoodMessage(float averageDifference) {
        // Calculate the average mood value
        float totalMood = 0;
        int count = 0;
        for (Entry entry : getMoodEntries()) {
            totalMood += entry.getY();
            count++;
        }
        float averageMood = totalMood / count;

        // Determine the message
        if(averageDifference>=3&&averageMood<=3){
            return "Your mood varies too much \n You're not so happy, maybe do something that would brings you joy";
        }
        if (averageDifference >= 3) {
            return "Your mood varies too much";
        } else if (averageMood <= 3) {
            return "You're not so happy, maybe do something that would bring joy to you";
        } else {
            return "You seems cheerful, enjoy!";
        }
    }

    private float getWaterIntakeForToday() {
        float totalWaterIntake = 0;
        Cursor cursor = databaseHelper.getWaterDataForToday();  // Method should query water intake for today

        if (cursor.getCount() == 0) {
            Log.d("Graph", "No water intake data for today.");
        } else {
            while (cursor.moveToNext()) {
                float waterAmount = cursor.getFloat(0);  // Assuming the water intake value is in the first column
                totalWaterIntake += waterAmount;
            }
        }
        cursor.close();
        return totalWaterIntake;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
