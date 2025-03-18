package com.example.dataenter.prompttools;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UnlockTiming {
    private static final String TAG = "UnlockTiming";
    private String previousIntervalEnd;
    private Context context;
    private String userCustomPeriod; // Custom period in HH:mm format
    private String startTime;        // Start of the notification period
    private String endTime;          // End of the notification period
    private static final String KEY_PREVIOUS_INTERVAL_END = "PreviousIntervalEnd";

    public UnlockTiming(Context context, String userCustomPeriod, String startTime, String endTime) {
        this.context = context;
        this.userCustomPeriod = userCustomPeriod;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Gets the current time in HH:mm format
    public String getTime() {
        LocalDateTime now = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return now.format(formatter);}
        Log.w(TAG, "Unable to retrieve current time");
        return null;
    }

    // Checks if notifications can be sent based on the period and start/end times
    public boolean unlockChecks() {
        String currentTime = getTime();
        Log.d(TAG, "Checking unlock constraints at " + currentTime);
        Log.d(TAG, "Start: " + startTime+ ", End: " + endTime);
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", MODE_PRIVATE);

        if (!isWithinActiveHours(currentTime)) {
            Log.d(TAG, "Outside active hours. Start: " + startTime + ", End: " + endTime);
            if (previousIntervalEnd == null || currentTime.compareTo(previousIntervalEnd) >= 0 || isNewDay(currentTime)) {
                sharedPreferences.edit().putString(KEY_PREVIOUS_INTERVAL_END, null).apply();
                previousIntervalEnd = null;
            }
            return false;
        }

        previousIntervalEnd = sharedPreferences.getString(KEY_PREVIOUS_INTERVAL_END, null);

        // If this is the first time or the current time is beyond the last interval end
        if (previousIntervalEnd == null || currentTime.compareTo(previousIntervalEnd) >= 0) {
            String currentIntervalEnd = getNextIntervalEnd(currentTime);
            Log.d(TAG, "New interval reached. Previous interval end: "
                    + previousIntervalEnd + ", Next interval end: "
                    + currentIntervalEnd);
            sharedPreferences.edit().putString(KEY_PREVIOUS_INTERVAL_END, currentIntervalEnd).apply();
            previousIntervalEnd = currentIntervalEnd;
            return true; // Allow notification
        } else {
            Log.d(TAG, "Previous interval: "
                    + previousIntervalEnd );
            Log.d(TAG, "Current time still within the last interval. No notification sent.");
            return false; // Block notification
        }
    }

    private boolean isNewDay(String currentTime) {
        return currentTime.compareTo("00:00") >= 0 && (previousIntervalEnd != null && previousIntervalEnd.compareTo("23:59") <= 0);
    }

    // Checks if the current time is within the active start and end times
    private boolean isWithinActiveHours(String currentTime) {
        if (startTime.compareTo(endTime) <= 0) {
            return currentTime.compareTo(startTime) >= 0 && currentTime.compareTo(endTime) < 0;
        } else {
            // Active hours span across midnight
            return currentTime.compareTo(startTime) >= 0 || currentTime.compareTo(endTime) < 0;
        }
    }

    private String getNextIntervalEnd(String currentTime) {
        Log.d(TAG, "userCustomPeriod"+userCustomPeriod);
        int currentHour = Integer.parseInt(currentTime.substring(0, 2));
        int currentMinute = Integer.parseInt(currentTime.substring(3));
        int customHour = Integer.parseInt(userCustomPeriod.substring(0, 2));
        int customMinute = Integer.parseInt(userCustomPeriod.substring(3));

        // Time interval in minutes
        int intervalMinutes = customHour * 60 + customMinute;

        // Current time in minutes
        int totalMinutes = currentHour * 60 + currentMinute;

        // Calculate the next interval boundary
        int nextIntervalMinutes = ((totalMinutes / intervalMinutes) + 1) * intervalMinutes;

        int nextHour = (nextIntervalMinutes / 60) % 24; // Handle wrapping around midnight
        int nextMinute = nextIntervalMinutes % 60;

        return String.format(Locale.UK, "%02d:%02d", nextHour, nextMinute);
    }

}
