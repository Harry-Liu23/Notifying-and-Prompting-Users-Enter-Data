package com.example.dataenter.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dataenter.R;

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

        setupTimeSpinners(startTimeSpinner, endTimeSpinner);
        setupIntervalSpinner(intervalSpinner);

        return root;
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
        intervalOptions.add("1 hour");
        intervalOptions.add("2 hours");

        ArrayAdapter<String> intervalAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                intervalOptions
        );

        intervalSpinner.setAdapter(intervalAdapter);
    }
}
