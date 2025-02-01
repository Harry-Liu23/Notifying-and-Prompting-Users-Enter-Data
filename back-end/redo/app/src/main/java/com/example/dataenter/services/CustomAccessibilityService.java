package com.example.dataenter.services;

import android.accessibilityservice.AccessibilityService;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.example.dataenter.prompttools.UnlockReceiver;

public class CustomAccessibilityService extends AccessibilityService {

    public static String triggeredBy = "no";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Check the current package
        String packageName = event.getPackageName().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        for (PackageNameEnum packageNameEnum : PackageNameEnum.values()) {
            if (packageName.equals(packageNameEnum.getPackageName()) && sharedPreferences.getBoolean(packageNameEnum.name(), true)) {
                // Pass the context to UnlockReceiver to initialize unlockTiming
                triggeredBy = packageName;

                UnlockReceiver unlockReceiver = new UnlockReceiver(this);
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
