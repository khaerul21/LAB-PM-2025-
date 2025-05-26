package com.example.notes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextTitleAdd, editTextDescriptionAdd;
    private Button buttonSubmitAdd;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Toolbar toolbar = findViewById(R.id.toolbar_add_note);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Note");
        }

        editTextTitleAdd = findViewById(R.id.edit_text_title_add);
        editTextDescriptionAdd = findViewById(R.id.edit_text_description_add);
        buttonSubmitAdd = findViewById(R.id.button_submit_add);

        dbHelper = new DatabaseHelper(this);

        // Listener untuk mengaktifkan/menonaktifkan tombol submit berdasarkan input judul
        editTextTitleAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonSubmitAdd.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        buttonSubmitAdd.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String title = editTextTitleAdd.getText().toString().trim();
        String description = editTextDescriptionAdd.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Judul tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        long currentTime = System.currentTimeMillis();
        Note newNote = new Note();
        newNote.setTitle(title);
        newNote.setDescription(description);
        newNote.setCreatedAt(currentTime);
        newNote.setUpdatedAt(currentTime); // Saat baru dibuat, createdAt dan updatedAt sama

        long id = dbHelper.addNote(newNote);

        if (id != -1) {
            Toast.makeText(this, "Catatan berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
            finish(); // Kembali ke MainActivity
        } else {
            Toast.makeText(this, "Gagal menambahkan catatan.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle tombol back di toolbar
        if (item.getItemId() == android.R.id.home) {
            finish(); // Kembali ke MainActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}