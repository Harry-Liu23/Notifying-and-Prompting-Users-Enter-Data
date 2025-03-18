package com.example.dataenter.database;

import static com.example.dataenter.services.CustomAccessibilityService.triggeredBy;
import static com.example.dataenter.ui.home.HomeFragment.moodProgress;

import com.example.dataenter.MainActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dataenter.R;

public class SaveData {

    private final MainActivity mainActivity;
    private final DatabaseHelper databaseHelper;

    public SaveData(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.databaseHelper = new DatabaseHelper(mainActivity);
    }

    public void saveButton() {
            EditText waterInput = mainActivity.findViewById(R.id.water_input);
            EditText calorieInput = mainActivity.findViewById(R.id.calorie_input);
            Button saveButton = mainActivity.findViewById(R.id.save_button);

            saveButton.setOnClickListener(view -> {
                String mood = getMoodText(moodProgress);
                String water = waterInput.getText().toString().trim();
                String calorie = calorieInput.getText().toString().trim();

                if (mood.isEmpty() || water.isEmpty() || calorie.isEmpty()) {
                    Toast.makeText(mainActivity, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = databaseHelper.insertRecord(mood, water, calorie);
                    if (isInserted) {
                        Toast.makeText(mainActivity, "Record saved successfully", Toast.LENGTH_SHORT).show();
                        waterInput.setText("");
                        calorieInput.setText("");
                        triggeredBy = "no";
                    } else {
                        Toast.makeText(mainActivity, "Failed to save record", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    public static String getMoodText(int progress) {
        switch (progress) {
            case 0: return "Very Sad";
            case 1: return "Sad";
            case 2: return "Neutral";
            case 3: return "Happy";
            case 4: return "Very Happy";
            default: return "Unknown";
        }
    }

}
