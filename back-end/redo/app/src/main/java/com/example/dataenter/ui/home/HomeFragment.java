package com.example.dataenter.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dataenter.MainActivity;
import com.example.dataenter.database.SaveData;
import com.example.dataenter.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    public static int moodProgress = 2;
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Initialize ViewModel
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        // Inflate the layout using View Binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set text from ViewModel to TextView
        final TextView textView = binding.title;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        SeekBar moodSlider = binding.moodInput; // Assuming you set this in the layout XML
        moodSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                moodProgress = progress; // Save the progress for later use
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        binding.waterInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_NEXT) {
                binding.calorieInput.requestFocus();
                return true;
            }
            return false;
        });

        binding.calorieInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                binding.calorieInput.clearFocus();
                return true;
            }
            return false;
        });

        // Access SaveData instance from MainActivity
        MainActivity mainActivity = (MainActivity) requireActivity();
        SaveData saveData = mainActivity.getSaveData();

        // Set up Save button to call saveData logic
        binding.saveButton.setOnClickListener(v -> {
            if (saveData != null) {
                saveData.saveButton();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up binding reference to avoid memory leaks
        binding = null;
    }
}
