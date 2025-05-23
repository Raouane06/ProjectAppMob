package com.example.labb1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class AnnouncementDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Announcements.db";
    private static final int DB_VERSION = 2;
    private static final String TABLE_NAME = "announcements";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_AUTHOR = "author";

    public AnnouncementDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_CONTENT + " TEXT NOT NULL, " +
                    COLUMN_DATE + " TEXT NOT NULL, " +
                    COLUMN_AUTHOR + " TEXT NOT NULL)");
        } catch (Exception e) {
            Log.e("DBHelper", "Table creation failed", e);
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addAnnouncement(Announcement announcement) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, announcement.getTitle());
        values.put(COLUMN_CONTENT, announcement.getContent());
        values.put(COLUMN_DATE, announcement.getDate());
        values.put(COLUMN_AUTHOR, announcement.getAuthor());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<Announcement> getAllAnnouncements() {
        ArrayList<Announcement> announcements = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    announcements.add(new Announcement(
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR))));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Query failed", e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return announcements;
    }
}
