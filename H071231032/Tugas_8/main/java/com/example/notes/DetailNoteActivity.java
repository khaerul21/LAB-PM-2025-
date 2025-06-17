package com.example.notes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailNoteActivity extends AppCompatActivity {

    private EditText editTextTitleDetail, editTextDescriptionDetail;
    private Button buttonUpdateDetail;
    private DatabaseHelper dbHelper;
    private Note currentNote;
    private int noteId;
    private String originalTitle = "";
    private String originalDescription = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);

        Toolbar toolbar = findViewById(R.id.toolbar_detail_note);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Update Note");
        }

        editTextTitleDetail = findViewById(R.id.edit_text_title_detail);
        editTextDescriptionDetail = findViewById(R.id.edit_text_description_detail);
        buttonUpdateDetail = findViewById(R.id.button_update_detail);
        buttonUpdateDetail.setEnabled(false);

        dbHelper = new DatabaseHelper(this);

        noteId = getIntent().getIntExtra("note_id", -1);

        if (noteId == -1) {
            Toast.makeText(this, "Error: Note tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadNoteDetails();

        // Listener untuk mengaktifkan tombol Update jika ada perubahan
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfDataChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        editTextTitleDetail.addTextChangedListener(textWatcher);
        editTextDescriptionDetail.addTextChangedListener(textWatcher);

        buttonUpdateDetail.setOnClickListener(v -> updateNote());
    }

    private void loadNoteDetails() {
        currentNote = dbHelper.getNote(noteId);
        if (currentNote != null) {
            originalTitle = currentNote.getTitle();
            originalDescription = currentNote.getDescription();

            editTextTitleDetail.setText(originalTitle);
            editTextDescriptionDetail.setText(originalDescription);
        } else {
            Toast.makeText(this, "Gagal memuat detail catatan.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void checkIfDataChanged() {
        String currentTitle = editTextTitleDetail.getText().toString().trim();
        String currentDescription = editTextDescriptionDetail.getText().toString().trim();

        boolean titleChanged = !currentTitle.equals(originalTitle.trim());
        boolean descriptionChanged = !currentDescription.equals(originalDescription.trim());

        buttonUpdateDetail.setEnabled((titleChanged || descriptionChanged) && !currentTitle.isEmpty());
    }

    private void updateNote() {
        String newTitle = editTextTitleDetail.getText().toString().trim();
        String newDescription = editTextDescriptionDetail.getText().toString().trim();

        if (newTitle.isEmpty()) {
            Toast.makeText(this, "Judul tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentNote != null) {
            boolean titleActuallyChanged = !newTitle.equals(originalTitle);
            boolean descriptionActuallyChanged = !newDescription.equals(originalDescription);

            currentNote.setTitle(newTitle);
            currentNote.setDescription(newDescription);
            currentNote.setUpdatedAt(System.currentTimeMillis());

            if (titleActuallyChanged || descriptionActuallyChanged) {
                if (titleActuallyChanged) {
                    currentNote.setTitleChangedInLastUpdate(true);
                } else {
                    currentNote.setDescriptionChangedInLastUpdate(true);
                }
            } else {
                currentNote.setTitleChangedInLastUpdate(false);
                currentNote.setDescriptionChangedInLastUpdate(false);
            }

            int rowsAffected = dbHelper.updateNote(currentNote);
            if (rowsAffected > 0) {
                Toast.makeText(this, "Catatan berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                finish(); // Kembali ke MainActivity
            } else {
                Toast.makeText(this, "Gagal memperbarui catatan.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteNote() {
        if (currentNote != null) {
            dbHelper.deleteNote(currentNote);
            Toast.makeText(this, "Catatan berhasil dihapus!", Toast.LENGTH_SHORT).show();
            finish(); // Kembali ke MainActivity
        } else {
            Toast.makeText(this, "Gagal menghapus catatan, note tidak ditemukan.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Catatan")
                .setMessage("Apakah Anda yakin ingin menghapus catatan ini?")
                .setPositiveButton("Ya", (dialog, which) -> deleteNote())
                .setNegativeButton("Tidak", null)
                .setIcon(R.drawable.ic_delete)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish(); // Kembali ke MainActivity
            return true;
        } else if (id == R.id.action_delete_note) {
            showDeleteConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}