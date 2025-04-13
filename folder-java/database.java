package com.example.labb1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "User.db";
    private static final int DATABASE_VERSION = 2; 
    private static final String TABLE_NAME = "users";

    // Column names
    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ROLE = "role";

    public database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EMAIL + " TEXT UNIQUE NOT NULL, " +
                USERNAME + " TEXT UNIQUE NOT NULL, " +
                PASSWORD + " TEXT NOT NULL, " +
                ROLE + " TEXT NOT NULL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Add a new user
    public boolean addUser(String email, String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMAIL, email);
        values.put(USERNAME, username);
        values.put(PASSWORD, password);
        values.put(ROLE, role);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    // Get user data
    public Cursor getUserData(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME,
                new String[]{ID, EMAIL, USERNAME, PASSWORD, ROLE},
                USERNAME + "=?",
                new String[]{username},
                null, null, null);
    }

    // Update user profile
    public boolean updateUserProfile(String currentUsername, String newUsername, String email, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME, newUsername);
        values.put(EMAIL, email);
        values.put(ROLE, role);

        int rowsAffected = db.update(TABLE_NAME, values,
                USERNAME + "=?",
                new String[]{currentUsername});
        return rowsAffected > 0;
    }

    // Update password
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PASSWORD, newPassword);

        int rowsAffected = db.update(TABLE_NAME, values,
                USERNAME + "=?",
                new String[]{username});
        return rowsAffected > 0;
    }

    // Check user credentials
    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{PASSWORD},
                USERNAME + "=?",
                new String[]{username},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String storedPassword = cursor.getString(0);
            cursor.close();
            return storedPassword.equals(password);
        }
        return false;
    }

    // Check if username exists
    public boolean usernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{USERNAME},
                USERNAME + "=?",
                new String[]{username},
                null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // Check if email exists
    public boolean emailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{EMAIL},
                EMAIL + "=?",
                new String[]{email},
                null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
