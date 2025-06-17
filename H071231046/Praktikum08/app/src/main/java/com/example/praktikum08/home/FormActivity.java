package com.example.praktikum08.home;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.praktikum08.R;
import com.example.praktikum08.database.DatabaseContract;
import com.example.praktikum08.note.Note;
import com.example.praktikum08.note.NoteHelper;

public class FormActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE = "extra_note";
    public static final int RESULT_ADD = 101;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;
    public static final int REQUEST_UPDATE = 200;

    private NoteHelper noteHelper;
    private Note note;
    private EditText etJudul, etDeskripsi;
    private TextView nameForm;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        etJudul = findViewById(R.id.et_judul);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        nameForm = findViewById(R.id.nameForm);
        Button btnSave = findViewById(R.id.btn_save);
        ImageButton btnDelete = findViewById(R.id.btn_delete);
        ImageButton btnBack = findViewById(R.id.btn_back);

        noteHelper = NoteHelper.getInstance(getApplicationContext());
        noteHelper.open();

        note = getIntent().getParcelableExtra(EXTRA_NOTE);

        if (note != null) {
            isEdit = true;
        } else {
            note = new Note();
            note.setTanggalWaktu(getCurrentDateTime());
        }

        if (isEdit) {
            nameForm.setText("Ubah");
            btnSave.setText("Update");
            etJudul.setText(note.getJudul());
            etDeskripsi.setText(note.getDeskripsi());
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            nameForm.setText("Tambah");
            btnSave.setText("Save");
            btnDelete.setVisibility(View.GONE);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(isEdit ? "Edit Note" : "Add Note");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Tombol back custom
        btnBack.setOnClickListener(v -> showExitConfirmation());

        btnSave.setOnClickListener(v -> saveNote());
        btnDelete.setOnClickListener(v -> deleteNote());
    }

    private String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    private void saveNote() {
        String judul = etJudul.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();
        String currentDateTime = getCurrentDateTime();

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

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.NoteColumns.JUDUL, judul);
        values.put(DatabaseContract.NoteColumns.DESKRIPSI, deskripsi);

        Intent intent = new Intent();
        intent.putExtra(EXTRA_NOTE, note);

        if (isEdit) {
            String updatedAt = "Updated at " + currentDateTime;
            note.setTanggalWaktu(updatedAt);
            values.put(DatabaseContract.NoteColumns.TANGGAL_WAKTU, updatedAt);

            long result = noteHelper.update(String.valueOf(note.getId()), values);
            if (result > 0) {
                setResult(RESULT_UPDATE, intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        } else {
            String createdAt = "Created at " + currentDateTime;
            note.setTanggalWaktu(createdAt);
            values.put(DatabaseContract.NoteColumns.TANGGAL_WAKTU, createdAt);

            long result = noteHelper.insert(values);
            if (result > 0) {
                note.setId((int) result);
                setResult(RESULT_ADD, intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
    }

    private void deleteNote() {
        if (note == null || note.getId() <= 0) return;

        new AlertDialog.Builder(
                new ContextThemeWrapper(this, com.google.android.material.R.style.ThemeOverlay_AppCompat_Light))
                .setTitle("Hapus Catatan")
                .setMessage("Apakah Anda yakin ingin menghapus catatan ini?")
                .setPositiveButton("YA", (dialog, which) -> {
                    long result = noteHelper.deleteById(String.valueOf(note.getId()));
                    if (result > 0) {
                        setResult(RESULT_DELETE);
                        finish();
                    }
                })
                .setNegativeButton("TIDAK", null)
                .create()
                .show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmation();

    }
    private boolean isFormKosong() {
        String judul = etJudul.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();
        return judul.isEmpty() && deskripsi.isEmpty();
    }

    private boolean isFormChanged() {
        String currentJudul = etJudul.getText().toString().trim();
        String currentDeskripsi = etDeskripsi.getText().toString().trim();

        if (isEdit) {
            return !currentJudul.equals(note.getJudul()) || !currentDeskripsi.equals(note.getDeskripsi());
        } else {
            return !currentJudul.isEmpty() || !currentDeskripsi.isEmpty();
        }
    }


    private void showExitConfirmation() {
        Log.d("FormActivity", "showExitConfirmation dipanggil");

        boolean shouldShowDialog;

        if (isEdit) {
            // Jika edit mode dan tidak ada perubahan → tampilkan dialog
            shouldShowDialog = !isFormChanged();
        } else {
            // Jika tambah mode dan form kosong → tampilkan dialog
            shouldShowDialog = isFormKosong();
        }

        if (!shouldShowDialog) {
            Log.d("FormActivity", "Form telah diubah, langsung keluar tanpa dialog");
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return;
        }

        // Tampilkan dialog
        AlertDialog dialog = new AlertDialog.Builder(
                new ContextThemeWrapper(this, com.google.android.material.R.style.ThemeOverlay_AppCompat_Light))
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin keluar tanpa menyimpan?")
                .setPositiveButton("YA", (d, which) -> {
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                })
                .setNegativeButton("TIDAK", null)
                .create();

        dialog.show();
    }


    // Tombol back di toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            showExitConfirmation();
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
