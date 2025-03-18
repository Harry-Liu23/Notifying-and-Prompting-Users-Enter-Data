package com.example.dataenter.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.example.dataenter.R;
import com.example.dataenter.prompttools.UnlockReceiver;

public class ForegroundService extends Service {
    private static final String TAG = "ForegroundService";
    private static final String CHANNEL_ID = "FOREGROUND_SERVICE_CHANNEL";
    private UnlockReceiver unlockReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Foreground service created");
        unlockReceiver = new UnlockReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Foreground service started");

        // Make the service a foreground service immediately
        startForegroundService();

        // Register the unlock receiver
        IntentFilter filter = new IntentFilter(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(unlockReceiver, filter);

        // Return START_STICKY to keep the service running until explicitly stopped
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Foreground service destroyed");

        // Unregister the unlock receiver
        unregisterReceiver(unlockReceiver);
    }

    private void startForegroundService() {
        Notification notification = createServiceNotification(this);
        startForeground(1, notification); // Start the service as a foreground service
    }

    private Notification createServiceNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            notificationManager.createNotificationChannel(channel);
        }

        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Unlock Notification Service")
                .setContentText("The service is running to detect screen unlocks")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
