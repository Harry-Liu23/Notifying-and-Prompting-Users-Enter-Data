package com.example.dataenter.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dataenter.MainActivity;
import com.example.dataenter.R;
import com.example.dataenter.database.DatabaseHelper;

public class EnterDataActivity extends Activity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the DatabaseHelper instance
        databaseHelper = new DatabaseHelper(this);

        // Show the dialog
        showEnterDataDialog();
    }

    private void showEnterDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_dataenter, null);
        builder.setView(dialogView);

        // Initialize dialog components
        EditText etMood = dialogView.findViewById(R.id.et_mood);
        EditText etWaterIntake = dialogView.findViewById(R.id.et_water_intake);
        EditText etNothing = dialogView.findViewById(R.id.et_nothing);

        Button btnEnterData = dialogView.findViewById(R.id.btn_enter_data);
        Button btnOpenMain = dialogView.findViewById(R.id.btn_open_main);

        AlertDialog dialog = builder.create();

        // Button to enter data (close dialog)
        btnEnterData.setOnClickListener(v -> {
            String mood = etMood.getText().toString().trim();
            String water = etWaterIntake.getText().toString().trim();
            String other = etNothing.getText().toString().trim();

            if (mood.isEmpty() || water.isEmpty() || other.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean isInserted = databaseHelper.insertRecord(mood, water, other);
                if (isInserted) {
                    Toast.makeText(this, "Record saved successfully", Toast.LENGTH_SHORT).show();
                    etMood.setText("");
                    etWaterIntake.setText("");
                    etNothing.setText("");
                } else {
                    Toast.makeText(this, "Failed to save record", Toast.LENGTH_SHORT).show();
                }
            }
            dialog.dismiss();
        });

        // Button to open main activity
        btnOpenMain.setOnClickListener(v -> {
            Intent mainIntent = new Intent(EnterDataActivity.this, MainActivity.class);
            startActivity(mainIntent);
            dialog.dismiss();
        });

        dialog.setOnDismissListener(dialogInterface -> finish()); // Close activity when dialog is dismissed
        dialog.show();
    }
}
