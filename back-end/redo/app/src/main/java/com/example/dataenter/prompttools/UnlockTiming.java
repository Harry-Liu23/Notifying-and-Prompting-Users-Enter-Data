package com.example.dataenter.prompttools;

import android.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UnlockTiming {
    private static final String TAG = "UnlockTiming";
    private String previousIntervalEnd = null;
    private String userCustomPeriod; // Custom period in HH:mm format
    private String startTime;        // Start of the notification period
    private String endTime;          // End of the notification period

    public UnlockTiming(String userCustomPeriod, String startTime, String endTime) {
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

        if (!isWithinActiveHours(currentTime)) {
            Log.d(TAG, "Outside active hours. Start: " + startTime + ", End: " + endTime);
            return false;
        }

        String currentIntervalEnd = getNextIntervalEnd(currentTime);

        if (previousIntervalEnd == null || currentTime.compareTo(previousIntervalEnd) >= 0) {
            Log.d(TAG, "New interval reached. Previous interval end: " + previousIntervalEnd);
            previousIntervalEnd = currentIntervalEnd;
            return true;
        } else {
            Log.d(TAG, "Current time still within the last interval.");
            return false;
        }
    }


    // Checks if the current time is within the active start and end times
    private boolean isWithinActiveHours(String currentTime) {
        return currentTime.compareTo(startTime) >= 0 && currentTime.compareTo(endTime) <= 0;
    }

    // Checks if the custom period has passed since the last notification
    private String getNextIntervalEnd(String currentTime) {
        int currentHour = Integer.parseInt(currentTime.substring(0, 2));
        int currentMinute = Integer.parseInt(currentTime.substring(3));
        int customHour = Integer.parseInt(userCustomPeriod.substring(0, 2));
        int customMinute = Integer.parseInt(userCustomPeriod.substring(3));

//      Time interval in minutes
        int intervalMinutes = customHour * 60 + customMinute;
//      Current time in mins
        int totalMinutes = currentHour * 60 + currentMinute;

        // Find the next interval end time after the start time
        int activeStartMinutes = Integer.parseInt(startTime.substring(0, 2)) * 60
                + Integer.parseInt(startTime.substring(3));
        int intervalOffset = ((totalMinutes - activeStartMinutes) / intervalMinutes + 1) * intervalMinutes;

        int nextIntervalMinutes = activeStartMinutes + intervalOffset;

        int nextHour = (nextIntervalMinutes / 60) % 24; // Handle wrapping around midnight
        int nextMinute = nextIntervalMinutes % 60;

        return String.format(Locale.UK, "%02d:%02d", nextHour, nextMinute);
    }
}
