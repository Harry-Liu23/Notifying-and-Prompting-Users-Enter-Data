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

            if (!isValidTimeRange(startTime, endTime)) {
                Toast.makeText(requireContext(), "End time must be at least 8 hours after start time!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save settings to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("startTime", startTime);
            editor.putString("endTime", endTime);
            editor.putString("interval", interval);
            editor.apply();

            Toast.makeText(requireContext(), "Settings saved!", Toast.LENGTH_SHORT).show();
            saveSettingsAndNavigate();
        });

        return root;
    }

    private boolean isValidTimeRange(String startTime, String endTime) {
        int startHour = Integer.parseInt(startTime.split(":" )[0]);
        int endHour = Integer.parseInt(endTime.split(":" )[0]);

        return (endHour - startHour) >= 8 || (endHour + 24 - startHour) >= 8;
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
