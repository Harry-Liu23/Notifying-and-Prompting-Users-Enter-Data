package com.example.dataenter;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.dataenter.database.SaveData;
import com.example.dataenter.prompttools.UnlockReceiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.dataenter.databinding.ActivityMainBinding;
import com.example.dataenter.services.ForegroundService;
import com.example.dataenter.services.UnlockService;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 101;
    private UnlockReceiver unlockReceiver;
    private SaveData saveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize unlock receiver
        unlockReceiver = new UnlockReceiver();
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        }
//        unlockReceiver.onReceive(this,serviceIntent);

        // Initialize SaveData
        if (saveData == null) {
            saveData = new SaveData(this);
        }
        saveData = new SaveData(this);

        // Navigation setup
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_record, R.id.navigation_analysis)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Request notification permission
        requestNotificationPermission();
    }

    /**
     * Getter method for SaveData instance.
     * Allows fragments like HomeFragment to access SaveData.
     */
    public SaveData getSaveData() {
        return saveData;
    }

    private void requestNotificationPermission() {
        // Check if notification permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the unlock receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(unlockReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the unlock receiver
        unregisterReceiver(unlockReceiver);
    }

    public void startForegroundService() {
        Intent serviceIntent = new Intent(this, UnlockService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent); // Start the service as a foreground service
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Perform cleanup if needed
        saveData = null; // Optional: Clear the SaveData reference if necessary
    }
}
