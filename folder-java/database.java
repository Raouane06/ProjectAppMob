package com.example.lab1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class database extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "User.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE = "user";

    private static final String ID = "usernameId";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    public database(@Nullable Context context)
    {
        super(context,"User.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTable="CREATE TABLE " + TABLE + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EMAIL + " TEXT," +
            USERNAME + " TEXT UNIQUE, " +
            PASSWORD + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }

    public Boolean insertData(String email,String username,String password)
    {
        SQLiteDatabase mydb=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("email",email);
        contentValues.put("username",username);
        contentValues.put("password",password);
        long result= mydb.insert(TABLE,null,contentValues);

        if(result==-1)
        {
            return false;
        }
        else return true;
    }
    public Boolean checkUserName(String username)
    {
        SQLiteDatabase mydb=this.getReadableDatabase();
        Cursor cur=mydb.rawQuery("SELECT * from user where username=? ",new  String[]{username});
        if(cur.getCount()>0)
        {
            return true;
        }
        else return false;
    }

    public Boolean checkUserPass(String username,String password)
    {
        SQLiteDatabase mydb=this.getWritableDatabase();
        Cursor cur=mydb.rawQuery("SELECT * from user where username=? and password=? ",new  String[]{username,password});
        if(cur.getCount()>0)
        {
            return true;
        }
        else return false;
    }
}
