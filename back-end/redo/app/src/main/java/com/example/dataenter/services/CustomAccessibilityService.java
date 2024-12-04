package com.example.dataenter.services;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.NotificationManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.example.dataenter.R;

public class CustomAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Check the current package
        String packageName = event.getPackageName().toString();
        if ("com.google.android.youtube".equals(packageName)) {
            sendNotification();
        }
    }

    @Override
    public void onInterrupt() {
        // Required override
    }

    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, "CHANNEL_ID")
                    .setContentTitle("YouTube Detected")
                    .setContentText("You are using YouTube!")
                    .setSmallIcon(R.drawable.ic_notification)
                    .build();
        }

        notificationManager.notify(1, notification);
    }
}
