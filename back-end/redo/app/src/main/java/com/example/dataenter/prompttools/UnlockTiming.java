package com.example.dataenter.prompttools;

import android.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UnlockTiming {
    private static final String TAG = "UnlockTiming";
    private String previousTime = getTime();
    private String userCustomPeriod; // Custom period in HH:mm format
    private String startTime;        // Start of the notification period
    private String endTime;          // End of the notification period
    private Boolean notifyCheck;

    public UnlockTiming(String userCustomPeriod, String startTime, String endTime) {
        this.userCustomPeriod = userCustomPeriod;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notifyCheck = false;
    }

    public void setCustomPeriod(int hours, int minutes) {
        this.userCustomPeriod = String.format("%02d:%02d", hours, minutes);
        Log.d(TAG, "Custom period set to: " + this.userCustomPeriod);
    }

    public UnlockTiming() {
        this("02:00", "00:00", "23:59");
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
            setNotifyCheck(false); // Reset if outside active hours
            return false;
        }

        if (previousTime == null) {
            Log.d(TAG, "First run. Setting previousTime to " + currentTime);
            previousTime = currentTime;
            setNotifyCheck(true);
            return true;
        }

        if (isTimeExceeded(currentTime)) {
            Log.d(TAG, "Time exceeded. Previous: " + previousTime + ", Current: " + currentTime);
            previousTime = currentTime; // Update the time for the next check
            setNotifyCheck(true);
        } else {
            Log.d(TAG, "Time not exceeded. Previous: " + previousTime + ", Current: " + currentTime);
        }

        return notifyCheck;
    }


    // Checks if the current time is within the active start and end times
    private boolean isWithinActiveHours(String currentTime) {
        return currentTime.compareTo(startTime) >= 0 && currentTime.compareTo(endTime) <= 0;
    }

    // Checks if the custom period has passed since the last notification
    private boolean isTimeExceeded(String currentTime) {
        int previousHour = Integer.parseInt(previousTime.substring(0, 2));
        int previousMinute = Integer.parseInt(previousTime.substring(3));
        int currentHour = Integer.parseInt(currentTime.substring(0, 2));
        int currentMinute = Integer.parseInt(currentTime.substring(3));
        int customHour = Integer.parseInt(userCustomPeriod.substring(0, 2));
        int customMinute = Integer.parseInt(userCustomPeriod.substring(3));

        int elapsedMinutes = (currentHour * 60 + currentMinute) - (previousHour * 60 + previousMinute);
        int requiredMinutes = customHour * 60 + customMinute;

        Log.d(TAG, "Previous time: " + previousTime + ", Current time: " + currentTime);
        Log.d(TAG, "Elapsed minutes: " + elapsedMinutes + ", Required minutes: " + requiredMinutes);

        return elapsedMinutes >= requiredMinutes;
    }

    // Setter for notifyCheck
    public void setNotifyCheck(Boolean boo) {
        this.notifyCheck = boo;
    }
}
