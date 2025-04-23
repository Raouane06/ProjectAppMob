package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AnnouncementDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "User.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "announcements";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TEXT = "text";

    public AnnouncementDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TEXT + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not needed now
    }

    public void addAnnouncement(String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, text);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<String> getAllAnnouncements() {
        ArrayList<String> announcements = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                announcements.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return announcements;
    }
}
