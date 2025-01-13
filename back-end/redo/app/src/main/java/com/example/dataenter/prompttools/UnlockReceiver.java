package com.example.dataenter.prompttools;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.dataenter.R;
import com.example.dataenter.dialogs.EnterDataActivity;

public class UnlockReceiver extends BroadcastReceiver {

    private static final String TAG = "UnlockReceiver";
    public UnlockTiming unlockTiming;

    public UnlockReceiver() {
        // Initialize UnlockTiming with default values or customize as needed
        unlockTiming = new UnlockTiming("02:00", "08:00", "22:00"); // Default: 2 hours, active 8 AM to 10 PM
    }

    public UnlockTiming getUnlockTiming() {
        return unlockTiming;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
            Log.d(TAG, "User unlocked the screen");

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

        notificationManager.notify(2, notification);
    }
}
