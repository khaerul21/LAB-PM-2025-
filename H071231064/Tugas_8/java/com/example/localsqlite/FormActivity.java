package com.example.localsqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE = "extra_note";
    public static final int RESULT_ADD = 101;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;
    public static final int REQUEST_UPDATE = 200;
    private NoteHelper noteHelper;
    private Note note;
    private EditText etJudul, etDeskripsi;
    private TextView tvTimestamp;
    private boolean isEdit = false;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        etJudul = findViewById(R.id.et_name);
        etDeskripsi = findViewById(R.id.et_nim);
        tvTimestamp = findViewById(R.id.tv_timestamp);
        Button btnSave = findViewById(R.id.btn_save);
        Button btnDelete = findViewById(R.id.btn_delete);
        noteHelper = NoteHelper.getInstance(getApplicationContext());
        noteHelper.open();
        note = getIntent().getParcelableExtra(EXTRA_NOTE);
        if (note != null) {
            isEdit = true;
        } else {
            note = new Note();
        }
        String actionBarTitle;
        String buttonTitle;
        if (isEdit) {
            actionBarTitle = "Edit Note";
            buttonTitle = "Update";
            if (note != null) {
                etJudul.setText(note.getJudul());
                etDeskripsi.setText(note.getDeskripsi());
            }
            btnDelete.setVisibility(View.VISIBLE);
            tvTimestamp.setText("Update at " + (note.getUpdatedAt() != null ? note.getUpdatedAt() : "-"));
        } else {
            actionBarTitle = "Add Note";
            buttonTitle = "Save";
            tvTimestamp.setText("Created at " + LocalDateTime.now().format(FORMATTER));
        }
        btnSave.setText(buttonTitle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        btnSave.setOnClickListener(view -> saveNote());
        btnDelete.setOnClickListener(view -> deleteNote());
    }
    private void saveNote() {
        String judul = etJudul.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();
        if (judul.isEmpty()) {
            etJudul.setError("Please fill this field");
            return;
        }
        if (deskripsi.isEmpty()) {
            etDeskripsi.setError("Please fill this field");
            return;
        }
        note.setJudul(judul);
        note.setDeskripsi(deskripsi);
        String now = LocalDateTime.now().format(FORMATTER);
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.StudentColumns.NAME, judul);
        values.put(DatabaseContract.StudentColumns.NIM, deskripsi);
        if (isEdit) {
            values.put(DatabaseContract.StudentColumns.UPDATED_AT, now);
            note.setUpdatedAt(now);
            long result = noteHelper.update(String.valueOf(note.getId()), values);
            if (result > 0) {
                Intent intent = new Intent();
                note.setJudul(judul);
                note.setDeskripsi(deskripsi);
                intent.putExtra(EXTRA_NOTE, note);
                setResult(RESULT_UPDATE, intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show();
            }
        } else {
            values.put(DatabaseContract.StudentColumns.CREATED_AT, now);
            values.put(DatabaseContract.StudentColumns.UPDATED_AT, now);
            note.setCreatedAt(now);
            note.setUpdatedAt(now);
            long result = noteHelper.insert(values);
            if (result > 0) {
                note.setId((int) result);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_NOTE, note);
                setResult(RESULT_ADD, intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to add data", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void deleteNote() {
        if (note != null && note.getId() > 0) {
            long result = noteHelper.deleteById(String.valueOf(note.getId()));
            if (result > 0) {
                setResult(RESULT_DELETE);
                finish();
            } else {
                Toast.makeText(this, "Failed to delete data", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid note data", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null) {
            noteHelper.close();
        }
    }
}