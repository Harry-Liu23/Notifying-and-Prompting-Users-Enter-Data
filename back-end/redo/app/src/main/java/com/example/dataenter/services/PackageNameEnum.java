package com.example.dataenter.services;

import java.util.EnumSet;


    /**
     * Enum representing popular app package names.
     */
    public enum PackageNameEnum {
        YOUTUBE("com.google.android.youtube"),
        INSTAGRAM("com.instagram.android"),
        WHATSAPP("com.whatsapp"),
        TIKTOK("com.zhiliaoapp.musically"),
        AMAZON("com.amazon.mShop.android.shopping");

        private String packageName;

        // Constructor for the enum
        PackageNameEnum(String packageName) {
            this.packageName = packageName;
        }

        public String getPackageName() {
            return packageName;
        }
    }
