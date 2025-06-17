package com.example.notes;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {
    private RecyclerView recyclerViewNotes;
    private NoteAdapter noteAdapter;
    private List<Note> noteList = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private TextView textViewNoData;
    private SearchView searchView;
    private FloatingActionButton fabAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        // Menghilangkan judul default dari Toolbar jika Anda menggunakan TextView custom di XML
         if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
         }

        dbHelper = new DatabaseHelper(this);

        recyclerViewNotes = findViewById(R.id.recycler_view_notes);
        textViewNoData = findViewById(R.id.text_view_no_data);
        searchView = findViewById(R.id.search_view);
        fabAddNote = findViewById(R.id.fab_add_note);

        setupRecyclerView();
        setupSearchView();

        fabAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        noteAdapter = new NoteAdapter(this, noteList, this);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setAdapter(noteAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadNotes() {
        noteList.clear();
        List<Note> notesFromDb = dbHelper.getAllNotes();
        noteList.addAll(notesFromDb);
        noteAdapter.notifyDataSetChanged(); // Memberitahu adapter bahwa data telah berubah

        checkIfNoData();
    }

    private void checkIfNoData() {
        if (noteList.isEmpty()) {
            recyclerViewNotes.setVisibility(View.GONE);
            textViewNoData.setVisibility(View.VISIBLE);
        } else {
            recyclerViewNotes.setVisibility(View.VISIBLE);
            textViewNoData.setVisibility(View.GONE);
        }
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotes(newText);
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            loadNotes(); // Muat ulang semua catatan saat search view ditutup
            return false;
        });

        // Menangani ketika query kosong (misalnya setelah menekan 'x' di searchview)
        View searchCloseButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        if (searchCloseButton != null) {
            searchCloseButton.setOnClickListener(v -> {
                searchView.setQuery("", false); // Hapus query
                searchView.clearFocus(); // Hapus fokus
                loadNotes(); // Muat ulang semua catatan
            });
        }
    }

    private void filterNotes(String query) {
        List<Note> filteredList;
        if (query == null || query.trim().isEmpty()) {
            filteredList = dbHelper.getAllNotes();
        } else {
            filteredList = dbHelper.searchNotes(query);
        }
        noteAdapter.filterList(filteredList);
        // Perbarui tampilan "No Data" berdasarkan hasil filter
        if (filteredList.isEmpty()) {
            recyclerViewNotes.setVisibility(View.GONE);
            textViewNoData.setText("No Data");
            textViewNoData.setVisibility(View.VISIBLE);
        } else {
            recyclerViewNotes.setVisibility(View.VISIBLE);
            textViewNoData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNoteClick(Note note) {
        Intent intent = new Intent(MainActivity.this, DetailNoteActivity.class);
        intent.putExtra("note_id", note.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset search view jika ada teks
        if (searchView.getQuery().length() > 0) {
            filterNotes(searchView.getQuery().toString());
        } else {
            loadNotes();
        }
    }
}