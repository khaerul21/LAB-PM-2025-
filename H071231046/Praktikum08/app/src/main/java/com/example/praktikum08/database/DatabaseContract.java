package com.example.praktikum08.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    public static String TABLE_NAME = "notes";

    public static final class NoteColumns implements BaseColumns {
        public static String JUDUL = "judul";
        public static String DESKRIPSI = "deskripsi";
        public static String TANGGAL_WAKTU = "tanggal_waktu";
    }
}
