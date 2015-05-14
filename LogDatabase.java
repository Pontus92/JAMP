package com.example.martin.speechtotext;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Martin on 2015-05-14.
 */
public class LogDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Easy_Speech_Log.db";
    public static final String TABLE_NAME = "Table";
    public static final String COL_1 = "lights_on";
    public static final String COL_2 = "light_off";
    public static final String COL_3 = "head";
    public static final String COL_4 = "right_arm";
    public static final String COL_5 = "left_arm";
    public static final String COL_6 = "right_leg";
    public static final String COL_7 = "left_leg";

    public ContentValues values = new ContentValues();

    public SQLiteDatabase db;

    public LogDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public synchronized void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +
                " (" + COL_1 + " INTEGER," + COL_2 + " INTEGER," + COL_3 + " INTEGER," + COL_4 +
                " INTEGER," + COL_5 + " INTEGER," + COL_6 + " INTEGER," + COL_7 + " INTEGER)");
    }

    @Override
    public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void increment(String command, int value){
        values.put(command, value);
        db.insert(TABLE_NAME, command, values);
        onUpgrade(db,1,1);
    }
}
