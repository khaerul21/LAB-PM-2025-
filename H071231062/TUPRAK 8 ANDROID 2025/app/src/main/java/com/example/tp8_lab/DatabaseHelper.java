package com.example.tp8_lab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_IS_UPDATED = "is_updated";

    // SQL statement to create the table
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_ITEMS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_TIMESTAMP + " TEXT, " +
                    COLUMN_IS_UPDATED + " INTEGER DEFAULT 0)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    // timestamp
    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    // masukkan data
    public long insertData(String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_TIMESTAMP, "Created at " + getCurrentDateTime());
        values.put(COLUMN_IS_UPDATED, 0);

        return db.insert(TABLE_ITEMS, null, values);
    }

    // Update data yang sudah ada
    public int updateData(long id, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_TIMESTAMP, "Updated at " + getCurrentDateTime());
        values.put(COLUMN_IS_UPDATED, 1);

        return db.update(TABLE_ITEMS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    public int deleteData(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ITEMS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // Get all data
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLE_ITEMS,
                new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_TIMESTAMP},
                null,
                null,
                null,
                null,
                COLUMN_ID + " DESC"
        );
    }


    public Cursor getDataById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLE_ITEMS,
                new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_TIMESTAMP, COLUMN_IS_UPDATED},
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
    }

    // search
    public Cursor searchByTitle(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLE_ITEMS,
                new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_TIMESTAMP},
                COLUMN_TITLE + " LIKE ?",
                new String[]{"%" + query + "%"},
                null,
                null,
                COLUMN_ID + " DESC"
        );
    }
}