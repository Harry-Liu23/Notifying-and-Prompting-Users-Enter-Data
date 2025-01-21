package com.example.dataenter.services;

import android.accessibilityservice.AccessibilityService;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.example.dataenter.prompttools.UnlockReceiver;
import com.example.dataenter.prompttools.UnlockTiming;

public class CustomAccessibilityService extends AccessibilityService {

    UnlockReceiver unlockReceiver = new UnlockReceiver();
    public static String triggeredBy = "no";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Check the current package
        String packageName = event.getPackageName().toString();
        Log.e("something:", packageName);
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        for (PackageNameEnum packageNameEnum : PackageNameEnum.values()){
            if(packageName.equals(packageNameEnum.getPackageName()) && sharedPreferences.getBoolean(packageNameEnum.name(), true)) {
                UnlockReceiver unlockReceiver = new UnlockReceiver();
                if (unlockReceiver.getUnlockTiming().unlockChecks()) {
                    unlockReceiver.showNotification(this);
                }
                break;
            }
        }

    }

    @Override
    public void onInterrupt() {
        // Required override
    }

}
