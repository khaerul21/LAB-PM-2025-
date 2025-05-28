package com.example.praktikum08.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "Notes.db";
    private static final int DATABASE_VERSION = 2;

    private static final String SQL_CREATE_TABLE_NOTES =
            String.format(
                    "CREATE TABLE %s"
                            + " (%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + " %s TEXT NOT NULL,"
                            + " %s TEXT NOT NULL,"
                            + " %s TEXT NOT NULL)",
                    DatabaseContract.TABLE_NAME,
                    DatabaseContract.NoteColumns._ID,
                    DatabaseContract.NoteColumns.JUDUL,
                    DatabaseContract.NoteColumns.DESKRIPSI,
                    DatabaseContract.NoteColumns.TANGGAL_WAKTU
            );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_NOTES);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + DatabaseContract.TABLE_NAME + " ADD COLUMN " +
                    DatabaseContract.NoteColumns.TANGGAL_WAKTU + " TEXT");
        }
    }


}