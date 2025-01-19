package com.example.dataenter.database;

import static com.example.dataenter.prompttools.UnlockReceiver.lastNotificationTime;
import static com.example.dataenter.services.CustomAccessibilityService.triggeredBy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "records.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "records";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MOOD = "mood";
    public static final String COLUMN_WATER = "water";
    public static final String COLUMN_CALORIE = "calorie";
    public static final String COLUMN_DATA_ENTRY_TIME = "data_entry_time";
    public static final String COLUMN_NOTIFICATION_TIME = "notification_time";
    public static final String COLUMN_TRIGGERED_BY = "triggered_by";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MOOD + " TEXT, " +
                COLUMN_WATER + " TEXT, " +
                COLUMN_CALORIE + " TEXT," +
                COLUMN_DATA_ENTRY_TIME + "TEXT," +
                COLUMN_NOTIFICATION_TIME + " TEXT,"+
                COLUMN_TRIGGERED_BY + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertRecord(String mood, String water, String calorie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MOOD, mood);
        contentValues.put(COLUMN_WATER, water);
        contentValues.put(COLUMN_CALORIE, calorie);

        String entryTime = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis());
        contentValues.put(COLUMN_DATA_ENTRY_TIME, entryTime);
        contentValues.put(COLUMN_NOTIFICATION_TIME, lastNotificationTime);
        contentValues.put(COLUMN_TRIGGERED_BY, triggeredBy);

        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return result != -1; // Returns true if data is inserted successfully
    }

    public Cursor getAllRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
