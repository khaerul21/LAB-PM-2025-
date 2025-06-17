package com.example.praktikum08.map;

import android.database.Cursor;

import com.example.praktikum08.database.DatabaseContract;
import com.example.praktikum08.note.Note;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<Note> mapCursorToArrayList(Cursor cursor) {
        ArrayList<Note> notes = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID));
            String judul =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.JUDUL));
            String deskripsi =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESKRIPSI));
            String tanggalWaktu =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.TANGGAL_WAKTU));
            notes.add(new Note(id, judul, deskripsi, tanggalWaktu));
        }
        return notes;
    }
}