package com.example.dataenter.prompttools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.time.LocalTime;
import java.util.Calendar;

public class NotificationScheduler {
    private static final String TAG = "NotificationScheduler";

    public static void scheduleNextNotification(Context context) {
        UnlockTiming unlockTiming = new UnlockTiming(context);
        LocalTime nextNotificationTime = unlockTiming.getNextNotificationTime(LocalTime.now());

        if (nextNotificationTime == null) {
            Log.d(TAG, "No more notifications for today.");
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, nextNotificationTime.getHour());
        calendar.set(Calendar.MINUTE, nextNotificationTime.getMinute());
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, UnlockReceiver.class);
        intent.setAction("android.intent.action.NOTIFY_USER");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
            unlockTiming.saveLastNotificationTime(nextNotificationTime);
            Log.d(TAG, "Scheduled next notification at " + nextNotificationTime);
        }
    }
}
