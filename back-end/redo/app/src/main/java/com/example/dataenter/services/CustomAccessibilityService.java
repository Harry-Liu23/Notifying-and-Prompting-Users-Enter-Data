package com.example.dataenter.services;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.example.dataenter.prompttools.UnlockReceiver;
import com.example.dataenter.prompttools.UnlockTiming;

public class CustomAccessibilityService extends AccessibilityService {

    UnlockReceiver unlockReceiver = new UnlockReceiver();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Check the current package
        String packageName = event.getPackageName().toString();
        Log.e("something:", packageName);
//        if ("com.google.android.youtube".equals(packageName)) {
//                unlockReceiver.showNotification(this);
//        }
        PackageNameEnum[] packageNameEnums = PackageNameEnum.values();
        for (PackageNameEnum packageNameEnum : packageNameEnums){
            UnlockTiming unlockTiming;
            if(packageName.equals(packageNameEnum.getPackageName()) && unlockReceiver.getUnlockTiming().unlockChecks()){
                unlockReceiver.showNotification(this);
                break;
            }
        }

    }

    @Override
    public void onInterrupt() {
        // Required override
    }

}
