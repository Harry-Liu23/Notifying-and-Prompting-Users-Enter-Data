package com.example.dataenter.services;

    /**
     * Enum representing popular app package names.
     */
    public enum PackageNameEnum {
        YOUTUBE("com.google.android.youtube"),
        INSTAGRAM("com.instagram.android"),
        TIKTOK("com.zhiliaoapp.musically"),
        AMAZON("com.amazon.mShop.android.shopping"),
        DISCORD("com.discord"),
        TWITTER("com.twitter.android"),
        REDnote("com.xingin.xhs"),
        SHEIN("com.zzkko"),
        FACEBOOK("com.facebook.katana");


        private String packageName;

        // Constructor for the enum
        PackageNameEnum(String packageName) {
            this.packageName = packageName;
        }

        public String getPackageName() {
            return packageName;
        }
    }
