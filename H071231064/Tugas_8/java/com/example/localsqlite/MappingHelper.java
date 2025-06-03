package com.example.localsqlite;

import android.database.Cursor;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<Note> mapCursorToArrayList(Cursor cursor) {
        ArrayList<Note> notes = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.StudentColumns._ID));
            String judul = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.StudentColumns.NAME));
            String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.StudentColumns.NIM));
            String createdAt = "";
            String updatedAt = "";
            int idxCreated = cursor.getColumnIndex(DatabaseContract.StudentColumns.CREATED_AT);
            int idxUpdated = cursor.getColumnIndex(DatabaseContract.StudentColumns.UPDATED_AT);
            if (idxCreated != -1) createdAt = cursor.getString(idxCreated);
            if (idxUpdated != -1) updatedAt = cursor.getString(idxUpdated);
            notes.add(new Note(id, judul, deskripsi, createdAt, updatedAt));
        }
        return notes;
    }
}