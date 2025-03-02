package com.example.dataenter.prompttools;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.dataenter.R;
import com.example.dataenter.dialogs.EnterDataActivity;

public class UnlockReceiver extends BroadcastReceiver {
    private static final String TAG = "UnlockReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Scheduled notification triggered.");

        // Ensure this receiver was triggered by the alarm, not something else
        if (!intent.getAction().equals("android.intent.action.NOTIFY_USER")) {
            Log.d(TAG, "Ignoring non-scheduled event.");
            return;
        }

        showNotification(context);
        NotificationScheduler.scheduleNextNotification(context);
    }

    private void showNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "TIMELY_NOTIFICATION_CHANNEL";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Timely Reminder Notifications", NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, EnterDataActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
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
