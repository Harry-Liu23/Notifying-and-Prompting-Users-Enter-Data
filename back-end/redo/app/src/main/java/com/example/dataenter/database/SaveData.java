package com.example.dataenter.database;

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
        this.databaseHelper = new DatabaseHelper(mainActivity); // Initialize DatabaseHelper once
    }

    public void saveButton() {
            EditText moodInput = mainActivity.findViewById(R.id.mood_input);
            EditText waterInput = mainActivity.findViewById(R.id.water_input);
            EditText otherInput = mainActivity.findViewById(R.id.calorie_input);
            Button saveButton = mainActivity.findViewById(R.id.save_button);

            saveButton.setOnClickListener(view -> {
                String mood = moodInput.getText().toString().trim();
                String water = waterInput.getText().toString().trim();
                String other = otherInput.getText().toString().trim();

                if (mood.isEmpty() || water.isEmpty() || other.isEmpty()) {
                    Toast.makeText(mainActivity, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = databaseHelper.insertRecord(mood, water, other);
                    if (isInserted) {
                        Toast.makeText(mainActivity, "Record saved successfully", Toast.LENGTH_SHORT).show();
                        moodInput.setText("");
                        waterInput.setText("");
                        otherInput.setText("");
                    } else {
                        Toast.makeText(mainActivity, "Failed to save record", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
