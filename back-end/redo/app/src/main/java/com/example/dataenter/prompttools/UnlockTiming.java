package com.example.dataenter.prompttools;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

public class UnlockTiming {
    private static final String TAG = "UnlockTiming";
    private Context context;
    private String userCustomPeriod; // Expected format "HH:mm", e.g., "10:00" for absolute time

    public UnlockTiming(Context context, String userCustomPeriod) {
        this.context = context;
        this.userCustomPeriod = userCustomPeriod;
    }

    /**
     * Checks if the current time is at or after the scheduled notification time (e.g., 10:00)
     * and if a notification hasn't been sent yet today.
     * If yes, updates the last notification time and returns true; otherwise, returns false.
     */
    public boolean unlockChecks() {
        Calendar now = Calendar.getInstance();
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int nowHour = now.get(Calendar.HOUR_OF_DAY);
        int nowMinute = now.get(Calendar.MINUTE);

        int targetHour;
        int targetMinute;
        try {
            String[] parts = userCustomPeriod.split(":");
            targetHour = Integer.parseInt(parts[0]);
            targetMinute = Integer.parseInt(parts[1]);
        } catch (Exception e) {
            Log.w(TAG, "Error parsing userCustomPeriod, defaulting to 10:00", e);
            targetHour = 10;
            targetMinute = 0;
        }

        // Check if current time is at or after the target time.
        if (nowHour > targetHour || (nowHour == targetHour && nowMinute >= targetMinute)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", MODE_PRIVATE);
            long lastNotificationMillis = sharedPreferences.getLong("lastNotificationTime", 0);
            Calendar lastNoti = Calendar.getInstance();
            lastNoti.setTimeInMillis(lastNotificationMillis);
            int lastDay = lastNoti.get(Calendar.DAY_OF_YEAR);
            // If no notification has been sent today, send one.
            if (lastNotificationMillis == 0 || lastDay != nowDay) {
                sharedPreferences.edit().putLong("lastNotificationTime", now.getTimeInMillis()).apply();
                return true;
            }
        }
        return false;
    }
}
