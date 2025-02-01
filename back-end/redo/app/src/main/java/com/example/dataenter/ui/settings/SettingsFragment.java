package com.example.dataenter.ui.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.dataenter.R;
import com.example.dataenter.services.PackageNameEnum;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize ViewModel
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        // Set up Spinners
        Spinner startTimeSpinner = root.findViewById(R.id.start_time_spinner);
        Spinner endTimeSpinner = root.findViewById(R.id.end_time_spinner);
        Spinner intervalSpinner = root.findViewById(R.id.time_interval_spinner);
        Button saveButton = root.findViewById(R.id.save_button);

        setupTimeSpinners(startTimeSpinner, endTimeSpinner);
        setupIntervalSpinner(intervalSpinner);

        // Load previously saved preferences and set the spinners
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String savedStartTime = sharedPreferences.getString("startTime", "08:00"); // Default to "00:00"
        String savedEndTime = sharedPreferences.getString("endTime", "22:00"); // Default to "00:00"
        String savedInterval = sharedPreferences.getString("interval", "01:00"); // Default to "1 hour"

        setSpinnerSelection(startTimeSpinner, savedStartTime);
        setSpinnerSelection(endTimeSpinner, savedEndTime);
        setSpinnerSelection(intervalSpinner, savedInterval);

        saveButton.setOnClickListener(v -> {
            String startTime = startTimeSpinner.getSelectedItem().toString();
            String endTime = endTimeSpinner.getSelectedItem().toString();
            String interval = intervalSpinner.getSelectedItem().toString();

            // Save settings to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("startTime", startTime);
            editor.putString("endTime", endTime);
            editor.putString("interval", interval);
            editor.apply();

            Toast.makeText(requireContext(), "Settings saved!", Toast.LENGTH_SHORT).show();
            saveSettingsAndNavigate();
        });
        Button selectAppsButton = root.findViewById(R.id.select_apps_button);
        selectAppsButton.setOnClickListener(v -> showAppSelectionDialog());

        return root;
    }

    private void showAppSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Apps for Notifications");

        // Create a vertical LinearLayout to hold CheckBoxes
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 20);
        layout.setGravity(Gravity.CENTER);

        // Get the list of app names from the enum
        PackageNameEnum[] apps = PackageNameEnum.values();
        CheckBox[] checkBoxes = new CheckBox[apps.length];

        // SharedPreferences to load saved preferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);

        for (int i = 0; i < apps.length; i++) {
            CheckBox checkBox = new CheckBox(requireContext());
            checkBox.setText(apps[i].name());
            checkBox.setChecked(sharedPreferences.getBoolean(apps[i].name(), true));
            layout.addView(checkBox);
            checkBoxes[i] = checkBox;
        }

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for (int i = 0; i < apps.length; i++) {
                editor.putBoolean(apps[i].name(), checkBoxes[i].isChecked());
            }
            editor.apply();
            Toast.makeText(requireContext(), "App selections saved!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }


    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(value);
            if (position >= 0) {
                spinner.setSelection(position);
            }
        }
    }


    private void saveSettingsAndNavigate() {
        // Navigate back to Home
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_home);
    }



    private void setupTimeSpinners(Spinner startTimeSpinner, Spinner endTimeSpinner) {
        List<String> timeOptions = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            timeOptions.add(String.format("%02d:00", hour));
            timeOptions.add(String.format("%02d:30", hour));
        }

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                timeOptions
        );

        startTimeSpinner.setAdapter(timeAdapter);
        endTimeSpinner.setAdapter(timeAdapter);
    }

    private void setupIntervalSpinner(Spinner intervalSpinner) {
        List<String> intervalOptions = new ArrayList<>();
        intervalOptions.add("01:00");
        intervalOptions.add("02:00");

        ArrayAdapter<String> intervalAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                intervalOptions
        );

        intervalSpinner.setAdapter(intervalAdapter);
    }
}
