package com.example.dataenter.prompttools;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.dataenter.R;
import com.example.dataenter.dialogs.EnterDataActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class UnlockReceiver extends BroadcastReceiver {

    private static final String TAG = "UnlockReceiver";
    public UnlockTiming unlockTiming;
    public static String lastNotificationTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            .format(System.currentTimeMillis());

    public UnlockReceiver() {
        // Default constructor
    }

    public UnlockReceiver(Context context) {
        // Initialize UnlockTiming using SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String interval = sharedPreferences.getString("interval", "02:00"); // Default 2 hours
        String startTime = sharedPreferences.getString("startTime", "08:00"); // Default 8:00
        String endTime = sharedPreferences.getString("endTime", "22:00"); // Default 22:00
        this.unlockTiming = new UnlockTiming(context, interval, startTime, endTime);
    }

    public UnlockTiming getUnlockTiming() {
        return unlockTiming;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
            Log.d(TAG, "User unlocked the screen");


            // Load settings from SharedPreferences
            if (unlockTiming == null) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
                String interval = sharedPreferences.getString("interval", "02:00"); // Default 2 hours
                String startTime = sharedPreferences.getString("startTime", "08:00"); // Default 8:00
                String endTime = sharedPreferences.getString("endTime", "22:00"); // Default 22:00
                unlockTiming = new UnlockTiming(context, interval, startTime, endTime);
            }
            if (unlockTiming.unlockChecks()) {
                Log.d(TAG, "Notification allowed within the time constraints");
                showNotification(context);
            } else {
                Log.d(TAG, "Notification skipped due to time constraints");
            }
        }
    }

    public void showNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create Notification Channel (API 26+)
        String CHANNEL_ID = "UNLOCK_NOTIFICATION_CHANNEL";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Unlock Reminder Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, EnterDataActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Reminder")
                .setContentText("Tap to enter your data")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        lastNotificationTime = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault())
                .format(System.currentTimeMillis());

        notificationManager.notify(2, notification);
    }
}
