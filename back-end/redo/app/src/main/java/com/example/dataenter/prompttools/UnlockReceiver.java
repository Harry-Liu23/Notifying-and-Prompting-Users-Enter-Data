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

public class UnlockReceiver extends BroadcastReceiver {

    private static final String TAG = "UnlockReceiver";
    private UnlockTiming unlockTiming;

    public UnlockReceiver() {
        // Default constructor
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
            Log.d(TAG, "User unlocked the screen");

            // Initialize unlockTiming if not already done.
            if (unlockTiming == null) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
                String scheduledTime = sharedPreferences.getString("interval", "10:00"); // Using "10:00" as the default scheduled time.
                unlockTiming = new UnlockTiming(context, scheduledTime);
            }

            if (unlockTiming.unlockChecks()) {
                Log.d(TAG, "Scheduled time reached; sending notification");
                showNotification(context);
            } else {
                Log.d(TAG, "Notification not sent: scheduled time not reached or already sent today");
            }
        }
    }

    public void showNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create Notification Channel for API 26+.
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

        notificationManager.notify(2, notification);
    }
}
