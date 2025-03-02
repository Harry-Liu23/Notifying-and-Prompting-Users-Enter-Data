package com.example.dataenter.prompttools;

import android.content.Context;
import android.content.SharedPreferences;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class UnlockTiming {
    private static final String TAG = "UnlockTiming";
    private Context context;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalMinutes;
    private static final String PREFS_NAME = "AppPreferences";
    private static final String LAST_NOTIFICATION_TIME = "LastNotificationTime";

    public UnlockTiming(Context context) {
        this.context = context;
        loadSettings();
    }

    private void loadSettings() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.startTime = LocalTime.parse(sharedPreferences.getString("startTime", "10:00"), DateTimeFormatter.ofPattern("HH:mm"));
        this.endTime = LocalTime.parse(sharedPreferences.getString("endTime", "22:00"), DateTimeFormatter.ofPattern("HH:mm"));
        String interval = sharedPreferences.getString("interval", "02:00");
        String[] parts = interval.split(":");
        this.intervalMinutes = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]); // Convert HH:mm to minutes
    }

    public LocalTime getNextNotificationTime(LocalTime currentTime) {
        LocalTime lastNotified = getLastNotificationTime();
        if (lastNotified == null || lastNotified.isBefore(startTime)) {
            return startTime;
        }

        // Calculate next interval time
        LocalTime nextTime = lastNotified.plusMinutes(intervalMinutes);
        if (nextTime.isAfter(endTime)) {
            return null; // No more notifications for the day
        }

        return nextTime;
    }

    public void saveLastNotificationTime(LocalTime time) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_NOTIFICATION_TIME, time.toString());
        editor.apply();
    }

    private LocalTime getLastNotificationTime() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String timeStr = sharedPreferences.getString(LAST_NOTIFICATION_TIME, null);
        return timeStr == null ? null : LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
    }
}
