package com.example.dataenter.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.core.app.NotificationCompat;

import com.example.dataenter.R;
import com.example.dataenter.dialogs.EnterDataActivity;

public class UnlockWorker extends Worker {

    private static final String TAG = "UnlockWorker";
    private static final String CHANNEL_ID = "UnlockReminderChannel";
    private static final int NOTIFICATION_ID = 1;

    public UnlockWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public Result doWork() {
        Log.d(TAG, "Sending notification in background...");

        // Send the notification
        sendUnlockNotification(getApplicationContext());

        // Return success after notification is sent
        return Result.success();
    }

    private void sendUnlockNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create Notification Channel (required for API level 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Unlock Reminder Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Channel for unlock reminders");
            notificationManager.createNotificationChannel(channel);
        }

        // Intent to open EnterDataActivity when the notification is clicked
        Intent intent = new Intent(context, EnterDataActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Create the notification
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Reminder")
                .setContentText("Tap to enter your data")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Set the PendingIntent to open EnterDataActivity
                .setAutoCancel(true) // Automatically removes the notification after being tapped
                .build();

        // Show the notification
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
