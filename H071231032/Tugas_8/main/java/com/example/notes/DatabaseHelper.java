package com.example.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "NotesDB";
    private static final String TABLE_NOTES = "notes";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";
    // Kolom baru
    private static final String KEY_TITLE_CHANGED_LAST = "title_changed_last";
    private static final String KEY_DESC_CHANGED_LAST = "desc_changed_last";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_CREATED_AT + " INTEGER,"
                + KEY_UPDATED_AT + " INTEGER,"
                + KEY_TITLE_CHANGED_LAST + " INTEGER DEFAULT 0,"
                + KEY_DESC_CHANGED_LAST + " INTEGER DEFAULT 0"
                + ")";
        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public long addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_DESCRIPTION, note.getDescription());
        values.put(KEY_CREATED_AT, note.getCreatedAt());
        values.put(KEY_UPDATED_AT, note.getUpdatedAt());
        // Untuk catatan baru, flag perubahan adalah false
        values.put(KEY_TITLE_CHANGED_LAST, 0);
        values.put(KEY_DESC_CHANGED_LAST, 0);

        long id = db.insert(TABLE_NOTES, null, values);
        db.close();
        return id;
    }

    private Note cursorToNote(Cursor cursor) {
        return new Note(
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                cursor.getLong(cursor.getColumnIndexOrThrow(KEY_CREATED_AT)),
                cursor.getLong(cursor.getColumnIndexOrThrow(KEY_UPDATED_AT)),
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_TITLE_CHANGED_LAST)) == 1, // Konversi INTEGER ke boolean
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_DESC_CHANGED_LAST)) == 1   // Konversi INTEGER ke boolean
        );
    }

    public Note getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES,
                new String[]{KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_CREATED_AT, KEY_UPDATED_AT, KEY_TITLE_CHANGED_LAST, KEY_DESC_CHANGED_LAST},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Note note = null;
        if (cursor.moveToFirst()) {
            note = cursorToNote(cursor);
        }
        cursor.close();
        db.close();
        return note;
    }

    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NOTES + " ORDER BY " + KEY_UPDATED_AT + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                noteList.add(cursorToNote(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return noteList;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_DESCRIPTION, note.getDescription());
        values.put(KEY_UPDATED_AT, note.getUpdatedAt());
        // Simpan flag perubahan
        values.put(KEY_TITLE_CHANGED_LAST, note.wasTitleChangedInLastUpdate() ? 1 : 0);
        values.put(KEY_DESC_CHANGED_LAST, note.wasDescriptionChangedInLastUpdate() ? 1 : 0);

        return db.update(TABLE_NOTES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public List<Note> searchNotes(String query) {
        List<Note> noteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NOTES + " WHERE " + KEY_TITLE + " LIKE ? ORDER BY " + KEY_UPDATED_AT + " DESC";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                noteList.add(cursorToNote(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return noteList;
    }
}