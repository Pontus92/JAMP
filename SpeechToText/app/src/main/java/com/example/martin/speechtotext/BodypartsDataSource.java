package com.example.martin.speechtotext;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.w3c.dom.Comment;

public class BodypartsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_BODYPART, MySQLiteHelper.COLUMN_USED };

    public BodypartsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Bodypart createBodypart(String bodypart) {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.COLUMN_BODYPART, bodypart);

        long insertId = database.insert(MySQLiteHelper.TABLE_BODYPARTS, null, values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BODYPARTS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        Bodypart newBodyPart = cursorToBodypart(cursor);
        cursor.close();
        return newBodyPart;
    }

    public List<Bodypart> getAllBodyparts() {
        List<Bodypart> bodyparts = new ArrayList<Bodypart>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BODYPARTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Bodypart bodypart = cursorToBodypart(cursor);
            bodyparts.add(bodypart);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return bodyparts;
    }

    private Bodypart cursorToBodypart(Cursor cursor) {
        Bodypart bodypart = new Bodypart();
        bodypart.setId(cursor.getLong(0));
        bodypart.setBodypart(cursor.getString(1));
        return bodypart;
    }
}
