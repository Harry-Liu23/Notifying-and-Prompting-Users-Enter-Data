package com.example.dataenter.prompttools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.dataenter.services.UnlockService;
import com.example.dataenter.services.UnlockWorker;

public class UnlockReceiver extends BroadcastReceiver {

    private static final String TAG = "UnlockReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive called with action: " + intent.getAction());
        if (Intent.ACTION_USER_PRESENT.equals(intent.getAction()) || Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            Log.d(TAG, "Device unlocked or screen turned on");

            // Start UnlockService to handle notification and activity launch
//            WorkRequest workRequest = new OneTimeWorkRequest.Builder(UnlockWorker.class)
//                    .build();
//            WorkManager.getInstance(context).enqueue(workRequest);
            Intent serviceIntent = new Intent(context, UnlockService.class);
            context.startService(serviceIntent);
        } else {
            Log.d(TAG, "Received unknown action: " + intent.getAction());
        }
    }
}
