package com.example.localsqlite;

import android.provider.BaseColumns;

public class DatabaseContract {
    public static String TABLE_NAME = "note";
    public static final class StudentColumns implements BaseColumns {
        public static String NAME = "judul";
        public static String NIM = "deskripsi";
        public static String CREATED_AT = "created_at";
        public static String UPDATED_AT = "updated_at";
    }
}